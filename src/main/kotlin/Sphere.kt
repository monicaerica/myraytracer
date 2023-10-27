import kotlin.math.*



/**
 * The sphere class, contains the information required to construct the sphere and place it in the scene
 *
 * @property transformation: The transformation to be applied to the sphere
 */
//class DiffuseBRDF(pigment: Pigment = UniformPigment(WHITE)): BRDF(pigment)
class Sphere(transformation: Transformation = Transformation(), material: Material = Material()) : Shape(transformation, material) {

//class Sphere() : Shape() {
    /**
     * Finds the point on the sphere (in three-dimensional space) at which there is an intersection with a [ray],
     * then converts it to uv coordinates and gives back a hit record containing the required information to render the sphere
     *
     * @param ray: The ray fired which may intersect the sphere
     * @return Either a "null" if there is no intersection or a hitrecord type if an intersection is recorded
     */
    override fun rayIntersection(ray: Ray): HitRecord? {
        val invRay: Ray = transformation.Inverse() * ray

        val originVec: Vec = invRay.Origin.PointToVec()

        val b: Float = originVec * invRay.Dir
        val c: Float = invRay.Dir.SquaredNorm() 

        val delta: Float = b * b - c * (originVec.SquaredNorm() - 1.0f)

        if (delta <= 0)
            return null

        val sqrtDelta: Float = sqrt(delta)
        val tmin: Float = (-b - sqrtDelta) / c
        val tmax: Float = (-b + sqrtDelta) / c

        val firstHit = when {
            tmin in ray.tmin..ray.tmax -> {
                tmin
            }
            tmax in ray.tmin..ray.tmax -> {
                tmax
            }
            else -> {
                return null
            }
        }
        val hitPoint: Point = invRay.At(firstHit)

        return HitRecord(
            worldPoint = transformation * hitPoint,
            normal = transformation * sphereNormal(hitPoint, invRay.Dir),
            surfacePoint = spherePointToUv(hitPoint),
            t = firstHit,
            ray = ray,
            shape = this
        )

    }

    /**
    * Given a point on the surface of the spheres gives the three components of the normal vector on that point on the sphere.
    *
    * @param point: A  point in three-dimensional space on the surface of the sphere
    * @return The normal at that point
    */
    private fun sphereNormal(point: Point, rayDir: Vec): Normal {
        val result = Normal(point.x, point.y, point.z)
        if (point.PointToVec() * rayDir < 0)
            return result
        else
            return result.Neg()
    }

    /**
    * Given a point in three dimensions this method returns a 2 dimensional vector containing as components the 2 dimensional uv coordinates.
    *
    * @param point: A point in three-dimensional space on the surface of the sphere
    * @return A Vec2 containing the two-dimensional uv coordinates
    */
    private fun spherePointToUv(point: Point): Vec2d {

        var u: Float = atan2(point.y, point.x) 
        if (point.y < 0) 
            u += 2.0f * PI.toFloat()
        val v: Float = acos(point.z) / PI.toFloat()
        if (u < 0.0f)
            u += 1.0f
        return Vec2d(u, v)
    }
}