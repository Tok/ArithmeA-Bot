package arithmea.gematria

import arithmea.AbstractTester
import arithmea.util.TransliterationUtil

class TransliterationUtilSuite extends AbstractTester {
  test("Single Letters") {
    assert(TransliterationUtil.getHebrew("A") === HebrewLetter.ALEPH.chr.toString)
    assert(TransliterationUtil.getHebrew("B") === HebrewLetter.BETH.chr.toString)
    assert(TransliterationUtil.getHebrew("G") === HebrewLetter.GIMEL.chr.toString)
    assert(TransliterationUtil.getHebrew("D") === HebrewLetter.DALETH.chr.toString)
    assert(TransliterationUtil.getHebrew("H") === HebrewLetter.HEH.chr.toString)
    assert(TransliterationUtil.getHebrew("V") === HebrewLetter.VAV.chr.toString)
    assert(TransliterationUtil.getHebrew("Z") === HebrewLetter.ZAIN.chr.toString)
    assert(TransliterationUtil.getHebrew("CH") === HebrewLetter.CHETH.chr.toString)
    assert(TransliterationUtil.getHebrew("T") === HebrewLetter.TETH.chr.toString)
    assert(TransliterationUtil.getHebrew("I") === HebrewLetter.YUD.chr.toString)
    assert(TransliterationUtil.getHebrew("K") === HebrewLetter.KAPH.chr.toString)
    assert(TransliterationUtil.getHebrew("L") === HebrewLetter.LAMED.chr.toString)
    assert(TransliterationUtil.getHebrew("M") === HebrewLetter.MEM.chr.toString)
    assert(TransliterationUtil.getHebrew("N") === HebrewLetter.NUN.chr.toString)
    assert(TransliterationUtil.getHebrew("S") === HebrewLetter.SAMEKH.chr.toString)
    assert(TransliterationUtil.getHebrew("O") === HebrewLetter.AYIN.chr.toString)
    assert(TransliterationUtil.getHebrew("P") === HebrewLetter.PEH.chr.toString)
    assert(TransliterationUtil.getHebrew("TZ") === HebrewLetter.TZADDI.chr.toString)
    assert(TransliterationUtil.getHebrew("Q") === HebrewLetter.QOPH.chr.toString)
    assert(TransliterationUtil.getHebrew("R") === HebrewLetter.RESH.chr.toString)
    assert(TransliterationUtil.getHebrew("SH") === HebrewLetter.SHIN.chr.toString)
    assert(TransliterationUtil.getHebrew("TH") === HebrewLetter.TAV.chr.toString)
  }

  test("Empty") {
    assert(TransliterationUtil.getHebrew("") === "")
  }

  test("Cheth and Kaph") {
    assert(TransliterationUtil.getHebrew("CH") === HebrewLetter.CHETH.chr.toString)
    assert(TransliterationUtil.getHebrew("C") === HebrewLetter.KAPH.chr.toString)
    assert(TransliterationUtil.getHebrew("CK") === HebrewLetter.KAPH.chr.toString)
    assert(TransliterationUtil.getHebrew("CC") === HebrewLetter.KAPH.chr.toString)
  }

  test("Heh") {
    assert(TransliterationUtil.getHebrew("H") === HebrewLetter.HEH.chr.toString)
    assert(TransliterationUtil.getHebrew("E") === HebrewLetter.HEH.chr.toString)
    assert(TransliterationUtil.getHebrew("EE") === HebrewLetter.HEH.chr.toString)
  }

  test("Ayin and Vav") {
    assert(TransliterationUtil.getHebrew("U") === HebrewLetter.VAV.chr.toString)
    assert(TransliterationUtil.getHebrew("V") === HebrewLetter.VAV.chr.toString)
    assert(TransliterationUtil.getHebrew("W") === HebrewLetter.VAV.chr.toString)
    assert(TransliterationUtil.getHebrew("O") === HebrewLetter.AYIN.chr.toString)
    assert(TransliterationUtil.getHebrew("OO") === HebrewLetter.AYIN.chr.toString)
    assert(TransliterationUtil.getHebrew("OU") === HebrewLetter.AYIN.chr.toString)
  }

  test("Peh") {
    assert(TransliterationUtil.getHebrew("P") === HebrewLetter.PEH.chr.toString)
    assert(TransliterationUtil.getHebrew("PH") === HebrewLetter.PEH.chr.toString)
  }

  test("Qoph") {
    assert(TransliterationUtil.getHebrew("Q") === HebrewLetter.QOPH.chr.toString)
    assert(TransliterationUtil.getHebrew("QU") === HebrewLetter.QOPH.chr.toString)
  }

