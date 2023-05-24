package codes.lyndon.nonogram.solver

import codes.lyndon.nonogram.{Blank, Crossed, Grid, Hints, Nonogram, Occupied, Square}
import org.scalatest.funsuite.AnyFunSuite

class LogicSolverTest extends AnyFunSuite {

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
  }

  test("solve full length works as expected on all 0s") {
    val grid = MutableGrid(empty)
    LogicSolver.solveFullLengths(allCrossed, grid)
    assertAllSame(grid, Crossed)
  }

  test("solve full length works as expected on all occupied") {
    val grid = MutableGrid(empty)
    LogicSolver.solveFullLengths(allOccupied, grid)
    assertAllSame(grid, Occupied)
  }

  test("solve full length works as expected on example") {

    val example = Nonogram(
      grid = Grid.empty(width, height),
      horizontalHints = Hints(),
      verticalHints = Hints()
    )

    val grid = MutableGrid(empty)
    LogicSolver.solveFullLengths(allOccupied, grid)
    assertAllSame(grid, Occupied)
  }


  def assertEmptyGrid(grid: MutableGrid): Unit = assertAllSame(grid, Blank)

  def assertAllSame(grid: MutableGrid, value: Square): Unit = {
    val emptyRow = Array.fill(grid.width)(value)
    for (y <- 0 until grid.height) {
      val row = grid.getRow(y)
      assert(row sameElements emptyRow)
    }

  }

}
