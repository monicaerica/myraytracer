import org.junit.Assert.*
import org.junit.Test

internal class PointTest {

    @Test
    fun point_tostring(){
        val point: Point = Point(1.0f, 2.0f, 4.0f)
        assertEquals(point.point_tostring(), "Point: (x:1.0, y:2.0, z:4.0)")
    }

    @Test
    fun is_close() {
        val a = Point(1.0f, 2.0f, 3.0f)
        val b = Point(4.0f, 6.0f, 8.0f)
        assert(a.is_close(Point(1.0f, 2.0f, 3.0f)))
        assert(!a.is_close(b))
    }

    @Test
    fun point_to_vec(){
        val a = Point(1.0f, 2.0f, 4.0f)
        assertEquals(a.point_to_vec(), Vec(1.0f, 2.0f, 4.0f))
    }

    @Test
    fun operations_overload(){
        val p1 = Point(1.0f, 2.0f, 3.0f)
        val p2 = Point(4.0f, 6.0f, 8.0f)
        val v = Vec(4.0f, 6.0f, 8.0f)
        assert((p1 * 2).is_close(Point(2.0f, 4.0f, 6.0f)))
        assert((p1 + v).is_close(Point(5.0f, 8.0f, 11.0f)))
        assert((p1 - v).is_close(Point(-3.0f, -4.0f, -5.0f)))
    }
}