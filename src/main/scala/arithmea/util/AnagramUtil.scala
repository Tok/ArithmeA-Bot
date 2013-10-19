package arithmea.util

import arithmea.Globals
import java.util.Locale
import scala.annotation.tailrec

object AnagramUtil {
  val maxWords: Int = 4
  val maxSize: Int = 12

  case class Entry(val chr: Char, val cnt: Int) extends Ordered[Entry] {
    def compare(that: Entry): Int = this.chr.compareTo(that.chr)
  }

  type Key = List[Entry]

  def getKey(s: String): Key = {
    val pairs = s.toUpperCase(Locale.getDefault).groupBy(x => x).mapValues(_.size).toList
    pairs.map(x => Entry(x._1, x._2)).sorted
  }

  def generateAnagrams(word: String): List[String] = {
    def combinations(key: Key): List[Key] = {
      key match {
        case Nil => Nil
        case _ =>
          val flat = stretchKey(key)
          val keyList = (1 to flat.size).flatMap(flat.combinations).toList
          compressKeyList(keyList).distinct
      }
    }
    def comb(l: List[String], n: Int): List[List[String]] = {
      n match {
        case 0 => List(List.empty)
        case _ => l.flatMap(w => comb(l.dropWhile(w != _), n - 1).map(w :: _))
      }
    }
    val wordList = List(word.replaceAll("^A-Z", ""))
    wordList match {
      case Nil => List.empty
      case _ =>
        val key = getKey(wordList.mkString)
        val words = combinations(key).flatMap(Globals.anagramMap.get(_).getOrElse(List.empty))
        val anagrams = (1 to maxWords).flatMap(comb(words, _))
        val perms = anagrams.filter(f => getKey(f.mkString) == key).flatMap(_.permutations).toList
        perms.map(_.sorted).distinct.map(_.mkString(" "))
    }
  }

  private def stretchKey(key: Key): Key = {
    key match {
      case Nil => List.empty
      case _ =>
        val (hit, other) = key.partition(_.cnt > 1)
        hit match {
          case Nil => other
          case _ =>
            val h = hit.head
            Entry(h.chr, 1) :: stretchKey(Entry(h.chr, h.cnt - 1) :: other.tail ::: other)
        }
    }
  }

  private def compressKeyList(keyList: List[Key]): List[Key] = {
    def compressKey(key: Key): Key = {
      key match {
        case Nil => List.empty
        case _ =>
          val h = key.head
          val entry = Entry(h.chr, key.count(_.chr == h.chr))
          List(entry) ::: compressKey(key.filterNot(_.chr == h.chr))
      }
    }
    keyList.map(compressKey(_).sorted)
  }
}
