package arithmea.util

import arithmea.gematria.Method
import arithmea.gematria.HebrewLetter
import arithmea.gematria.LatinLetter
import arithmea.gematria.Letter
import java.util.Locale
import scala.annotation.tailrec

object TransliterationUtil {
  val marker = '\u0000'

  def getHebrew(latin: String): String = {
    val latinChars: Vector[Char] = latin.toUpperCase(Locale.getDefault).toCharArray.toList.toVector
    def current(i: Int): Char = if (i < latinChars.length) { latinChars(i) } else { marker }
    def next(i: Int): Char = if (i + 1 < latinChars.length) { latinChars(i + 1) } else { marker }
    def after(i: Int): Char = if (i + 2 < latinChars.length) { latinChars(i + 2) } else { marker }
    @tailrec
    def generate(i: Int, accu: List[Char]): List[Char] = {
      val (c, skip) = getHebrewChar(current(i), next(i), after(i), i == 0)
      if (i >= latinChars.length) { accu } else { generate(i + skip + 1, accu ++ List(c)) }
    }
    generate(0, Nil).filterNot(_ == marker).mkString
  }

  private def getHebrewChar(current: Char, next: Char, afterNext: Char, isFirst: Boolean): (Char, Int) = {
    def ret(hl: HebrewLetter): (Char, Int) = (hl.chr, 0)
    def finalIfNotFirst(hl: HebrewLetter): HebrewLetter = {
      if (isFirst) { hl } else { HebrewLetter.getFinalFor(hl) }
    }
    def finalOrFirst(hl: HebrewLetter): (Char, Int) = {
      next match {
        case ' ' | '-' | '\u0000' => ret(finalIfNotFirst(hl))
        case _ => ret(hl)
      }
    }
    current match {
      case 'A' => ret(HebrewLetter.ALEPH)
      case 'B' => ret(HebrewLetter.BETH)
      case 'C' => next match {
        case 'H' => (HebrewLetter.CHETH.chr, 1)
        case 'C' | 'K' =>
          afterNext match {
            case ' ' | '-' | '\u0000' => (finalIfNotFirst(HebrewLetter.KAPH).chr, 1)
            case _ => (HebrewLetter.KAPH.chr, 1)
          }
        case '-' | ' ' | '\u0000' => ret(finalIfNotFirst(HebrewLetter.KAPH))
        case _ => ret(HebrewLetter.KAPH)
      }
      case 'D' => ret(HebrewLetter.DALETH)
      case 'E' => next match {
        case 'E' => (HebrewLetter.HEH.chr, 1)
        case _ => if (isFirst) { ret(HebrewLetter.HEH) } else { (marker, 0) }
      }
      case 'F' => ret(HebrewLetter.PEH)
      case 'G' => ret(HebrewLetter.GIMEL)
      case 'H' => ret(HebrewLetter.HEH)
      case 'I' => ret(HebrewLetter.YUD)
      case 'J' => ret(HebrewLetter.GIMEL)
      case 'K' => finalOrFirst(HebrewLetter.KAPH)
      case 'L' => ret(HebrewLetter.LAMED)
      case 'M' => finalOrFirst(HebrewLetter.MEM)
      case 'N' => finalOrFirst(HebrewLetter.NUN)
      case 'O' => next match {
        case 'O' | 'U' => (HebrewLetter.AYIN.chr, 1)
        case _ => if (isFirst) { ret(HebrewLetter.AYIN) } else { ret(HebrewLetter.VAV) }
      }
      case 'P' => next match {
        case 'H' => afterNext match {
          case ' ' | '-' | '\u0000' => (finalIfNotFirst(HebrewLetter.PEH).chr, 1)
          case _ => (HebrewLetter.PEH.chr, 1)
        }
        case ' ' | '-' | '\u0000' => ret(finalIfNotFirst(HebrewLetter.PEH))
        case _ => ret(HebrewLetter.PEH)
      }
      case 'Q' => next match {
        case 'U' => (HebrewLetter.QOPH.chr, 1)
        case _ => ret(HebrewLetter.QOPH)
      }
      case 'R' => ret(HebrewLetter.RESH)
      case 'S' => next match {
        case 'C' => afterNext match {
          case 'H' => (HebrewLetter.SHIN.chr, 2)
          case _ => ret(HebrewLetter.SAMEKH)
        }
        case 'H' => (HebrewLetter.SHIN.chr, 1)
        case 'S' => (HebrewLetter.ZAIN.chr, 1)
        case _ => ret(HebrewLetter.SAMEKH)
      }
      case 'T' => next match {
        case 'Z' | 'X' => afterNext match {
          case ' ' | '-' | '\u0000' => (finalIfNotFirst(HebrewLetter.TZADDI).chr, 1)
          case _ => (HebrewLetter.TZADDI.chr, 1)
        }
        case 'H' => (HebrewLetter.TAV.chr, 1)
        case 'S' => (HebrewLetter.ZAIN.chr, 1)
        case _ => ret(HebrewLetter.TETH)
      }
      case 'U' | 'V' | 'W' => ret(HebrewLetter.VAV)
      case 'X' => finalOrFirst(HebrewLetter.TZADDI)
      case 'Y' => ret(HebrewLetter.YUD)
      case 'Z' => ret(HebrewLetter.ZAIN)
      case _ => (current, 0)
    }
  }

  @tailrec
  def reduce(v: Int): Int = {
    val red = v.toString.map(_.asDigit).foldLeft(0)(_ + _)
    if (red.toString.size > 1) { reduce(red) } else { red }
  }
}
