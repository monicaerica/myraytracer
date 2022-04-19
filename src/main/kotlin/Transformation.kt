import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class Transformation(
    var M: HomMatrix = HomMatrix(),
    var InvM: HomMatrix = HomMatrix()
)
{

    fun Inverse(): Transformation{
        return Transformation(InvM, M)
    }

    fun Scaling(vec: Vec): Transformation{
        val M: HomMatrix = HomMatrix(floatArrayOf(vec.x, 0.0f, 0.0f, 0.0f,
            0.0f, vec.y, 0.0f, 0.0f,
            0.0f, 0.0f, vec.z, 0.0f,
            0.0f, 0.0f, 0.0f, 1.0f))
        val InvM: HomMatrix = HomMatrix(floatArrayOf(1.0f / vec.x, 0.0f, 0.0f, 0.0f,
            0.0f, 1.0f / vec.y, 0.0f, 0.0f,
            0.0f, 0.0f, 1.0f / vec.z, 0.0f,
            0.0f, 0.0f, 0.0f, 1.0f))
        return Transformation(M, InvM)
    }

    fun Translation(vec: Vec): Transformation{
        val M: HomMatrix = HomMatrix(floatArrayOf(0.0f, 0.0f, 0.0f, vec.x,
                                                0.0f, 0.0f, 0.0f, vec.y,
                                                0.0f, 0.0f, 0.0f, vec.z,
                                                0.0f, 0.0f, 0.0f, 1.0f))
        val InvM: HomMatrix = HomMatrix(floatArrayOf((0.0f, 0.0f, 0.0f, -vec.x,
                                                    0.0f, 0.0f, 0.0f, -vec.y,
                                                    0.0f, 0.0f, 0.0f, -vec.z,
                                                    0.0f, 0.0f, 0.0f, 1.0f))
        return Transformation(M, InvM)
    }

    /**
     * Rotate by an angle around the x axis
     * @param  ang: The value of the angle in degrees
     */
    fun RotationX(ang: Float): Transformation{
        var RadAng = ang * (PI.toFloat() / 180.0f)
        val COSang = cos(RadAng)
        val SINang = sin(RadAng)

        val M: HomMatrix = HomMatrix(floatArrayOf(1.0f, 0.0f, 0.0f, 0.0f,
                                                0.0f, COSang, -SINang, 0.0f,
                                                0.0f, SINang, COSang, 0.0f,
                                                0.0f, 0.0f, 0.0f, 1.0f))
        val InvM: HomMatrix = HomMatrix(floatArrayOf(1.0f, 0.0f, 0.0f, 0.0f,
                                                0.0f, COSang, SINang, 0.0f,
                                                0.0f, -SINang, COSang, 0.0f,
                                                0.0f, 0.0f, 0.0f, 1.0f))
        return Transformation(M, InvM)
    }

    /**
     * Rotate by an angle around the y axis
     * @param  ang: The value of the angle in degrees
     */
    fun RotationY(ang: Float): Transformation{
        var RadAng = ang * (PI.toFloat() / 180.0f)
        val COSang = cos(RadAng)
        val SINang = sin(RadAng)

        val M: HomMatrix = HomMatrix(floatArrayOf(COSang, 0.0f, SINang, 0.0f,
                                                0.0f, 1.0f, 0.0f, 0.0f,
                                                -SINang, 0.0f, COSang, 0.0f,
                                                0.0f, 0.0f, 0.0f, 1.0f))
        val InvM: HomMatrix = HomMatrix(floatArrayOf((COSang, 0.0f, -SINang, 0.0f,
                                                0.0f, 1.0f, 0.0f, 0.0f,
                                                SINang, 0.0f, COSang, 0.0f,
                                                0.0f, 0.0f, 0.0f, 1.0f))
        return Transformation(M, InvM)
    }
    /**
     * Rotate by an angle around the z axis
     * @param  ang: The value of the angle in degrees
     */
    fun RotationZ(ang: Float): Transformation{
        var RadAng = ang * (PI.toFloat() / 180.0f)
        val COSang = cos(RadAng)
        val SINang = sin(RadAng)

        val M: HomMatrix = HomMatrix(floatArrayOf(COSang, -SINang, 0.0f, 0.0f,
                                                    SINang, COSang, 0.0f, 0.0f,
                                                    0.0f, 0.0f, 1.0f, 0.0f,
                                                    0.0f, 0.0f, 0.0f, 1.0f))
        val InvM: HomMatrix = HomMatrix(floatArrayOf(COSang, SINang, 0.0f, 0.0f,
                                                    -SINang, COSang, 0.0f, 0.0f,
                                                    0.0f, 0.0f, 1.0f, 0.0f,
                                                    0.0f, 0.0f, 0.0f, 1.0f)))
        return Transformation(M, InvM)
    }



}