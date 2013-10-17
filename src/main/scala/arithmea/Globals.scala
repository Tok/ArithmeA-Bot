package arithmea

import java.util.Locale
import arithmea.gematria.Term

object Globals {
  val wordlistName = "words.txt"
  val ircPropertiesName = "irc.properties"  

  private def readTerms(): Set[Term] = {
    println("Parsing wordlist...")
    val lines = scala.io.Source.fromFile(Globals.wordlistName, "ISO-8859-1").getLines.toList
    println(lines.size + " words parsed.")
    val words = lines.map(_.toUpperCase(Locale.getDefault)).sorted
    println("Calculating numbers...")
    words.map(new Term(_)).toSet
  }

  var terms: Set[Term] = readTerms
  def updateTerms(newTerm: Term): Unit = terms = terms ++ Set(newTerm)
}
