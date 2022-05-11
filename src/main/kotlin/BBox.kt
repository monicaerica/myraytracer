import kotlin.math.abs

fun getNormal(face: Int): Normal{
    val facex: Normal = Normal(1.0f, 0.0f, 0.0f)
    val facey: Normal = Normal(0.0f, 1.0f, 0.0f)
    val facez: Normal = Normal(0.0f, 0.0f, 1.0f)
    var faceNorm: Normal = Normal()
    when(face){
        0 -> faceNorm = facex.Neg()
        1 -> faceNorm = facey.Neg()
        2 -> faceNorm = facez.Neg()
        3 -> faceNorm = facex
        4 -> faceNorm = facey
        5 -> faceNorm = facez
    }
    return faceNorm
}

fun getUv(face: Int, point: Point): Vec2d{
    var maxAxis: Float = 0.0f
    var uc: Float = 0.0f
    var vc: Float = 0.0f
    when(face) {
        0 -> {maxAxis = abs(point.x)
            uc = point.y
            vc = point.z}
        3 ->  {maxAxis = abs(point.x)
            uc = -point.y
            vc = point.z}
        2 -> {maxAxis = abs(point.y)
            uc = point.x
            vc = point.y}
        5 ->  {maxAxis = abs(point.y)
            uc = point.x
            vc = -point.y}
        1 -> {maxAxis = abs(point.z)
            uc = point.x
            vc = point.z}
        4 ->  {maxAxis = abs(point.z)
            uc = -point.x
            vc = point.z}
    }

    return Vec2d(0.5f * (uc / maxAxis + 1.0f), 0.5f * (vc / maxAxis + 1.0f))
}


class BBox(val corner1: Point = Point(0.0f, 0.0f, 0.0f), val corner2: Point = Point(0.0f, 0.0f, 1.0f), val transformation: Transformation): Shape() {

    /**
     * Detrmines whether a ray hits or not the box
     * @param ray: A ray
     * @return hit: true if ray hits the objects, false if it doesn't
     */
    fun boxHit(ray: Ray): Boolean{
        val txMin: Float
        val txMax: Float
        val tyMin: Float
        val tyMax: Float
        val tzMin: Float
        val tzMax: Float

        val a: Float = 1.0f / ray.Dir.x

        if (a >= 0.0f){
            txMin = (corner1.x - ray.Origin.x) * a
            txMax = (corner2.x - ray.Origin.x) * a
        }
        else{
            txMax = (corner1.x - ray.Origin.x) * a
            txMin = (corner2.x - ray.Origin.x) * a
        }

        val b: Float = 1.0f / ray.Dir.y

        if (a >= 0.0f){
            tyMin = (corner1.y - ray.Origin.y) * b
            tyMax = (corner2.y - ray.Origin.y) * b
        }
        else{
            tyMax = (corner1.y - ray.Origin.y) * b
            tyMin = (corner2.y - ray.Origin.y) * b
        }

        val c: Float = 1.0f / ray.Dir.z

        if (c >= 0.0f){
            tzMin = (corner1.z - ray.Origin.z) * b
            tzMax = (corner2.z - ray.Origin.z) * b
        }
        else{
            tzMax = (corner1.z - ray.Origin.z) * b
            tzMin = (corner2.z - ray.Origin.z) * b
        }

        var t0: Float
        var t1: Float

        if (txMin > tyMin)
            t0 = txMin
        else
            t0 = tyMin

        if (tzMin > t0)
            t0 = tzMin

        if (txMax < tyMax)
            t1 = txMax
        else
            t1 = tyMax

        if (tzMax < t1)
            t1 = tzMax

        return(t0 < t1 && t0 > ray.tmin && t1 < ray.tmax)

    }
    override fun rayIntersection(ray: Ray):HitRecord?{
        val txMin: Float
        val txMax: Float
        val tyMin: Float
        val tyMax: Float
        val tzMin: Float
        val tzMax: Float

        val a: Float = 1.0f / ray.Dir.x

        if (a >= 0.0f){
            txMin = (corner1.x - ray.Origin.x) * a
            txMax = (corner2.x - ray.Origin.x) * a
        }
        else{
            txMax = (corner1.x - ray.Origin.x) * a
            txMin = (corner2.x - ray.Origin.x) * a
        }

        val b: Float = 1.0f / ray.Dir.y

        if (b >= 0.0f){
            tyMin = (corner1.y - ray.Origin.y) * b
            tyMax = (corner2.y - ray.Origin.y) * b
        }
        else{
            tyMax = (corner1.y - ray.Origin.y) * b
            tyMin = (corner2.y - ray.Origin.y) * b
        }

        val c: Float = 1.0f / ray.Dir.z

        if (c >= 0.0f){
            tzMin = (corner1.z - ray.Origin.z) * b
            tzMax = (corner2.z - ray.Origin.z) * b
        }
        else{
            tzMax = (corner1.z - ray.Origin.z) * b
            tzMin = (corner2.z - ray.Origin.z) * b
        }

        var t0: Float
        var t1: Float

        var faceIn: Int
        var faceOut: Int

        if (txMin > tyMin){
            t0 = txMin
            faceIn = if (a>=0) 0 else 3
        }
        else{
            t0 = tyMin
            faceIn = if (b>=0) 1 else 4
        }
        if (tzMin > t0){
            t0 = tzMin
            faceIn = if (c>=0) 2 else 5
        }

        if (txMax < txMax){
            t1 = txMax
            faceOut = if (a>=0) 3 else 0
        }
        else{
            t1 = tyMax
            faceOut = if (b>=0) 4 else 1
        }
        if (tzMax < t1){
            t1 = tzMax
            faceOut = if (c>=0) 5 else 0
        }
        val t: Float
        val normal: Normal
        val record: HitRecord?
        val face: Int
        if (t0 < t1 && t1 < ray.tmax){
            if (t0 > ray.tmin){
                t = t0
                normal = getNormal(faceIn)
                face = faceIn
            }
            else{
                t = t1
                normal = getNormal(faceOut)
                face = faceOut
            }

            val hitPoint: Point = ray.At(t)
            record = HitRecord(
                worldPoint = this.transformation * hitPoint,
                normal = normal,
                surfacePoint = getUv(face, hitPoint),
                t = t,
                ray = ray
            )
        }
        else{
            record = null
        }
        return record
    }

}