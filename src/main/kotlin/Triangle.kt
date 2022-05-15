class matrixThreebyThree(var elements: FloatArray) {
    init {
        require(elements.size == 9) { "Matrix must be 3x3" }
    }

    constructor() : this(
        floatArrayOf(
            1.0f, 0.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 0.0f, 1.0f
        )
    )

    fun getElementAt(index: Int): Float {
        return this.elements.elementAt(index)
    }

    fun setElementAt(index: Int, value: Float) {
        this.elements[index] = value
    }

    fun determinant(): Float {
        val det1: Float =
            this.getElementAt(0) * (this.getElementAt(4) * this.getElementAt(8) - this.getElementAt(5) * this.getElementAt(
                7
            ))

        val det2: Float =
            this.getElementAt(1) * (this.getElementAt(3) * this.getElementAt(8) - this.getElementAt(5) * this.getElementAt(
                6
            ))

        val det3: Float =
            this.getElementAt(2) * (this.getElementAt(3) * this.getElementAt(7) - this.getElementAt(4) * this.getElementAt(
                6
            ))

        return det1 - det2 + det3
    }
}

class Triangle(val A: Point, val B: Point, val C: Point, transformation: Transformation = Transformation(), material: Material = Material()) : Shape(transformation, material) {
    private val vecAB: Vec = B - A
    private val vecAC: Vec = C - A

    var beta: Float = 0.0f
    var gamma: Float = 0.0f
    override fun rayIntersection(ray: Ray): HitRecord? {
        var t: Float = 0.0f
        val M: matrixThreebyThree = matrixThreebyThree(
            floatArrayOf(
                vecAB.x, vecAC.x, ray.Dir.x,
                vecAB.y, vecAC.y, ray.Dir.y,
                vecAB.z, vecAC.z, ray.Dir.z

            )
        )

        val detM = M.determinant()

        //Vector connecting the origin of the ray and the point A
        val vecOA: Vec = ray.Origin - this.A

        //Solution according to Cramer's formula

        val Mx: matrixThreebyThree = matrixThreebyThree(
            floatArrayOf(
                vecOA.x, vecAC.x, ray.Dir.x,
                vecOA.y, vecAC.y, ray.Dir.y,
                vecOA.z, vecAC.z, ray.Dir.z

            )
        )

        val My: matrixThreebyThree = matrixThreebyThree(
            floatArrayOf(
                vecAB.x, vecOA.x, ray.Dir.x,
                vecAB.y, vecOA.y, ray.Dir.y,
                vecAB.z, vecOA.z, ray.Dir.z

            )
        )

        val Mz: matrixThreebyThree = matrixThreebyThree(
            floatArrayOf(
                vecAB.x, vecAC.x, vecOA.x,
                vecAB.y, vecAC.y, vecOA.y,
                vecAB.z, vecAC.z, vecOA.z

            )
        )
        if (detM != 0.0f) {
            beta = Mx.determinant() / detM
            gamma = My.determinant() / detM
            t = -Mz.determinant() / detM
        }
        if (detM == 0.0f || t < ray.tmin || t > ray.tmax || beta < 0 || beta > 1 || gamma < 0 || gamma > 1) {
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
            material = this.material
        )
    }
}