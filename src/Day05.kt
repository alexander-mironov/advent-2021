import kotlin.math.max
import kotlin.math.min

fun main() {
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

    fun part2(input: List<String>): Int {
        return 0
    }

    val input = readInput("Day05")
    check(part1(readInput("Day05_test")) == 5)

    println(part1(input))
    println(part2(input))
}
