package codes.lyndon.nonogram.solver

import codes.lyndon.nonogram._

class LogicSolverTest extends NonogramFunSuite {

  private val width  = 10
  private val height = 10

  private val empty = Nonogram(
    grid = Grid.empty(width, height),
    horizontalHints = Hints(),
    verticalHints = Hints()
  )

  private val allCrossed = Nonogram(
    grid = Grid.empty(width, height),
    horizontalHints = Hints(
      0.until(width).map(x => Seq(0)):_*
    ),
    verticalHints = Hints(
      0.until(height).map(x => Seq(0)):_*
    )
  )


  private val allOccupied = Nonogram(
    grid = Grid.empty(width, height),
    horizontalHints = Hints(
      0.until(width).map(x => Seq(height)):_*
    ),
    verticalHints = Hints(
      0.until(height).map(x => Seq(width)):_*
    )
  )

  test("solve full length works as expected on empty") {
    val grid = MutableGrid(empty)
    LogicSolver.solveFullLengths(empty, grid)
    assertEmptyGrid(grid)
    val expected = MutableGrid(width, height)
    expected.fill(Blank)
    assertGrids(expected, grid)
  }

  test("solve full length works as expected on all 0s") {
    val grid = MutableGrid(empty)
    LogicSolver.solveFullLengths(allCrossed, grid)
    assertAllSame(grid, Crossed)
    val expected = MutableGrid(width, height)
    expected.fill(Crossed)
    assertGrids(expected, grid)
  }

  test("solve full length works as expected on all occupied") {
    val grid = MutableGrid(empty)
    LogicSolver.solveFullLengths(allOccupied, grid)
    assertAllSame(grid, Occupied)
    val expected = MutableGrid(width, height)
    expected.fill(Occupied)
    assertGrids(expected, grid)
  }

  test("solve full length works as expected on example") {

    val example = Nonogram(
      grid = Grid.empty(width, height),
      horizontalHints = Hints(Seq(10)),
      verticalHints = Hints(Seq(10))
    )

    val grid = MutableGrid(empty)
    LogicSolver.solveFullLengths(example, grid)
    val expected = MutableGrid(width, height)
    expected.setRowValues(0)(Array.fill(width)(Occupied))
    expected.setColumnValues(0)(Array.fill(height)(Occupied))
    assertGrids(expected, grid)
  }

}
