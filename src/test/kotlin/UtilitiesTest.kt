import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

internal class UtilitiesTest {
}

internal class transTest{
    @Test
    fun MatrixTest(){
        var M : Array<FloatArray> = ID4
        var invM : Array<FloatArray> = ID4

        assertTrue(Transformation().areMatrixClose(M, invM))
        assertTrue(Transformation().areMatrixClose(Transformation().MatrixProduct(invM, M), M))
        val testM:  Array<FloatArray> = arrayOf(
            
                floatArrayOf(1.0f, 2.0f, 3.0f, 4.0f),
                floatArrayOf(5.0f, 6.0f, 7.0f, 8.0f),
                floatArrayOf(9.0f, 9.0f, 8.0f, 7.0f),
                floatArrayOf(6.0f, 5.0f, 4.0f, 1.0f)
            )
        

        val testInvM:  Array<FloatArray> = arrayOf(
            
                floatArrayOf(-3.75f, 2.75f, -1.0f, 0.0f),
                floatArrayOf(4.375f, -3.875f, 2.0f, -0.5f),
                floatArrayOf(0.5f, 0.5f, -1.0f, 1.0f),
                floatArrayOf(-1.375f, 0.875f, 0.0f, -0.5f)
            )

        assertTrue(Transformation().areMatrixClose(Transformation().MatrixProduct(testM, testInvM), ID4))
    }
}


// internal class HomMatrixTest{

//     @Test
//     fun HomMatrixTest(){
//         val M: HomMatrix = HomMatrix()
//         var InvM: HomMatrix = HomMatrix()

//         assert(M.IsClose(InvM))
//         assert(MatrixProduct(InvM, M).IsClose(M))

//         val testM: HomMatrix = HomMatrix(
//             floatArrayOf(
//                 1.0f, 2.0f, 3.0f, 4.0f,
//                 5.0f, 6.0f, 7.0f, 8.0f,
//                 9.0f, 9.0f, 8.0f, 7.0f,
//                 6.0f, 5.0f, 4.0f, 1.0f
//             )
//         )

//         val testInvM: HomMatrix = HomMatrix(
//             floatArrayOf(
//                 -3.75f, 2.75f, -1.0f, 0.0f,
//                 4.375f, -3.875f, 2.0f, -0.5f,
//                 0.5f, 0.5f, -1.0f, 1.0f,
//                 -1.375f, 0.875f, 0.0f, -0.5f
//             )
//         )

//         assert(MatrixProduct(testM, testInvM).IsClose(M))
//     }
// }