package codes.lyndon.nonogram.reader
import codes.lyndon.nonogram.{Nonogram, Square}

import java.nio.file.{Files, Path}
import scala.jdk.CollectionConverters.CollectionHasAsScala

object SimpleNonogramReader extends NonogramReader {

  private val validSections: Set[String] = Section.types.map(_.text)

  private val validGridChars: Set[Char] = Square.charToTypes.keySet ++ Set(' ')

  override def parse(file: Path): Nonogram = {

    val allLines = Files.readAllLines(file).asScala

    // First line must be the dimensions of the Nonogram in the form:
    // [width]x[height]
    val firstLine = allLines.headOption match {
      case Some(value) => value
      case None        => throw NotAValidNonogram("File is empty")
    }

    val (width, height) = parseDimensions(firstLine) match {
      case Some(value) => value
      case None =>
        throw NotAValidNonogram(
          "Dimensions should be the first line in the format [width]x[height]"
        )
    }

    if (width <= 0) throw NotAValidNonogram("width must be greater than 0")
    if (height <= 0) throw NotAValidNonogram("height must be greater than 0")

    // Second line should be blank
    val secondLine = allLines.drop(1).headOption match {
      case Some(value) => value
      case None        => throw NotAValidNonogram("Missing all sections")
    }

    if (!secondLine.isBlank) {
      throw NotAValidNonogram("Second line must be blank")
    }

    // Now comes the true complexity, parsing the rest of the format
    parseSections(width, height, allLines.drop(2))
  }

  private def parseSections(
      width: Int,
      height: Int,
      lines: Iterable[String]
  ): Nonogram = {
    var currentSection: Option[Section] = None
    ???
  }

  private def getSectionHeader(line: String): Option[Section] =
    Section.headerMap.get(line)

  private def isValidSectionHeader(line: String): Boolean =
    validSections.contains(line)

  private def isValidGridLine(line: String): Boolean =
    line.forall(validGridChars.contains)

  private def isValidHintLine(line: String): Boolean =
    line.forall(c => c.isDigit || c == ' ' || c == ',')

  private def parseDimensions(line: String): Option[(Int, Int)] = {
    if (line.isBlank) return None
    val split = line.split('x')
    if (split.length != 2) return None
    val arr = split
      .map(_.toIntOption match {
        case Some(value) => value
        case None        => return None
      })
    Some(arr(0), arr(1))
  }

  object Section {
    val types: Set[Section]             = Set(Title, Author, Rows, Columns, Grid, Solution)
    val headerMap: Map[String, Section] = types.map(t => (t.text, t)).toMap
  }

  sealed abstract class Section(val text: String) {
    def validateLine(line: String): Boolean
  }

  case object Title extends Section("title") {
    override def validateLine(line: String): Boolean = true
  }
  case object Author extends Section("author") {
    override def validateLine(line: String): Boolean = true
  }
  case object Rows extends Section("rows") {
    override def validateLine(line: String): Boolean = isValidHintLine(line)
  }
  case object Columns extends Section("columns") {
    override def validateLine(line: String): Boolean = isValidHintLine(line)
  }
  case object Grid extends Section("grid") {
    override def validateLine(line: String): Boolean = isValidGridLine(line)
  }
  case object Solution extends Section("solution") {
    override def validateLine(line: String): Boolean = isValidGridLine(line)
  }
}
