import java.awt.image.BufferedImage
import java.io.FileInputStream
import javax.imageio.ImageIO


fun main(args: Array<String>) {
    val inStream = FileInputStream("memorial.pfm")
    var image = HDRImage()
    image.ReadPFMImage(inStream)
    val gamma: Float = 1.5f
    image.SaveLDR("memorial"+gamma.toString(), "JPEG", gamma)
}
