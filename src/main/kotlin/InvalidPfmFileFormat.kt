class InvalidPfmFileFormat (message: String) : RuntimeException (message)

class GrammarError(message: String, location: SourceLocation) : RuntimeException (message)