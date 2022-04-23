import java.awt.image.BufferedImage
import java.io.FileInputStream
import javax.imageio.ImageIO


fun main(args: Array<String>) {
    val inStream = FileInputStream(args[0])
    var image = HDRImage()
    image.ReadPFMImage(inStream)
    val gamma: Float = 1.5f
    val format: String = "jpg"
    val name: String = args[0] + format
    image.SaveLDR(name, "JPEG", gamma)
}
