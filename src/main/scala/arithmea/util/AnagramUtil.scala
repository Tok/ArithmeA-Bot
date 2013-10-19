package arithmea.util

import arithmea.Globals
import java.util.Locale

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
    def generateAnagrams(): List[List[String]] = {
      def combinations(key: Key): List[Key] = {
        def stretchKey(k: Key): Key = {
          if (!k.isEmpty) {
            val (hit, other) = k.partition(_.cnt > 1)
            if (hit.isEmpty) { other }
            else {
              Entry(hit.head.chr, 1) :: stretchKey(Entry(hit.head.chr, hit.head.cnt - 1) :: other.tail ::: other)
            }
          } else { List.empty }
        }
        def compress(keyList: List[Key]): List[Key] = {
          def compressKey(key: Key): Key = {
            if (!key.isEmpty) {
              List(Entry(key.head.chr, key.count(_.chr == key.head.chr))) :::
                compressKey(key.filterNot(_.chr == key.head.chr)).sorted
            } else { List.empty }
          }
          if (!keyList.isEmpty) { keyList.map(k => compressKey(k.sorted)) } else { List.empty }
        }
        val combos = if (!key.isEmpty) {
          val flat = stretchKey(key)
          compress((1 to flat.size).flatMap(flat.combinations).toList)
        } else { Nil }
        (List(Nil) ::: combos).distinct
      }
      def comb(l: List[String], n: Int): List[List[String]] = {
        if (n == 0) { List(List.empty) } else l.flatMap(w => comb(l.dropWhile(w != _), n - 1).map(w :: _))
      }
      val wordList = List(word.replaceAll("^A-Z", ""))
      if (!wordList.isEmpty) {
        val key = getKey(wordList.mkString)
        val words = combinations(key).flatMap(Globals.anagramMap.get(_).getOrElse(List.empty)).toList
        val anagrams = (1 to maxWords).flatMap(comb(words, _))
        anagrams.filter(f => getKey(f.mkString) == key).flatMap(_.permutations).toList
      } else { List.empty }
    }
    generateAnagrams.map(_.sorted).distinct.map(_.mkString(" "))
  }
}
