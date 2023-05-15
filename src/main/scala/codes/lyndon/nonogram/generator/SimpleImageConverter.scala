package codes.lyndon.nonogram.generator
import codes.lyndon.nonogram.{Blank, Grid, Nonogram, Occupied}

import java.awt.image.BufferedImage
import scala.util.Try

object SimpleImageConverter extends ImageConverter {
  override def convert(image: BufferedImage): Try[Nonogram] =
    Try {

      val width  = image.getWidth
      val height = image.getHeight

      val blackAndWhite = new BufferedImage(
        width,
        height,
        BufferedImage.TYPE_BYTE_BINARY
      )
      blackAndWhite
        .createGraphics()
        .drawImage(image, 0, 0, width, height, null)

      val rows = 0.until(height).map { y =>
        val arr = 0.until(width).toArray[Int]
        blackAndWhite.getData.getPixels(0, y, width, 1, arr)
        arr.map {
          case 0 => Blank
          case _ => Occupied
        }.toSeq
      }

      val grid = Grid(rows: _*)

      val emptyGrid = Grid.empty(width, height)

      val (horz, vert) = NonogramGeneratorTools.generateHints(grid)

      Nonogram(emptyGrid, horz, vert, solution = Some(grid))
    }
}
