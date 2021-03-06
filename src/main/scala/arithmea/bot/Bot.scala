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
import arithmea.gematria.Explaination
import arithmea.util.TransliterationUtil
import arithmea.tarot.Spread
import arithmea.tarot.Card
import scala.annotation.tailrec

class Bot extends PircBot {
  val SPACE = " "
  val DASH = "-"
  val TRIPPLE_DOT = "..."
  val OPEN = " ("
  val CLOSE = ") "
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
      super.setVerbose(prop.getProperty("verbose").toBoolean)
      super.setAutoNickChange(false)
      super.startIdentServer
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
    val ADD, SHOW, TERMS, TAROT, HELP, INFO, EXPLAIN, COMMENT, ANA, QUIT, GTFO = Value
  }

  def connectAndJoin(): Unit = {
    try {
      super.setName(settings.nick)
      println("Connecting to " + settings.network + " on port " + settings.port + TRIPPLE_DOT)
      connect(settings.network, settings.port)
    } catch {
      case nie: NickAlreadyInUseException => println("Fail: Nick is in use.")
      case ioe: IOException => println("Fail: IOException: " + ioe)
      case ie: IrcException => println("Fail: IrcException: " + ie)
      case e: Exception => println("Fail: Exception: " + e)
    }
  }

  override def onConnect(): Unit = {
    println("Joining " + settings.channel + TRIPPLE_DOT)
    joinChannel(settings.channel)
    println("Ready.")
  }

  private def executeCommand(source: String, command: String, rest: List[String]): Unit = {
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
        case Command.TAROT => drawTarot(source, rest.head)
        case Command.HELP => showHelp(source)
        case Command.GTFO | Command.QUIT =>
          if (getChannels.toList.contains(source)) { disconnect }
          else { sendMessage(source, "This command only works in the channel.") }
      }
    } catch {
      case nse: NoSuchElementException =>
        val cmd = if (rest.isEmpty) { command } else { command + DASH + rest.mkString(DASH) }
        evaluate(source, cmd, false)
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
        case Method.Chaldean | Method.Pythagorean => OPEN + GematriaUtil.reduce(v) + CLOSE
        case Method.IA => OPEN + (v * 6) + CLOSE
        case _ => SPACE
      }
      m + ": " + v + prep
    }
    val prepared = Method.values.map(prepare(_)).toList.mkString(SPACE).trim
    val colored = ColorUtil.colorString(prepared)
    val hebrew = TransliterationUtil.getHebrew(word)
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

  private def drawTarot(source: String, spreadName: String): Unit = {
    def makeLine(s: String, c: Card): String = s + ": " + c.toString + OPEN + c.meaning + CLOSE
    def makeLines(s: Vector[String], c: Vector[Card]): List[String] = {
      def impl(i: Int): List[String] = {
        if (i >= 0) { List(makeLine(s(i), c(i))) ::: impl(i - 1) } else { Nil }
      }
      impl(s.size - 1).reverse
    }
    def draw(spread: Spread): List[String] = {
      spread match {
        case Spread.Trump =>
          val trump = Random.shuffle(Card.values.filter(_.isTrump)).head
          List(makeLine(Spread.Trump.layout.head, trump))
        case s: Spread => makeLines(s.layout, Random.shuffle(Card.values).toVector)
      }
    }
    val spread = Spread.valueOf(spreadName)
    spread match {
      case Some(s) => draw(s).foreach(msg => sendMessage(source, msg))
      case None => sendMessage(source, "Spread unknown: " + spreadName)
    }
  }

  private def explain(source: String, method: String): Unit = {
    val text = Method.valueOf(method) match {
      case Some(m) => Explaination.getFor(m)
      case None => "Method unknown: " + method
    }
    sendMessage(source, text)
  }

  private def comment(source: String): Unit = {
    val (firstLine, secondLine) = Explaination.transliterationComment
    sendMessage(source, firstLine)
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
    sendMessage(source, "Generate anagrams (takes time): ARITHMEA ANA [word]")
    sendMessage(source, "Draw Tarot Cards: ARITHMEA TAROT [spread]")
    sendMessage(source, "  - Spreads are: " + Spread.values.map(_.toString.toUpperCase(Locale.getDefault)).mkString(SPACE))
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
    val message = msg.split(SPACE).toList
    if (isForBot(message.head.toUpperCase(Locale.getDefault))) {
      executeCommand(channel, message.tail.head.toUpperCase, message.tail.tail)
    }
  }

  override def onPrivateMessage(sender: String, login: String, host: String, msg: String): Unit = {
    val message = msg.split(SPACE).toList
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
    super.onVersion(nick, login, hostname, target)
    info.foreach(sendNotice(nick, _))
    println("Version request by " + nick)
  }

  override def onPing(nick: String, login: String, hostname: String, target: String, pingValue: String): Unit = {
    super.onPing(nick, login, hostname, target, pingValue)
    println("Pinged by " + nick)
  }

  override def onFinger(nick: String, login: String, hostname: String, target: String): Unit = {
    sendNotice(nick, ":o")
    println("Fingered by " + nick)
  }

  override def onServerPing(response: String): Unit = {
    super.onServerPing(response)
    print(".")
  }

  override def onTime(nick: String, login: String, hostname: String, target: String): Unit = {
    super.onTime(nick, login, hostname, target)
    sendNotice(nick, "Time is an illusion.")
    println("Time request by " + nick)
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
