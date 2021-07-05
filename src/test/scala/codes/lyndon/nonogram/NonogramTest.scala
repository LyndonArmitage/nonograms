package codes.lyndon.nonogram

import org.scalatest.funsuite.AnyFunSuite

class NonogramTest extends AnyFunSuite {

  test("pretty printing works as expected") {
    val nonogram = Nonogram(
      NonogramTestExamples.exampleGrid,
      NonogramTestExamples.exampleHorzHints,
      NonogramTestExamples.exampleVertHints
    )

    val printed = nonogram.pretty()
    assert(printed == NonogramTestExamples.exampleExpected)
  }

}
