package arithmea.util

import arithmea.Globals

object AnagramUtil {
  val maxWords: Int = 4
  val maxSize: Int = 12

  type Key = List[(Char, Int)]

  def getKey(s: String): Key = s.toUpperCase.toList.groupBy(x => x).mapValues(_.size).toList.sorted

  def generateAnagrams(word: String): List[String] = {
    def generateAnagrams(): List[List[String]] = {
      def combinations(key: Key): List[Key] = {
        def stretchKey(k: Key): Key = {
          if (!k.isEmpty) {
            val (hit, other) = k.partition(_._2 > 1)
            if (hit.isEmpty) { other } else {
              ((hit.head._1, 1)) :: stretchKey(((hit.head._1, hit.head._2 - 1)) :: other.tail ::: other)
            }
          } else { List.empty }
        }
        def compress(keyList: List[Key]): List[Key] = {
          def compressKey(key: Key): Key = {
            if (!key.isEmpty) {
              List(((key.head._1, key.count(_._1 == key.head._1)))) :::
                compressKey(key.filterNot(_._1 == key.head._1)).sorted
            } else { List.empty }
          }
          if (!keyList.isEmpty) { keyList.map(k => compressKey(k.sorted)) } else { List() }
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
      val wordList = List(word.replaceAll(" +", ""))
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
