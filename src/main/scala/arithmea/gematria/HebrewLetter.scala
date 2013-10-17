package arithmea.gematria

sealed abstract class HebrewLetter(val chr: Char, val isFinal: Boolean, val ordinal: Int, val full: Int, val katan: Int) extends Letter {
  override def valueFor(m: Method): Int = {
    m match {
      case Method.Ordinal => ordinal
      case Method.Full => full
      case Method.Katan => katan
      case _ => throw new IllegalArgumentException("Method is not hebrew: " + m)
    }
  }
}

object HebrewLetter {
  case object ALEPH extends HebrewLetter('\u05D0', false, 1, 1, 1)
  case object BETH extends HebrewLetter('\u05D1', false, 2, 2, 2)
  case object GIMEL extends HebrewLetter('\u05D2', false, 3, 3, 3)
  case object DALETH extends HebrewLetter('\u05D3', false, 4, 4, 4)
  case object HEH extends HebrewLetter('\u05D4', false, 5, 5, 5)
  case object VAV extends HebrewLetter('\u05D5', false, 6, 6, 6)
  case object ZAIN extends HebrewLetter('\u05D6', false, 7, 7, 7)
  case object CHETH extends HebrewLetter('\u05D7', false, 8, 8, 8)
  case object TETH extends HebrewLetter('\u05D8', false, 9, 9, 9)
  case object YUD extends HebrewLetter('\u05D9', false, 10, 10, 1)
  case object KAPH extends HebrewLetter('\u05DB', false, 11, 20, 2)
  case object LAMED extends HebrewLetter('\u05DC', false, 12, 30, 3)
  case object MEM extends HebrewLetter('\u05DE', false, 13, 40, 4)
  case object NUN extends HebrewLetter('\u05E0', false, 14, 50, 5)
  case object SAMEKH extends HebrewLetter('\u05E1', false, 15, 60, 6)
  case object AYIN extends HebrewLetter('\u05E2', false, 16, 70, 7)
  case object PEH extends HebrewLetter('\u05E4', false, 17, 80, 8)
  case object TZADDI extends HebrewLetter('\u05E6', false, 18, 90, 9) // !*
  case object QOPH extends HebrewLetter('\u05E7', false, 19, 100, 1)
  case object RESH extends HebrewLetter('\u05E8', false, 20, 200, 2)
  case object SHIN extends HebrewLetter('\u05E9', false, 21, 300, 3)
  case object TAV extends HebrewLetter('\u05EA', false, 22, 400, 4)
  case object KAPH_FINAL extends HebrewLetter('\u05DA', true, 23, 500, 5)
  case object MEM_FINAL extends HebrewLetter('\u05DD', true, 24, 600, 6)
  case object NUN_FINAL extends HebrewLetter('\u05DF', true, 25, 700, 7)
  case object PEH_FINAL extends HebrewLetter('\u05E3', true, 26, 800, 8)
  case object TZADDI_FINAL extends HebrewLetter('\u05E5', true, 27, 900, 9)
  val values: List[HebrewLetter] = List(
    ALEPH, BETH, GIMEL, DALETH, HEH, VAV, ZAIN, CHETH, TETH, YUD, KAPH,
    LAMED, MEM, NUN, SAMEKH, AYIN, PEH, TZADDI, QOPH, RESH, SHIN, TAV,
    KAPH_FINAL, MEM_FINAL, NUN_FINAL, PEH_FINAL, TZADDI_FINAL)
  def valueOf(c: Char): Option[HebrewLetter] = values.find(_.chr == c)
}
