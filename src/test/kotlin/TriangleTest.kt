import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class TriangleTest{
    @Test
    fun determinant(){
        val M: matrix = matrix(
            arrayOf(
                floatArrayOf(2.0f, -3.0f, 1.0f),
                floatArrayOf(2.0f, 0.0f, -1.0f),
                floatArrayOf(1.0f, 4.0f, 5.0f)
            ))
        val determinantCalc = M.calculateDeterminant()
        val determinant = 49.0f
        println(determinantCalc)
        assertEquals(determinant, determinantCalc)
    }

    @Test
    fun rayIntersection(){
        val triangle: Triangle = Triangle(Point(1.0f, 5.0f, 0.0f), Point(1.0f, -5.0f, 0.0f), Point(1.0f, 0.0f, 5.0f))
        val ray: Ray = Ray(Origin = Point(-2.0f, 0.0f, 2.5f), Dir = Vec(1.0f, 0.0f, 0.0f))
/*
        assert(triangle.rayIntersection(ray)!=null)
        val worldPoint: Point? = triangle.rayIntersection(ray)?.worldPoint
        if (worldPoint != null) {
            assertTrue(worldPoint.is_close(Point(1.0f, 0.0f, 2.5f)))
        }
*/
        val ray2: Ray = Ray(Origin = Point(-2.0f, 5.0f, 2.5f), Dir = VecX)
        assert(triangle.rayIntersection(ray2) == null)
        val worldPoint2: Point? = triangle.rayIntersection(ray)?.worldPoint
    }
}