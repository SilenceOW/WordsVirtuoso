package wordsvirtuoso

import java.io.File
import java.time.Duration
import kotlin.random.Random

class WordsVirtuoso(allWordsFile: File, candidateWordsFile: File) {
    private val words = if (allWordsFile.exists()) allWordsFile.readLines() else throw WordsFileNotFoundException(allWordsFile)
    private val candidates = if (candidateWordsFile.exists()) candidateWordsFile.readLines() else throw CandidateFileNotFoundException(candidateWordsFile)
    init {
        val invalidWordsCounter = this.countInvalidWords(words)
        if (invalidWordsCounter != 0) throw InvalidWordsFoundException(invalidWordsCounter, allWordsFile)

        val invalidCandidatesCounter = countInvalidWords(candidates)
        if (invalidCandidatesCounter != 0) throw InvalidWordsFoundException(invalidCandidatesCounter, candidateWordsFile)

        val invalidCandidates = countInvalidCandidates(candidates, words)
        if (invalidCandidates != 0) throw CandidateWordsNotIncludedException(invalidCandidates, allWordsFile)
    }

    private fun isWordInvalid(wordU: String): Boolean {
        val word = wordU.lowercase()
        return (word.length != 5 ||
                !Regex("[a-zA-Z]{5}").matches(word) ||
                word.toList().distinct().size != word.length)
    }

    private fun countInvalidWords(words: List<String>): Int {
        var counter = 0
        for (word in words) counter += if (isWordInvalid(word)) 1 else 0
        return counter
    }

    private fun checkCandidate(candidate: String, words: List<String>): Boolean {
        return candidate.lowercase() in words.map { it.lowercase() }
    }

    private fun countInvalidCandidates(candidates: List<String>, words: List<String>): Int {
        var counter = 0
        for (candidate in candidates) counter += if (checkCandidate(candidate, words)) 0 else 1
        return counter
    }

    fun start() {
        println("Words Virtuoso")
        val secretWord: String = candidates[ Random.nextInt(0, candidates.size) ].lowercase()
        var tries = mutableListOf<String>()
        val wrongChars = sortedSetOf<Char>()
        val timeStart = System.currentTimeMillis()
        while (true) {
            val playerWord = "Input a 5-letter word:".reply().lowercase()
            println()
            when {
                playerWord == "exit" -> return println("The game is over.")
                !isPlayerWordValid(playerWord) -> continue
            }
            for (letter in playerWord) {
                if (letter !in secretWord) wrongChars.add(letter)
            }
            val formattedWord = formatPlayerWord(playerWord, secretWord)
            tries.add(formattedWord)
            for (word in tries) {
                println(word)
            }
            when (playerWord) {
                secretWord -> {
                    println("\nCorrect!")
                    val timeFinish = System.currentTimeMillis()
                    if (tries.size == 1) return println("Amazing luck! The solution was found at once.")
                    return println("The solution was found after ${tries.size} tries in ${(timeFinish - timeStart) / 1000} seconds.")
                }
                else -> println("\n\u001B[48:5:14m${wrongChars.joinToString("").uppercase()}\u001B[0m")
            }
            println()
        }
    }

    private fun formatPlayerWord(word: String, secretWord: String): String {
        val finalWord: MutableList<String> = mutableListOf()
        for (i in word.indices) {
            when {
                word[i] in secretWord && word[i] == secretWord[i] -> finalWord.add("\u001B[48:5:10m${word[i].uppercase()}\u001B[0m")
                word[i] in secretWord -> finalWord.add("\u001B[48:5:11m${word[i].uppercase()}\u001B[0m")
                else -> finalWord.add("\u001B[48:5:7m${word[i].uppercase()}\u001B[0m")
            }
        }
        return finalWord.joinToString("")
    }

    private fun isPlayerWordValid(word: String): Boolean {
        when {
            word.length != 5 -> {
                println("The input isn't a 5-letter word.")
                return false
            }
            !Regex("[a-zA-Z]{5}").matches(word) -> {
                println("One or more letters of the input aren't valid.")
                return false
            }
            word.toList().distinct().size != word.length -> {
                println("The input has duplicate letters.")
                return false
            }
            word !in this.words.map { it.lowercase() } -> {
                println("The input word isn't included in my words list.")
                return false
            }
            else -> return true
        }
    }
}