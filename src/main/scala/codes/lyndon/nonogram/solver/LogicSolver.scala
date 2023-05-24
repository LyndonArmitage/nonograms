package codes.lyndon.nonogram.solver
import codes.lyndon.nonogram._
import org.slf4j.{Logger, LoggerFactory}

object LogicSolver extends NonogramSolver {

  private[this] val logger: Logger = LoggerFactory.getLogger(getClass)

  override def solve(puzzle: Nonogram): Option[Grid] = {
    puzzle.solution match {
      case Some(solution) =>
        val mutable = MutableGrid(solution)
        if (mutable.countOf(Blank) == 0) {
          logger.warn("Using existing solution")
          Some(solution)
        } else {
          logger.warn("Existing solution is incomplete, will solve")
          Some(attemptSolve(puzzle).toGrid)
        }
      case None => Some(attemptSolve(puzzle).toGrid)
    }
  }

  private def attemptSolve(nonogram: Nonogram): MutableGrid = {
    val grid = MutableGrid(nonogram)

    // Use players intuition to solve the low hanging fruit

    // Solve for full length runs
    solveFullLengths(nonogram, grid)

    // Solve for must be occupied based on hints
    solveMustBeOccupied(nonogram, grid)

    // Solve for single length combos
    solveCombos(nonogram, grid)

    // TODO: Solve missing parts
    // This could be groups or runs that can now be solved

    val blanks = grid.countOf(Blank)
    if (blanks > 0) {
      logger.warn(
        s"Could not fully solve, still got $blanks blanks out of ${grid.size}"
      )
    }

    grid
  }

  private def solveFullLengths(
      nonogram: Nonogram,
      grid: MutableGrid
  ): Unit = {
    val width  = grid.width
    val height = grid.height
    for (y <- 0 until height) {
      val hints = nonogram.verticalHints(y)
      if (hints.size == 1) {
        val hint = hints.head
        if (hint == width) {
          // full length occupied
          grid.setRow(y)(Occupied)
        } else if (hint == 0) {
          // full length empty
          grid.setRow(y)(Crossed)
        }
      }
    }

    for (x <- 0 until width) {
      val hints = nonogram.horizontalHints(x)
      if (hints.size == 1) {
        val hint = hints.head
        if (hint == width) {
          // full length occupied
          grid.setColumn(x)(Occupied)
        } else if (hint == 0) {
          // full length empty
          grid.setColumn(x)(Crossed)
        }
      }

    }
  }

  private def solveMustBeOccupied(
      nonogram: Nonogram,
      grid: MutableGrid
  ): Unit = {
    // As a player I know that there are certain sequences that
    // necessitate certain squares being occupied.
    // For example:
    // On a length 10 the hint 6 requires that the centre squares be occupied:
    // ....##....
    // This seems like something easier to represent as it requires:
    // hint > length/2
    // Examples:

    // On a length 4:
    // hint 3, .##.

    // On a length 10:
    // hint 6, ....##....
    // hint 7, ...####...
    // hint 8, ..######..
    // hint 9, .########.

    // For odd numbers:

    // On a length 5:
    // hint 3, ..#..
    // hint 4, .###.

    // On a length 7:
    // hint 4, ...#...
    // hint 5, ..###..
    // hint 6, .#####.

    val width  = grid.width
    val height = grid.height

    for (y <- 0 until height) {
      val hints   = nonogram.verticalHints(y)
      val halfLen = width / 2.0f
      hints.find { hint => hint > halfLen }.foreach { hint =>
        val blanksOnEitherSide = width - hint
        var count              = 0
        for (x <- 0 until width) {
          if (x < halfLen) {
            count += 1
          } else if (x > halfLen) {
            count -= 1
          }
          if (count > blanksOnEitherSide) {
            grid.set(x)(y)(Occupied)
          }
        }
      }
    }

    for (x <- 0 until width) {
      val hints   = nonogram.horizontalHints(x)
      val halfLen = height / 2.0f
      hints.find { hint => hint > halfLen }.foreach { hint =>
        val blanksOnEitherSide = width - hint
        var count              = 0
        for (y <- 0 until width) {
          if (y < halfLen) {
            count += 1
          } else if (y > halfLen) {
            count -= 1
          }
          if (count > blanksOnEitherSide) {
            grid.set(x)(y)(Occupied)
          }
        }
      }

    }

  }

  private def solveCombos(
      nonogram: Nonogram,
      grid: MutableGrid
  ): Unit = {
    // There must be an algorithm for determining if a sequence can only be
    // produced in a single combination.
    // For example:
    // On a length 10 the hints 3,2,3 can only be produced like so:
    // ###X##X###

    val width  = grid.width
    val height = grid.height

    for (y <- 0 until height) {
      val hints = nonogram.verticalHints(y)
      if (hints.size > 1) {
        val array: Array[Square] = Array.fill(width)(Blank)
        var i                    = 0
        for (hint <- hints) {
          for (n <- 0 until hint) {
            array(i + n) = Occupied
          }
          i += hint
          if (i + 1 < width) {
            array(i + 1) = Crossed
            i += 1
          }
        }
        if (!hints.contains(Blank)) {
          // fully populated
          grid.setRowValues(y)(array)
        }
      }
    }

    for (x <- 0 until width) {
      val hints = nonogram.horizontalHints(x)
      if (hints.size > 1) {
        val array: Array[Square] = Array.fill(width)(Blank)
        var i                    = 0
        for (hint <- hints) {
          for (n <- 0 until hint) {
            array(i + n) = Occupied
          }
          i += hint
          if (i + 1 < width) {
            array(i + 1) = Crossed
            i += 1
          }
        }
        if (!hints.contains(Blank)) {
          // fully populated
          grid.setColumnValues(x)(array)
        }
      }
    }
  }

}
