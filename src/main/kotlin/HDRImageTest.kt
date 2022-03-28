import org.junit.Test
import org.junit.Assert.*

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
}