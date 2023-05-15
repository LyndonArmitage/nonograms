package codes.lyndon.nonogram.generator

import codes.lyndon.nonogram._

import scala.collection.mutable.ListBuffer

object NonogramGeneratorTools {

  def generateHints(grid: Grid): (Hints, Hints) = {
    val width  = grid.width
    val height = grid.height

    val verticalHints   = ListBuffer[Hints.Hint]()
    val horizontalHints = ListBuffer[Hints.Hint]()

    0.until(height).foreach { y =>
      val result  = ListBuffer[Int]()
      var counter = 0
      val row     = grid.rows(y)
      row.foreach {
        case Blank | Crossed =>
          if (counter > 0) {
            result.addOne(counter)
            counter = 0
          }
        case Occupied => counter += 1
      }
      if (counter != 0 || result.isEmpty) result.addOne(counter)
      verticalHints.addOne(result.toSeq)
    }

    0.until(width).foreach { x =>
      val result  = ListBuffer[Int]()
      var counter = 0
      0.until(height).foreach { y =>
        val sq = grid(x)(y)
        sq match {
          case Blank | Crossed =>
            if (counter > 0) {
              result.addOne(counter)
              counter = 0
            }
          case Occupied => counter += 1
        }
      }
      if (counter != 0 || result.isEmpty) result.addOne(counter)
      horizontalHints.addOne(result.toSeq)
    }

    (Hints(horizontalHints.toSeq: _*), Hints(verticalHints.toSeq: _*))
  }

  def isAmbiguousGrid(grid: Grid): Boolean = {
    // Check for a 2x2 pattern like:
    // . #
    // # .
    // or
    // # .
    // . #
    val width  = grid.width
    val height = grid.height

    0.until(width).foreach { x =>
      0.until(height).foreach { y =>
        val topLeft     = grid(x)(y)
        val topRight    = grid.opt(x + 1)(y)
        val bottomLeft  = grid.opt(x)(y + 1)
        val bottomRight = grid.opt(x + 1)(y + 1)

        val hasAmbig = (topRight, bottomLeft, bottomRight) match {
          case (Some(trSq), Some(blSq), Some(brSq)) =>
            val tl = topLeft == Occupied
            val tr = trSq == Occupied
            val bl = blSq == Occupied
            val br = brSq == Occupied

            (tl, tr, bl, br) match {
              case (true, false, false, true) => true
              case (false, true, true, false) => true
              case _                          => false
            }
          case _ => false
        }
        if (hasAmbig) {
          return true
        }
      }
    }

    false
  }

}
