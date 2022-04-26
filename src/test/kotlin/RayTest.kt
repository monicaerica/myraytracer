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
}