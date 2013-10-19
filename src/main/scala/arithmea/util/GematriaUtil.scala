package arithmea.util

import arithmea.gematria.Method
import arithmea.gematria.HebrewLetter
import arithmea.gematria.LatinLetter
import arithmea.gematria.Letter
import java.util.Locale
import scala.annotation.tailrec

object GematriaUtil {
  def getValues(latin: String): Map[Method, Int] = {
    val latinChars: Vector[Char] = latin.toUpperCase(Locale.getDefault).toCharArray.toList.toVector
    val hebrewChars = TransliterationUtil.getHebrew(latin).toCharArray.toList.toVector
    def makeSum(opts: Vector[Option[Letter]], m: Method): Int = {
      opts.filterNot(_ == None).map(_.get).map(_.valueFor(m)).sum
    }
    def getLatinFor(m: Method): Int = makeSum(latinChars.map(LatinLetter.valueOf(_)), m)
    def getHebrewFor(m: Method): Int = makeSum(hebrewChars.map(HebrewLetter.valueOf(_)), m)
    val latinMap = Method.latinValues.map(m => (m, getLatinFor(m))).toMap
    val hebrewMap = Method.hebrewValues.map(m => (m, getHebrewFor(m))).toMap
    latinMap ++ hebrewMap
  }

  @tailrec
  def reduce(v: Int): Int = {
    val red = v.toString.map(_.asDigit).foldLeft(0)(_ + _)
    if (red.toString.size > 1) { reduce(red) } else { red }
  }
}
