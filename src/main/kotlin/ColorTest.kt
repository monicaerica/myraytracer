import org.junit.Test
import org.junit.Assert.*

internal class ColorTest {

    @Test
    fun plus() {
        val a = Color(1.0f, 2.0f, 4.0f)
        val b = Color(2.0f, 3.0f, 5.0f)
        val sum = a + b
        assertEquals(Color(3.0f, 5.0f, 9.0f), sum)
    }

    @Test
    fun times() {
        val a = Color(1.0f, 2.0f, 4.0f)
        val scal = 2.0f
        val times = a * scal
        assertEquals(Color(2.0f, 4.0f, 8.0f), times)
    }

    @Test
    fun is_close() {
        val a = Color(1.0f, 2.0f, 4.0f)
        assertEquals(Color(1.0f, 2.0f, 4.0f), a)
    }

    @Test
    fun Luminosity() {
        val col1: Color = Color(1.0f, 2.0f, 3.0f)
        val col2: Color = Color(9.0f, 5.0f, 7.0f)

        val lum1: Float = 2.0f
        val lum2: Float = 7.0f

        assertEquals(lum1, col1.Luminosity())
        assertEquals(lum2, col2.Luminosity())

    }
}