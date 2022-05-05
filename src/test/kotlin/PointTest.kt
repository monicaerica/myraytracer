import org.junit.Assert
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class PointTest2 {

    companion object {
        val a = Point(1.0f, 2.0f, 3.0f)
        val b = Point(4.0f, 6.0f, 8.0f)
        val v = Vec(4.0f, 6.0f, 8.0f)
        val point: Point = Point(1.0f, 2.0f, 4.0f)
    }

    @Test
    fun point_tostring(){
        Assert.assertEquals(point.PointToString(), "Point: (x:1.0, y:2.0, z:4.0)")
    }

    @Test
    fun is_close() {
        assert(a.IsClose(Point(1.0f, 2.0f, 3.0f)))
        assert(!a.IsClose(b))
    }

    @Test
    fun point_to_vec(){
        assertEquals(a.PointToVec(), Vec(1.0f, 2.0f, 3.0f))
    }

    @Test
    fun operations_overload(){
        assert((a * 2).IsClose(Point(2.0f, 4.0f, 6.0f)))
        assert((a + v).IsClose(Point(5.0f, 8.0f, 11.0f)))
        assert((a - v).IsClose(Point(-3.0f, -4.0f, -5.0f)))
    }

    @Test
    fun test_points(){
        assert(a.IsClose(a))
        assert(!a.IsClose(b))
        assert((a * 2).IsClose(Point(2.0f, 4.0f, 6.0f)))
        assert((a + v).IsClose(Point(5.0f, 8.0f, 11.0f)))
        assert((b - a).IsClose(Vec(3.0f, 4.0f, 5.0f)))
        assert((a - v).IsClose(Point(-3.0f, -4.0f, -5.0f)))
    }

}