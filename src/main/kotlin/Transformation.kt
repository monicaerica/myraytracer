import java.lang.Math.abs
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class Transformation(
    var M: HomMatrix = HomMatrix(),
    var InvM: HomMatrix = HomMatrix()
) {

//    fun IsClose(a: HomMatrix, b: HomMatrix, epsilon: Float = 1e-5f): Boolean {
//        for (i in 0 until 4) {
//            for (j in 0 until 4) {
//                if (abs(a.GetIndex(i, j) - b.GetIndex(i, j)) > epsilon)
//                    return false
//            }
//        }
//        return true
//    }

    fun IsClose(other: Transformation): Boolean{
        return ((this.M.IsClose(other.M)) && (this.InvM.IsClose(other.InvM)))
    }

    fun IsConsistent(): Boolean{
        val identity: HomMatrix = HomMatrix()
        return identity.IsClose(MatrixProduct(this.M, this.InvM))
    }

    operator fun times(other: Transformation): Transformation{
        return Transformation(MatrixProduct(M, other.M), MatrixProduct(InvM, other.InvM))
    }

    operator fun times(other: Vec): Vec {
        val row0 = M.elements.slice(0..3)
        val row1 = M.elements.slice(4..7)
        val row2 = M.elements.slice(8..11)
        val row3 = M.elements.slice(12..15)

        return Vec(
            row0[0] * other.x + row0[1] * other.y + row0[2] * other.z,
            row1[0] * other.x + row1[1] * other.y + row1[2] * other.z,
            row2[0] * other.x + row2[1] * other.y + row2[2] * other.z,
        )
    }

    operator fun times(other: Normal): Normal {
        val row0 = InvM.elements.slice(0..3)
        val row1 = InvM.elements.slice(4..7)
        val row2 = InvM.elements.slice(8..11)
        val row3 = InvM.elements.slice(12..15)

        return Normal(
            row0[0] * other.x + row1[0] * other.y + row2[0] * other.z,
            row0[1] * other.x + row1[1] * other.y + row2[1] * other.z,
            row0[2] * other.x + row1[2] * other.y + row2[2] * other.z,
        )
    }

    operator fun times(other: Point): Point {
        val row0 = M.elements.slice(0..3)
        val row1 = M.elements.slice(4..7)
        val row2 = M.elements.slice(8..11)
        val row3 = M.elements.slice(12..15)

        var w = row3[0] * other.x + row3[1] * other.y + row3[2] * other.z + row3[3]
        var p : Point = Point(
            row0[0] * other.x + row0[1] * other.y + row0[2] * other.z + row0[3],
            row1[0] * other.x + row1[1] * other.y + row1[2] * other.z + row1[3],
            row2[0] * other.x + row2[1] * other.y + row2[2] * other.z + row2[3],
            )
        if (w == 1.0f) return p
        else {
            p.x = p.x / w
            p.y = p.y / w
            p.z = p.z / w
            return p
        }
    }

    /**
     * Exchange inverse matrix and matrix
     */
    fun Inverse(): Transformation {
        return Transformation(InvM, M)
    }
}

/**
 *
 */
fun Scaling(vec: Vec): Transformation {
    val M: HomMatrix = HomMatrix(
        floatArrayOf(
            vec.x, 0.0f, 0.0f, 0.0f,
            0.0f, vec.y, 0.0f, 0.0f,
            0.0f, 0.0f, vec.z, 0.0f,
            0.0f, 0.0f, 0.0f, 1.0f
        )
    )
    val InvM: HomMatrix = HomMatrix(
        floatArrayOf(
            1.0f / vec.x, 0.0f, 0.0f, 0.0f,
            0.0f, 1.0f / vec.y, 0.0f, 0.0f,
            0.0f, 0.0f, 1.0f / vec.z, 0.0f,
            0.0f, 0.0f, 0.0f, 1.0f
        )
    )
    return Transformation(M, InvM)
}

fun Translation(vec: Vec): Transformation {
    val M: HomMatrix = HomMatrix(
        floatArrayOf(
            0.0f, 0.0f, 0.0f, vec.x,
            0.0f, 0.0f, 0.0f, vec.y,
            0.0f, 0.0f, 0.0f, vec.z,
            0.0f, 0.0f, 0.0f, 1.0f
        )
    )
    val InvM: HomMatrix = HomMatrix(
        floatArrayOf(
            0.0f, 0.0f, 0.0f, -vec.x,
            0.0f, 0.0f, 0.0f, -vec.y,
            0.0f, 0.0f, 0.0f, -vec.z,
            0.0f, 0.0f, 0.0f, 1.0f
        )
    )
    return Transformation(M, InvM)
}

/**
 * Rotate by an angle around the x axis
 * @param  ang: The value of the angle in degrees
 */
fun RotationX(ang: Float): Transformation {
    var RadAng = ang * (PI.toFloat() / 180.0f)
    val COSang = cos(RadAng)
    val SINang = sin(RadAng)

    val M: HomMatrix = HomMatrix(
        floatArrayOf(
            1.0f, 0.0f, 0.0f, 0.0f,
            0.0f, COSang, -SINang, 0.0f,
            0.0f, SINang, COSang, 0.0f,
            0.0f, 0.0f, 0.0f, 1.0f
        )
    )
    val InvM: HomMatrix = HomMatrix(
        floatArrayOf(
            1.0f, 0.0f, 0.0f, 0.0f,
            0.0f, COSang, SINang, 0.0f,
            0.0f, -SINang, COSang, 0.0f,
            0.0f, 0.0f, 0.0f, 1.0f
        )
    )
    return Transformation(M, InvM)
}

/**
 * Rotate by an angle around the y axis
 * @param  ang: The value of the angle in degrees
 */
fun RotationY(ang: Float): Transformation {
    var RadAng = ang * (PI.toFloat() / 180.0f)
    val COSang = cos(RadAng)
    val SINang = sin(RadAng)

    val M: HomMatrix = HomMatrix(
        floatArrayOf(
            COSang, 0.0f, SINang, 0.0f,
            0.0f, 1.0f, 0.0f, 0.0f,
            -SINang, 0.0f, COSang, 0.0f,
            0.0f, 0.0f, 0.0f, 1.0f
        )
    )
    val InvM: HomMatrix = HomMatrix(
        floatArrayOf(
            COSang, 0.0f, -SINang, 0.0f,
            0.0f, 1.0f, 0.0f, 0.0f,
            SINang, 0.0f, COSang, 0.0f,
            0.0f, 0.0f, 0.0f, 1.0f
        )
    )
    return Transformation(M, InvM)
}

/**
 * Rotate by an angle around the z axis
 * @param  ang: The value of the angle in degrees
 */
fun RotationZ(ang: Float): Transformation {
    var RadAng = ang * (PI.toFloat() / 180.0f)
    val COSang = cos(RadAng)
    val SINang = sin(RadAng)

    val M: HomMatrix = HomMatrix(
        floatArrayOf(
            COSang, -SINang, 0.0f, 0.0f,
            SINang, COSang, 0.0f, 0.0f,
            0.0f, 0.0f, 1.0f, 0.0f,
            0.0f, 0.0f, 0.0f, 1.0f
        )
    )
    val InvM: HomMatrix = HomMatrix(
        floatArrayOf(
            COSang, SINang, 0.0f, 0.0f,
            -SINang, COSang, 0.0f, 0.0f,
            0.0f, 0.0f, 1.0f, 0.0f,
            0.0f, 0.0f, 0.0f, 1.0f
        )
    )
    return Transformation(M, InvM)
}