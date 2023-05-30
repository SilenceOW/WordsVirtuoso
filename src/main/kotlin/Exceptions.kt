package wordsvirtuoso

import java.io.File
import kotlin.Exception

class InvalidNumberOfArgumentsException : Exception("Error: Wrong number of arguments.")
class WordsFileNotFoundException(filename: File) : Exception("Error: The words file $filename doesn't exist.")
class CandidateFileNotFoundException(filename: File) : Exception("Error: The candidate words file $filename doesn't exist.")
class InvalidWordsFoundException(invalidWordsCount: Int, filename: File) : Exception("Error: $invalidWordsCount invalid words were found in the $filename file.")
class CandidateWordsNotIncludedException(excludedCandidateWordsCount: Int, filename: File) : Exception("Error: $excludedCandidateWordsCount candidate words are not included in the $filename file.")