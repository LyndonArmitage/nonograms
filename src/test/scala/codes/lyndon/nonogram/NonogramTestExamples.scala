package codes.lyndon.nonogram

import java.io.File
import java.nio.charset.StandardCharsets
import java.nio.file.Files

object NonogramTestExamples {

  val exampleExpected: String =
    Files.readString(new File(getClass.getResource("/expected.test.output").toURI).toPath, StandardCharsets.UTF_8)

  val exampleSolution: Grid = Grid(
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

  val exampleGrid: Grid = Grid(
    Grid.rowFromString("XXXXXXXX"),
    Grid.rowFromString(".#......"),
    Grid.rowFromString(".#.##..."),
    Grid.rowFromString("........"),
    Grid.rowFromString("X...X..."),
    Grid.rowFromString(".......X"),
    Grid.rowFromString("........"),
    Grid.rowFromString("...XX..."),
    Grid.rowFromString(".......X"),
    Grid.rowFromString("..#....."),
    Grid.rowFromString("XX...X.."),
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
