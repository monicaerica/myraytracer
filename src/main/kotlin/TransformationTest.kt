import org.junit.Assert.*
import org.junit.Test

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
}