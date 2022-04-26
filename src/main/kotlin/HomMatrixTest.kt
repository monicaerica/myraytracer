import org.junit.Assert.*
import org.junit.Test

internal class HomMatrixTest{

    @Test
    fun IsClose(){
        var M: HomMatrix = HomMatrix()
        var InvM: HomMatrix = HomMatrix()

        assert(M.IsClose(InvM))
    }
}