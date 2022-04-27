import kotlin.math.*

fun sphereNormal(point: Point, rayDir: Vec): Normal{
    val result = Normal(point.x, point.y, point.z)
    if (point.point_to_vec() * rayDir < 0)
        return result
    else
        return result.neg()
}

fun spherePointToUv(point: Point): Vec2d{
    var u: Float = atan2(point.y, point.x) / (2.0f * PI.toFloat())
    val v: Float = acos(point.z) / PI.toFloat()
    if (u<0.0f)
        u += 1.0f
    return Vec2d(u, v)
}
class Sphere(val transformation: Transformation): Shape() {
    override fun rayIntersection(ray: Ray): HitRecord? {
        val invRay: Ray = ray.transform(this.transformation.Inverse())
        val originVec: Vec = invRay.Origin.point_to_vec()
        val a: Float = invRay.Dir.SquaredNorm()
        val b: Float = originVec * 2.0f * invRay.Dir
        val c: Float = originVec.SquaredNorm() - 1.0f

        val delta: Float = b * b - 4.0f * a * c

        if (delta <= 0)
            return null

        val sqrtDelta: Float = sqrt(delta)
        val tmin: Float = (-b - sqrtDelta) / (2.0f * a)
        val tmax: Float = (-b + sqrtDelta) / (2.0f * a)
        var firstHit: Float = 0.0f
        if (tmin > invRay.tmin && tmin < invRay.tmax)
            firstHit = tmin
        if (tmax > invRay.tmin && tmax < invRay.tmax)
            firstHit = tmax

        val hitPoint: Point = invRay.At(firstHit)

        return HitRecord(
            worldPoint = this.transformation * hitPoint,
            normal = this.transformation * sphereNormal(hitPoint, invRay.Dir),
            surfacePoint = spherePointToUv(hitPoint),
            t = firstHit,
            ray = ray
        )

    }
}