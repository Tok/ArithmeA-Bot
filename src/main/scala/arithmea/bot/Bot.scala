package arithmea.bot

import java.io.FileInputStream
import java.io.FileWriter
import java.io.IOException
import java.util.Locale
import java.util.Properties

import scala.util.Random

import org.jibble.pircbot.IrcException
import org.jibble.pircbot.NickAlreadyInUseException
import org.jibble.pircbot.PircBot

import arithmea.Globals
import arithmea.gematria.Method
import arithmea.gematria.Term
import arithmea.util.ColorUtil
import arithmea.util.GematriaUtil

class Bot extends PircBot {
  val SPACE = " "
  val TRIPPLE_DOT = "..."
  val resultCount = 30

  case class IrcSettings(val network: String, val port: Int, val channel: String, val nick: String)

  def init: IrcSettings = {
    try {
      val prop = new Properties
      prop.load(new FileInputStream(Globals.ircPropertiesName))
      val network = prop.getProperty("network")
      val port = prop.getProperty("port").toInt
      val channel = prop.getProperty("channel")
      val nick = prop.getProperty("nick")
      val login = prop.getProperty("login")
      val version = prop.getProperty("version")
      val delayMs = prop.getProperty("delayMs").toLong
      super.setEncoding("UTF-8")
      super.setLogin(login)
      super.setVersion(version)
      super.setMessageDelay(delayMs)
      super.setAutoNickChange(false)
      IrcSettings(network, port, channel, nick)
    } catch {
      case e: Exception =>
        println("Fail reading IRC properties: " + e.getMessage)
        println("Using defaults.")
        IrcSettings("efnet.xs4all.nl", 6669, "#Thelema", "ArithmeA")
    }
  }
  val settings = init

  object Command extends Enumeration {
    type Command = Value
    val ADD, SHOW, TERMS, HELP, INFO, EXPLAIN, COMMENT, QUIT, GTFO = Value
  }

  def connectAndJoin(): Unit = {
    try {
      super.setName(settings.nick)
      println("Connecting to " + settings.network + " on port " + settings.port + TRIPPLE_DOT)
      connect(settings.network, settings.port)
      println("Joining " + settings.channel + TRIPPLE_DOT)
      joinChannel(settings.channel)
      println("Ready.")
    } catch {
      case nie: NickAlreadyInUseException => println("Fail: Nick is in use.")
      case ioe: IOException => println("Fail: IOException: " + ioe)
      case ie: IrcException => println("Fail: IrcException: " + ie)
    }
  }

  override def onMessage(channel: String, sender: String, login: String, hostname: String, msg: String): Unit = {
    val message = splitAtSpace(msg)
    if (isForBot(message)) {
      executeCommand(channel, message.tail.head.toUpperCase, message.tail.tail)
    }
  }

  private def executeCommand(channel: String, command: String, rest: Array[String]): Unit = {
    try {
      Command.withName(command) match {
        case Command.ADD => evaluate(channel, rest.head, true)
        case Command.SHOW =>
          Method.valueOf(rest.head) match {
            case Some(m) =>
              rest.tail.head match {
                case Int(v) => show(channel, m, v)
                case _ => sendMessage(channel, "Invalid number: " + rest.tail.head)
              }
            case _ => sendMessage(channel, "Method unknown: " + rest.head)
          }
        case Command.INFO => info(channel)
        case Command.EXPLAIN => explain(channel, rest.head)
        case Command.COMMENT => comment(channel)
        case Command.TERMS => showTerms(channel)
        case Command.HELP => showHelp(channel)
        case Command.GTFO | Command.QUIT => disconnect
      }
    } catch {
      case nse: NoSuchElementException => evaluate(channel, command, false)
    }
  }

  private def evaluate(channel: String, word: String, addWord: Boolean): Unit = {
    val numbers: Map[Method, Int] = GematriaUtil.getValues(word)
    def prepare(m: Method): String = {
      val v = numbers.get(m).get
      val prep = m match {
        case Method.Chaldean | Method.Pythagorean => " (" + GematriaUtil.reduce(v) + ") "
        case Method.IA => " (" + (v * 6) + ") "
        case _ => SPACE
      }
      m + ": " + v + prep
    }
    val prepared = Method.values.map(prepare(_)).toList.mkString(SPACE).trim
    val colored = ColorUtil.colorString(prepared)
    val hebrew = GematriaUtil.getHebrew(word)
    sendMessage(channel, colored + " -- " + hebrew)
    if (addWord) {
      val upper = word.toUpperCase(Locale.getDefault)
      if (Globals.terms.find(_.latinString.equalsIgnoreCase(upper)).isDefined) {
        sendMessage(channel, upper + " is already in the List.")
      } else {
        val term = new Term(upper)
        Globals.updateTerms(term)
        val out = new FileWriter(Globals.wordlistName, true)
        try {
          out.write("\n" + upper)
          sendMessage(channel, upper + " added to List. New size: " + Globals.terms.size)
        } finally { out.close }
      }
    }
  }

  private def show(channel: String, method: Method, number: Int): Unit = {
    def isNumber(opt: Option[Int]): Boolean = {
      opt match {
        case Some(v) => v == number
        case _ => false
      }
    }
    val all = Globals.terms.filter(t => isNumber(t.values.get(method))).map(_.latinString)
    if (all.size > 0) {
      val shuffled = Random.shuffle(all.toList)
      val results = if (shuffled.size > resultCount) { shuffled.take(resultCount) } else { shuffled }
      val diff = all.size - results.size
      val percent: Double = (all.size * 100D) / Globals.terms.size
      val percentString = "%3.2f".format(percent) + "% --> "
      val more = if (diff > 0) { " +" + diff + " more." } else { "" }
      sendMessage(channel, percentString + results.mkString(SPACE) + more)
    } else {
      sendMessage(channel, "No matches found for method " + method + " with number " + number + ".")
    }
  }

