package arithmea.gematria

import arithmea.bot.IrcColor

sealed abstract class Reason(val color: IrcColor)

object Reason {
  case object MASTER_NUMBER extends Reason(IrcColor.RED)
  case object SUPER_MASTER_NUMBER extends Reason(IrcColor.ORANGE)
  case object HYPER_MASTER_NUMBER extends Reason(IrcColor.ORANGE)
  case object FIBONACCI_NUMBER extends Reason(IrcColor.GREEN)
  case object CUBE_NUMBER extends Reason(IrcColor.YELLOW)
  case object WEIRD_NUMBER extends Reason(IrcColor.TEAL)
  case object VERY_EVEN extends Reason(IrcColor.BLUE)
  case object SPECIAL_SQUARE extends Reason(IrcColor.YELLOW)

  case object FOUR_TWENTY extends Reason(IrcColor.DARKGREEN)
  case object BICYCLE_DAY extends Reason(IrcColor.TEAL)
  case object LEET extends Reason(IrcColor.DARKBLUE)
  case object ITS_OVER extends Reason(IrcColor.CYAN)

  case object SPRING_EQUINOX extends Reason(IrcColor.PURPLE)
  case object GREAT_WORK extends Reason(IrcColor.PURPLE)
  case object WORD_OF_FLIGHT extends Reason(IrcColor.PURPLE)
  case object BEAST extends Reason(IrcColor.PURPLE)
  case object NU extends Reason(IrcColor.PURPLE)
  case object HAD extends Reason(IrcColor.PURPLE)
  case object KEY extends Reason(IrcColor.PURPLE)

  case object SEVENTY_TWO extends Reason(IrcColor.PURPLE)
  case object NINETY_THREE extends Reason(IrcColor.PURPLE)
  case object ONEHUNDRED_AND_FIFTY_SIX extends Reason(IrcColor.PURPLE)
  case object TWOHUNDRED_AND_SEVEN extends Reason(IrcColor.PURPLE)
  case object THREEHUNDRED_AND_FOURTY_ONE extends Reason(IrcColor.PURPLE)
  case object ONE_SEVEN_SEVEN_SIX extends Reason(IrcColor.PURPLE)
}
