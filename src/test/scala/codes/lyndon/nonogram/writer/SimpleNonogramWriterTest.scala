package codes.lyndon.nonogram.writer

import codes.lyndon.nonogram.{Nonogram, NonogramTestExamples}
import org.scalatest.funsuite.AnyFunSuite

import java.io.{File, FileOutputStream}
import java.nio.charset.StandardCharsets
import java.nio.file.Files

class SimpleNonogramWriterTest extends AnyFunSuite {

  private val exampleNonogram = Nonogram(
    NonogramTestExamples.exampleGrid,
    NonogramTestExamples.exampleHorzHints,
    NonogramTestExamples.exampleVertHints,
    "test title",
    "test",
    Some(NonogramTestExamples.exampleSolution)
  )

  private val exampleExpectedOutput: String =
    Files.readString(
      new File(getClass.getResource("/expected.non").toURI).toPath,
      StandardCharsets.UTF_8
    )


  test("can write") {
    val temp   = Files.createTempFile("nonogram", ".non").toFile
    temp.deleteOnExit()

    val output = new FileOutputStream(temp, true)
    val wrote = SimpleNonogramWriter.write(exampleNonogram, output)
    assert(wrote.isSuccess, "Failed to write")
  }

  test("example works") {

    val example = Nonogram(
      NonogramTestExamples.exampleGrid,
      NonogramTestExamples.exampleHorzHints,
      NonogramTestExamples.exampleVertHints,
      "test title",
      "test",
      Some(NonogramTestExamples.exampleSolution)
    )

    val temp   = Files.createTempFile("nonogram", ".non").toFile
    temp.deleteOnExit()

    val output = new FileOutputStream(temp, true)
    val wrote = SimpleNonogramWriter.write(example, output)
    assert(wrote.isSuccess, "Failed to write")

    val outputString = Files.readString(temp.toPath)

    assert(outputString == exampleExpectedOutput, "Output does not match input")

  }

}
