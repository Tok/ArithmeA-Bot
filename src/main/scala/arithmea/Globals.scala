package arithmea

import java.util.Locale
import arithmea.gematria.Term

object Globals {
  val wordlistName = "words.txt"
  val ircPropertiesName = "irc.properties"
  val allWords = readWords

  private def readWords(): List[String] = {
    println("Parsing wordlist...")
    val lines = scala.io.Source.fromFile(Globals.wordlistName, "ISO-8859-1").getLines.toList
    println(lines.size + " words parsed.")
    lines.map(_.toUpperCase(Locale.getDefault)).sorted
  }

  private def makeTerms(): Set[Term] = {
    println("Calculating numbers...")
    allWords.map(new Term(_)).toSet
  }

  var terms: Set[Term] = makeTerms
  def updateTerms(newTerm: Term): Unit = terms = terms ++ Set(newTerm)
}
