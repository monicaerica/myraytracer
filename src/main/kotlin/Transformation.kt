import java.lang.Math.abs
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class Transformation(
    var m: Array<FloatArray> = ID4,
    var invm: Array<FloatArray> = ID4
) {

    override fun toString(): String {
        var row = "|${m[0][0]}, ${m[0][1]}, ${m[0][2]}, ${m[0][3]}|\n"
        for (matEl in m.slice(1 until m.size)){
            row += "\t|${matEl[0]} ${matEl[1]} ${matEl[2]} ${matEl[3]}|\n"
        }
        return "T $row"
    }

    fun areMatrixClose(A: Array<FloatArray>, B: Array<FloatArray>, epsilon: Float = 0.001f): Boolean{
        for (i in 0 until 4){
            for (j in 0 until 4){
                if (abs(A[i][j] - B[i][j])>epsilon){return false}
            }
        }
        return true
    }

    fun MatrixProduct(A: Array<FloatArray>, B: Array<FloatArray>): Array<FloatArray>{
        var C = Array(A.size) {FloatArray(B[0].size) {0.0f}}

        for (j in C.indices) {
            for (i in 0 until C[0].size) {
                for (k in 0 until A[0].size) {
                    C[i][j] += A[i][k] * B[k][j]
                }
            }
        }
        return C
    }

    operator fun times(other: Transformation): Transformation{
        return Transformation(MatrixProduct(m, other.m), MatrixProduct(other.invm, invm))
    }

    operator fun times(other: Vec): Vec{
        val r0 = m[0]
        val r1 = m[1]
        val r2 = m[2]
        return Vec(
            r0[0] * other.x + r0[1] * other.y + r0[2] * other.z,
            r1[0] * other.x + r1[1] * other.y + r1[2] * other.z,
            r2[0] * other.x + r2[1] * other.y + r2[2] * other.z
        )
    }
    
    operator fun times(other: Point): Point{
        val r0 = this.m[0]
        val r1 = this.m[1]
        val r2 = this.m[2]
        val r3 = this.m[3]
        val p = Point(
            r0[0] * other.x + r0[1] * other.y + r0[2] * other.z + r0[3],
            r1[0] * other.x + r1[1] * other.y + r1[2] * other.z + r1[3],
            r2[0] * other.x + r2[1] * other.y + r2[2] * other.z + r2[3]
        )
        val lambda = other.x * r3[0] + other.y * r3[1] + other.z * r3[2] + r3[3]
        return if (lambda == 1.0F) {
            p
        } else {
            Point(p.x / lambda, p.y / lambda, p.z / lambda)
        }
    }


    operator fun times(other: Normal): Normal{
        val r0 = m[0]
        val r1 = m[1]
        val r2 = m[2]
        return Normal(
            r0[0] * other.x + r0[1] * other.y + r0[2] * other.z,
            r1[0] * other.x + r1[1] * other.y + r1[2] * other.z,
            r2[0] * other.x + r2[1] * other.y + r2[2] * other.z
        )
    }

    operator fun times(other: Ray): Ray{
        return Ray( this * other.Origin,
                    this * other.Dir,
                    other.tmin,
                    other.tmax,
                    other.Depth)
    }

    // operator fun times(other: HitRecord): HitRecord {
    //     return HitRecord(
    //         this * other.worldPoint,
    //         this * other.normal,
    //         this * other.surfacePoint,
    //         other.t,
    //         this * other.ray,
    //         other.shape
    //     )
    // }

    fun Inverse(): Transformation {
        return Transformation(invm, m)
    }

    fun Translation(vec: Vec): Transformation {
        val m = arrayOf(
            floatArrayOf(1.0f, 0.0f, 0.0f, vec.x),
            floatArrayOf(0.0f, 1.0f, 0.0f, vec.y),
            floatArrayOf(0.0f, 0.0f, 1.0f, vec.z),
            floatArrayOf(0.0f, 0.0f, 0.0f, 1.0f)
        )
        val invm = arrayOf(
            floatArrayOf(1.0f, 0.0f, 0.0f, -vec.x),
            floatArrayOf(0.0f, 1.0f, 0.0f, -vec.y),
            floatArrayOf(0.0f, 0.0f, 1.0f, -vec.z),
            floatArrayOf(0.0f, 0.0f, 0.0f, 1.0f)
        )
        return Transformation(m, invm)
    }

    fun Scaling(vec: Vec): Transformation {
        val m = arrayOf(
            floatArrayOf(vec.x, 0.0f, 0.0f, 0.0f),
            floatArrayOf(0.0f, vec.y, 0.0f, 0.0f),
            floatArrayOf(0.0f, 0.0f, vec.z, 0.0f),
            floatArrayOf(0.0f, 0.0f, 0.0f, 1.0f)
        )
        val invm = arrayOf(
            floatArrayOf(1.0f / vec.x, 0.0f, 0.0f, 0.0f),
            floatArrayOf(0.0f, 1.0f /vec.y, 0.0f, 0.0f),
            floatArrayOf(0.0f, 0.0f, 1.0f /vec.z, 0.0f),
            floatArrayOf(0.0f, 0.0f, 0.0f, 1.0f)
        )
        return Transformation(m, invm)
    }

    fun RotationX(theta: Float): Transformation{
        var thetaRad = theta * (PI.toFloat() / 180.0f)
        val cosTh = cos(thetaRad)
        val sinTh = sin(thetaRad)
        val m = arrayOf(
            floatArrayOf(1.0f, 0.0f, 0.0f, 0.0f),
            floatArrayOf(0.0f, cosTh, -sinTh, 0.0f),
            floatArrayOf(0.0f,sinTh, cosTh, 0.0f),
            floatArrayOf(0.0f, 0.0f, 0.0f, 1.0f)
        )
        val invm = arrayOf(
            floatArrayOf(1.0f, 0.0f, 0.0f, 0.0f),
            floatArrayOf(0.0f, cosTh, sinTh, 0.0f),
            floatArrayOf(0.0f,-sinTh, cosTh, 0.0f),
            floatArrayOf(0.0f, 0.0f, 0.0f, 1.0f)
        )
        return Transformation(m, invm)
    }

    fun RotationY(theta: Float): Transformation{
        var thetaRad = theta * (PI.toFloat() / 180.0f)
        val cosTh = cos(thetaRad)
        val sinTh = sin(thetaRad)
        val m = arrayOf(
            floatArrayOf(cosTh, 0.0f, sinTh, 0.0f),
            floatArrayOf(0.0f, 1.0f, 0.0f, 0.0f),
            floatArrayOf(-sinTh,0.0f, cosTh, 0.0f),
            floatArrayOf(0.0f, 0.0f, 0.0f, 1.0f)
        )
        val invm = arrayOf(
            floatArrayOf(cosTh, 0.0f, -sinTh, 0.0f),
            floatArrayOf(0.0f, 1.0f, 0.0f, 0.0f),
            floatArrayOf(sinTh,0.0f, cosTh, 0.0f),
            floatArrayOf(0.0f, 0.0f, 0.0f, 1.0f)
        )
        return Transformation(m, invm)
    }

    fun RotationZ(theta: Float): Transformation{
        var thetaRad = theta * (PI.toFloat() / 180.0f)
        val cosTh = cos(thetaRad)
        val sinTh = sin(thetaRad)
        val m = arrayOf(
            floatArrayOf(cosTh, -sinTh, 0.0f, 0.0f),
            floatArrayOf(sinTh, cosTh , 0.0f , 0.0f),
            floatArrayOf(0.0f,0.0f, 1.0f, 0.0f),
            floatArrayOf(0.0f, 0.0f, 0.0f, 1.0f)
        )
        val invm = arrayOf(
            floatArrayOf(cosTh, sinTh, 0.0f, 0.0f),
            floatArrayOf(-sinTh, cosTh , 0.0f , 0.0f),
            floatArrayOf(0.0f,0.0f, 1.0f, 0.0f),
            floatArrayOf(0.0f, 0.0f, 0.0f, 1.0f)
        )
        return Transformation(m, invm)
    }

    fun isClose(other: Transformation, epsilon: Float = 1e-5F): Boolean {
        return areMatrixClose(m, other.m, epsilon) && areMatrixClose(invm, other.invm, epsilon)
    }

    fun isConsistent(): Boolean {
        return areMatrixClose(ID4, MatrixProduct(m, invm))
    }
}

