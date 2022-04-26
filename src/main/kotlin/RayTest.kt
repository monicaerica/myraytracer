import org.junit.Assert.*
import org.junit.Test
internal class RayTest{
    @Test
    fun IsClose(){
        val ray1: Ray = Ray(Origin = Point(1.0f, 2.0f, 3.0f), Dir = Vec(5.0f, 4.0f, -1.0f))
        val ray2: Ray = Ray(Origin = Point(1.0f, 2.0f, 3.0f), Dir = Vec(5.0f, 4.0f, -1.0f))
        val ray3: Ray = Ray(Origin = Point(5.0f, 1.0f, 4.0f), Dir = Vec(3.0f, 9.0f, 4.0f))

        assertTrue(ray1.IsClose(ray2))
        assertFalse(ray1.IsClose(ray3))
    }

    @Test
    fun At(){
        val ray:Ray = Ray(Origin=Point(1.0f, 2.0f, 4.0f), Dir=Vec(4.0f, 2.0f, 1.0f))
        assertTrue(ray.At(0.0f).is_close(ray.Origin))
        assertTrue(ray.At(1.0f).is_close(Point(5.0f, 4.0f, 5.0f)))
        assertTrue(ray.At(2.0f).is_close(Point(9.0f, 6.0f, 6.0f)))
    }

    @Test
    fun Transform(){
        val ray: Ray = Ray(Origin = Point(1.0f, 2.0f, 3.0f), Dir = Vec(6.0f, 5.0f, 4.0f))
        val transformation: Transformation = Translation(Vec(10.0f, 11.0f, 12.0f)) * RotationX(90.0f)
        val transformed: Ray = ray.transform(transformation)
        assertTrue(transformed.Origin.is_close(Point(11.0f, 8.0f, 14.0f)))
        assertTrue(transformed.Dir.IsClose(Vec(6.0f, -4.0f, 5.0f)))
    }
}