  private def explain(channel: String, method: String): Unit = {
    val text = Method.valueOf(method) match {
      case Some(Method.Chaldean) =>
        "Chaldean Numerology. " +
          "The result is not reduced in order to preserve the full information, " +
          "but the number in parentheses is reduced, even if the result is a master number. " +
          "http://en.wikipedia.org/wiki/Arithmancy#The_Chaldean_method"
      case Some(Method.Pythagorean) =>
        "Pythagorean Numerology (also known as Agrippan Numerology). " +
          "The result is not reduced in order to preserve the full information, " +
          "but the number in parentheses is reduced, even if the result is a master number. " +
          "http://en.wikipedia.org/wiki/Arithmancy#The_Agrippan_method"
      case Some(Method.IA) =>
        "1=A, 2=B, 3=C .. 26=Z. Also known as Simple English Gematria. " +
          "http://wmjas.wikidot.com/simple-english-gematria " +
          "There is another system of english gematria, that uses the sixfold of every letter: " +
          "A=6, B=12, C=18 .. Z=156. It matches the same words and yields higher values but doesn't preserve the number qualities well. " +
          "The sixfold result is shown in parentheses."
      case Some(Method.NAEQ) =>
        "New Aeon English Qabalah " +
          "http://en.wikipedia.org/wiki/English_Qabalah#ALW_Cipher " +
          "The NAEQ Cipher can be obtained, by arranging the letters of the alphabet on an elevenfold star."
      case Some(Method.TQ) =>
        "Trigrammaton Qabalah " +
          "http://en.wikipedia.org/wiki/English_Qabalah#Trigrammaton_Qabalah_.28TQ.29"
      case Some(Method.German) =>
        "A cipher specific to the german language, that was discovered by Heinz Borchardt. " +
          "http://www.rolf-keppler.de/schluessel.htm " +
          "The interpretation of the results for this cipher is very different from the other methods " +
          "and generally leads to low numbers and a lot of matches because most letters have a value of 0. " +
          "The number obtained by applying this method is supposed to directly describe the quality of german words."
      case Some(Method.EQ) =>
        "English Qabalah method that uses the same sequence as NAEQ, but without reducing the Values. " +
          "This method is also known as EQ26 or Azure Lidded Woman Cipher (thanks to Alien696)."
      case Some(Method.Full) =>
        "Transliterates the letters to hebrew and uses their absolute or normative numerical value. " +
          "This method may be refered to as Mispar Hechrachi or Mispar ha-Panim. " +
          "http://en.wikipedia.org/wiki/Gematria#Methods"
      case Some(Method.Ordinal) =>
        "Transliterates the letters to hebrew and gives each letter a value from one to twenty-two. " +
          "Also known as Mispar Siduri. " +
          "http://en.wikipedia.org/wiki/Gematria#Methods"
      case Some(Method.Katan) =>
        "Transliterates the letters to hebrew and calculates the value of the letters, but truncates all of the zeros. " +
          "It is also sometimes called Mispar Me'ugal. " +
          "http://en.wikipedia.org/wiki/Gematria#Methods " +
          "Metatron declares: Mishpar Katan is important ;)"
      case None => "Method unknown: " + method
    }
    sendMessage(channel, text)
  }

  private def comment(channel: String): Unit = {
    val firstLine = "In a first step, the words are transliterated into a representation with hebrew letters " +
      "according to a predefined method. The method of transliteration is missusing hebrew consonants " +
      "as if they were vowels. " +
      "Transliteration is different from translation and results in words and accumulations of letters, " +
      "that most likey don't have any meaning in hebrew."
    sendMessage(channel, firstLine)
    val secondLine = "There are different possibilities on how the transliteration may be performed. " +
      "http://en.wikipedia.org/wiki/Romanization_of_Hebrew#How_to_transliterate " +
      "If you want to know the exact method, you may look at the sourcecode of ArithmeA."
    sendMessage(channel, secondLine)
  }

  private def info(channel: String): Unit = {
    sendMessage(channel, "Web Version: http://arithmea2000.appspot.com")
    sendMessage(channel, "Source Code: https://github.com/Tok/ArithmeA-Bot")
  }

  private def showTerms(channel: String): Unit = {
    sendMessage(channel, "Number of terms: " + Globals.terms.size)
  }

  private def showHelp(channel: String): Unit = {
    sendMessage(channel, "Evaluate: ARITHMEA [word]")
    sendMessage(channel, "Show numbers: ARITHMEA SHOW [method] [number]")
    sendMessage(channel, "Explain method: ARITHMEA EXPLAIN [method]")
    sendMessage(channel, "  - Methods are: " + Method.values.mkString(SPACE))
    sendMessage(channel, "Show Information about this bot: ARITHMEA INFO")
    sendMessage(channel, "Comment transliteration: ARITHMEA COMMENT")
    sendMessage(channel, "Count terms: ARITHMEA TERMS")
    sendMessage(channel, "Disconnect Bot: ARITHMEA QUIT")
  }

  private def splitAtSpace(message: String): Array[String] = message.split(SPACE)

  private def isForBot(message: Array[String]): Boolean = {
    message.head.equalsIgnoreCase("ArithmeA") ||
      message.head.equalsIgnoreCase(getLogin) ||
      message.head.equalsIgnoreCase(getNick)
  }

  object Int {
    def unapply(s: String): Option[Int] = try {
      Some(s.toInt)
    } catch {
      case _: java.lang.NumberFormatException => None
    }
  }
}
