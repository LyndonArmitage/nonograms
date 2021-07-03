package codes.lyndon.nonogram

final case class Nonogram(
    grid: Grid,
    horizontalHints: Hints,
    verticalHints: Hints
) {

  val width: Int  = grid.width
  val height: Int = grid.height

  def pretty(): String = {
    val horizontalString = horizontalHints.pretty()
    val verticalString   = verticalHints.pretty()
    val gridString       = grid.pretty()
    s"column hints:\n$horizontalString\nrow hints:\n$verticalString\ngrid:\n$gridString"
  }

  override def toString: String = pretty()
}
