package arithmea.tarot

sealed abstract class Card(val number: Int, val isTrump: Boolean, val meaning: String)

object Card {
  case object The_Fool extends Card(0, true, "new beginning, infinite possibilities")
  case object The_Magus extends Card(1, true, "willpower and creativity")
  case object The_Priestess extends Card(2, true, "intuition")
  case object The_Empress extends Card(3, true, "creation")
  case object The_Emperor extends Card(4, true, "power and authority")
  case object The_Hierophant extends Card(5, true, "education, knowledge, discipline")
  case object The_Lovers extends Card(6, true, "love, romance, fulfilment and peace")
  case object The_Chariot extends Card(7, true, "concentration, willpower and determination")
  case object Justice extends Card(8, true, "fairness, impartiality and balance")
  case object The_Hermit extends Card(9, true, "solitude, contemplation and thought")
  case object Fortune extends Card(10, true, "twists of faith and unexpected changes")
  case object Strength extends Card(11, true, "strength and courage")
  case object The_Hanged_Man extends Card(12, true, "change of perspective and punishment")
  case object Death extends Card(13, true, "endings and transformation")
  case object Temperance extends Card(14, true, "balance and union of opposites")
  case object The_Devil extends Card(15, true, "addiction and obsession")
  case object The_Tower extends Card(16, true, "sudden change and release of tension")
  case object The_Star extends Card(17, true, "spiritual renewal, harmony and hope")
  case object The_Moon extends Card(18, true, "mystery, secrets and confusion")
  case object The_Sun extends Card(19, true, "rationality and inner light")
  case object Judgement extends Card(20, true, "rebirth and ressurection")
  case object The_World extends Card(21, true, "completion")

  case object Ace_of_Wands extends Card(-1, false, "inspiration, passion and new enthusiasm")
  case object Two_of_Wands extends Card(-1, false, "boldness and personal power")
  case object Three_of_Wands extends Card(-1, false, "foresight and strategy")
  case object Four_of_Wands extends Card(-1, false, "congratulation and celebration")
  case object Five_of_Wands extends Card(-1, false, "competition and discord")
  case object Six_of_Wands extends Card(-1, false, "victory and triumph")
  case object Seven_of_Wands extends Card(-1, false, "assertion, courage and defiance")
  case object Eight_of_Wands extends Card(-1, false, "acceleration, direction and purpose ")
  case object Nine_of_Wands extends Card(-1, false, "perseverance and defense")
  case object Ten_of_Wands extends Card(-1, false, "overextension")
  case object Page_of_Wands extends Card(-1, false, "yearning for new approaches")
  case object Knight_of_Wands extends Card(-1, false, "enthusiasm and restless overcommitment")
  case object Queen_of_Wands extends Card(-1, false, "happiness, warmth and openness")
  case object King_of_Wands extends Card(-1, false, "leadership and confidence")

  case object Ace_of_Cups extends Card(-1, false, "empathy, emotion and passion")
  case object Two_of_Cups extends Card(-1, false, "emotianal connection and interaction")
  case object Three_of_Cups extends Card(-1, false, "bonding and support")
  case object Four_of_Cups extends Card(-1, false, "melancholy and obstinacy")
  case object Five_of_Cups extends Card(-1, false, "failure and emotional loss")
  case object Six_of_Cups extends Card(-1, false, "kindness and compassion")
  case object Seven_of_Cups extends Card(-1, false, "imagination and illusion")
  case object Eight_of_Cups extends Card(-1, false, "search and new direction")
  case object Nine_of_Cups extends Card(-1, false, "indulgence, contentment and self-satisfaction")
  case object Ten_of_Cups extends Card(-1, false, "fulfillment, joy and serenity")
  case object Page_of_Cups extends Card(-1, false, "intuition, spiritual awareness and imagination")
  case object Knight_of_Cups extends Card(-1, false, "idealism")
  case object Queen_of_Cups extends Card(-1, false, "sensitivity and dedication")
  case object King_of_Cups extends Card(-1, false, "dignity and diplomacy")

