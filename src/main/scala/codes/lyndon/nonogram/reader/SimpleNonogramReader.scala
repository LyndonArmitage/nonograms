package codes.lyndon.nonogram.reader
import codes.lyndon.nonogram.Grid.GridRow
import codes.lyndon.nonogram.{Hints, Nonogram, NonogramBuilder, Square}
import org.slf4j.LoggerFactory

import java.io.InputStream
import scala.collection.mutable
import scala.collection.mutable.ListBuffer

object SimpleNonogramReader extends NonogramReader {

  private val logger = LoggerFactory.getLogger(getClass)

  private val validGridChars: Set[Char] = Square.charToTypes.keySet ++ Set(' ')

  def parse(inputStream: InputStream): Nonogram = {

    val allLines =
      scala.io.Source.fromInputStream(inputStream).getLines().toIndexedSeq

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

    logger.debug(s"Nonogram has dimensions of: $width x $height")

    // Now comes the true complexity, parsing the rest of the format
    parseSections(width, height, allLines.drop(2))
  }

  private def parseSections(
      width: Int,
      height: Int,
      lines: IndexedSeq[String]
  ): Nonogram = {
    val sectionBuilder = new SectionBuilder(width, height)
    var lineNum        = 0
    while (lineNum < lines.length) {
      val line = lines(lineNum)
      if (line.isBlank) {
        if (sectionBuilder.hasSection) {
          sectionBuilder.buildSection()
          sectionBuilder.clearSection()
        }
      } else {
        if (sectionBuilder.hasSection) {
          sectionBuilder.parseLine(line)
        } else {
          sectionBuilder.section = getSectionHeader(line)
          if (!sectionBuilder.hasSection) {
            throw NotAValidNonogram(s"$line is not a valid section")
          }
        }
      }
      lineNum = lineNum + 1
    }

    if (sectionBuilder.hasSection) {
      sectionBuilder.buildSection()
      sectionBuilder.clearSection()
    }
    sectionBuilder.build()
  }

  private class SectionBuilder(width: Int, height: Int) {
    var section: Option[Section]          = None
    val lines: mutable.ListBuffer[String] = ListBuffer.empty

    private val builder = NonogramBuilder(width, height)

    def hasSection: Boolean = section.isDefined

    def parseLine(line: String): Unit = {
      section.foreach { sec =>
        if (sec.validateLine(line)) {
          lines.addOne(line)
        } else {
          throw NotAValidNonogram(s"$line is not a valid line for $sec")
        }
      }
    }

    def buildSection(): Unit = {
      if (!hasSection) {
        return
      }
      section.foreach {
        case Title =>
          builder.setTitle(lines.head)
        case Author =>
          builder.setAuthor(lines.head)
        case Rows =>
          builder.setVerticalHints(parseLinesAsHints())
        case Columns =>
          builder.setHorizontalHints(parseLinesAsHints())
        case Grid =>
          val grid = parseLinesAsGrid()
          builder.setGrid(grid)
        case Solution =>
          val grid = parseLinesAsGrid()
          builder.setSolution(grid)
      }
    }

    private def parseLinesAsGrid(): codes.lyndon.nonogram.Grid = {
      val rows: Seq[GridRow] = lines.map { line =>
        line
          .split(' ')
          .map { token =>
            if (token.length != 1) {
              throw NotAValidNonogram(s"$token is not a valid grid token")
            }
            token.head
          }
          .map(Square.fromChar)
          .map {
            case Some(value) => value
            case c @ None =>
              throw NotAValidNonogram(s"$c is not a valud grid token")
          }
          .toSeq
      }.toSeq
      codes.lyndon.nonogram.Grid(rows: _*)
    }

    private def parseLinesAsHints(): Hints = {
      val hints = lines.map { line =>
        line.split(',').map(_.toInt).toSeq
      }
      Hints(hints.toSeq: _*)
    }

    def build(): Nonogram = {
      builder.build()
    }

    def clearSection(): Unit = {
      section = None
      lines.clear()
    }
  }

  private def getSectionHeader(line: String): Option[Section] =
    Section.headerMap.get(line)

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
    override def toString: String = text
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
