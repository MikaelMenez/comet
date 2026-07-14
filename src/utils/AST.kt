package utils

sealed interface AST {
    data class Literal(val value: Any?) : AST
    data class Unary(val operator: Token, val right: AST) : AST
    data class Binary(val left: AST, val operator: Token, val right: AST) : AST
    data class Grouping(val expression: AST) : AST
}
