package codes.lyndon.nonogram.reader

import codes.lyndon.nonogram.NonogramTestExamples
import org.scalatest.funsuite.AnyFunSuite

import java.io.InputStream

class SimpleNonogramReaderTest extends AnyFunSuite {

  private def getResource(path: String): InputStream =
    getClass.getResourceAsStream(path)

  test("can parse example") {
    val example  = getResource("/example.non")
    val nonogram = SimpleNonogramReader.parse(example)
    assert(nonogram != null)
    val pretty = nonogram.pretty()
    println(pretty)
    assert(pretty == NonogramTestExamples.exampleExpected)
  }

}
