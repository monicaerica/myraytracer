import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

internal class NormalTest {

    @Test
    fun TestNormal(){
        val normal: Normal = Normal(1.0f, 2.0f, 3.0f)
        val normalneg: Normal = Normal(-1.0f, -2.0f, -3.0f)
        println(normal.neg())
        assert(normal.neg().IsClose(normalneg))
        normal.Normalize()
        val norm: Float = normal.Norm()
        assertEquals(norm, 1.0f, 1e-5f)
    }
}