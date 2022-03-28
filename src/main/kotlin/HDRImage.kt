import java.nio.charset.StandardCharsets
import java.io.InputStream
import java.io.*
import java.nio.*
import InvalidPfmFileFormat
import javax.imageio.ImageIO
import kotlin.math.*

fun Clamp(x: Float): Float{
    return x / (1.0f + x)
}


data class HDRImage (
    val width: Int,
    val height: Int,
    var pixels: Array<Color> = Array(width * height) {Color (0.0f,0.0f,0.0f )}
        ) {
    fun ValidCoordinates(x: Int, y: Int): Boolean{
        return ((x>=0 && x<= this.width) && (y>=0 && y<= this.height))
    }

    //pixel_offset() obtain the 1 dimensional position of a pixel from a tuple (x, y)
    fun pixel_offset(x: Int, y: Int): Int{
        return (y * this.width + x)
    }

    //get pixel(): obtain the pixel instance in the (x, y) position of the image
    fun GetPixel(x: Int, y: Int): Color{
        assert(ValidCoordinates(x, y))
        val pos = pixel_offset(x, y)
        val pixel: Color = this.pixels[pos]
        return pixel
    }

    //set pixel(): sets the pixel instance in the (x, y) position of the image
    fun SetPixel(x: Int, y: Int, color: Color): Unit{
        assert(ValidCoordinates(x, y))
        val pos = pixel_offset(x, y)
        this.pixels[pos] = color
    }

    /**
     * This function reads from an input up to a linefeed character and writes to a string the line
     * @param: insTream: The stream to be read as an input
     */

    private fun ReadLine(inStream: InputStream): String {
            val buff = ByteArray(50)
            var j: Int = 0
            for (i in buff.indices) {
                val b = inStream.read()
                if (b == '\n'.code) {
                    val j = i
                    break
                }
                else
                    buff[i] = b.toByte()
            }
            return String(buff, offset = 0, j, charset = StandardCharsets.US_ASCII)
    }

    /**
     * Calculates the average luminosity
     */

    fun AverageLuminosity(delta: Float = 1e-10f): Float {
        var Sum: Float = 0.0f
        for (pix in pixels){
            Sum += log10(delta + pix.Luminosity())
        }
        return 10.0f.pow(Sum / pixels.size)
    }

    fun NormalizeImage(factor: Float, luminosity: Float? = null){
        var lum = luminosity ?: AverageLuminosity();
        for (i in 0..pixels.size-1){
            this.pixels[i] = this.pixels[i] * (factor / lum)
        }
    }

    /**
     * This function reads data from a stream 4 bits at a time and converts to a float based on the endianess
     * @param: InputStream:
     * @param: endianess: defines the byte order: big or little endian based on number +/-1.0
     *
     */
    private fun StreamToFloat(stream: InputStream, endianness: ByteOrder = ByteOrder.BIG_ENDIAN ): Float{
        try {
            val buffer = ByteBuffer.wrap(stream.readNBytes(4))
            buffer.order(endianness)
            return buffer.float
        }
            catch(e: java.nio.BufferUnderflowException) {
            throw InvalidPfmFileFormat("Not enough bytes left")
        }
    }

    fun writeFloatToStream(stream: OutputStream, value: Float, order: ByteOrder){
        val bytes = ByteBuffer.allocate(4).putFloat(value).array()
        //reverse the byte order if little endian
        if (order == ByteOrder.LITTLE_ENDIAN) {
            bytes.reverse()
        }

        stream.write(bytes)
    }

    fun WritePFM(outStream: OutputStream, endianness: ByteOrder = ByteOrder.LITTLE_ENDIAN){
        var endiannessStr = ""
        if (endianness == ByteOrder.LITTLE_ENDIAN)
            endiannessStr = "-1.0"
        else
            endiannessStr = "1.0"

        val header: String = "PF\n${this.width} ${this.height}\n${endiannessStr}\n"
        outStream.write(header.toByteArray())

        for (y in this.height - 1  downTo 0){
            for (x in 0 until this.width){
                var color: Color = this.GetPixel(x, y)
                writeFloatToStream(outStream, color.r, endianness)
                writeFloatToStream(outStream, color.g, endianness)
                writeFloatToStream(outStream, color.b, endianness)
            }
        }
    }

    /**
     * Saves a .pfm file, as a filename enter the name, the extension is added automatically
     */
    fun SavePFM(filename: String){
        FileOutputStream(filename+".pfm").use {outStream ->
            WritePFM(outStream)
        }
    }




}
