import kotlin.math.abs
import kotlin.math.floor

abstract class Shape(val transformation: Transformation, val material: Material) {
    open fun rayIntersection(ray: Ray): HitRecord?{
        return null
    }
}

class Plane(transformation: Transformation = Transformation(), material: Material = Material()): Shape(transformation, material) {
    override fun rayIntersection(ray: Ray): HitRecord? {
        val invRay: Ray = ray.transform(this.transformation.Inverse())
        if (abs(invRay.Dir.z)<1e-5f)
            return null
        val t: Float = invRay.Origin.z / invRay.Dir.z

        if (t <= invRay.tmin || t > invRay.tmax)
            return null

        val hitPoint: Point = invRay.At(t)
        var norm: Normal = Normal(0.0f, 0.0f, -1.0f)
        if (invRay.Dir.z < 0.0f)
            norm = Normal(0.0f, 0.0f, 1.0f)


        return HitRecord(
            worldPoint = this.transformation * hitPoint,
            normal = norm,
            surfacePoint = Vec2d(hitPoint.x - floor(hitPoint.x), hitPoint.y - floor(hitPoint.y)),
            t = t,
            ray = ray,
            material = this.material
        )
    }
}