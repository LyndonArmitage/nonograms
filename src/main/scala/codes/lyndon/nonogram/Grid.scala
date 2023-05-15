package codes.lyndon.nonogram

import codes.lyndon.nonogram.Grid.GridRow

object Grid {
  type GridRow = Seq[Square]

  def emptyGridRow(width: Int): GridRow = Seq.fill[Square](width)(Blank)

  def rowFromString(string: String): GridRow = {
    if (string.isEmpty)
      throw new IllegalArgumentException("string cannot be empty")
    string.map { c =>
      val x = Square.fromChar(c)
      x match {
        case Some(value) => value
        case None =>
          throw new IllegalArgumentException(s"$c is not a valid grid value")
      }
    }
  }

  def empty(width: Int, height: Int): Grid = {
    val row  = emptyGridRow(width)
    val rows = Seq.fill[GridRow](height)(row)
    Grid(rows: _*)
  }

}

final case class Grid(
    rows: GridRow*
) {
  val width: Int  = rows.map(_.length).max
  val height: Int = rows.length

  def pretty(): String = {
    val sb = new StringBuilder((width * 3) * (height * 2))
    0.until(height).map { y =>
      val row: GridRow = rows.lift(y).getOrElse(Grid.emptyGridRow(width))
      0.until(width).map { x =>
        val square: Square = row.lift(x).getOrElse(Blank)
        sb.append(square.pretty())
        if (x < width - 1) sb.append(" ")
      }
      sb.append("\n")
    }

    sb.toString()
  }

  override def toString: String = pretty()

  def apply(x: Int)(y: Int) : Square = rows(y)(x)

  def opt(x: Int)(y: Int): Option[Square] = rows.lift(y).flatMap(_.lift(x))
}
