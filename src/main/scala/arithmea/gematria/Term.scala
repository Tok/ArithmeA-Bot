package arithmea.gematria

import arithmea.util.GematriaUtil
import arithmea.util.TransliterationUtil

class Term(val latinString: String) {
  if (latinString.length <= 0) {
    throw new IllegalArgumentException("Fail: String has no letters.");
  }

  val hebrewString: String = TransliterationUtil.getHebrew(latinString)
  val values: Map[Method, Int] = GematriaUtil.getValues(latinString)
}