//     /**
//      * Tells if two Transformations are close together
//      * @param other: the other Transformation
//      * @return Boolean value
//      */
//     fun IsClose(other: Transformation): Boolean{
//         return ((this.M.IsClose(other.M)) && (this.invm.IsClose(other.invm)))
//     }

//     /**
//      * Tells if the inverse matrix is indeed the inverse of the matrix describing the Transformation
//      * @return Boolean value
//      */
//     fun IsConsistent(): Boolean{
//         val identity: HomMatrix = HomMatrix()
//         return identity.IsClose(MatrixProduct(this.M, this.invm))
//     }

//     /**
//      * Returns the product of two Transformations
//      * @param other: the other Transformation
//      * @return variable of type Transformation
//      */
//     operator fun times(other: Transformation): Transformation{
//         return Transformation(MatrixProduct(this.M, other.M), MatrixProduct(this.invm, other.invm))
//     }

//     /**
//      * Transforms a Vec
//      * @param other: The vector (Vec) we want to apply the Transformation to
//      * @return The vector transformed
//      */
//     operator fun times(other: Vec): Vec {
//         val row0 = M.elements.slice(0..3)
//         val row1 = M.elements.slice(4..7)
//         val row2 = M.elements.slice(8..11)
//         val row3 = M.elements.slice(12..15)

