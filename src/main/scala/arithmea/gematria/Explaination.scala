package arithmea.gematria

object Explaination {
  def getFor(m: Method): String = {
    val builder = new StringBuilder
    m match {
      case Method.Chaldean =>
        builder.append("Chaldean Numerology. ")
        builder.append("The result is not reduced in order to preserve the full information, ")
        builder.append("but the number in parentheses is reduced, even if the result is a master number. ")
        builder.append("http://en.wikipedia.org/wiki/Arithmancy#The_Chaldean_method")
      case Method.Pythagorean =>
        builder.append("Pythagorean Numerology (also known as Agrippan Numerology). ")
        builder.append("The result is not reduced in order to preserve the full information, ")
        builder.append("but the number in parentheses is reduced, even if the result is a master number. ")
        builder.append("http://en.wikipedia.org/wiki/Arithmancy#The_Agrippan_method")
      case Method.IA =>
        builder.append("1=A, 2=B, 3=C .. 26=Z. Also known as Simple English Gematria. ")
        builder.append("http://wmjas.wikidot.com/simple-english-gematria ")
        builder.append("There is another system of english gematria, that uses the sixfold of every letter: ")
        builder.append("A=6, B=12, C=18 .. Z=156. It matches the same words and yields higher values ")
        builder.append("but doesn't preserve the number qualities well. ")
        builder.append("The sixfold result is shown in parentheses.")
      case Method.NAEQ =>
        builder.append("New Aeon English Qabalah http://en.wikipedia.org/wiki/English_Qabalah#ALW_Cipher ")
        builder.append("The NAEQ Cipher can be obtained, by arranging the letters of the alphabet ")
        builder.append("on an elevenfold star.")
      case Method.TQ =>
        builder.append("Trigrammaton Qabalah ")
        builder.append("http://en.wikipedia.org/wiki/English_Qabalah#Trigrammaton_Qabalah_.28TQ.29")
      case Method.German =>
        builder.append("A cipher specific to the german language, that was discovered by Heinz Borchardt. ")
        builder.append("http://www.rolf-keppler.de/schluessel.htm ")
        builder.append("The interpretation of the results for this cipher is very different ")
        builder.append("from the other methods and generally leads to low numbers and a lot ")
        builder.append("of matches because most letters have a value of 0. ")
        builder.append("The number obtained by applying this method is supposed to ")
        builder.append("directly describe the quality of german words.")
      case Method.EQ =>
        builder.append("English Qabalah method that uses the same sequence as NAEQ, ")
        builder.append("but without reducing the Values. ")
        builder.append("This method is also known as EQ26 or Azure Lidded Woman Cipher (thanks to Alien696).")
      case Method.Full =>
        builder.append("Transliterates the letters to hebrew and uses their absolute or normative numerical value. ")
        builder.append("This method may be refered to as Mispar Hechrachi or Mispar ha-Panim. ")
        builder.append("http://en.wikipedia.org/wiki/Gematria#Methods")
      case Method.Ordinal =>
        builder.append("Transliterates the letters to hebrew and gives each letter a value from one to twenty-two. ")
        builder.append("Also known as Mispar Siduri. http://en.wikipedia.org/wiki/Gematria#Methods")
      case Method.Katan =>
        builder.append("Transliterates the letters to hebrew and calculates the value of the letters, ")
        builder.append("but truncates all of the zeros. It is also sometimes called Mispar Me'ugal. ")
        builder.append("http://en.wikipedia.org/wiki/Gematria#Methods ")
        builder.append("Metatron declares: Mishpar Katan is important ;)")
    }
    builder.toString
  }

  def transliterationComment(): (String, String) = {
    val firstLine = "In a first step, the words are transliterated into a representation with hebrew letters " +
      "according to a predefined method. The method of transliteration is missusing hebrew consonants " +
      "as if they were vowels. " +
      "Transliteration is different from translation and results in words and accumulations of letters, " +
      "that most likey don't have any meaning in hebrew."
    val secondLine = "There are different possibilities on how the transliteration may be performed. " +
      "http://en.wikipedia.org/wiki/Romanization_of_Hebrew#How_to_transliterate " +
      "If you want to know the exact method, you may look at the sourcecode of ArithmeA."
    (firstLine, secondLine)
  }
}
