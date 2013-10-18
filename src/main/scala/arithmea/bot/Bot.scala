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
import arithmea.util.AnagramUtil
import arithmea.util.ColorUtil
import arithmea.util.GematriaUtil

class Bot extends PircBot {
  val SPACE = " "
  val DASH = "-"
  val TRIPPLE_DOT = "..."
  val maxResults = 30
  val maxAnagrams = 20

  val info = List("Web Version: http://arithmea2000.appspot.com",
    "Source Code: https://github.com/Tok/ArithmeA-Bot",
    "Copyright © 2013 by ORDO .'. ILLUMINATORUM .'. DIGITALIS .'.")

  case class IrcSettings(val network: String, val port: Int, val channel: String, val nick: String,
    val joinMessage: String, val greetingNotice: String)

  def init: IrcSettings = {
    try {
      val prop = new Properties
      prop.load(new FileInputStream(Globals.ircPropertiesName))
      val network = prop.getProperty("network")
      val port = prop.getProperty("port").toInt
      val channel = prop.getProperty("channel")
      val nick = prop.getProperty("nick")
      val joinMessage = prop.getProperty("joinMessage")
      val greetingNotice = prop.getProperty("greetingNotice")
      super.setEncoding("UTF-8")
      super.setLogin(prop.getProperty("login"))
      super.setVersion(prop.getProperty("version"))
      super.setMessageDelay(prop.getProperty("delayMs").toLong)
      super.setAutoNickChange(false)
      IrcSettings(network, port, channel, nick, joinMessage, greetingNotice)
    } catch {
      case e: Exception =>
        println("Fail reading IRC properties: " + e.getMessage)
        println("Using defaults.")
        IrcSettings("efnet.xs4all.nl", 6669, "#Thelema", "ArithmeA", "", "")
    }
  }
  val settings = init

  object Command extends Enumeration {
    type Command = Value
    val ADD, SHOW, TERMS, HELP, INFO, EXPLAIN, COMMENT, ANA, QUIT, GTFO = Value
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

  private def executeCommand(source: String, command: String, rest: Array[String]): Unit = {
    try {
      Command.withName(command) match {
        case Command.ADD => evaluate(source, rest.mkString(DASH), true)
        case Command.ANA => anagram(source, rest.mkString)
        case Command.SHOW =>
          Method.valueOf(rest.head) match {
            case Some(m) =>
              rest.tail.head match {
                case Int(v) => show(source, m, v)
                case _ => sendMessage(source, "Invalid number: " + rest.tail.head)
              }
            case _ => sendMessage(source, "Method unknown: " + rest.head)
          }
        case Command.INFO => info(source)
        case Command.EXPLAIN => explain(source, rest.head)
        case Command.COMMENT => comment(source)
        case Command.TERMS => showTerms(source)
        case Command.HELP => showHelp(source)
        case Command.GTFO | Command.QUIT =>
          if (getChannels.contains(source)) { disconnect }
          else { sendMessage(source, "This command only works in the channel.") }
      }
    } catch {
      case nse: NoSuchElementException => evaluate(source, command.mkString(DASH), false)
    }
  }

  private def anagram(source: String, word: String): Unit = {
    if (word.size > AnagramUtil.maxSize) {
      sendMessage(source, "Maximum size for anagrams is " + AnagramUtil.maxSize + " letters.")      
    } else {
      val anagrams = AnagramUtil.generateAnagrams(word)
      if (!anagrams.isEmpty) {
        val message = makeResultMessage(anagrams, maxAnagrams, " - ")
        sendMessage(source, message)
      } else {
        sendMessage(source, "No anagrams found for: " + word)
      }      
    }
  }

  private def evaluate(source: String, word: String, addWord: Boolean): Unit = {
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
    sendMessage(source, colored + " -- " + hebrew)
    if (addWord) {
      val upper = word.toUpperCase(Locale.getDefault)
      if (Globals.terms.find(_.latinString.equalsIgnoreCase(upper)).isDefined) {
        sendMessage(source, upper + " is already in the List.")
      } else {
        val term = new Term(upper)
        Globals.updateTerms(term)
        val out = new FileWriter(Globals.wordlistName, true)
        try {
          out.write("\n" + upper)
          sendMessage(source, upper + " added to List. New size: " + Globals.terms.size)
        } finally { out.close }
      }
    }
  }

  private def show(source: String, method: Method, number: Int): Unit = {
    def isNumber(opt: Option[Int]): Boolean = {
      opt match {
        case Some(v) => v == number
        case _ => false
      }
    }
    val all = Globals.terms.filter(t => isNumber(t.values.get(method))).map(_.latinString)
    if (all.size > 0) {
      val percent: Double = (all.size * 100D) / Globals.terms.size
      val percentString = "%3.2f".format(percent) + "% --> "
      val message = makeResultMessage(all.toList, maxResults, SPACE)
      sendMessage(source, percentString + message)
    } else {
      sendMessage(source, "No matches found for method " + method + " with number " + number + ".")
    }
  }

