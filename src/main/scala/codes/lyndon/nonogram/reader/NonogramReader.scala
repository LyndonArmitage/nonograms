package codes.lyndon.nonogram.reader

import codes.lyndon.nonogram.Nonogram

import java.io.{FileInputStream, InputStream}
import java.nio.file.Path
import scala.util.{Try, Using}

trait NonogramReader {

  def apply(file: Path): Try[Nonogram] = parse(file)

  def parse(file: Path): Try[Nonogram] =
    Using(new FileInputStream(file.toFile)) { is =>
      parse(is)
    }

  def parse(inputStream: InputStream): Nonogram
}
