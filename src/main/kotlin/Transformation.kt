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


}