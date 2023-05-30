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
        grid.set(x)(y)(solution(x)(y))
      }
    }

    grid
  }

}

trait MutableGrid {

  def width: Int
  def height: Int
  def size: Int = width * height

  def getRow(y: Int): Array[Square]
  def getColumn(x: Int): Array[Square]

  def apply(x: Int)(y: Int): Square = get(x)(y)
  def get(x: Int)(y: Int): Square
  def set(x: Int)(y: Int)(value: Square): Unit
  def setRow(y: Int)(value: Square): Unit
  def setRowValues(y: Int)(values: Array[Square]): Unit
  def setColumn(x: Int)(value: Square): Unit
  def setColumnValues(x: Int)(values: Array[Square]): Unit

  def fill(value: Square): Unit

  def countOf(value: Square): Int

  def toGrid: Grid
}

private class MutableGridImpl(
    override val width: Int,
    override val height: Int
) extends MutableGrid {

  private val grid: Array[Array[Square]] = new Array(height)
  for (y <- 0 until height) {
    grid(y) = Array.fill(width) { Blank }
  }

  override def get(x: Int)(y: Int): Square = grid(y)(x)

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

  override def setRowValues(y: Int)(values: Array[Square]): Unit = {
    grid(y) = values.clone()
  }

  override def setColumnValues(x: Int)(values: Array[Square]): Unit = {
    for (y <- values.indices) {
      val value = values(y)
      set(x)(y)(value)
    }
  }

  override def fill(value: Square): Unit = {
    for (y <- 0 until height) {
      val values = Array.fill[Square](width)(value)
      setRowValues(y)(values)
    }
  }

  override def getRow(y: Int): Array[Square] =
    grid(y).clone()

  override def getColumn(x: Int): Array[Square] = {
    val column = Array.fill[Square](height)(Blank)
    for (y <- 0 until height) {
      column(y) = grid(y)(x)
    }
    column
  }


  def canEqual(other: Any): Boolean = other.isInstanceOf[MutableGridImpl]

  override def equals(other: Any): Boolean = other match {
    case that: MutableGridImpl =>
      (that canEqual this) &&
        (grid sameElements that.grid) &&
        width == that.width &&
        height == that.height
    case _ => false
  }

  override def hashCode(): Int = {
    val state = Seq(grid, width, height)
    state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
  }

  override def toString: String = toGrid.toString
}
