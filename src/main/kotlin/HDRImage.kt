import java.io.*
import java.nio.*
import InvalidPfmFileFormat

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
}
