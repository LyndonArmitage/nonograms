package codes.lyndon.nonogram

import org.scalatest.funsuite.AnyFunSuite

class NonogramTest extends AnyFunSuite {

  test("pretty printing works as expected") {
    val nonogram = Nonogram(
      NonogramTestExamples.exampleGrid,
      NonogramTestExamples.exampleHorzHints,
      NonogramTestExamples.exampleVertHints,
      title = "Wikipedia P Nonogram",
      author = "Unknown",
      solution = Some(NonogramTestExamples.exampleSolution)
    )

    val printed = nonogram.pretty()
    assert(printed == NonogramTestExamples.exampleExpected)
  }

}
