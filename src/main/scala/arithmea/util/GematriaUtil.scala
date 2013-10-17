package arithmea.util

import arithmea.gematria.Method
import arithmea.gematria.HebrewLetter
import arithmea.gematria.LatinLetter
import arithmea.gematria.Letter
import java.util.Locale
import scala.annotation.tailrec

object GematriaUtil {
  val marker = '\u0000'

  def getValues(latin: String): Map[Method, Int] = {
    val latinChars: Vector[Char] = latin.toUpperCase(Locale.getDefault).toCharArray.toList.toVector
    val hebrewChars = getHebrew(latin).toCharArray.toList.toVector
    def makeSum(opts: Vector[Option[Letter]], m: Method): Int = {
      opts.filterNot(_ == None).map(_.get).map(_.valueFor(m)).sum
    }
    def getLatinFor(m: Method): Int = makeSum(latinChars.map(LatinLetter.valueOf(_)), m)
    def getHebrewFor(m: Method): Int = makeSum(hebrewChars.map(HebrewLetter.valueOf(_)), m)
    val latinMap = Method.latinValues.map(m => (m, getLatinFor(m))).toMap
    val hebrewMap = Method.hebrewValues.map(m => (m, getHebrewFor(m))).toMap
    latinMap ++ hebrewMap
  }

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
    current match {
      case 'A' => ret(HebrewLetter.ALEPH)
      case 'B' => ret(HebrewLetter.BETH)
      case 'C' => next match {
        case 'H' => (HebrewLetter.CHETH.chr, 1)
        case 'C' | 'K' => (marker, 1)
        case '-' => ret(HebrewLetter.KAPH_FINAL)
        case _ => afterNext match {
          case ' ' | '\u0000' => ret(HebrewLetter.KAPH_FINAL)
          case _ => ret(HebrewLetter.KAPH)
        }
      }
      case 'D' => ret(HebrewLetter.DALETH)
      case 'E' => next match {
        case 'E' => (HebrewLetter.HEH.chr, 1)
        case _ => (marker, 0)
      }
      case 'F' => ret(HebrewLetter.PEH)
      case 'G' => ret(HebrewLetter.GIMEL)
      case 'H' => ret(HebrewLetter.HEH)
      case 'I' => ret(HebrewLetter.YUD)
      case 'J' => ret(HebrewLetter.GIMEL)
      case 'K' => next match {
        case ' ' | '-' | '\u0000' => ret(HebrewLetter.KAPH_FINAL)
        case _ => ret(HebrewLetter.KAPH)
      }
      case 'L' => ret(HebrewLetter.LAMED)
      case 'M' => next match {
        case ' ' | '-' | '\u0000' => ret(HebrewLetter.MEM_FINAL)
        case _ => ret(HebrewLetter.MEM)
      }
      case 'N' => next match {
        case ' ' | '-' | '\u0000' => ret(HebrewLetter.NUN_FINAL)
        case _ => ret(HebrewLetter.NUN)
      }
      case 'O' => next match {
        case 'O' | 'U' => (HebrewLetter.AYIN.chr, 1)
        case _ => if (isFirst) { ret(HebrewLetter.AYIN) } else { ret(HebrewLetter.VAV) }
      }
      case 'P' => next match {
        case 'H' => afterNext match {
          case ' ' | '-' | '\u0000' => (HebrewLetter.PEH_FINAL.chr, 1)
          case _ => (HebrewLetter.PEH.chr, 1)
        }
        case ' ' | '-' | '\u0000' => ret(HebrewLetter.PEH_FINAL)
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
          case ' ' | '-' | '\u0000' => (HebrewLetter.TZADDI_FINAL.chr, 1)
          case _ => (HebrewLetter.TZADDI.chr, 1)
        }
        case 'H' => (HebrewLetter.TAV.chr, 1)
        case 'S' => (HebrewLetter.ZAIN.chr, 1)
        case _ => ret(HebrewLetter.TETH)
      }
      case 'U' | 'V' | 'W' => ret(HebrewLetter.VAV)
      case 'X' => next match {
        case ' ' | '-' | '\u0000' => ret(HebrewLetter.TZADDI_FINAL)
        case _ => ret(HebrewLetter.TZADDI)
      }
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
