import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

internal class TransformationTest {

    @Test
    fun IsConsistentTest(){
        val testM: HomMatrix = HomMatrix(
            floatArrayOf(
                1.0f, 2.0f, 3.0f, 4.0f,
                5.0f, 6.0f, 7.0f, 8.0f,
                9.0f, 9.0f, 8.0f, 7.0f,
                6.0f, 5.0f, 4.0f, 1.0f
            )
        )

        val testInvM: HomMatrix = HomMatrix(
            floatArrayOf(
                -3.75f, 2.75f, -1.0f, 0.0f,
                4.375f, -3.875f, 2.0f, -0.5f,
                0.5f, 0.5f, -1.0f, 1.0f,
                -1.375f, 0.875f, 0.0f, -0.5f
            )
        )
        val identity: HomMatrix = HomMatrix()

        var product: HomMatrix = HomMatrix()
        val tr: Transformation = Transformation(testM, testInvM)
        assert(tr.IsConsistent())

    }

    @Test
    fun TransMult(){
        val m1: Transformation = Transformation(
            M = HomMatrix(
                floatArrayOf(
                    1.0f, 2.0f, 3.0f, 4.0f,
                    5.0f, 6.0f, 7.0f, 8.0f,
                    9.0f, 9.0f, 8.0f, 7.0f,
                    6.0f, 5.0f, 4.0f, 1.0f
                )
            ),
            InvM = HomMatrix(
                floatArrayOf(
                    -3.75f, 2.75f, -1.0f, 0.0f,
                    4.375f, -3.875f, 2.0f, -0.5f,
                    0.5f, 0.5f, -1.0f, 1.0f,
                    -1.375f, 0.875f, 0.0f, -0.5f
                )
            )
        )

        val m2: Transformation = Transformation(
            M = HomMatrix(
                floatArrayOf(
                    3.0f, 5.0f, 2.0f, 4.0f,
                    4.0f, 1.0f, 0.0f, 5.0f,
                    6.0f, 3.0f, 2.0f, 0.0f,
                    1.0f, 4.0f, 2.0f, 1.0f,
                )
            ),
            InvM = HomMatrix(
                floatArrayOf(
                    0.4f, -0.2f, 0.2f, -0.6f,
                    2.9f, -1.7f, 0.2f, -3.1f,
                    -5.55f, 3.15f, -0.4f, 6.45f,
                    -0.9f, 0.7f, -0.2f, 1.1f
                )
            )
        )

        val expected = Transformation(
            M = HomMatrix(
                floatArrayOf(
                    33.0f, 32.0f, 16.0f, 18.0f,
                    89.0f, 84.0f, 40.0f, 58.0f,
                    118.0f, 106.0f, 48.0f, 88.0f,
                    63.0f, 51.0f, 22.0f, 50.0f,
                )
            ),
            InvM = HomMatrix(
                floatArrayOf(
                    -1.45f, 1.45f, -1.0f, 0.6f,
                    -13.95f, 11.95f, -6.5f, 2.6f,
                    25.525f, -22.025f, 12.25f, -5.2f,
                    4.825f, -4.325f, 2.5f, -1.1f,
                )
            )
        )
        assertTrue(expected.M.IsClose((m1*m2).M))
    }

    @Test
    fun TransfornationClassic() {

        val vec: Vec = Vec(1.0f, 2.0f, 3.0f)
        var tr1: Transformation = Translation(vec)
//        assert(tr1.IsConsistent())

//        var tr2: Transformation = Translation(Vec(4.0f, 6.0f, 8.0f))
//        assert(tr2.IsConsistent())

//        var prod = tr1 * tr2
//        assert(prod.IsConsistent())

//        var expected : Transformation = Translation(Vec(5.0f, 8.0f, 11.0f))
//        assert(prod.IsClose(expected))

        var tr3: Transformation = RotationX(0.1f)
        assert(tr3.IsConsistent())
        var tr4: Transformation = RotationY(0.1f)
        assert(tr4.IsConsistent())
        var tr5: Transformation = RotationZ(0.1f)
        assert(tr5.IsConsistent())

        val VEC_X : Vec = Vec(1.0f, 0.0f, 0.0f)
        val VEC_Y : Vec = Vec(0.0f, 1.0f, 0.0f)
        val VEC_Z : Vec = Vec(0.0f, 0.0f, 1.0f)

        tr3 = RotationX(90f)
        var vec2 = tr3 * VEC_Y
        assert(vec2.IsClose(VEC_Z))
        tr3 = RotationY(90f)
        vec2 = tr3 * VEC_Z
        assert(vec2.IsClose(VEC_X))
        tr3 = RotationZ(90f)
        vec2 = tr3 * VEC_X
        assert(vec2.IsClose(VEC_Y))

        tr3 = Scaling(Vec(2.0f, 5.0f, 10.0f))
        assert(tr3.IsConsistent())
        tr4 = Scaling(Vec(3.0f, 2.0f, 4.0f))
        assert(tr4.IsConsistent())

        var expected_tr : Transformation = Scaling(Vec(6.0f, 10.0f, 40.0f))
        assert(expected_tr.IsClose(tr3*tr4))

    }
}