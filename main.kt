import java.io.File
import kotlin.system.exitProcess

enum class TokenType {
    LEFT_PAREN, RIGHT_PAREN, LEFT_BRACE, RIGHT_BRACE,
    COMMA, DOT, MINUS, PLUS, SEMICOLON, SLASH, STAR,


    BANG, BANG_EQUAL,
    EQUAL, EQUAL_EQUAL,
    GREATER, GREATER_EQUAL,
    LESS, LESS_EQUAL,

    IDENTIFIER, STRING, NUMBER,

    AND, CLASS, ELSE, FALSE, FUN, FOR, IF, NIL, OR,
    PRINT, RETURN, SUPER, THIS, TRUE, VAR, WHILE,

    EOF
}

data class Token(
    val type: TokenType,
    val lexeme: String,
    val literal: Any?,
    val line: Int
)

class Parser(private val source: String) {
    private val tokens = mutableListOf<Token>()
    private var start: Int = 0
    private var current: Int = 0
    private var line: Int = 1
    fun scanTokens(): List<Token> {
        while (!isAtEnd()) {
            start = current
            scanToken()
        }
        tokens.add(Token(TokenType.EOF, "", null, line))
        return tokens
    }

    private fun isAtEnd(): Boolean {
        return current >= source.length
    }

    private fun scanToken() {
        var c = advance()
        when (c) {
            '(' -> addToken(TokenType.LEFT_PAREN)
            ')' -> addToken(TokenType.RIGHT_PAREN)
            '{' -> addToken(TokenType.LEFT_BRACE)
            '}' -> addToken(TokenType.RIGHT_BRACE)
            ',' -> addToken(TokenType.COMMA)
            '.' -> addToken(TokenType.DOT)
            '-' -> addToken(TokenType.MINUS)
            '+' -> addToken(TokenType.PLUS)
            ';' -> addToken(TokenType.SEMICOLON)
            '*' -> addToken(TokenType.STAR)
            '!' -> addToken(if (match('=')) TokenType.BANG_EQUAL else TokenType.BANG)
            '=' -> addToken(if (match('=')) TokenType.EQUAL_EQUAL else TokenType.EQUAL)
            '>' -> addToken(if (match('=')) TokenType.GREATER_EQUAL else TokenType.GREATER)
            '<' -> addToken(if (match('=')) TokenType.LESS_EQUAL else TokenType.LESS)
            ' ', '\t', '\r' -> {}
            '/' -> {
                if (match('/')) {
                    while (peek() != '\n' && !isAtEnd()) advance()
                } else {
                    addToken(TokenType.SLASH)
                }
            }

            '"' -> string()

            '\n' -> line++
            else -> error(line, "Caractere inesperado: $c")
        }

    }

    private fun string() {
        while (peek() != '"' && !isAtEnd()) {
            if (peek() == '\n') line++
            advance()
        }
        if (isAtEnd()) {
            error(line, "Unterminated string")
            return
        }
        advance()
        val str = source.substring(start + 1, current - 1)
        addToken(TokenType.STRING, str)
    }

    private fun peek(): Char {
        return (if (isAtEnd()) '\u0000' else source[current])
    }

    private fun match(expected: Char): Boolean {
        if (isAtEnd()) return false
        if (source[current] != expected) return false
        current++
        return true
    }

    private fun advance(): Char {
        return source[current++]
    }

    private fun addToken(type: TokenType) {
        addToken(type, null)
    }

    private fun addToken(type: TokenType, literal: Any?) {
        val text = source.substring(start, current)
        tokens.add(Token(type, text, literal, line))
    }

}

object ANSI {
    const val GREEN = "\u001B[32m"
    const val RESET = "\u001B[0m"
    const val BOLD = "\u001B[1m"
    const val RED = "\u001B[91m"
    const val BLUE = "\u001b[34m"

}

fun error(line: Int, message: String) {
    report(line, "", message)
}

fun report(line: Int, where: String, message: String) {
    System.err.println("[line " + line.toString().blue() + "] Error" + where + ": " + message.red())
}

fun String.red() = "${ANSI.RED}$this${ANSI.RESET}"
fun String.green() = "${ANSI.GREEN}$this${ANSI.RESET}"
fun String.bold() = "${ANSI.BOLD}$this${ANSI.RESET}"
fun String.blue() = "${ANSI.BLUE}$this${ANSI.RESET}"


fun run(src: String) {
    val sc = Parser(src)
    val tokens: List<Token> = sc.scanTokens()
    tokens.forEach { println(it) }
}

fun runFile(path: String) {
    val file = File(path).readText()
    run(file)
}

fun runPrompt() {

    while (true) {
        print("> ".red())
        var line = readlnOrNull()
        line ?: run {
            println("\n")
            break
        }
        run(line)
    }
}

fun main(args: Array<String>) {
    if (args.size > 1) {
        println("comet cli shoud be used like this:  " + "comet".green() + "[file.ct$]".bold())
        exitProcess(64)
    } else if (args.size == 1) {
        runFile(args[0])
    } else {
        runPrompt()
    }
}
