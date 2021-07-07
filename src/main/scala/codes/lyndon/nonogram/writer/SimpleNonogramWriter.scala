package codes.lyndon.nonogram.writer
import codes.lyndon.nonogram.{Grid, Nonogram}

import java.io.{BufferedWriter, OutputStream, OutputStreamWriter}
import java.nio.charset.StandardCharsets
import scala.util.Try

object SimpleNonogramWriter extends NonogramWriter {

  override def write(
      nonogram: Nonogram,
      outputStream: OutputStream
  ): Try[Unit] =
    Try {
      val writer = new BufferedWriter(
        new OutputStreamWriter(outputStream, StandardCharsets.UTF_8)
      )

      writer.write(s"${nonogram.width}x${nonogram.height}\n")
      writer.write("\n")

      writer.write("title\n")
      writer.write(nonogram.title)
      writer.write("\n\n")

      writer.write("author\n")
      writer.write(nonogram.author)
      writer.write("\n\n")

      writer.write("rows\n")
      nonogram.verticalHints.hints
        .map { hint =>
          s"${hint.mkString(",")}\n"
        }
        .foreach(writer.write)
      writer.write("\n")

      writer.write("columns\n")
      nonogram.horizontalHints.hints
        .map { hint =>
          s"${hint.mkString(",")}\n"
        }
        .foreach(writer.write)
      writer.write("\n")

      writer.write("grid\n")
      writer.write(writeGrid(nonogram.grid))

      nonogram.solution.foreach { solution =>
        writer.write("\nsolution\n")
        writer.write(writeGrid(solution))
      }

      writer.flush()
    }

  private def writeGrid(grid: Grid): String = grid.pretty()
}