  test("Shin, Samekh and Zain") {
    assert(TransliterationUtil.getHebrew("SH") === HebrewLetter.SHIN.chr.toString)
    assert(TransliterationUtil.getHebrew("SCH") === HebrewLetter.SHIN.chr.toString)
    assert(TransliterationUtil.getHebrew("S") === HebrewLetter.SAMEKH.chr.toString)
    assert(TransliterationUtil.getHebrew("Z") === HebrewLetter.ZAIN.chr.toString)
    assert(TransliterationUtil.getHebrew("SS") === HebrewLetter.ZAIN.chr.toString)
  }

  test("Tzaddi, Teth, Zain and Tav") {
    assert(TransliterationUtil.getHebrew("TZ") === HebrewLetter.TZADDI.chr.toString)
    assert(TransliterationUtil.getHebrew("TX") === HebrewLetter.TZADDI.chr.toString)
    assert(TransliterationUtil.getHebrew("X") === HebrewLetter.TZADDI.chr.toString)
    assert(TransliterationUtil.getHebrew("TH") === HebrewLetter.TAV.chr.toString)
    assert(TransliterationUtil.getHebrew("TS") === HebrewLetter.ZAIN.chr.toString)
    assert(TransliterationUtil.getHebrew("T") === HebrewLetter.TETH.chr.toString)
  }

  test("Final Kaph") {
    assert(TransliterationUtil.getHebrew("K") === HebrewLetter.KAPH.chr.toString)
    assert(TransliterationUtil.getHebrew("KA").head === HebrewLetter.KAPH.chr)
    assert(TransliterationUtil.getHebrew("AK").last === HebrewLetter.KAPH_FINAL.chr)
  }

  test("Final Mem") {
    assert(TransliterationUtil.getHebrew("M") === HebrewLetter.MEM.chr.toString)
    assert(TransliterationUtil.getHebrew("MA").head === HebrewLetter.MEM.chr)
    assert(TransliterationUtil.getHebrew("AM").last === HebrewLetter.MEM_FINAL.chr)
    assert(TransliterationUtil.getHebrew("AMA").tail.head === HebrewLetter.MEM.chr)
  }

  test("Final Nun") {
    assert(TransliterationUtil.getHebrew("N") === HebrewLetter.NUN.chr.toString)
    assert(TransliterationUtil.getHebrew("NA").head === HebrewLetter.NUN.chr)
    assert(TransliterationUtil.getHebrew("AN").last === HebrewLetter.NUN_FINAL.chr)
    assert(TransliterationUtil.getHebrew("ANA").tail.head === HebrewLetter.NUN.chr)
  }

  test("Final Peh") {
    assert(TransliterationUtil.getHebrew("P") === HebrewLetter.PEH.chr.toString)
    assert(TransliterationUtil.getHebrew("PH") === HebrewLetter.PEH.chr.toString)
    assert(TransliterationUtil.getHebrew("PA").head === HebrewLetter.PEH.chr)
    assert(TransliterationUtil.getHebrew("PHA").head === HebrewLetter.PEH.chr)
    assert(TransliterationUtil.getHebrew("AP").last === HebrewLetter.PEH_FINAL.chr)
    assert(TransliterationUtil.getHebrew("APH").last === HebrewLetter.PEH_FINAL.chr)
    assert(TransliterationUtil.getHebrew("APA").tail.head === HebrewLetter.PEH.chr)
    assert(TransliterationUtil.getHebrew("APHA").tail.head === HebrewLetter.PEH.chr)
  }

  test("Final Tzaddi") {
    assert(TransliterationUtil.getHebrew("X") === HebrewLetter.TZADDI.chr.toString)
    assert(TransliterationUtil.getHebrew("TZ") === HebrewLetter.TZADDI.chr.toString)
    assert(TransliterationUtil.getHebrew("XA").head === HebrewLetter.TZADDI.chr)
    assert(TransliterationUtil.getHebrew("TZA").head === HebrewLetter.TZADDI.chr)
    assert(TransliterationUtil.getHebrew("AX").last === HebrewLetter.TZADDI_FINAL.chr)
    assert(TransliterationUtil.getHebrew("ATZ").last === HebrewLetter.TZADDI_FINAL.chr)
    assert(TransliterationUtil.getHebrew("AXA").tail.head === HebrewLetter.TZADDI.chr)
    assert(TransliterationUtil.getHebrew("ATZA").tail.head === HebrewLetter.TZADDI.chr)
  }

  test("Words") {
    assert(TransliterationUtil.getHebrew("ARITHMEA") === "אריתמא")
    assert(TransliterationUtil.getHebrew("HERMETICS") === "הרמטיכס")
    assert(TransliterationUtil.getHebrew("TRICKS") === "טריכס")
    assert(TransliterationUtil.getHebrew("ABRAHADABRA") === "אבראהאדאברא")
  }
}
