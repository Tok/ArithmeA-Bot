package arithmea.gematria

sealed abstract class Method(val methodType: MethodType)

object Method {
  case object Full extends Method(MethodType.HEBREW)
  case object Ordinal extends Method(MethodType.HEBREW)
  case object Katan extends Method(MethodType.HEBREW)
  case object Chaldean extends Method(MethodType.LATIN)
  case object Pythagorean extends Method(MethodType.LATIN)
  case object IA extends Method(MethodType.LATIN)
  case object NAEQ extends Method(MethodType.LATIN)
  case object TQ extends Method(MethodType.LATIN)
  case object German extends Method(MethodType.LATIN)
  case object EQ extends Method(MethodType.LATIN)
  val hebrewValues: List[Method] = List(Full, Ordinal, Katan)
  val latinValues: List[Method] = List(Chaldean, Pythagorean, IA, NAEQ, TQ, German, EQ)
  val values: List[Method] = latinValues ++ hebrewValues
  def valueOf(name: String): Option[Method] = values.find(_.toString.equalsIgnoreCase(name))
}
