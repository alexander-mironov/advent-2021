import java.util.*

sealed class Result {
    object Complete : Result()
    data class Corrupted(val score: Long) : Result()
    data class Incomplete(val score: Long) : Result()
}

val closingCharsToOpeningChar = mapOf(')' to '(', ']' to '[', '}' to '{', '>' to '<')
val charToPointsCorrupted = mapOf(')' to 3L, ']' to 57L, '}' to 1197L, '>' to 25137L)
val charToPointsIncomplete = mapOf('(' to 1L, '[' to 2L, '{' to 3L, '<' to 4L)

fun main() {
    fun processLine(line: String): Result {
        val chars = LinkedList<Char>()
        for (c in line) {
            if (c in closingCharsToOpeningChar.keys) {
                if (chars.lastOrNull() != closingCharsToOpeningChar[c]) {
                    return Result.Corrupted(charToPointsCorrupted[c]!!)
                } else {
                    chars.removeLast()
                }
            } else {
                chars.add(c)
            }
        }
        return if (chars.isEmpty()) {
            Result.Complete
        } else {
            val score = chars.reversed().fold(0L) { acc, c ->
                acc * 5 + charToPointsIncomplete[c]!!
            }
            Result.Incomplete(score)
        }
    }

    fun part1(input: List<String>): Long {
        return input.map(::processLine).filterIsInstance<Result.Corrupted>().sumOf { it.score }
    }

    fun part2(input: List<String>): Long {
        val incompleteLines = input.map(::processLine).filterIsInstance<Result.Incomplete>().sortedBy { it.score }
        return incompleteLines[incompleteLines.lastIndex / 2].score
    }

    val input = readInput("Day10")
    check(part1(readInput("Day10_test")) == 26397L)
    check(part2(readInput("Day10_test")) == 288957L)

    println(part1(input))
    println(part2(input))
}
