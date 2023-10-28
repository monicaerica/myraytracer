import org.junit.jupiter.api.Assertions.assertTrue
import java.io.InputStreamReader

const val WHITESPACE = " \n\r\t"
const val SYMBOL = "(),[]<>*"
data class SourceLocation(var file_name : String = "", var line_num : Int = 0, var col_num : Int = 0) {
    override fun toString(): String {
        return "$file_name line: $line_num col: $col_num"
    }
}

enum class keywordEnum() {
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
    UNIFORM,
    CHECKERED,
    IMAGE,
    SPECULAR,
    IDENTITY,
    FLOAT
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
    "uniform" to keywordEnum.UNIFORM,
    "checkered" to keywordEnum.CHECKERED,
    "image" to keywordEnum.IMAGE,
    "specular" to keywordEnum.SPECULAR,
    "identity" to keywordEnum.IDENTITY,
    "float" to keywordEnum.FLOAT

)



abstract class Token (val location: SourceLocation = SourceLocation()) {

}

class keywordToken(val keyword: keywordEnum, location: SourceLocation): Token(){
    override fun toString(): String {
        return keyword.toString()
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
    private var saved_token: Token? = null

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

    @Throws(grammarError::class)
    fun parseStringToken(tokenLocation: SourceLocation): stringToken{
        var token: String = ""
        while (true){
            var ch = this.ReadChar()

            if (ch == '"'){
                break
            }

            if (ch == '\u0000'){
                //For some reason can't add sourcelocation!!!
                throw grammarError("unterminated string", location)
            }

            token += ch
        }

        return stringToken(token)
    }

    /**
     * Reads a token and, based on the first character determines the type of token (literal, number, symbol)
     */
    fun readToken(): Token {
        if (this.saved_token != null){
            val this_saved_token : Token = this.saved_token!! //not-null-assertion operator
            this.saved_token = null
            return this_saved_token
        }
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
            throw grammarError("Invalid character: $ch", this.location)
        }
    }


