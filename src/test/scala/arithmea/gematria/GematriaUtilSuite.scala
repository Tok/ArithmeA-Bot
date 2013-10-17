package arithmea.gematria

import arithmea.AbstractTester

class GematriaUtilSuite extends AbstractTester {
  val ABC = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
  val ARITHMEA = "ArithmeA"
  val DER_SCHLUESSEL = "Der Schluessel"

  test("Chaldean") {
    assert(new Term(ABC).values.get(Method.Chaldean).get === 103)
    assert(new Term(ARITHMEA).values.get(Method.Chaldean).get === 23)
    assert(new Term(DER_SCHLUESSEL).values.get(Method.Chaldean).get === 50)
  }

  test("Pythagorean") {
    assert(new Term(ABC).values.get(Method.Pythagorean).get === 126)
    assert(new Term(ARITHMEA).values.get(Method.Pythagorean).get === 39)
    assert(new Term(DER_SCHLUESSEL).values.get(Method.Pythagorean).get === 51)
  }

  test("Simple English Gematria") {
    assert(new Term(ABC).values.get(Method.IA).get === 351)
    assert(new Term(ARITHMEA).values.get(Method.IA).get === 75)
    assert(new Term(DER_SCHLUESSEL).values.get(Method.IA).get === 150)
  }

  test("New Aeon English Qabalah") {
    assert(new Term(ABC).values.get(Method.NAEQ).get === 361)
    assert(new Term(ARITHMEA).values.get(Method.NAEQ).get === 111)
    assert(new Term(DER_SCHLUESSEL).values.get(Method.NAEQ).get === 146)
  }

  test("Trigrammaton Qabalah") {
    assert(new Term(ABC).values.get(Method.TQ).get === 351)
    assert(new Term(ARITHMEA).values.get(Method.TQ).get === 96)
    assert(new Term(DER_SCHLUESSEL).values.get(Method.TQ).get === 153)
  }

  test("German") {
    assert(new Term(DER_SCHLUESSEL).values.get(Method.German).get === 55)
  }

  test("Azure Lidded Woman Cipher") {
    assert(new Term("AADHAAR").values.get(Method.EQ).get === 44)
    assert(new Term("VOODOO").values.get(Method.EQ).get === 44)
    assert(new Term("BLOOD").values.get(Method.EQ).get === 222)
    assert(new Term("WILL").values.get(Method.EQ).get === 507)
    assert(new Term("ABRAXAS").values.get(Method.EQ).get === 638)
    assert(new Term("BABALON").values.get(Method.EQ).get === 461)
  }

  test("Full Values") {
    assert(new Term(DER_SCHLUESSEL).values.get(Method.Full).get === 577)
  }

  test("Ordinal Values") {
    assert(new Term(DER_SCHLUESSEL).values.get(Method.Ordinal).get === 82)
  }

  test("Katan Values") {
    assert(new Term(DER_SCHLUESSEL).values.get(Method.Katan).get === 28)
  }
}
