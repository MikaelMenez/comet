package utils

sealed interface AST {
    data class Literal(val value: Any) : AST
    data class Unary(val operator: TokenType, val right: AST)
}
