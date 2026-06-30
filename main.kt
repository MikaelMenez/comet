import java.io.File
import java.util.Scanner
import kotlin.system.exitProcess

object ANSI {
    const val GREEN = "\u001B[32m"
    const val RESET = "\u001B[0m"
    const val BOLD = "\u001B[1m"
    const val RED = "\u001B[91m"

}

fun String.red() = "${ANSI.RED}$this${ANSI.RESET}"
fun String.green() = "${ANSI.GREEN}$this${ANSI.RESET}"
fun String.bold() = "${ANSI.BOLD}$this${ANSI.RESET}"

fun run(src: String) {
    val tokens: List<String> = src.split(" ")
    tokens.forEach { println(it) }
}

fun run_file(path: String) {
    val file = File(path).readText()
    run(file)
}

fun run_prompt() {

    while (true) {
        print("> ".red())
        var line = readlnOrNull()
        if (line == null) {
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
        run_file(args[0])
    } else {
        run_prompt()
    }
}
