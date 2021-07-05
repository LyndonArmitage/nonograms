package codes.lyndon.nonogram.reader

import org.scalatest.funsuite.AnyFunSuite

import java.io.InputStream

class SimpleNonogramReaderTest extends AnyFunSuite {

  private def getResource(path: String): InputStream =
    getClass.getResourceAsStream(path)

  test("can parse example") {
    val example = getResource("/example.non")
    val nonogram = SimpleNonogramReader.parse(example)
    assert(nonogram != null)

    println(nonogram.pretty())
  }

}
