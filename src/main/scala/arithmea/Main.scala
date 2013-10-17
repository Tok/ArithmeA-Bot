package arithmea

import arithmea.bot.Bot
import arithmea.gematria.Term
import java.util.Locale
import arithmea.util.WordlistUtil

object Main {
  def main(args: Array[String]): Unit = {
    if (args.isEmpty) { runBot } else { makeWordlist(args.head) }
  }

  private def runBot(): Unit = {
    val bot = new Bot
    bot.connectAndJoin
  }

  private def makeWordlist(fileName: String): Unit = {
    println("Creating new wordlist from file: " + fileName)
    WordlistUtil.createWordlist(fileName)
  }
}
