package codes.lyndon.nonogram

final case class CouldNotBuildNonogram(
    message: String,
    cause: Throwable = null
) extends Exception(message, cause)

final case class NonogramBuilder(
    width: Int,
    height: Int
) {

  private var title: String  = ""
  private var author: String = ""

  private var grid: Option[Grid]             = None
  private var horizontalHints: Option[Hints] = None
  private var verticalHints: Option[Hints]   = None

  def setTitle(title: String): Unit = this.title = title

  def setAuthor(author: String): Unit = this.author = author

  def setGrid(grid: Grid): Unit = this.grid = Some(grid)

  def setHorizontalHints(hints: Hints): Unit = horizontalHints = Some(hints)

  def setVerticalHints(hints: Hints): Unit = verticalHints = Some(hints)

  def build(): Nonogram = {

    val grid = this.grid.getOrElse(Grid.empty(width, height))

    val horizontalHints = this.horizontalHints match {
      case Some(value) => value
      case None        => throw CouldNotBuildNonogram("No horizontalHints supplied")
    }

    val verticalHints = this.verticalHints match {
      case Some(value) => value
      case None        => throw CouldNotBuildNonogram("No verticalHints supplied")
    }

    Nonogram(grid, horizontalHints, verticalHints)
  }
}
