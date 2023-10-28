import kotlin.math.abs
import kotlin.math.floor

abstract class Shape(val transformation: Transformation, val material: Material) {
    open fun rayIntersection(ray: Ray): HitRecord?{
        return null
    }

}

class Plane(transformation: Transformation = Transformation(), material: Material = Material()): Shape(transformation, material) {
    
    override fun rayIntersection(ray: Ray): HitRecord? {

        val invRay: Ray = transformation.Inverse() * ray

        if (abs(invRay.Dir.z)<1e-5f)
            return null

        val t: Float = -invRay.Origin.z / invRay.Dir.z

        if (t <= invRay.tmin || t > invRay.tmax)
            return null

        val hitPoint: Point = invRay.At(t)
        var norm: Normal = transformation*Normal(0.0f, 0.0f, -1.0f)
        if (invRay.Dir.z < 0.0f)
            norm = transformation*Normal(0.0f, 0.0f, 1.0f)


        return HitRecord(
            worldPoint = transformation * hitPoint,
            normal = norm,
            surfacePoint = Vec2d(hitPoint.x - floor(hitPoint.x), hitPoint.y - floor(hitPoint.y)),
            t = t,
            ray = ray,
            shape = this
        )
    }
}