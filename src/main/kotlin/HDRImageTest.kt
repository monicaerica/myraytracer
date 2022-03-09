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
}