package utils

sealed class BfktException( msg: String ): Exception(msg)
sealed class SyntaxError( msg: String ): BfktException(msg)

class UnmatchedBracket( msg: String ): SyntaxError(msg)
class PointerOutOfBounds( msg: String ): SyntaxError(msg)
class CellValueOutOfBounds( msg: String ): SyntaxError(msg)
