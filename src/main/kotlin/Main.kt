package wordsvirtuoso

import java.io.File

fun String.reply(): String { println(this); return readln() }


fun main(args: Array<String>) {
    try {
        // Getting files
        if (args.size != 2) throw InvalidNumberOfArgumentsException()
        val allWordsFilename = args[0]
        val allWordsFile = File(allWordsFilename)
        val candidateWordsFilename = args[1]
        val candidateWordsFile = File(candidateWordsFilename)

        // Creating game engine
        val game = WordsVirtuoso(allWordsFile, candidateWordsFile)

        // Starting game
        game.start()
    } catch (e: Exception) {
        println(e.message)
    }
}
