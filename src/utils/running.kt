package utils

import java.io.File

fun run(src: String) {
    val sc = Tokenizer(src)
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
