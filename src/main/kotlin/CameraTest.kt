import org.junit.Assert.*
import org.junit.Test
import kotlin.math.PI

internal class CameraTest{
    @Test
    fun TestOrthogonal(){
        val cam: OrthogonalCamera = OrthogonalCamera(AspectRatio = 2.0f, trans = Transformation())
        val ray1: Ray = cam.FireRay(0.0f, 0.0f)
        val ray2: Ray = cam.FireRay(1.0f, 0.0f)
        val ray3: Ray = cam.FireRay(0.0f, 1.0f)
        val ray4: Ray = cam.FireRay(1.0f, 1.0f)

        assertEquals(0.0f, ray1.Dir.Cross(ray2.Dir).SquaredNorm())
        assertEquals(0.0f, ray1.Dir.Cross(ray3.Dir).SquaredNorm())
        assertEquals(0.0f, ray1.Dir.Cross(ray4.Dir).SquaredNorm())

        assertTrue(ray1.At(1.0f).is_close(Point(0.0f, 2.0f, -1.0f)))
        assertTrue(ray2.At(1.0f).is_close(Point(0.0f, -2.0f, -1.0f)))
        assertTrue(ray3.At(1.0f).is_close(Point(0.0f, 2.0f, 1.0f)))
        assertTrue(ray4.At(1.0f).is_close(Point(0.0f, -2.0f, 1.0f)))
    }

    @Test
    fun TestOrthogonalTrans(){
        val cam: OrthogonalCamera = OrthogonalCamera(trans = Translation(VecY.Neg() * 2.0f) * RotationZ(90.0f))
        val ray: Ray = cam.FireRay(0.5f, 0.5f)

        assertTrue(ray.At(1.0f).is_close(Point(0.0f, -2.0f, 0.0f)))
    }

    @Test
    fun TestPerspective(){
        val cam: PerpectiveCamera = PerpectiveCamera(AspectRatio = 2.0f, trans = Transformation(), distance = 1.0f)
        val ray1: Ray = cam.FireRay(0.0f, 0.0f)
        val ray2: Ray = cam.FireRay(1.0f, 0.0f)
        val ray3: Ray = cam.FireRay(0.0f, 1.0f)
        val ray4: Ray = cam.FireRay(1.0f, 1.0f)

        assertTrue(ray1.Origin.is_close(ray2.Origin))
        assertTrue(ray1.Origin.is_close(ray3.Origin))
        assertTrue(ray1.Origin.is_close(ray4.Origin))

        assertTrue(ray1.At(1.0f).is_close(Point(0.0f, 2.0f, -1.0f)))
        assertTrue(ray2.At(1.0f).is_close(Point(0.0f, -2.0f, -1.0f)))
        assertTrue(ray3.At(1.0f).is_close(Point(0.0f, 2.0f, 1.0f)))
        assertTrue(ray4.At(1.0f).is_close(Point(0.0f, -2.0f, 1.0f)))
    }

    @Test
    fun TestPerspectiveTrans(){
        val cam: PerpectiveCamera = PerpectiveCamera(trans = Translation(VecY.Neg() * 2.0f) * RotationZ(90.0f))
        val ray: Ray = cam.FireRay(0.5f, 0.5f)

        assertTrue(ray.At(1.0f).is_close(Point(0.0f, -2.0f, 0.0f)))
    }

}