package codes.lyndon.nonogram.solver

import codes.lyndon.nonogram.{Blank, Grid, Nonogram, Square}

object MutableGrid {

  def apply(width: Int, height: Int): MutableGrid =
    new MutableGridImpl(width, height)

  def apply(nonogram: Nonogram): MutableGrid = {
    nonogram.solution match {
      case Some(value) => apply(value)
      case None        => apply(nonogram.width, nonogram.height)
    }
  }

  def apply(solution: Grid): MutableGrid = {
    val grid = apply(solution.width, solution.height)
    for (y <- 0 until solution.height) {
      for (x <- 0 until solution.width) {
        grid(x)(y) = solution(x)(y)
      }
    }

    grid
  }

}

trait MutableGrid {

  def width: Int
  def height: Int
  def size: Int = width * height

  def apply(x: Int)(y: Int): Square
  def set(x: Int)(y: Int)(value: Square): Unit
  def setRow(y: Int)(value: Square): Unit
  def setColumn(x: Int)(value: Square): Unit

  def countOf(value: Square): Int

  def toGrid: Grid
}

private class MutableGridImpl(
    override val width: Int,
    override val height: Int
) extends MutableGrid {

  private val grid: Array[Array[Square]] = new Array(height)
  for (y <- 0 until height) {
    grid(y) = Array.fill(width) { Blank}
  }

  override def apply(x: Int)(y: Int): Square = grid(y)(x)

  override def set(x: Int)(y: Int)(value: Square): Unit = grid(y)(x) = value

  override def toGrid: Grid = {
    val rows = 0.until(height).map { y =>
      grid(y).toSeq
    }
    Grid(rows: _*)
  }

  override def setRow(y: Int)(value: Square): Unit = {
    grid(y) = Array.fill(width) { value }
  }

  override def setColumn(x: Int)(value: Square): Unit = {
    for (y <- 0 until height) {
      set(x)(y)(value)
    }
  }

  override def countOf(value: Square): Int = {
    var count = 0
    grid.foreach { rows =>
      count += rows.count(_ == value)
    }
    count
  }
}
