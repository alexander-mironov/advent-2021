import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sign

fun main() {
    fun calculateDangerous(field: Array<IntArray>): Int {
        var dangerous = 0
        for (x in 0..field.lastIndex) {
            for (y in 0..field[0].lastIndex) {
                if (field[x][y] > 1) {
                    dangerous += 1
                }
            }
        }
        return dangerous
    }

    fun part1(input: List<String>): Int {

        val field = Array(1000) { IntArray(1000) }
        for (line in input) {
            val (x1, y1, x2, y2) = line.split(",", " -> ").map { it.toInt() }
            if (x1 == x2) {
                val yMin = min(y1, y2)
                val yMax = max(y1, y2)
                for (y in yMin..yMax)
                    field[x1][y] += 1
            } else if (y1 == y2) {
                val xMin = min(x1, x2)
                val xMax = max(x1, x2)
                for (x in xMin..xMax)
                    field[x][y1] += 1
            }
        }

        return calculateDangerous(field)
    }

    fun part2(input: List<String>): Int {
        val field = Array(1000) { IntArray(1000) }
        for (line in input) {
            val (x1, y1, x2, y2) = line.split(",", " -> ").map { it.toInt() }
            if (x1 == x2) {
                val yMin = min(y1, y2)
                val yMax = max(y1, y2)
                for (y in yMin..yMax)
                    field[x1][y] += 1
            } else if (y1 == y2) {
                val xMin = min(x1, x2)
                val xMax = max(x1, x2)
                for (x in xMin..xMax)
                    field[x][y1] += 1
            } else {
                val diagonal = abs(x1 - x2) == abs(y1 - y2)
                if (diagonal) {
                    val len = abs(x1 - x2) + 1
                    val stepX = sign(x2.toDouble() - x1).toInt()
                    val stepY = sign(y2.toDouble() - y1).toInt()
                    for (i in 0 until len) {
                        field[x1 + stepX * i][y1 + stepY * i] += 1
                    }
                }
            }
        }

        return calculateDangerous(field)
    }

    val input = readInput("Day05")
    check(part1(readInput("Day05_test")) == 5)
    check(part2(readInput("Day05_test")) == 12)

    println(part1(input))
    println(part2(input))
}
