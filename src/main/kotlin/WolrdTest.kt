import org.junit.Assert
import org.junit.Test
import org.junit.Assert.*

internal class WolrdTest {
    companion object {
        val VEC_X : Vec = Vec(1.0f, 0.0f, 0.0f)
        val VEC_Y : Vec = Vec(0.0f, 1.0f, 0.0f)
        val VEC_Z : Vec = Vec(0.0f, 0.0f, 1.0f)
        var sphere1: Sphere = Sphere(Translation(VEC_X * 2.0f))
        var sphere2: Sphere = Sphere(Translation(VEC_X * 8.0f))
    }

    @Test
    fun TestRayIntersections(){
        var world = World()
        var intersection1 : HitRecord?
        var intersection2 : HitRecord?
        world.AddShape(sphere1)
        world.AddShape(sphere2)

        intersection1 = world.rayIntersection(Ray(Point(0.0f, 0.0f, 0.0f), VEC_X))
        if (intersection1 != null) {
            assert(intersection1.worldPoint.is_close(Point(1.0f, 0.0f, 0.0f)))
        }
        assertNotNull(intersection1)
        intersection2 = world.rayIntersection(Ray(Point(10.0f, 0.0f, 0.0f), VEC_X * -1.0f))
        if (intersection2 != null) {
            assert(intersection2.worldPoint.is_close(Point(9.0f, 0.0f, 0.0f)))
        }
        assertNotNull(intersection2)
    }
}