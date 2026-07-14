package utils

object ANSI {
    const val GREEN = "\u001B[32m"
    const val RESET = "\u001B[0m"
    const val BOLD = "\u001B[1m"
    const val RED = "\u001B[91m"
    const val BLUE = "\u001b[34m"

}

fun String.red() = "${ANSI.RED}$this${ANSI.RESET}"
fun String.green() = "${ANSI.GREEN}$this${ANSI.RESET}"
fun String.bold() = "${ANSI.BOLD}$this${ANSI.RESET}"
fun String.blue() = "${ANSI.BLUE}$this${ANSI.RESET}"