//         return Vec(
//             row0[0] * other.x + row0[1] * other.y + row0[2] * other.z,
//             row1[0] * other.x + row1[1] * other.y + row1[2] * other.z,
//             row2[0] * other.x + row2[1] * other.y + row2[2] * other.z,
//         )
//     }

//     /**
//      * Transforms a Normal
//      * @param other: The Normal we want to apply the transformation to
//      * @return The Normal transformed
//      */
//     operator fun times(other: Normal): Normal {
//         val row0 = invm.elements.slice(0..3)
//         val row1 = invm.elements.slice(4..7)
//         val row2 = invm.elements.slice(8..11)
//         val row3 = invm.elements.slice(12..15)

//         return Normal(
//             row0[0] * other.x + row1[0] * other.y + row2[0] * other.z,
//             row0[1] * other.x + row1[1] * other.y + row2[1] * other.z,
//             row0[2] * other.x + row1[2] * other.y + row2[2] * other.z,
//         )
//     }

//     /**
//      * Transforms a Point
//      * @param other: The Point we want to apply the transformation to
//      * @return The Point transformed
//      */
//     operator fun times(other: Point): Point {
//         val row0 = M.elements.slice(0..3)
//         val row1 = M.elements.slice(4..7)
//         val row2 = M.elements.slice(8..11)
//         val row3 = M.elements.slice(12..15)

//         var w = row3[0] * other.x + row3[1] * other.y + row3[2] * other.z + row3[3]
//         var p : Point = Point(
//             row0[0] * other.x + row0[1] * other.y + row0[2] * other.z + row0[3],
//             row1[0] * other.x + row1[1] * other.y + row1[2] * other.z + row1[3],
//             row2[0] * other.x + row2[1] * other.y + row2[2] * other.z + row2[3],
//             )
//         if (w == 1.0f) return p
//         else {
//             p.x = p.x / w
//             p.y = p.y / w
//             p.z = p.z / w
//             return p
//         }
//     }

//     /**
//      * Exchange inverse matrix and matrix
//      */
//     fun Inverse(): Transformation {
//         return Transformation(invm, M)
//     }
// }

// /**
//  * Scales by a vector
//  * @param vec: The vector we want uor object to be scaled by
//  */
// fun Scaling(vec: Vec): Transformation {
//     val M: HomMatrix = HomMatrix(
//         floatArrayOf(
//             vec.x, 0.0f, 0.0f, 0.0f,
//             0.0f, vec.y, 0.0f, 0.0f,
//             0.0f, 0.0f, vec.z, 0.0f,
//             0.0f, 0.0f, 0.0f, 1.0f
//         )
//     )
//     val invm: HomMatrix = HomMatrix(
//         floatArrayOf(
//             1.0f / vec.x, 0.0f, 0.0f, 0.0f,
//             0.0f, 1.0f / vec.y, 0.0f, 0.0f,
//             0.0f, 0.0f, 1.0f / vec.z, 0.0f,
//             0.0f, 0.0f, 0.0f, 1.0f
//         )
//     )
//     return Transformation(M, invm)
// }

