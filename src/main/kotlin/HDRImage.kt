import java.io.*
import java.nio.*
import InvalidPfmFileFormat
import java.io.InputStream
import java.util.Arrays
import java.lang.Float

data class HDRImage (
    val width: Int,
    val height: Int,
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

    fun StreamToFloat(stream: InputStream, endianness: ByteOrder = ByteOrder.BIG_ENDIAN): kotlin.Float {
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

    fun ReadPFMImage(stream: InputStream): HDRImage{
        var magic = ReadLine(stream)
        if (magic != "PF") throw InvalidPfmFileFormat("invalid magic in PFM file")
        var img_size = ReadLine(stream)
        var (width, height) = ParseImageSize(img_size)
        var endiannessline = ReadLine(stream)
        var endianness = parseEndianness(endiannessline)

        var result = HDRImage(width, height)
        pixels = Array(width * height) { Color(0.0F, 0.0F, 0.0F) }
        for (y in (height - 1) downTo 0) {
            for (x in 0 until width) {
                val r = StreamToFloat(stream, endianness)
                val g = StreamToFloat(stream, endianness)
                val b = StreamToFloat(stream, endianness)
                SetPixel(x, y, Color(r, g, b))
            }
        }
        return result
    }
}
