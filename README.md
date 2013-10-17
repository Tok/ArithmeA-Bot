# Simple IRC Bot for ArithmeA 2000 Gematria.

This is an IRC-Bot with the functionality of Arithmea 2000.
The original application can be found at: https://github.com/Tok/ArithmeA-2000

#### Frameworks and Technologies:
* Scala 2.10
* PircBot
* ScalaTest
* Maven

#### Methods working on the latin alphabet
The following methods work on the 26 letters that are used in the latin (or english) alphabet.
 
##### Chaldean:
Shows the value of the word in chaldean numerology. The result is not reduced in order to preserve the full information.
http://en.wikipedia.org/wiki/Arithmancy#The_Chaldean_method
 
##### Pythagorean:
Shows the value of the word in pythagorean (or agrippan) numerology. The result is not reduced in order to preserve the full information.
http://en.wikipedia.org/wiki/Arithmancy#The_Agrippan_method

##### IA:
1=A, 2=B, 3=C .. 26=Z. Also known as Simple English Gematria.
http://wmjas.wikidot.com/simple-english-gematria
There is another system of english gematria, that uses the sixfold of every letter: A=6, B=12, C=18 .. Z=156. It matches the same words as simple gematria, but yields higher values. ArithmeA prefers simple english gematria, because it can output odd numbers and therefore preserves the original number qualities better.
 
##### NAEQ:
New Aeon English Qabalah
http://en.wikipedia.org/wiki/English_Qabalah#ALW_Cipher
The NAEQ Cipher can be obtained, by arranging the letters of the alphabet on an elevenfold star.
 
##### TQ:
Trigrammaton Qabalah
http://en.wikipedia.org/wiki/English_Qabalah#Trigrammaton_Qabalah_.28TQ.29
 
##### German:
A cipher specific to the german language, that was discovered by Heinz Borchardt.
http://www.rolf-keppler.de/schluessel.htm
The interpretation of the results for this cipher is very different from the other methods and generally leads to low numbers and a lot of matches because most letters have a value of 0. The number obtained by applying this method is supposed to directly describe the quality of german words.
 
##### EQ:
English Qabalah method that uses the same sequence as NAEQ, but without reducing the Values. This method is also known as EQ26 or Azure Lidded Woman Cipher (thanks to Alien696).

#### Methods working on the hebrew transliteration
In a first step, the words are transliterated into a representation with hebrew letters according to a predefined method. The method of transliteration is misusing hebrew consonants as if they were vowels.
Transliteration is different from translation and results in words and accumulations of letters, that most likey don't have any meaning in hebrew. There are different possibilities on how the transliteration may be performed.
http://en.wikipedia.org/wiki/Romanization_of_Hebrew#How_to_transliterate
 
If you want to know the exact method, you may have a look at the sourcecode of the class arithmea.shared.gematria.GematriaUtil in the repository that is referenced at the bottom of this page.
 
##### Full:
Uses absolute or normative numerical value of the twenty-two hebrew letters. This method may be refered to as Mispar Hechrachi or Mispar ha-Panim.
 
##### Ordinal:
Each of the 22 hebrew letters are given a value from one to twenty-two. Also known as Mispar Siduri.
 
##### Katan:
Calculates the value of each hebrew letter, but truncates all of the zeros. It is also sometimes called Mispar Me'ugal.
http://en.wikipedia.org/wiki/Gematria#Methods