    /**
     * Given a token in input this 'unreads' the token and puts the token unread in this inputstream 'unread_token' variable
     */
    fun unreadToken(token: Token):Unit{
        assertTrue(this.saved_token == null)
        this.saved_token = token
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
            throw grammarError("Invalid floating point number: $token", this.location)
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
            keywordToken(inToKeyword[token]!!, location = this.location)
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

/**
 * Raises a grammar error if the token is not a symbol
 */
fun expectSymbol(inFile: InputStream, symbol: String) {
    val token = inFile.readToken()
    if (token !is symbolToken || token.symbol != symbol) {
        throw grammarError("Got $token instead of $symbol", sourceLocation = token.location)
    }
}

/**
 * Raises a grammar error if the token is not a keyword
 */

fun expectKeyword(inFile: InputStream, keywords: List<keywordEnum>): keywordEnum {
    val token = inFile.readToken()
    if (token !is keywordToken) {
        throw grammarError("Expected a keyword, got $token", sourceLocation = token.location)
    }
    if (token.keyword !in keywords) {
        throw grammarError("Expected a specific keyword, got $token", sourceLocation = token.location)
    }

    return token.keyword
}


fun expectNumber(inFile: InputStream, scene: Scene): Float {
    val token = inFile.readToken()
    if (token is literalNumberToken) { return token.litnum}
    else if (token is identifierToken) {
        val varName = token.identifier
        if (varName !in scene.floatVariables) {
            throw grammarError("Unknown variable: $token", sourceLocation = token.location)
        }
        return scene.floatVariables[varName]!!

    }

    throw grammarError("Got $token instead of a number", token.location)
}

fun expectString(inFile: InputStream): String {
    val token = inFile.readToken()

    if (token !is stringToken) { throw grammarError("Got $token instead of a string", token.location) }

    return token.string
}

fun expectIdentifier(inFile: InputStream): String {
    val token = inFile.readToken()

    if (token !is identifierToken) { throw grammarError("Got $token instead of an identifier", token.location) }
    return token.identifier
}

/**
 * Parse a color to a Color type, the format in the file should be <r, g, b>
 */
fun parseColor(inFile: InputStream, scene: Scene): Color {
    expectSymbol(inFile, "<")
    val red = expectNumber(inFile, scene)
    expectSymbol(inFile, ",")

    val blue = expectNumber(inFile, scene)
    expectSymbol(inFile, ",")

    val green = expectNumber(inFile, scene)
    expectSymbol(inFile, ">")

    return Color(red, blue, green)
}

fun parseVector(inFile: InputStream, scene: Scene): Vec {
    expectSymbol(inFile, "[")
    val x = expectNumber(inFile, scene)
    expectSymbol(inFile, ",")

    val y = expectNumber(inFile, scene)
    expectSymbol(inFile, ",")

    val z = expectNumber(inFile, scene)
    expectSymbol(inFile, "]")

    return Vec(x, y, z)
}

fun parsePigment(inFile: InputStream, scene: Scene): Pigment {
    val keyword = expectKeyword(inFile, listOf(keywordEnum.CHECKERED, keywordEnum.UNIFORM, keywordEnum.IMAGE))
    expectSymbol(inFile, "(")
    var result: Pigment = UniformPigment(WHITE)
    when (keyword) {
        keywordEnum.UNIFORM -> {
            val color = parseColor(inFile, scene)
            result = UniformPigment(color = color)
        }

        keywordEnum.CHECKERED -> {
            val color1 = parseColor(inFile, scene)
            expectSymbol(inFile, ",")
            val color2 = parseColor(inFile, scene)
            expectSymbol(inFile, ",")
            val nSteps = (expectNumber(inFile, scene)).toInt()
            result = CheckredPigment(color1, color2, nSteps)
        }
        //Implement image, need to check how we defined it
    }
    expectSymbol(inFile, ")")
    return result

}

fun parseBRDF(inFile: InputStream, scene: Scene): BRDF {
    val BRDFKeyword = expectKeyword(inFile, listOf(keywordEnum.DIFFUSE, keywordEnum.SPECULAR))
    expectSymbol(inFile, "(")
    val pigment = parsePigment(inFile, scene)
    expectSymbol(inFile,")")

    return when (BRDFKeyword) {
        keywordEnum.SPECULAR ->  SpecularBRDF(pigment)
        keywordEnum.DIFFUSE ->  DiffuseBRDF(pigment)
        else -> {throw RuntimeException("Dummy excpetion")}
    }
}

fun parseMaterial(inFile: InputStream, scene: Scene): Pair<String, Material> {
    val name = expectIdentifier(inFile)

    expectSymbol(inFile,"(")
    val brdf: BRDF = parseBRDF(inFile, scene)
    expectSymbol(inFile, ",")
    val emittedRadiance = parsePigment(inFile, scene)
    expectSymbol(inFile, ")")
    return Pair(name, Material(brdf, emittedRadiance))
}

fun parseTransformation(inFile: InputStream, scene: Scene): Transformation {
    var result = Transformation()

    while(true) {
        val transformationKw = expectKeyword(inFile, listOf<keywordEnum>(
            keywordEnum.ROTATIONX,
            keywordEnum.ROTATIONY,
            keywordEnum.ROTATIONZ,
            keywordEnum.IDENTITY,
            keywordEnum.TRANSLATION,
            keywordEnum.SCALING
        )
        )

        if (transformationKw == keywordEnum.IDENTITY) { }

        if (transformationKw == keywordEnum.TRANSLATION) {
            expectSymbol(inFile,"(")
            result *= Translation(parseVector(inFile, scene))
            expectSymbol(inFile, ")")
        }
        if (transformationKw == keywordEnum.ROTATIONX) {
            expectSymbol(inFile, "(")
            result *= RotationX(expectNumber(inFile, scene))
            expectSymbol(inFile, ")")
        }
        if (transformationKw == keywordEnum.ROTATIONY) {
            expectSymbol(inFile, "(")
            result *= RotationY(expectNumber(inFile, scene))
            expectSymbol(inFile, ")")
        }
        if (transformationKw == keywordEnum.ROTATIONZ) {
            expectSymbol(inFile, "(")
            result *= RotationZ(expectNumber(inFile, scene))
            expectSymbol(inFile, ")")
        }
        if (transformationKw == keywordEnum.SCALING) {
            expectSymbol(inFile, "(")
            result *= Scaling(parseVector(inFile, scene))
            expectSymbol(inFile, ")")
        }

        var nextKw = inFile.readToken()

        if (nextKw !is symbolToken || nextKw.symbol != "*") {
            inFile.unreadToken(nextKw)
            return result
        }

    }
}

fun parseSphere(inFile: InputStream, scene: Scene): Sphere? {
    expectSymbol(inFile, "(")
    val matName = expectIdentifier(inFile)

    if (matName !in scene.materials.keys) {
        throw grammarError("$matName is an unknown material", sourceLocation = SourceLocation())
    }

    expectSymbol(inFile, ",")
    val transformation: Transformation = parseTransformation(inFile, scene)
    expectSymbol(inFile, ")")

    return scene.materials[matName]?.let { Sphere(transformation, it) }
}

fun parseCamera(inFile: InputStream, scene: Scene): Camera {
    expectSymbol(inFile, "(")
    val typeKw = expectKeyword(inFile, listOf(keywordEnum.PERSPECTIVE, keywordEnum.ORTHOGONAL))
    expectSymbol(inFile, ",")
    val transformation = parseTransformation(inFile, scene)
    expectSymbol(inFile, ",")
    val ar = expectNumber(inFile, scene)
    expectSymbol(inFile, ",")
    val distance = expectNumber(inFile, scene)
    expectSymbol(inFile, ")")
    var cam: Camera = PerpectiveCamera(trans = Transformation())
    if (typeKw == keywordEnum.PERSPECTIVE) {
         cam = PerpectiveCamera(ar, transformation, distance)
    }
    else if (typeKw == keywordEnum.ORTHOGONAL) {
        cam =  OrthogonalCamera(ar, transformation, distance)
    }
    return cam
}

/**
 * Read a scene description from a stream and returns a Scene() object
 */
fun parseScene(inFile: InputStream): Scene {
    var scene = Scene()
    var what_name : String
    var what_where : SourceLocation
    var variable_value : Float
    while (true){
        var what = inFile.readToken()

        if(what is stopToken){
            break
        }
        if(what !is keywordToken){
            throw grammarError("expected a keyword instead of $what", sourceLocation = what.location)
        }

        if(what.keyword == keywordEnum.FLOAT){
            what_name = expectIdentifier(inFile)
            what_where = inFile.location

            expectSymbol(inFile, "(")
            variable_value = expectNumber(inFile, scene)
            expectSymbol(inFile, ")")

            if((what_name in scene.floatVariables) && (what_name !in scene.overriddenVariables)){
                throw grammarError("variable $what_name cannot be redefined", sourceLocation = what_where)
            }
            if(what_name !in scene.overriddenVariables){
                scene.floatVariables[what_name] = variable_value
            }
        }

        else if (what.keyword == keywordEnum.SPHERE){
            what_where = inFile.location
            parseSphere(inFile, scene)?.let { scene.world.AddShape(it) }
        }

//        else if (what.keyword == keywordEnum.PLANE){
//            parsePlane(inFile, scene)?.let { scene.world.AddShape(it) }
//        }

        else if (what.keyword == keywordEnum.CAMERA){
            what_where = inFile.location
            if(scene.camera != null){
                throw grammarError("You cannot define more than one camera", sourceLocation = what_where)
            }
            parseCamera(inFile, scene)?.let { scene.camera = it }
        }

        else if(what.keyword == keywordEnum.MATERIAL){
            var (material_name, material) = parseMaterial(inFile, scene)
            scene.materials[material_name] = material
        }
    }
    return scene
}