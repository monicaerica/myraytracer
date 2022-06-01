import org.junit.jupiter.api.Test
import java.io.FileReader
import java.io.PushbackReader

class provastream {
    @Test
    fun prova() {
        val reader: FileReader = FileReader("/Users/monicaericasardena/Library/Mobile Documents/com~apple~CloudDocs/Documents/Universitá/Magistrale/calcolo_numerico_per_la_generazione_di_immagini_fotorealistiche/myraytracer/src/test/kotlin/ciao.txt")
        val pushback_reader = PushbackReader(reader)
        var char: Int = 0
        var str: Char
        while (char != -1) {
            char = pushback_reader.read()
            println(char)
            println(char.toChar())
        }
    }
}