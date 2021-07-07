package codes.lyndon.nonogram.writer

import codes.lyndon.nonogram.{Nonogram, NonogramTestExamples}
import org.scalatest.funsuite.AnyFunSuite

import java.nio.file.Files

class ImageNonogramWriterTest extends AnyFunSuite {

  test("example") {

    val example: Nonogram = Nonogram(
      NonogramTestExamples.exampleGrid,
      NonogramTestExamples.exampleHorzHints,
      NonogramTestExamples.exampleVertHints,
      solution = Some(NonogramTestExamples.exampleSolution)
    )
    val writer = ImageNonogramWriter()

    val tempFile = Files.createTempFile("nonogram", ".png")
    tempFile.toFile.deleteOnExit()

    val wrote = writer.write(example, tempFile)

    wrote.failed.foreach { throwable =>
      throwable.printStackTrace()
    }

    assert(wrote.isSuccess, "Write failed")
  }

}
