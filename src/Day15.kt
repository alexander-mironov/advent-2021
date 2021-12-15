import java.util.*

data class Day15Point(val row: Int, val column: Int)

fun main() {

    fun findWay(riskLevels: Array<IntArray>): Int {
        val lastRowIndex = riskLevels.lastIndex
        val lastColumnIndex = riskLevels[0].lastIndex

        fun neighbors(point: Day15Point): List<Day15Point> {
            val (row, column) = point
            val top = if (row > 0) Day15Point(row - 1, column) else null
            val left = if (column > 0) Day15Point(row, column - 1) else null
            val right = if (column < lastColumnIndex) Day15Point(row, column + 1) else null
            val bottom = if (row < lastRowIndex) Day15Point(row + 1, column) else null
            return listOfNotNull(top, left, right, bottom)
        }

        val frontier = PriorityQueue<Pair<Day15Point, Int>> { o1, o2 -> o1.second.compareTo(o2.second) }
        val start = Day15Point(0, 0)
        frontier.add(start to 0)
        val accumulatedRisk = mutableMapOf<Day15Point, Int>()
        accumulatedRisk[start] = 0

        val goal = Day15Point(lastRowIndex, lastColumnIndex)
        while (frontier.isNotEmpty()) {
            val current = frontier.poll().first

            if (current == goal)
                return accumulatedRisk[goal]!!

            for (next in neighbors(current)) {
                val newRisk = accumulatedRisk[current]!! + riskLevels[next.row][next.column]
                if (next !in accumulatedRisk || newRisk < accumulatedRisk[next]!!) {
                    accumulatedRisk[next] = newRisk
                    frontier.add(next to newRisk)
                }
            }
        }
        return 0
    }

    fun part1(input: List<String>): Int {
        val riskLevels = input.map { line -> line.map { it.digitToInt() }.toIntArray() }.toTypedArray()
        riskLevels[0][0] = 0

        return findWay(riskLevels)
    }

    fun part2(input: List<String>): Int {
        val riskLevels = input.map { line -> line.map { it.digitToInt() }.toIntArray() }.toTypedArray()
        val biggerCave = Array(5 * riskLevels.size) { IntArray(5 * riskLevels[0].size) }
        for (i in 0..4) {
            for (j in 0..4) {
                for (row in riskLevels.indices) {
                    for (column in riskLevels[0].indices) {
                        var newRiskLevel = riskLevels[row][column] + (i + j)
                        if (newRiskLevel > 9) {
                            newRiskLevel -= 9
                        }
                        biggerCave[i * riskLevels.size + row][j * riskLevels[0].size + column] = newRiskLevel
                    }
                }
            }
        }

        biggerCave[0][0] = 0
        return findWay(biggerCave)
    }

    check(part1(readInput("Day15_test")) == 40)
    check(part2(readInput("Day15_test")) == 315)

    val input = readInput("Day15")
    println(part1(input))
    check(part2(readInput("Day15")) == 3016)
    println(part2(input))
}
