package codes.lyndon.nonogram

import codes.lyndon.nonogram.solver.MutableGrid
import org.scalatest.funsuite.AnyFunSuite

import scala.collection.mutable.ArrayBuffer

abstract class NonogramFunSuite extends AnyFunSuite {

  def assertEmptyGrid(grid: MutableGrid): Unit = assertAllSame(grid, Blank)

  def assertAllSame(grid: MutableGrid, value: Square): Unit = {
    val expectedRow = Array.fill(grid.width)(value)
    for (y <- 0 until grid.height) {
      val row = grid.getRow(y)
      if (!(row sameElements expectedRow)) {
        fail(
          s"Grid does not just contain $value at y=$y\n${grid.toGrid.pretty()}"
        )
      }
    }

  }

  def assertGrids(expected: Grid, grid: MutableGrid): Unit =
    assertGrids(MutableGrid(expected), grid)

  def assertGrids(expected: Grid, grid: Grid): Unit =
    assertGrids(MutableGrid(expected), MutableGrid(grid))

  def assertGrids(expected: MutableGrid, grid: MutableGrid): Unit = {
    if (expected == grid) return
    if (expected.width != grid.width) {
      fail(s"Grid size mismatch: ${grid.width} != ${expected.width}")
      return
    }
    val mismatches = new ArrayBuffer[((Int, Int), Square, Square)](expected.size)
    for (y <- 0 until expected.height) {
      for (x <- 0 until expected.width) {
        val expectedCell = expected(x)(y)
        val actualCell = grid(x)(y)
        if (actualCell != expectedCell) {
          mismatches.addOne(((x, y), expectedCell, actualCell))
        }
      }
    }
    if (mismatches.nonEmpty) {
      val notes = mismatches.map { case ((x, y), expected, actual) =>
        s"(${x},${y}) $actual != $expected"
      }.mkString("\n")
      fail(
        s"${mismatches.size} mismatching cells\n" +
          s"${grid.toGrid.pretty()}\n!=\n${expected.toGrid.pretty()}\n" +
          s"at: ${notes}"
      )
    }
  }

}
