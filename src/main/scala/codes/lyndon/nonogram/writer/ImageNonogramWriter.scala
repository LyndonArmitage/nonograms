package codes.lyndon.nonogram.writer

import codes.lyndon.nonogram._

import java.awt.image.BufferedImage
import java.awt.{Color, Font}
import java.io.OutputStream
import javax.imageio.ImageIO
import scala.util.Try

final case class CouldNotWriteImage(message: String, cause: Throwable = null)
    extends Exception(message, cause)

final case class ImageNonogramWriter(
    cellSize: Int = 15,
    fontSize: Int = 12,
    fontBorder: Int = 2,
    fontName: String = Font.MONOSPACED,
    renderSolution: Boolean = true
) extends NonogramWriter {

  private val font: Font = new Font(fontName, Font.PLAIN, fontSize)

  private val emptyColours = (
    new Color(219, 219, 219),
    new Color(255, 255, 255)
  )

  private val crossedColour = Color.DARK_GRAY
  private val filledColour  = Color.BLACK

  override def write(
      nonogram: Nonogram,
      outputStream: OutputStream
  ): Try[Unit] =
    Try {
      if (cellSize < 1)
        throw CouldNotWriteImage("cellSize must be at least 1 pixel big")

      // Split this into 2 parts:
      // Grid rendering and hint rendering
      val gridImage = renderGrid(nonogram.grid, cellSize)

      // Embed the grid images into a wider image

      val maxNumberOfHorizontalHints =
        nonogram.horizontalHints.hints.map(f => f.length).maxOption.getOrElse(0)
      val maxNumberOfVerticalHints =
        nonogram.verticalHints.hints.map(f => f.length).maxOption.getOrElse(0)

      val horizontalHintSectionSize =
        (fontSize + fontBorder) * maxNumberOfHorizontalHints
      val verticalHintSectionSize =
        (fontSize + fontBorder) * maxNumberOfVerticalHints

      val withHints = new BufferedImage(
        gridImage.getWidth + verticalHintSectionSize,
        gridImage.getHeight + horizontalHintSectionSize,
        BufferedImage.TYPE_INT_ARGB
      )
      val g2 = withHints.createGraphics()

      // Draw existing grid
      g2.drawImage(gridImage, 0, horizontalHintSectionSize, null)

      // set up the font
      g2.setColor(Color.BLACK)
      g2.setFont(font)

      nonogram.horizontalHints.hints.zipWithIndex.foreach {
        case (hints, x) =>
          hints.zipWithIndex.foreach {
            case (hint, hintNumber) =>
              val xPos = x * cellSize
              val yPos = (fontSize + fontBorder) * (hintNumber + 1)
              g2.drawString(s"$hint", xPos, yPos)
          }
      }

      nonogram.verticalHints.hints.zipWithIndex.foreach {
        case (hints, y) =>
          hints.zipWithIndex.foreach {
            case (hint, hintNumber) =>
              val xPos =
                (nonogram.width * cellSize) + (fontSize * hintNumber) + (fontBorder * hintNumber + 1)
              val yPos = horizontalHintSectionSize + ((y + 1) * cellSize)
              g2.drawString(s"$hint", xPos, yPos)
          }
      }

      val finalImage: BufferedImage =
        (renderSolution, nonogram.solution) match {
          case (true, Some(solution)) =>
            val solutionImage = renderGrid(solution, cellSize)

            val borderSectionSize = fontSize + (fontBorder * 2)
            val combinedImage = new BufferedImage(
              withHints.getWidth,
              withHints.getHeight + solutionImage.getHeight + borderSectionSize,
              BufferedImage.TYPE_INT_ARGB
            )

            val combG2 = combinedImage.createGraphics()
            combG2.drawImage(withHints, 0, 0, null)
            combG2.setColor(Color.BLACK)
            combG2.drawString(
              "Solution:",
              fontBorder,
              withHints.getHeight + fontSize
            )
            combG2.drawImage(
              solutionImage,
              0,
              withHints.getHeight() + borderSectionSize,
              null
            )

            combinedImage
          case (false, _) | (true, None) => withHints
        }

      // finally write out the image
      ImageIO.write(finalImage, "png", outputStream)
    }

  private def renderGrid(
      grid: Grid,
      cellSize: Int
  ): BufferedImage = {
    // create the image

    val width  = grid.width
    val height = grid.height

    val imageWidth  = cellSize * width
    val imageHeight = cellSize * height

    val img = new BufferedImage(
      imageWidth,
      imageHeight,
      BufferedImage.TYPE_INT_ARGB
    )
    val g2 = img.createGraphics()

    var currentBg = emptyColours._1

    0.until(width).foreach { x =>
      0.until(height).foreach { y =>
        if (currentBg == emptyColours._1) currentBg = emptyColours._2
        else currentBg = emptyColours._1
        g2.setColor(currentBg)
        g2.fillRect(
          x * cellSize,
          y * cellSize,
          cellSize,
          cellSize
        )

        val square: Square = grid(x)(y)
        square match {
          case Blank =>
          case Occupied =>
            g2.setColor(filledColour)
            g2.fillRect(
              x * cellSize,
              y * cellSize,
              cellSize,
              cellSize
            )
          case Crossed =>
            g2.setColor(crossedColour)
            g2.drawLine(
              x * cellSize,
              y * cellSize,
              (x * cellSize) + cellSize,
              (y * cellSize) + cellSize
            )
            g2.drawLine(
              x * cellSize,
              (y * cellSize) + cellSize - 1,
              (x * cellSize) + cellSize - 1,
              y * cellSize
            )
        }
      }
    }

    img
  }
}
