package arithmea.tarot

sealed abstract class Spread(val layout: Vector[String]) {
  val count: Int = layout.size
}

object Spread {
  private val Future = "Future"
  private val Environment = "Environment"
  private val Question = "Question"
  case object Trump extends Spread(Vector("Trump"))
  case object Single extends Spread(Vector("Single"))
  case object Time extends Spread(Vector("Past", "Present", Future))
  case object Partnership extends Spread(Vector("Me", "Partner", Environment))
  case object Action extends Spread(Vector("Pro", "Contra", "Outcome"))
  case object Compass extends Spread(Vector(Question, "Near Future", Environment, Future))
  case object Cross extends Spread(Vector(Question, "Avoid This", "Do This", Future))
  val values: List[Spread] = List(Trump, Single, Time, Partnership, Action, Compass, Cross)
  def valueOf(name: String): Option[Spread] = values.find(_.toString.equalsIgnoreCase(name))
}
