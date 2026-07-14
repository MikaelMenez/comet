import kotlin.system.exitProcess
import utils.*

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
