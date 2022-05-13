import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

internal class PCGTest{
    @Test
    fun TestRandom(){
        var pcg = PCG()
        assert(pcg.state == 1753877967969059832u)
        assert(pcg.inc == 109u.toULong())

        val list : List<UInt> = listOf(2707161783u, 2068313097u, 3122475824u, 2211639955u, 3215226955u, 3421331566u)

        for (expected in list) {
            assert(expected == pcg.Random())
        }
    }
}