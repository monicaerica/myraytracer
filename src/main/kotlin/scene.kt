import java.io.Reader
import java.io.*
import java.nio.charset.Charset

data class SourceLocation(var file_name : String = "", var line_num : Int = 0, var col_num : Int = 0) {
}

abstract class Token {
    val location: SourceLocation = SourceLocation()

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
            '\t' -> this.location.col_num = this.tabulations
            else -> this.location.col_num += 1
        }
    }

    fun ReadChar(): Char?{
        var ch : Char?
        if (this.saved_char != null){
            ch = this.saved_char
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
}