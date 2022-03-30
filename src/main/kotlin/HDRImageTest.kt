import org.junit.Test
import org.junit.Assert.*
import java.io.ByteArrayOutputStream
import java.util.*


internal class HDRImageTest {
    fun byteArrayOfInts(vararg ints: Int) =
        ByteArray(ints.size) {pos -> ints[pos].toByte()}
    val reference_le = byteArrayOfInts(
        0x50, 0x46, 0x0a, 0x33, 0x20, 0x32, 0x0a, 0x2d, 0x31, 0x2e, 0x30, 0x0a,
        0x00, 0x00, 0xc8, 0x42, 0x00, 0x00, 0x48, 0x43, 0x00, 0x00, 0x96, 0x43,
        0x00, 0x00, 0xc8, 0x43, 0x00, 0x00, 0xfa, 0x43, 0x00, 0x00, 0x16, 0x44,
        0x00, 0x00, 0x2f, 0x44, 0x00, 0x00, 0x48, 0x44, 0x00, 0x00, 0x61, 0x44,
        0x00, 0x00, 0x20, 0x41, 0x00, 0x00, 0xa0, 0x41, 0x00, 0x00, 0xf0, 0x41,
        0x00, 0x00, 0x20, 0x42, 0x00, 0x00, 0x48, 0x42, 0x00, 0x00, 0x70, 0x42,
        0x00, 0x00, 0x8c, 0x42, 0x00, 0x00, 0xa0, 0x42, 0x00, 0x00, 0xb4, 0x42
    )

    val reference_be = byteArrayOfInts(
        0x50, 0x46, 0x0a, 0x33, 0x20, 0x32, 0x0a, 0x31, 0x2e, 0x30, 0x0a, 0x42,
        0xc8, 0x00, 0x00, 0x43, 0x48, 0x00, 0x00, 0x43, 0x96, 0x00, 0x00, 0x43,
        0xc8, 0x00, 0x00, 0x43, 0xfa, 0x00, 0x00, 0x44, 0x16, 0x00, 0x00, 0x44,
        0x2f, 0x00, 0x00, 0x44, 0x48, 0x00, 0x00, 0x44, 0x61, 0x00, 0x00, 0x41,
        0x20, 0x00, 0x00, 0x41, 0xa0, 0x00, 0x00, 0x41, 0xf0, 0x00, 0x00, 0x42,
        0x20, 0x00, 0x00, 0x42, 0x48, 0x00, 0x00, 0x42, 0x70, 0x00, 0x00, 0x42,
        0x8c, 0x00, 0x00, 0x42, 0xa0, 0x00, 0x00, 0x42, 0xb4, 0x00, 0x00
    )

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
    fun pixel_offset(){
        val image = HDRImage(4, 4)
        val testpos: Int = image.pixel_offset(3, 2)
        val position: Int = 11
        assertEquals(position, testpos)
    }

    @Test
    fun AverageLuminosity(){
        val img = HDRImage(2, 1)
        img.SetPixel(0, 0, Color(5.0f, 10.0f, 15.0f))
        img.SetPixel(1, 0, Color(500.0f, 1000.0f, 1500.0f))
        assertEquals(100.0f, img.AverageLuminosity(0.0f))
    }

    @Test
    fun NormalizeImage() {
        val img = HDRImage(2, 1)
        img.SetPixel(0, 0, Color(5.0f, 10.0f, 15.0f))
        img.SetPixel(1, 0, Color(500.0f, 1000.0f, 1500.0f))

        img.NormalizeImage(factor = 1000.0f, luminosity = 100.0f)
        assert(img.GetPixel(0, 0).is_close(Color(0.5e2f, 1.0e2f, 1.5e2f)))
        assert(img.GetPixel(1, 0).is_close(Color(0.5e4f, 1.0e4f, 1.5e4f)))
    }

    @Test
    fun WritePFM() {
        val img: HDRImage = HDRImage(3, 2)
        img.SetPixel(0, 0, Color(1.0e1f, 2.0e1f, 3.0e1f))
        img.SetPixel(1, 0, Color(4.0e1f, 5.0e1f, 6.0e1f))
        img.SetPixel(2, 0, Color(7.0e1f, 8.0e1f, 9.0e1f))
        img.SetPixel(0, 1, Color(1.0e2f, 2.0e2f, 3.0e2f))
        img.SetPixel(1, 1, Color(4.0e2f, 5.0e2f, 6.0e2f))
        img.SetPixel(2, 1, Color(7.0e2f, 8.0e2f, 9.0e2f))

        val buf = ByteArrayOutputStream()
        img.WritePFM(buf)
        assertTrue(Arrays.equals(buf.toByteArray(), reference_le))

    }
}