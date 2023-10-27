import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class SphereKtTest {

    @Test
    fun sphereNormal() {
    }

    @Test
    fun spherePointToUv() {
    }

    @Test
    fun testHit(){
        val sphere: Sphere = Sphere()

        val ray1: Ray = Ray(Point(0.0f, 0.0f, 2.0f), VecZ.Neg())
        val intersection1 = sphere.rayIntersection(ray1)
        assertTrue(intersection1!=null)
        
        assertTrue(
            HitRecord(
                worldPoint = Point(0.0f, 0.0f, 1.0f),
                normal = Normal(0.0f, 0.0f, 1.0f),
                surfacePoint = Vec2d(0.0f, 0.0f),
                t = 1.0f,
                ray = ray1
            ).isClose(intersection1)
        )

        val ray2: Ray = Ray(Point(3.0f, 0.0f, 0.0f), VecX.Neg())
        val intersection2 = sphere.rayIntersection(ray2)
        assertTrue(intersection2!=null)
        assertTrue(
            HitRecord(
                worldPoint = Point(1.0f, 0.0f, 0.0f),
                normal = Normal(1.0f, 0.0f, 0.0f),
                surfacePoint = Vec2d(0.0f, 0.5f),
                t = 2.0f,
                ray = ray2
            ).isClose(intersection2)
        )
    }

    @Test
    fun testTransformation(){
        val sphere: Sphere = Sphere(transformation = Transformation().Translation(Vec(10.0f, 0.0f, 0.0f)))

        val ray1: Ray = Ray(Point(10.0f, 0.0f, 2.0f), VecZ.Neg())
        val intersection1 = sphere.rayIntersection(ray1)
        assertTrue(intersection1!=null)
        println(intersection1!!.normal.NormalToString())
        assertTrue(
            HitRecord(
                worldPoint = Point(10.0f, 0.0f, 1.0f),
                normal = Normal(0.0f, 0.0f, 1.0f),
                surfacePoint = Vec2d(0.0f, 0.0f),
                t = 1.0f,
                ray = ray1
            ).isClose(intersection1)
        )

        val ray2: Ray = Ray(Point(13.0f, 0.0f, 0.0f), VecX.Neg())
        val intersection2 = sphere.rayIntersection(ray2)
        assertTrue(intersection1!=null)
        assertTrue(
            HitRecord(
                worldPoint = Point(11.0f, 0.0f, 0.0f),
                normal = Normal(1.0f, 0.0f, 0.0f),
                surfacePoint = Vec2d(0.0f, 0.5f),
                t = 2.0f,
                ray = ray2
            ).isClose(intersection2)
        )

        assertTrue(sphere.rayIntersection(ray = Ray(Point(0.0f, 0.0f, 2.0f), Dir = VecZ.Neg()))== null )
        assertTrue(sphere.rayIntersection(ray = Ray(Point(-10.0f, 0.0f, 0.0f), Dir = VecZ.Neg()))== null )
    }
}