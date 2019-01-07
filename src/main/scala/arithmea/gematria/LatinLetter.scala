package arithmea.gematria

/**
 * Thanks to Alien696 for the EQ cipher.
 */
sealed abstract class LatinLetter(val chal: Int, val pyth: Int, val ia: Int, val naeq: Int, val tq: Int, val ger: Int, val eq: Int) extends Letter {
  override def valueFor(m: Method): Int = {
    m match {
      case Method.Chaldean => chal
      case Method.Pythagorean => pyth
      case Method.IA => ia
      case Method.NAEQ => naeq
      case Method.TQ => tq
      case Method.German => ger
      case Method.EQ => eq //The Azure Lidded Woman Qabalah’s cipher
      case _ => throw new IllegalArgumentException("Method is not latin: " + m)
    }
  }
}

object LatinLetter {
  case object A extends LatinLetter(1, 1, 1, 1, 5, 1, 1)
  case object B extends LatinLetter(2, 2, 2, 20, 20, 0, 200)
  case object C extends LatinLetter(3, 3, 3, 13, 2, 0, 40)
  case object D extends LatinLetter(4, 4, 4, 6, 23, 0, 6)
  case object E extends LatinLetter(5, 5, 5, 25, 13, 2, 700)
  case object F extends LatinLetter(8, 6, 6, 18, 12, 0, 90)
  case object G extends LatinLetter(3, 7, 7, 11, 11, 7, 20)
  case object H extends LatinLetter(5, 8, 8, 4, 3, 8, 4)
  case object I extends LatinLetter(1, 9, 9, 23, 0, 3, 500)
  case object J extends LatinLetter(1, 1, 10, 16, 7, 0, 70)
  case object K extends LatinLetter(2, 2, 11, 9, 17, 0, 9)
  case object L extends LatinLetter(3, 3, 12, 2, 1, 9, 2)
  case object M extends LatinLetter(4, 4, 13, 21, 21, 0, 300)
  case object N extends LatinLetter(5, 5, 14, 14, 24, 0, 50)
  case object O extends LatinLetter(7, 6, 15, 19, 10, 4, 7)
  case object P extends LatinLetter(8, 7, 16, 26, 4, 0, 800)
  case object Q extends LatinLetter(1, 8, 17, 17, 16, 0, 100)
  case object R extends LatinLetter(2, 9, 18, 12, 14, 0, 30)
  case object S extends LatinLetter(3, 1, 19, 5, 15, 6, 5)
  case object T extends LatinLetter(4, 2, 20, 24, 9, 0, 600)
  case object U extends LatinLetter(6, 3, 21, 17, 25, 5, 80)
  case object V extends LatinLetter(6, 4, 22, 10, 22, 0, 10)
  case object W extends LatinLetter(6, 5, 23, 3, 8, 0, 3)
  case object X extends LatinLetter(5, 6, 24, 22, 6, 0, 400)
  case object Y extends LatinLetter(1, 7, 25, 15, 18, 7, 60)
  case object Z extends LatinLetter(7, 8, 26, 8, 19, 0, 8)
  val values: List[LatinLetter] = List(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z)
  def valueOf(name: Char): Option[LatinLetter] = values.find(_.toString.equalsIgnoreCase(name.toString))
}
