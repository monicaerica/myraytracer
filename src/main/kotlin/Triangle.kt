class matrix(
    var a: Array<FloatArray> = ID3,
){
    fun calculateDeterminant(): Float {
        val d1: Float = a[0][0] * (a[1][1] * a[2][2] - a[1][2] * a[2][1])
        val d2: Float = a[0][1] * (a[1][0] * a[2][2] - a[1][2] * a[2][0])
        val d3: Float = a[0][2] * (a[1][0] * a[2][1] - a[1][1] * a[2][0])

        return d1 - d2 + d3
    }
    
    fun getElementAt(i1: Int, i2: Int): Float {
        return a[i1][i2]
    }

    fun setElementAt(i1: Int, i2: Int, value: Float) {
        a[i1][i2] = value
    }

}

class Triangle(val A: Point, val B: Point, val C: Point, transformation: Transformation = Transformation(), material: Material = Material()) : Shape(transformation, material) {
    private val vecAB: Vec = B - A
    private val vecAC: Vec = C - A
    var alpha: Float = 0.0f
    var beta: Float = 0.0f
    var gamma: Float = 0.0f
    override fun rayIntersection(ray: Ray): HitRecord? {
        var t: Float = 0.0f



        val M: matrix = matrix(
            arrayOf(
                floatArrayOf(vecAB.x, vecAC.x, ray.Dir.x),
                floatArrayOf(vecAB.y, vecAC.y, ray.Dir.y),
                floatArrayOf(vecAB.z, vecAC.z, ray.Dir.z)

            )
        )

        val detM = M.calculateDeterminant()

        //Vector connecting the origin of the ray and the point A
        val vecOA: Vec = ray.Origin - this.A

        //Solution according to Cramer's formula

        val Mx: matrix = matrix(
            arrayOf(
                floatArrayOf(vecOA.x, vecAC.x, ray.Dir.x),
                floatArrayOf(vecOA.y, vecAC.y, ray.Dir.y),
                floatArrayOf(vecOA.z, vecAC.z, ray.Dir.z)

            )
        )

        val My: matrix = matrix(
            arrayOf(
                floatArrayOf(vecAB.x, vecOA.x, ray.Dir.x),
                floatArrayOf(vecAB.y, vecOA.y, ray.Dir.y),
                floatArrayOf(vecAB.z, vecOA.z, ray.Dir.z)

            )
        )

        val Mz: matrix = matrix(
            arrayOf(
                floatArrayOf(vecAB.x, vecAC.x, vecOA.x),
                floatArrayOf(vecAB.y, vecAC.y, vecOA.y),
                floatArrayOf(vecAB.z, vecAC.z, vecOA.z)

            )
        )
        if (detM != 0.0f) {
            beta = Mx.calculateDeterminant() / detM
            gamma = My.calculateDeterminant() / detM
            t = -Mz.calculateDeterminant() / detM
        }
        if (detM == 0.0f || t < ray.tmin || t > ray.tmax || beta < 0 || beta > 1 || gamma < 0 || gamma > 1 || beta + gamma > 1) {
            return null
        }

        val pointOnTriangle: Point = A + vecAB * beta + vecAC * gamma
        val pointUV: Vec2d = Vec2d(beta, gamma)
        val hitPoint: Point = ray.At(t)
        val normal: Normal = vecAB.Cross(vecAC).toNormal()
        return HitRecord(
            worldPoint = hitPoint,
            normal = normal,
            surfacePoint = pointUV,
            t = t,
            ray = ray,
            shape = this
        )
    }
}