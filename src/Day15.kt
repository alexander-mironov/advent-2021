import java.util.*
import kotlin.math.abs

data class Day15Point(val row: Int, val column: Int)

fun main() {

    fun findWay(riskLevels: Array<IntArray>): Int {
        val accumulatedRisk = mutableMapOf<Day15Point, Int>()
        val priorityQueue =
            PriorityQueue<Day15Point> { o1, o2 -> accumulatedRisk[o1]!!.compareTo(accumulatedRisk[o2]!!) }
        for (row in riskLevels.indices) {
            for (column in riskLevels[0].indices) {
                val point = Day15Point(row, column)
                accumulatedRisk[point] = Int.MAX_VALUE
                priorityQueue.add(point)
            }
        }
        accumulatedRisk[Day15Point(0, 0)] = 0

        while (priorityQueue.isNotEmpty()) {
            val point = priorityQueue.poll()
            val neighbours = priorityQueue.filter {
                (it.row == point.row && (abs(it.column - point.column) == 1)) ||
                        (it.column == point.column && (abs(it.row - point.row) == 1))
            }
            neighbours.forEach { neighbour ->
                val alt = accumulatedRisk[point]!! + riskLevels[neighbour.row][neighbour.column]
                if (alt < accumulatedRisk[neighbour]!!) {
                    accumulatedRisk[neighbour] = alt
                    priorityQueue.remove(neighbour)
                    priorityQueue.add(neighbour)
                }
            }
        }

        return accumulatedRisk[Day15Point(riskLevels.lastIndex, riskLevels[0].lastIndex)]!!
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
