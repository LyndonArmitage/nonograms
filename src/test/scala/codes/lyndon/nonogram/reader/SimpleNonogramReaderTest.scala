package codes.lyndon.nonogram.reader

import codes.lyndon.nonogram.NonogramTestExamples
import org.scalatest.funsuite.AnyFunSuite

import java.io.InputStream
import scala.util.Using

class SimpleNonogramReaderTest extends AnyFunSuite {

  private def getResource(path: String): InputStream =
    getClass.getResourceAsStream(path)

  test("can parse example") {
    Using(getResource("/example.non")) { example =>
      val nonogram = SimpleNonogramReader.parse(example)
      assert(nonogram != null)
      val pretty = nonogram.pretty()
      println(pretty)
      assert(pretty == NonogramTestExamples.exampleExpected)
    }
  }

}
