import org.junit.Assert.*
import org.junit.Test
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.io.InputStream.*
import java.nio.ByteOrder
import InvalidPfmFileFormat

internal class HDRImageTest {

    @Test
    fun validCoordinates() {
        val image = HDRImage(10, 5)
        val valid = image.ValidCoordinates(2, 3)
        val nonval = image.ValidCoordinates(-1, 3)
        assertEquals(true, valid)
        assertEquals(false, nonval)

    }

    @Test
    fun GetPixel(){
        val colors = arrayOf<Color>(Color(1.0f, 1.0f, 1.0f), Color(1.0f, 1.0f, 0f), Color(1.0f, 0f, 0f), Color(0f, 0f, 0f))
        val image = HDRImage(2, 2, colors)
        val pixel_color = image.GetPixel(0, 0)
        assertEquals(Color(1.0f, 1.0f, 1.0f), pixel_color)
        assertNotEquals(Color(0f, 1.0f, 1.0f), pixel_color)
    }

    @Test
    fun SetPixel(){
        val colors = arrayOf<Color>(Color(1.0f, 1.0f, 1.0f), Color(1.0f, 1.0f, 0f), Color(1.0f, 0f, 0f), Color(0f, 0f, 0f))
        var image = HDRImage(2, 2, colors)
        image.SetPixel(0, 0, Color(0f, 1.0f, 0f))
        val pixel_color = image.GetPixel(0, 0)
        assertEquals(Color(0f, 1.0f, 0f), pixel_color)
        assertNotEquals(Color(0f, 1.0f, 1.0f), pixel_color)
    }

    @Test
    fun PixelOffset(){
        val image = HDRImage(4, 4)
        val testpos: Int = image.PixelOffset(3, 2)
        val position: Int = 11
        assertEquals(position, testpos)

    }

    @Test
    fun ReadLine(){
        val image = HDRImage(4, 4)
        var str = "hello\nworld"
        val stream: InputStream = ByteArrayInputStream(str.toByteArray())
        assertEquals(image.ReadLine(stream), "hello")
        assertEquals(image.ReadLine(stream), "world")
        assertEquals(image.ReadLine(stream), "")
    }

    @Test
    fun parseEndianness() {
        val image = HDRImage(4, 4)
        assertEquals(image.parseEndianness("1.0"), ByteOrder.BIG_ENDIAN)
        assertEquals(image.parseEndianness("-1.0"), ByteOrder.LITTLE_ENDIAN)
        assertThrows(InvalidPfmFileFormat::class.java) {
            image.parseEndianness("0.0")
        }
        assertThrows(InvalidPfmFileFormat::class.java) {
            image.parseEndianness("abc")
        }
    }

    @Test
    fun ParseImageSize(){
        val image = HDRImage(4, 4)
        assertEquals(image.ParseImageSize("3 2"), Pair(3,2))
        assertThrows(InvalidPfmFileFormat::class.java) {
            image.ParseImageSize("-3 2")
        }
        assertThrows(InvalidPfmFileFormat::class.java) {
            image.ParseImageSize("3 2 2")
        }
    }
}