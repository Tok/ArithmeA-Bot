package arithmea

import java.util.Locale
import arithmea.gematria.Term
import arithmea.util.AnagramUtil

object Globals {
  val wordlistName = "words.txt"
  val ircPropertiesName = "irc.properties"
  val encoding = "ISO-8859-1"

  val allWords = readWords
  val anagramMap = generateAnagramMap

  private def readWords(): List[String] = {
    println("Parsing wordlist...")
    val lines = scala.io.Source.fromFile(wordlistName, encoding).getLines.toList
    println(lines.size + " words parsed.")
    lines.map(_.toUpperCase(Locale.getDefault)).sorted
  }

  type AnagramMap = Map[AnagramUtil.Key, List[String]]
  private def generateAnagramMap(): AnagramMap = {
    println("Calculating anagram keys...")
    allWords.groupBy(AnagramUtil.getKey(_))
  }

  private def makeTerms(): Set[Term] = {
    println("Calculating gematria numbers...")
    allWords.map(new Term(_)).toSet
  }

  var terms: Set[Term] = makeTerms
  def updateTerms(newTerm: Term): Unit = terms = terms ++ Set(newTerm)
}
