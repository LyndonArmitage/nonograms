package codes.lyndon.nonogram.generator

import codes.lyndon.nonogram.NonogramTestExamples.getClass
import codes.lyndon.nonogram.writer.ImageNonogramWriter
import org.scalatest.funsuite.AnyFunSuite

import java.io.File

class SimpleImageConverterTest extends AnyFunSuite {

  test("can convert example image") {

    val non = SimpleImageConverter.convert(
      new File(getClass.getResource("/test-image.png").toURI)
    )
    assert(non.isSuccess)

    non.foreach { println(_) }

    non.foreach { f =>
    val file = File.createTempFile("nono", ".png")
      println(file)
      ImageNonogramWriter(cellSize = 20, fontBorder = 5, renderSolution = false).write(f, file)
    }
  }

}
