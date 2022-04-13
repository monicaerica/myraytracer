import org.junit.Assert
import org.junit.Test
import org.junit.Assert.*

internal class VecTest {

    @Test
    fun plus() {
        val vec1: Vec = Vec(1.0f, 2.0f, 3.0f)
        val vec2: Vec = Vec(2.0f, 3.0f, 4.0f)
        val sum: Vec = Vec(3.0f, 5.0f, 7.0f)
        val vec3: Vec = vec1 + vec2
        assertEquals(sum, vec3)
    }

    @Test
    fun times() {
        val vec: Vec = Vec(1.0f, 2.0f, 3.0f)
        val scalar: Float = 2.0f
        val res: Vec = Vec(2.0f, 4.0f, 6.0f)
        assertEquals(res, vec * scalar)
    }

    @Test
    fun neg() {
        val vec: Vec = Vec(1.0f, 2.0f, 3.0f)
        val negvec: Vec = Vec(-1.0f, -2.0f, -3.0f)
        assertEquals(negvec, vec.Neg())
    }

    @Test
    fun minus() {
        val vec1: Vec = Vec(1.0f, 2.0f, 3.0f)
        val vec2: Vec = Vec(2.0f, 4.0f, 2.0f)
        val vecsub: Vec = Vec(-1.0f, -2.0f, 1.0f)
        assertEquals(vecsub, vec1 - vec2)
    }

    @Test
    fun testTimes() {
        val vec1: Vec = Vec(1.0f, 2.0f, 3.0f)
        val vec2: Vec = Vec(2.0f, 4.0f, 2.0f)
        val dot: Float = 16.0f
        assertEquals(dot, vec1 * vec2)
    }

    @Test
    fun squaredNorm() {
        val vec: Vec = Vec(1.0f, 2.0f, 3.0f)
        val norm2: Float = 14.0f
        assertEquals(norm2, vec.SquaredNorm())
    }

    @Test
    fun norm() {
        val vec: Vec = Vec(1.0f, 2.0f, 3.0f)
        Assert.assertEquals(14.0f, vec.Norm() * vec.Norm(), 1e-5f)
    }

    @Test
    fun cross() {
        val vec1: Vec = Vec(1.0f, 2.0f, 3.0f)
        val vec2: Vec = Vec(2.0f, 3.0f, 4.0f)
        val cross: Vec = Vec(-1.0f, 2.0f, -1.0f)
        assertEquals(cross, vec1.Cross(vec2))
    }

    @Test
    fun normalize() {
        val vec: Vec = Vec(1.0f, 2.0f, 3.0f)
        val normalized: Vec = Vec(1.0f / vec.Norm(), 2.0f / vec.Norm(), 3.0f / vec.Norm())
        assertNotEquals(normalized, vec.Normalize())
    }
}