// /**
//  * Builds a variable of type Transformation describing the translation by a vector
//  * @param vec: the vector we want our object to be translated by
//  * @return variable of type Transformation
//  */
// fun Translation(vec: Vec): Transformation {
//     val M: HomMatrix = HomMatrix(
//         floatArrayOf(
//             1.0f, 0.0f, 0.0f, vec.x,
//             0.0f, 1.0f, 0.0f, vec.y,
//             0.0f, 0.0f, 1.0f, vec.z,
//             0.0f, 0.0f, 0.0f, 1.0f
//         )
//     )
//     val invm: HomMatrix = HomMatrix(
//         floatArrayOf(
//             1.0f, 0.0f, 0.0f, -vec.x,
//             0.0f, 1.0f, 0.0f, -vec.y,
//             0.0f, 0.0f, 1.0f, -vec.z,
//             0.0f, 0.0f, 0.0f, 1.0f
//         )
//     )
//     return Transformation(M, invm)
// }

// /**
//  * Rotate by an angle around the x axis
//  * @param  ang: The value of the angle in degrees
//  */
// fun RotationX(ang: Float): Transformation {
//     var RadAng = ang * (PI.toFloat() / 180.0f)
//     val COSang = cos(RadAng)
//     val SINang = sin(RadAng)

//     val M: HomMatrix = HomMatrix(
//         floatArrayOf(
//             1.0f, 0.0f, 0.0f, 0.0f,
//             0.0f, COSang, -SINang, 0.0f,
//             0.0f, SINang, COSang, 0.0f,
//             0.0f, 0.0f, 0.0f, 1.0f
//         )
//     )
//     val invm: HomMatrix = HomMatrix(
//         floatArrayOf(
//             1.0f, 0.0f, 0.0f, 0.0f,
//             0.0f, COSang, SINang, 0.0f,
//             0.0f, -SINang, COSang, 0.0f,
//             0.0f, 0.0f, 0.0f, 1.0f
//         )
//     )
//     return Transformation(M, invm)
// }

// /**
//  * Rotate by an angle around the y axis
//  * @param  ang: The value of the angle in degrees
//  */
// fun RotationY(ang: Float): Transformation {
//     var RadAng = ang * (PI.toFloat() / 180.0f)
//     val COSang = cos(RadAng)
//     val SINang = sin(RadAng)

//     val M: HomMatrix = HomMatrix(
//         floatArrayOf(
//             COSang, 0.0f, SINang, 0.0f,
//             0.0f, 1.0f, 0.0f, 0.0f,
//             -SINang, 0.0f, COSang, 0.0f,
//             0.0f, 0.0f, 0.0f, 1.0f
//         )
//     )
//     val invm: HomMatrix = HomMatrix(
//         floatArrayOf(
//             COSang, 0.0f, -SINang, 0.0f,
//             0.0f, 1.0f, 0.0f, 0.0f,
//             SINang, 0.0f, COSang, 0.0f,
//             0.0f, 0.0f, 0.0f, 1.0f
//         )
//     )
//     return Transformation(M, invm)
// }

// /**
//  * Rotate by an angle around the z axis
//  * @param  ang: The value of the angle in degrees
//  */
// fun RotationZ(ang: Float): Transformation {
//     var RadAng = ang * (PI.toFloat() / 180.0f)
//     val COSang = cos(RadAng)
//     val SINang = sin(RadAng)

//     val M: HomMatrix = HomMatrix(
//         floatArrayOf(
//             COSang, -SINang, 0.0f, 0.0f,
//             SINang, COSang, 0.0f, 0.0f,
//             0.0f, 0.0f, 1.0f, 0.0f,
//             0.0f, 0.0f, 0.0f, 1.0f
//         )
//     )
//     val invm: HomMatrix = HomMatrix(
//         floatArrayOf(
//             COSang, SINang, 0.0f, 0.0f,
//             -SINang, COSang, 0.0f, 0.0f,
//             0.0f, 0.0f, 1.0f, 0.0f,
//             0.0f, 0.0f, 0.0f, 1.0f
//         )
//     )
//     return Transformation(M, invm)
// }