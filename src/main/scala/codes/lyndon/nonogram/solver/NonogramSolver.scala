package codes.lyndon.nonogram.solver

import codes.lyndon.nonogram.{Grid, Nonogram}

trait NonogramSolver {

  def solve(puzzle: Nonogram): Option[Grid]

}
