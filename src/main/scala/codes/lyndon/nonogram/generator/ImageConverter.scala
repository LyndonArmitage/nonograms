package codes.lyndon.nonogram.generator

import codes.lyndon.nonogram.Nonogram

import java.awt.image.BufferedImage
import java.io.File
import java.nio.file.Path
import javax.imageio.ImageIO
import scala.util.Try

trait ImageConverter {

  def convert(path: Path): Try[Nonogram] = convert(path.toFile)

  def convert(file: File): Try[Nonogram] =
    Try {
      convert(ImageIO.read(file))
    }.flatten

  def convert(image: BufferedImage): Try[Nonogram]
}
