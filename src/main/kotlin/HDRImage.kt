import java.io.InputStream
import java.io.*
import java.nio.*
import java.util.Arrays
//import java.lang.Float
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import kotlin.math.*

fun Clamp(x: Float): Float{
    return x / (1.0f + x)
}

data class HDRImage (
    var width: Int = 0,
    var height: Int = 0,
    var pixels: Array<Color> = Array(width * height) {Color (0.0f,0.0f,0.0f )}
        ) {
    fun ValidCoordinates(x: Int, y: Int): Boolean {
        return ((x >= 0 && x <= this.width) && (y >= 0 && y <= this.height))
    }

    //pixel_offset() obtain the 1 dimensional position of a pixel from a tuple (x, y)
    fun PixelOffset(x: Int, y: Int): Int {
        return (y * this.width + x)
    }

    //get pixel(): obtain the pixel instance in the (x, y) position of the image
    fun GetPixel(x: Int, y: Int): Color {
        assert(ValidCoordinates(x, y))
        val pos = PixelOffset(x, y)
        val pixel: Color = this.pixels[pos]
        return pixel
    }

    //set pixel(): sets the pixel instance in the (x, y) position of the image
    fun SetPixel(x: Int, y: Int, color: Color): Unit {
        assert(ValidCoordinates(x, y))
        val pos = PixelOffset(x, y)
        this.pixels[pos] = color
    }


    fun ReadLine(stream: InputStream): String {
        var result = byteArrayOf()
        while (true) {
            var cur_byte = stream.readNBytes(1)
            if (Arrays.equals(cur_byte, "".toByteArray()) || Arrays.equals(cur_byte, "\n".toByteArray())){
                return String(result)
            }
            result += cur_byte[0]
        }
    }

    /**
     * This function reads from an input up to a linefeed character and writes to a string the line
     * @param: insTream: The stream to be read as an input
     */

//    private fun ReadLine(inStream: InputStream): String {
//            val buff = ByteArray(50)
//            var j: Int = 0
//            for (i in buff.indices) {
//                val b = inStream.read()
//                if (b == '\n'.code) {
//                    val j = i
//                    break
//                }
//                else
//                    buff[i] = b.toByte()
//            }
//            return String(buff, offset = 0, j, charset = StandardCharsets.US_ASCII)
//    }

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
            var buffer = ByteBuffer.wrap(stream.readNBytes(4))
            buffer.order(endianness)
            return buffer.float
        } catch (e: java.nio.BufferUnderflowException) {
            throw InvalidPfmFileFormat("Not enough bytes left")
        }
    }

    fun parseEndianness(line: String): ByteOrder {
        var fileendianness = 1.0F
        try {
            fileendianness = line.toFloat()
        } catch(e: NumberFormatException){ throw InvalidPfmFileFormat("Cannot find endianness") }
        when{
            (fileendianness > 0.0F) -> return ByteOrder.BIG_ENDIAN
            (fileendianness < 0.0F) -> return ByteOrder.LITTLE_ENDIAN
            else -> throw InvalidPfmFileFormat("Cannot parse endianness")
        }
    }

    fun ParseImageSize(line: String): Pair<Int, Int>{
        val elements = line.split(" ")
        var wi : Int?
        var he : Int?
        if (elements.size != 2) throw InvalidPfmFileFormat("invalid image size specification")
        try{
            wi = elements[0].toInt()
            he = elements[1].toInt()
            if ((wi < 0) || (he < 0)) {throw NumberFormatException("Invalid image size specification: width and height must be >=0")}
        }
        catch(e: NumberFormatException) {throw InvalidPfmFileFormat("invalid width/height")}
        return Pair(wi, he)
    }

    fun ReadPFMImage(stream: InputStream) {
        var magic = ReadLine(stream)
        if (magic != "PF") throw InvalidPfmFileFormat("invalid magic in PFM file")
        var img_size = ReadLine(stream)
        var (w, h) = ParseImageSize(img_size)
        width = w
        height = h
        var endiannessline = ReadLine(stream)
        var endianness = parseEndianness(endiannessline)

        pixels = Array(width * height) { Color(0.0F, 0.0F, 0.0F) }
        for (y in (height - 1) downTo 0) {
            for (x in 0 until width) {
                val r = StreamToFloat(stream, endianness)
                val g = StreamToFloat(stream, endianness)
                val b = StreamToFloat(stream, endianness)
                SetPixel(x, y, Color(r, g, b))
            }
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

    fun ClampImg (){
        for (i in 0 until this.pixels.size){
            this.pixels[i].r = Clamp(this.pixels[i].r)
            this.pixels[i].g = Clamp(this.pixels[i].g)
            this.pixels[i].b = Clamp(this.pixels[i].b)
        }

    }

    /**
     * Creates a LDR image where colors are defined as threeplets of integers ranging between 0 and 255
     * Requires a HDR image and a value for gamma (used in the tone mapping formula
     */
    fun WriteLDR(stream: OutputStream, format: String, gamma: Float = 1.0f){
        val img: BufferedImage = BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_RGB)
        for (y in 0 until this.height){
            for (x in 0 until this.width){
                val curCol = this.GetPixel(x, y)
                val sR: Int = (255 * curCol.r.pow(1 / gamma)).toInt()
                val sG: Int = (255 * curCol.g.pow(1 / gamma)).toInt()
                val sB: Int = (255 * curCol.b.pow(1 / gamma)).toInt()
                val rgb: Int = sR.shl(16) + sG.shl(8) + sB
                img.setRGB(x, y, rgb)
            }
        }
        ImageIO.write(img, format, stream)
    }

    fun SaveLDR(filename: String, format: String, gamma: Float = 1.0f){
        FileOutputStream(filename).use {outStream ->
            WriteLDR(outStream, format, gamma)

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