  case object Ace_of_Swords extends Card(-1, false, "dispelling illusions and clarity of thought")
  case object Two_of_Swords extends Card(-1, false, "incedision, compromise and truce")
  case object Three_of_Swords extends Card(-1, false, "betrayal, heartbreak and sorrow")
  case object Four_of_Swords extends Card(-1, false, "recuperation rest and retreat")
  case object Five_of_Swords extends Card(-1, false, "victory and revenge")
  case object Six_of_Swords extends Card(-1, false, "objectivity and science")
  case object Seven_of_Swords extends Card(-1, false, "risk and dishonesty")
  case object Eight_of_Swords extends Card(-1, false, "hopelessness and restriction")
  case object Nine_of_Swords extends Card(-1, false, "despair and anxiety")
  case object Ten_of_Swords extends Card(-1, false, "failure, loss and defeat")
  case object Page_of_Swords extends Card(-1, false, "vigilance and secret activities")
  case object Knight_of_Swords extends Card(-1, false, "vehemence and persuasion")
  case object Queen_of_Swords extends Card(-1, false, "seperation, independence and barrenness")
  case object King_of_Swords extends Card(-1, false, "integrity and authority")

  case object Ace_of_Disks extends Card(-1, false, "pragmatism and realism")
  case object Two_of_Disks extends Card(-1, false, "flexibility and adaptiveness")
  case object Three_of_Disks extends Card(-1, false, "cooperation and venture")
  case object Four_of_Disks extends Card(-1, false, "financial stability, economic security and protectiveness")
  case object Five_of_Disks extends Card(-1, false, "loss, adversity and depression")
  case object Six_of_Disks extends Card(-1, false, "generosity, charity and philanthropy")
  case object Seven_of_Disks extends Card(-1, false, "disillusionment and reassessment")
  case object Eight_of_Disks extends Card(-1, false, "competence and diligence")
  case object Nine_of_Disks extends Card(-1, false, "abundance, refinement and fulfillment")
  case object Ten_of_Disks extends Card(-1, false, "wealth and inheritance")
  case object Page_of_Disks extends Card(-1, false, "aspiration and opportunity for prosperity")
  case object Knight_of_Disks extends Card(-1, false, "prudence, loyalty and responsibility")
  case object Queen_of_Disks extends Card(-1, false, "fertility, abundance and prosperity")
  case object King_of_Disks extends Card(-1, false, "skill and adeption")

  val values: List[Card] = List(
    The_Fool, The_Magus, The_Priestess, The_Empress, The_Emperor, The_Hierophant,
    The_Lovers, The_Chariot, Justice, The_Hermit, Fortune, Strength, The_Hanged_Man, Death,
    Temperance, The_Devil, The_Tower, The_Star, The_Moon, The_Sun, Judgement, The_World,
    Ace_of_Wands, Two_of_Wands, Three_of_Wands, Four_of_Wands, Five_of_Wands, Six_of_Wands, Seven_of_Wands,
    Eight_of_Wands, Nine_of_Wands, Ten_of_Wands, Page_of_Wands, Knight_of_Wands, Queen_of_Wands, King_of_Wands,
    Ace_of_Cups, Two_of_Cups, Three_of_Cups, Four_of_Cups, Five_of_Cups, Six_of_Cups, Seven_of_Cups,
    Eight_of_Cups, Nine_of_Cups, Ten_of_Cups, Page_of_Cups, Knight_of_Cups, Queen_of_Cups, King_of_Cups,
    Ace_of_Swords, Two_of_Swords, Three_of_Swords, Four_of_Swords, Five_of_Swords, Six_of_Swords, Seven_of_Swords,
    Eight_of_Swords, Nine_of_Swords, Ten_of_Swords, Page_of_Swords, Knight_of_Swords, Queen_of_Swords, King_of_Swords,
    Ace_of_Disks, Two_of_Disks, Three_of_Disks, Four_of_Disks, Five_of_Disks, Six_of_Disks, Seven_of_Disks,
    Eight_of_Disks, Nine_of_Disks, Ten_of_Disks, Page_of_Disks, Knight_of_Disks, Queen_of_Disks, King_of_Disks)
}
