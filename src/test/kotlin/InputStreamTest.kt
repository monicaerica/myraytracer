import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import java.io.InputStreamReader

internal class InputStreamTest(){
    @Test
    fun testInputFile(){
        val stream: InputStream = InputStream(InputStreamReader("abc \nd\nef".byteInputStream()))
        assertTrue(stream.location.line_num == 1)
        assertTrue(stream.location.col_num == 1)

        assertTrue(stream.ReadChar() == 'a')
        assertTrue(stream.location.line_num == 1)
        assertTrue(stream.location.col_num == 2)

        stream.UnreadChar('A')
        assertTrue(stream.location.line_num == 1)
        assertTrue(stream.location.col_num == 1)

        assertTrue(stream.ReadChar() == 'A')
        assertTrue(stream.location.line_num == 1)
        assertTrue(stream.location.col_num == 2)

        assertTrue(stream.ReadChar() == 'b')
        assertTrue(stream.location.line_num == 1)
        assertTrue(stream.location.col_num == 3)

        assertTrue(stream.ReadChar() == 'c')
        assertTrue(stream.location.line_num == 1)
        assertTrue(stream.location.col_num == 4)

        stream.skipWhitespaceAndComents()

        assertTrue(stream.ReadChar() == 'd')
        assertTrue(stream.location.line_num == 2)
        assertTrue(stream.location.col_num == 2)

        assertTrue(stream.ReadChar() == '\n')
        assertTrue(stream.location.line_num == 3)
        assertTrue(stream.location.col_num == 1)

        assertTrue(stream.ReadChar() == 'e')
        assertTrue(stream.location.line_num == 3)
        assertTrue(stream.location.col_num == 2)

        assertTrue(stream.ReadChar() == 'f')
        assertTrue(stream.location.line_num == 3)
        assertTrue(stream.location.col_num == 3)

        assertTrue(stream.ReadChar() == null)

    }
}