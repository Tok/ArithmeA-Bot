package arithmea.bot

sealed abstract class IrcColor(val irc: Int)

object IrcColor {
  case object WHITE extends IrcColor(0)
  case object BLACK extends IrcColor(1)
  case object DARKBLUE extends IrcColor(2)
  case object DARKGREEN extends IrcColor(3)
  case object RED extends IrcColor(4)
  case object BROWN extends IrcColor(5)
  case object PURPLE extends IrcColor(6)
  case object ORANGE extends IrcColor(7)
  case object YELLOW extends IrcColor(8)
  case object GREEN extends IrcColor(9)
  case object TEAL extends IrcColor(10)
  case object CYAN extends IrcColor(11)
  case object BLUE extends IrcColor(12)
  case object MAGENTA extends IrcColor(13)
  case object GRAY extends IrcColor(14)
  case object LIGHTGRAY extends IrcColor(15)
}
