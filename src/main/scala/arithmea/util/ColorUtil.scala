package arithmea.util

import arithmea.gematria.Method
import arithmea.gematria.HebrewLetter
import arithmea.gematria.LatinLetter
import arithmea.gematria.Letter
import arithmea.gematria.Highlight

object ColorUtil {
  val BC = String.valueOf('\u0002') // bold character for IRC
  val CC = String.valueOf('\u0003') // color character for IRC
  val digitRegex = """\d+""".r

  private def colorNumber(n: Int): String = {
    Highlight.values.get(n) match {
      case Some(reason) => CC + reason.color.irc + BC + n + BC + CC
      case None => BC + n.toString + BC
    }
  }

  def colorString(in: String): String = {
    digitRegex.replaceAllIn(in, m => colorNumber(m.matched.toInt))
  }
}
