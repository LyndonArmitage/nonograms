package codes.lyndon.nonogram

import org.scalatest.funsuite.AnyFunSuite

class NonogramTest extends AnyFunSuite {

  private val exampleGrid: Grid = Grid(
    Grid.emptyGridRow(8),
    Grid.rowFromString(".####..."),
    Grid.rowFromString(".######."),
    Grid.rowFromString(".##..##."),
    Grid.rowFromString(".##..##."),
    Grid.rowFromString(".######."),
    Grid.rowFromString(".####..."),
    Grid.rowFromString(".##....."),
    Grid.rowFromString(".##....."),
    Grid.rowFromString(".##....."),
    Grid.emptyGridRow(8)
  )

  private val exampleHorzHints: Hints = Hints(
    Seq(0),
    Seq(9),
    Seq(9),
    Seq(2, 2),
    Seq(2, 2),
    Seq(4),
    Seq(4),
    Seq(0)
  )

  private val exampleVertHints: Hints = Hints(
    Seq(0),
    Seq(4),
    Seq(6),
    Seq(2, 2),
    Seq(2, 2),
    Seq(6),
    Seq(4),
    Seq(2),
    Seq(2),
    Seq(2),
    Seq(0)
  )

  test("pretty printing works as expected") {
    val nonogram = Nonogram(exampleGrid, exampleHorzHints, exampleVertHints)

    val printed = nonogram.pretty()
    val expected =
      """|column hints:
        |0
        |9
        |9
        |2,2
        |2,2
        |4
        |4
        |0
        |row hints:
        |0
        |4
        |6
        |2,2
        |2,2
        |6
        |4
        |2
        |2
        |2
        |0
        |grid:
        |. . . . . . . .
        |. # # # # . . .
        |. # # # # # # .
        |. # # . . # # .
        |. # # . . # # .
        |. # # # # # # .
        |. # # # # . . .
        |. # # . . . . .
        |. # # . . . . .
        |. # # . . . . .
        |. . . . . . . .
""".stripMargin
    assert(printed == expected)
  }

}
