import java.io.*

const val WHITESPACE = " \n\r\t"
const val SYMBOL = "(),[]<>*"
data class SourceLocation(var file_name : String = "", var line_num : Int = 0, var col_num : Int = 0) {
    override fun toString(): String {
        return "$file_name line: $line_num col: $col_num"
    }
}

/**
 * Class used for errors found while reading the input file, given a source location should return a user friendly message
 * describing the error
 */
data class grammarError(override val message: String, val sourceLocation: SourceLocation): Exception(){

}


enum class keywordEnum {
    NEW,
    WORLD,
    SHAPE,
    SPHERE,
    PLANE,
    TRIANGLE,
    DISK,
    CAMERA,
    ORTHOGONAL,
    PERSPECTIVE,
    TRANSFORMATION,
    TRANSLATION,
    SCALING,
    ROTATIONX,
    ROTATIONY,
    ROTATIONZ
}

val inToKeyword = mapOf(
    "new" to keywordEnum.NEW,
    "world" to keywordEnum.WORLD,
    "shape" to keywordEnum.SHAPE,
    "sphere" to keywordEnum.SPHERE,
    "plane" to keywordEnum.PLANE,
    "triangle" to keywordEnum.TRIANGLE,
    "disk" to keywordEnum.DISK,
    "camera" to keywordEnum.CAMERA,
    "orthogonal" to keywordEnum.ORTHOGONAL,
    "perspective" to keywordEnum.PERSPECTIVE,
    "transformation" to keywordEnum.TRANSFORMATION,
    "translation" to keywordEnum.TRANSLATION,
    "scaling" to keywordEnum.SCALING,
    "rotationx" to keywordEnum.ROTATIONX,
    "rotationy" to keywordEnum.ROTATIONY,
    "rotationz" to keywordEnum.ROTATIONZ
)



abstract class Token (val location: SourceLocation = SourceLocation()) {

}

class keywordToken(val keyword: String): Token(){
    override fun toString(): String {
        return keyword
    }
}
class stopToken(location: SourceLocation) : Token(){

}

class symbolToken(val symbol: String, location: SourceLocation): Token(){
    override fun toString(): String {
        return symbol
    }
}

class identifierToken(val identifier: String): Token(){
    override fun toString(): String {
        return identifier.toString()
    }
}

class literalNumberToken(val litnum: Float): Token(){
    override fun toString(): String {
        return litnum.toString()
    }
}

class stringToken(val string: String): Token(){
    override fun toString(): String {
        return string.toString()
    }
}




//val reader: FileReader = FileReader(file_name)
//val pushback_reader = PushbackReader(reader)
////Charset(US-ASCII))

class InputStream(val stream: PushbackReader, val file_name : String = "", val tabulations : Int = 4, var location : SourceLocation = SourceLocation(file_name = file_name, line_num = 1, col_num = 1), var saved_char : Char? = null, var saved_location: SourceLocation = SourceLocation(file_name = file_name, line_num = 1, col_num = 1)){

    fun UpdatePos(ch: Char?){
        when(ch){
            '\n' -> {this.location.col_num = 1
                    this.location.line_num += 1}
            null -> return
            ' ' -> return
            '\t' -> this.location.col_num = this.tabulations
            else -> this.location.col_num += 1
        }
    }

    fun ReadChar(): Char{
        var ch : Char
        if (this.saved_char != null){
            ch = this.saved_char!!
            this.saved_char = null
        }
//        else {
//            var ascii = this.stream.read()
//            if (ascii != -1) ch = ascii.toChar()
//                else ch = null
//        }
        else{ch = this.stream.read().toChar()}

        this.UpdatePos(ch)
        this.saved_location = this.location

        return ch
    }

    fun UnreadChar(ch: Char){
        assert(this.saved_char == null)
        this.saved_char = ch
        this.location = this.saved_location
    }

    fun skipWhitespaceAndComents(){
        var ch: Char = this.ReadChar()
        while (ch!! in WHITESPACE || ch == '#'){
            if (ch == '#'){
                while (!(this.ReadChar() in "\r\n" || this.ReadChar() in "" )){
                    {}
                }
            }
            ch = this.ReadChar()
            if (ch in ""){
                return
            }
        }
        this.UnreadChar(ch)
    }

    fun parseStringToken(tokenLocation: SourceLocation): stringToken{
        var token: String = ""
        while (true){
            var ch = this.ReadChar()

            if (ch == '"'){
                break
            }

            if (ch == '\u0000'){
                //For some reason can't add sourcelocation!!!
                GrammarError("unterminated string", location)
            }

            token += ch
        }

        return stringToken(token)
    }

    /**
     * Reads a token and, based on the first character determines the type of token (literal, number, symbol)
     */
    fun readToken(): Token {
        this.skipWhitespaceAndComents()
        val ch = this.ReadChar()
        if (ch == '\u0000'){
            return stopToken(location = this.location)
        }

        if (ch in SYMBOL){
            return symbolToken(symbol = ch.toString(),location = this.location)
        }
        else if (ch == '"'){
            return this.parseStringToken(tokenLocation = this.location)
        }
        else if (ch.isDigit() || ch in "+-."){
            return this.parseFloatToken()
            //Need to implement
        }
        else if (ch.isLetter() || ch == '_'){
            return this.parseKeywordOrIdentifierToken(firstChar = ch, tokenLocation = this.location)
        }
        else {
            throw GrammarError("Invalid character: $ch", this.location)
        }
    }

    private fun parseFloatToken(): Token {

    }

    private fun parseKeywordOrIdentifierToken(firstChar: Char, tokenLocation: SourceLocation): Token {

    }


}

