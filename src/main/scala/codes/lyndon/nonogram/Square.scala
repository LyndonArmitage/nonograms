package codes.lyndon.nonogram

object Square {
  val types: Seq[Square] = Seq(Blank, Occupied, Crossed)
  val charToTypes: Map[Char, Square] = types.map(t => (t.char, t)).toMap

  def fromChar(char: Char): Option[Square] = charToTypes.get(char)
}

sealed abstract class Square(
    val char: Char
) {
  def pretty(): String = s"$char"

  override def toString: String = pretty()
}

case object Blank    extends Square('.')
case object Occupied extends Square('#')
case object Crossed  extends Square('X')
