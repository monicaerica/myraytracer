import jdk.dynalink.linker.support.Guards.isInstance
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import java.io.InputStreamReader

fun assertIsKeyword(token: Token, keyword: keywordEnum) {
    assert(token is keywordToken)
}

fun assertIsIdentifier(token: Token, identifier: String) {
    assert(token is identifierToken)
}

fun assertIsSymbol(token: Token, Symbol: String) {
    assert(token is symbolToken)
}

fun assertIsNumber(token: Token, number: String) {
    assert(token is literalNumberToken)
}

fun assertIsString(token: Token, s: String) {
    assert(token is stringToken)
}

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

    @Test
    fun testLexer () {
        val inFile: InputStream = InputStream(InputStreamReader("""
        # This is a comment
        # This is another comment
        new material sky_material(
            diffuse(image("my file.pfm")),
            <5.0, 500.0, 300.0>
        ) # Comment at the end of the line
""".byteInputStream()))

        assertIsKeyword(inFile.readToken(), keywordEnum.NEW)
        assertIsKeyword(inFile.readToken(), keywordEnum.MATERIAL)
        assertIsIdentifier(inFile.readToken(), "sky_material")
        assertIsSymbol(inFile.readToken(), "(")
        assertIsKeyword(inFile.readToken(), keywordEnum.DIFFUSE)
        assertIsSymbol(inFile.readToken(), "(")
        assertIsKeyword(inFile.readToken(), keywordEnum.IMAGE)
        assertIsSymbol(inFile.readToken(), "(")
        assertIsString(inFile.readToken(), "my file.pfm")
        assertIsSymbol(inFile.readToken(), ")")




    }
}