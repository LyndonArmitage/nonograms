package codes.lyndon.nonogram

import codes.lyndon.nonogram.Hints.Hint

object Hints {
  type Hint = Seq[Int]

}

final case class Hints(
    hints: Hint*
) {

  def pretty(): String = hints.map(hint => hint.mkString(",")).mkString("\n")

  def isEmpty: Boolean = hints.isEmpty

  def has(n: Int): Boolean = n < hints.size

  def apply(n: Int): Hint = productElement(n)

  def get(n: Int): Hint = apply(n)

  override def productElement(n: Int): Hint = {
    if (isEmpty) throw new IndexOutOfBoundsException(n)
    hints(n)
  }

  override def productIterator: Iterator[Hint] = hints.iterator

  override def toString: String = pretty()
}
