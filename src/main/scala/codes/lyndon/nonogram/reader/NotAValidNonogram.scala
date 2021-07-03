package codes.lyndon.nonogram.reader

case class NotAValidNonogram(message: String, cause: Throwable = null)
    extends Exception(message, cause)