  private def explain(source: String, method: String): Unit = {
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
    sendMessage(source, text)
  }

  private def comment(source: String): Unit = {
    val firstLine = "In a first step, the words are transliterated into a representation with hebrew letters " +
      "according to a predefined method. The method of transliteration is missusing hebrew consonants " +
      "as if they were vowels. " +
      "Transliteration is different from translation and results in words and accumulations of letters, " +
      "that most likey don't have any meaning in hebrew."
    sendMessage(source, firstLine)
    val secondLine = "There are different possibilities on how the transliteration may be performed. " +
      "http://en.wikipedia.org/wiki/Romanization_of_Hebrew#How_to_transliterate " +
      "If you want to know the exact method, you may look at the sourcecode of ArithmeA."
    sendMessage(source, secondLine)
  }

  private def info(source: String): Unit = info.foreach(sendMessage(source, _))

  private def showTerms(source: String): Unit = sendMessage(source, "Number of terms: " + Globals.terms.size)

  private def showHelp(source: String): Unit = {
    sendMessage(source, "Evaluate: ARITHMEA [word]")
    sendMessage(source, "Evaluate and add: ARITHMEA ADD [word]")
    sendMessage(source, "Show numbers: ARITHMEA SHOW [method] [number]")
    sendMessage(source, "Explain method: ARITHMEA EXPLAIN [method]")
    sendMessage(source, "  - Methods are: " + Method.values.mkString(SPACE))
    sendMessage(source, "Show Information about this bot: ARITHMEA INFO")
    sendMessage(source, "Comment transliteration: ARITHMEA COMMENT")
    sendMessage(source, "Count terms: ARITHMEA TERMS")
    sendMessage(source, "Generate anagrams: ARITHMEA ANA [word]")
    sendMessage(source, "Disconnect Bot: ARITHMEA QUIT")
  }

  private def isForBot(firstWord: String): Boolean = {
    firstWord.startsWith("ARITHMEA") ||
    firstWord.startsWith(getLogin.toUpperCase(Locale.getDefault)) ||
    firstWord.startsWith(getNick.toUpperCase(Locale.getDefault))
  }

  private def makeResultMessage(result: List[String], limit: Int, separator: String): String = {
    val shuffled = Random.shuffle(result.toList)
    val limited = if (result.size > limit) { shuffled.take(limit) } else { shuffled }
    val diff = result.size - limited.size
    val more = if (diff > 0) { " +" + diff + " more." } else { "" }
    limited.mkString(separator) + more
  }

  override def onMessage(channel: String, sender: String, login: String, host: String, msg: String): Unit = {
    val message = msg.split(SPACE)
    if (isForBot(message.head.toUpperCase(Locale.getDefault))) {
      executeCommand(channel, message.tail.head.toUpperCase, message.tail.tail)
    }
  }

  override def onPrivateMessage(sender: String, login: String, host: String, msg: String): Unit = {
    val message = msg.split(SPACE)
    executeCommand(sender, message.head.toUpperCase, message.tail)
  }

  override def onJoin(channel: String, joiner: String, login: String, hostname: String): Unit = {
    if (joiner.equals(getNick)) {
      if (!settings.joinMessage.equals("")) { sendMessage(channel, settings.joinMessage) }
    } else {
      if (!settings.greetingNotice.equals("")) { sendNotice(joiner, settings.greetingNotice) }
    }
  }

  override def onVersion(nick: String, login: String, hostname: String, target: String): Unit = {
    println("Version request by " + nick)
    info.foreach(sendNotice(nick, _))
  }

  override def onPing(nick: String, login: String, hostname: String, target: String, pingValue: String): Unit = {
    println("Pinged by " + nick)
    sendNotice(nick, "pong")
  }

  override def onFinger(nick: String, login: String, hostname: String, target: String): Unit = {
    println("Fingered by " + nick)
    sendNotice(nick, ":o")
  }

  override def onServerPing(response: String): Unit = print(".")

  override def onTime(nick: String, login: String, hostname: String, target: String): Unit = {
    println("Time request by " + nick)
    sendNotice(nick, "Time is an illusion.")
  }

  override def onDisconnect(): Unit = {
    println("Disconnected.")
    System.exit(0)
  }

  object Int {
    def unapply(s: String): Option[Int] = try {
      Some(s.toInt)
    } catch {
      case _: java.lang.NumberFormatException => None
    }
  }
}
