import org.junit.jupiter.api.Assertions.assertTrue
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
    ROTATIONZ,
    MATERIAL,
    DIFFUSE,
    IMAGE
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
    "rotationz" to keywordEnum.ROTATIONZ,
    "material" to keywordEnum.MATERIAL,
    "diffuse" to keywordEnum.DIFFUSE,
    "image" to keywordEnum.IMAGE

)



abstract class Token (val location: SourceLocation = SourceLocation()) {

}

class keywordToken(val keyword: String, location: SourceLocation): Token(){
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

class identifierToken(val identifier: String, location: SourceLocation): Token(){
    override fun toString(): String {
        return identifier.toString()
    }
}

class literalNumberToken(val litnum: Float, location: SourceLocation): Token(){
    override fun toString(): String {
        return litnum.toString()
    }
}

class stringToken(val string: String): Token(){
    override fun toString(): String {
        return string
    }
}




//val reader: FileReader = FileReader(file_name)
//val pushback_reader = PushbackReader(reader)
////Charset(US-ASCII))

class InputStream(val stream: InputStreamReader, val file_name : String = "", val tabulations : Int = 4, var location : SourceLocation = SourceLocation(file_name = file_name, line_num = 1, col_num = 1)){


    private var saved_char : Char? = null
    private var saved_location: SourceLocation = this.location.copy()

    private fun UpdatePos(ch: Char?){
        when(ch){
            '\n' -> {this.location.col_num = 1
                    this.location.line_num += 1}
            null -> return
            ' ' -> return
            '\t' -> this.location.col_num = this.tabulations
            else -> this.location.col_num += 1
        }
    }

    fun ReadChar(): Char?{
        var ch : Char?
        if (this.saved_char != null){
            ch = this.saved_char!!
            this.saved_char = null
        }
        else {
            var ascii = this.stream.read()
            if (ascii != -1) {ch = ascii.toChar()}
            else { ch = null }
        }
        this.saved_location = this.location.copy()

        this.UpdatePos(ch)

        return ch
    }

    fun UnreadChar(ch: Char){
        assertTrue(this.saved_char == null)
        this.saved_char = ch
        this.location = this.saved_location.copy()
    }

    fun skipWhitespaceAndComents(){
        var ch: Char? = this.ReadChar()
        while (ch!! in WHITESPACE || ch == '#'){
            if (ch == '#'){
                while (!(this.ReadChar()!! in "\r\n" || this.ReadChar()!! in "" )){
                    {}
                }
            }
            ch = this.ReadChar()
            if (ch == null){
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
        if (ch == null){
            return stopToken(location = this.location)
        }

        if (ch in SYMBOL){
            return symbolToken(symbol = ch.toString(),location = this.location)
        }
        else if (ch == '"'){
            return this.parseStringToken(tokenLocation = this.location)
        }
        else if (ch.isDigit() || ch in "+-."){
            return this.parseFloatToken(firstChar = ch.toString(), tokenLocation = this.location)
            //Need to implement
        }
        else if (ch.isLetter() || ch == '_'){
            return this.parseKeywordOrIdentifierToken(firstChar = ch.toString(), tokenLocation = this.location)
        }
        else {
            throw GrammarError("Invalid character: $ch", this.location)
        }
    }

    private fun parseFloatToken(firstChar: String, tokenLocation: SourceLocation): literalNumberToken {
        var token: String = firstChar
        while (true){
            var ch =this.ReadChar()

            if (ch != null) {
                if (!(ch.isDigit() || ch == '.' || ch in "eE")){
                    this.UnreadChar(ch)
                    break
                }
            }

            token += ch
        }

        val num: Float
        try {
            num= token.toFloat()
        }catch (err: NumberFormatException) {
            throw GrammarError("Invalid floating point number: $token", this.location)
        }

        return literalNumberToken(litnum = num, location = this.location)
    }

    private fun parseKeywordOrIdentifierToken(firstChar: String, tokenLocation: SourceLocation): Token {
        var token = firstChar
        while (true) {
            var ch = this.ReadChar()
            if (ch != null) {
                if (!(ch.isLetterOrDigit() || ch == '_')){
                    if (ch != null) {
                        this.UnreadChar(ch)
                    }
                    break
                }
            }
            token += ch
        }

        return if (inToKeyword.containsKey(token)) {
            keywordToken(token, location = this.location)
        } else {
            identifierToken(token, location = this.location)
        }
    }


}

/**
 * Scene data class, produced by parsing a scene descriptor file, contains everything that's needed to render a scene
 */
data class Scene(var materials: MutableMap<String, Material> = mutableMapOf(),
                 var world: World = World(), var camera: Camera? = null,
                 var floatVariables: MutableMap<String, Float> = mutableMapOf(),
                 var overriddenVariables: Set<String> = setOf()
)
