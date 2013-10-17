package arithmea.gematria

sealed abstract class MethodType

object MethodType {
  case object LATIN extends MethodType
  case object HEBREW extends MethodType
  val values: List[MethodType] = List(LATIN, HEBREW)
}
