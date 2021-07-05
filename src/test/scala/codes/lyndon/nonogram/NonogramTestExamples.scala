package codes.lyndon.nonogram

object NonogramTestExamples {

  val exampleExpected: String =
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

  val exampleGrid: Grid = Grid(
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

  val exampleHorzHints: Hints = Hints(
    Seq(0),
    Seq(9),
    Seq(9),
    Seq(2, 2),
    Seq(2, 2),
    Seq(4),
    Seq(4),
    Seq(0)
  )

  val exampleVertHints: Hints = Hints(
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

}
