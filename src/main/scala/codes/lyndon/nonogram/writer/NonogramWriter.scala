package codes.lyndon.nonogram.writer

import codes.lyndon.nonogram.Nonogram

import java.io.{File, FileOutputStream, OutputStream}
import java.nio.file.Path
import scala.util.{Try, Using}

trait NonogramWriter {

  def write(nonogram: Nonogram, path: Path): Try[Unit] =
    write(nonogram, path.toFile)

  def write(nonogram: Nonogram, file: File): Try[Unit] =
    Using(new FileOutputStream(file)) {
      write(nonogram, _)
    }.flatten

  def write(nonogram: Nonogram, outputStream: OutputStream): Try[Unit]
}
