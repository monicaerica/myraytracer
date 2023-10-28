/**
 * Class used for errors found while reading the input file, given a source location should return a user friendly message
 * describing the error
 */
data class grammarError(override val message: String, val sourceLocation: SourceLocation): Exception(){

}