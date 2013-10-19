package arithmea.util

import java.io.FileWriter
import java.util.Locale

import arithmea.Globals

object WordlistUtil {
  val lettersAndDashRegex = "[A-Z-]+".r
  val consecutiveRegex = ".*(.)\\1{2,}.*".r
  val dashStartRegex = "^[-].*".r
  val baseNamesRegex = "[ATGC]{5,}".r

  //nice wordlist: http://www.mieliestronk.com/corncob_caps.txt
  //top 10000 words: http://wortschatz.uni-leipzig.de/html/wliste.html
  //ridiculously huge list: http://codehappy.net/wordlist.htm
  //huge wordlist, but with crap: http://www.keithv.com/software/wlist/

  //run JAVA -jar ArithmeA-Bot-1.0-SNAPSHOT-jar-with-dependencies.jar [inputfilename]
  def createWordlist(inputName: String): Unit = {
    println("Reading file: " + inputName)
    val lines = scala.io.Source.fromFile(inputName, Globals.encoding).getLines.toList
    println("Number of lines: " + lines.size)
    val words = lines.map(_.toUpperCase(Locale.getDefault)).distinct
    println("Number of distinct words: " + words.size)

    val letters = words.filter(lettersAndDashRegex.pattern.matcher(_).matches)
    val nonConsec = letters.filterNot(consecutiveRegex.pattern.matcher(_).matches)
    val noDashStart = nonConsec.filterNot(dashStartRegex.pattern.matcher(_).matches)
    val noBaseNames = noDashStart.filterNot(baseNamesRegex.pattern.matcher(_).matches)

    println("Words after filter application: " + noBaseNames.size)
    val sorted = noBaseNames.sorted
    println("Writing to: " + Globals.wordlistName)
    val out = new FileWriter(Globals.wordlistName)
    out.write(sorted.mkString("\n"))
    out.close
    println("Done.")
  }
}
