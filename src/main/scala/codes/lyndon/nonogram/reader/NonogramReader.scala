package codes.lyndon.nonogram.reader

import codes.lyndon.nonogram.Nonogram

import java.nio.file.Path

trait NonogramReader {

  def apply(file: Path): Nonogram = parse(file)

  def parse(file: Path): Nonogram
}
