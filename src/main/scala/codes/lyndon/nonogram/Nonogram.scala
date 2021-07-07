package codes.lyndon.nonogram

final case class Nonogram(
    grid: Grid,
    horizontalHints: Hints,
    verticalHints: Hints,
    title: String = "",
    author: String = "",
    solution: Option[Grid] = None
) {

  val width: Int  = grid.width
  val height: Int = grid.height

  def pretty(): String = {
    val horizontalString = horizontalHints.pretty()
    val verticalString   = verticalHints.pretty()
    val gridString       = grid.pretty()
    val solutionString   = solution.map(_.pretty()).getOrElse("")

    val sb = new StringBuilder(
      title.length +
        author.length +
        horizontalString.length +
        verticalString.length +
        gridString.length +
        solutionString.length +
        100
    )

    sb.append("title:")
    if(title.nonEmpty) sb.append(title)
    sb.append("\n\n")

    sb.append("author:")
    if(title.nonEmpty) sb.append(author)
    sb.append("\n\n")

    sb.append("column hints:\n")
    sb.append(horizontalString)
    sb.append("\n\n")

    sb.append("row hints:\n")
    sb.append(verticalString)
    sb.append("\n\n")

    sb.append("grid:\n")
    sb.append(gridString)
    sb.append("\n")

    sb.append("solution:\n")
    sb.append(solutionString)

    sb.toString()
  }

  override def toString: String = pretty()
}
