import java.io.File

fun main(args: Array<String>) {
    if (args.size < 2) {
        println("Uso: tool <build|run> <jvm|native> [argumentos...]")
        System.exit(1)
    }

    val action = args[0]
    val target = args[1]
    val appArgs = args.drop(2)

    val outDir = File("out")
    if (!outDir.exists()) outDir.mkdirs()

    when (action) {
        "build" -> buildProject(target, outDir)
        "run" -> runProject(target, appArgs)
        else -> {
            println("❌ Ação desconhecida: $action. Use 'build' ou 'run'.")
            System.exit(1)
        }
    }
}

fun buildProject(target: String, outDir: File) {
    val jarPath = "out/comet.jar"
    val nativePath = "out/comet"

    println("🔨 Compilando JAR (JVM)...")
    val srcFiles = File("src").walk().filter { it.extension == "kt" }.map { it.absolutePath }.toList()

    runCommand(listOf("kotlinc") + srcFiles + listOf("-include-runtime", "-d", jarPath))
    println("✅ JAR gerado em $jarPath")

    if (target == "native") {
        println("⚡ Gerando binário nativo (GraalVM)...")
        runCommand(listOf("native-image", "-jar", jarPath, "-o", nativePath))
        println("✅ Binário nativo gerado em $nativePath")
    }
}

fun runProject(target: String, appArgs: List<String>) {
    val jarPath = "out/comet.jar"
    val nativePath = "out/comet"

    val command = when (target) {
        "jvm" -> listOf("java", "-jar", jarPath) + appArgs
        "native" -> listOf(nativePath) + appArgs
        else -> {
            println("❌ Alvo inválido: $target. Use 'jvm' ou 'native'.")
            System.exit(1)
            emptyList() // Nunca alcançado devido ao exit
        }
    }

    println("🚀 Executando: ${command.joinToString(" ")}")
    runCommand(command)
}

fun runCommand(command: List<String>) {
    try {
        val process = ProcessBuilder(command)
            .inheritIO()
            .start()

        val exitCode = process.waitFor()
        if (exitCode != 0) {
            println("❌ Erro ao executar o comando. Código de saída: $exitCode")
            System.exit(exitCode)
        }
    } catch (e: Exception) {
        println("❌ Erro ao tentar rodar comando: ${e.message}")
        System.exit(1)
    }
}
