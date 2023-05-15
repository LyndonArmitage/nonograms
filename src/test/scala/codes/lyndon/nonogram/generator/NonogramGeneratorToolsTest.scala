package codes.lyndon.nonogram.generator

import codes.lyndon.nonogram.{Blank, Grid, NonogramTestExamples, Occupied}
import org.scalatest.funsuite.AnyFunSuite

class NonogramGeneratorToolsTest extends AnyFunSuite {

  test("works on example solution") {

    val grid = NonogramTestExamples.exampleSolution

    val (horizontalHints, verticalHints) =
      NonogramGeneratorTools.generateHints(grid)

    assert(horizontalHints != null)
    assert(verticalHints != null)

    assert(horizontalHints.hints.length == 8)
    assert(verticalHints.hints.length == 11)

    assert(horizontalHints == NonogramTestExamples.exampleHorzHints)
    assert(verticalHints == NonogramTestExamples.exampleVertHints)

  }

  test("isAmbiguousGrid works on example grid") {
    val grid = NonogramTestExamples.exampleSolution
    assert(!NonogramGeneratorTools.isAmbiguousGrid(grid))
  }

  test("isAmbiguousGrid works on ambiguous grids") {
    val grids = Seq(
      Grid(
        Seq(Occupied, Blank),
        Seq(Blank, Occupied)
      ),
      Grid(
        Seq(Blank, Occupied),
        Seq(Occupied, Blank)
      )
    )

    grids.foreach(grid => assert(NonogramGeneratorTools.isAmbiguousGrid(grid)))
  }
}
