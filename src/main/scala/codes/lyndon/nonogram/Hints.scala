package codes.lyndon.nonogram

import codes.lyndon.nonogram.Hints.Hint

object Hints {
  type Hint = Seq[Int]

}

final case class Hints(
    hints: Hint*
) {

  def pretty(): String = hints.map(hint => hint.mkString(",")).mkString("\n")

  override def productElement(n: Int): Hint = hints(n)

  override def productIterator: Iterator[Hint] = hints.iterator

  override def toString: String = pretty()
}
