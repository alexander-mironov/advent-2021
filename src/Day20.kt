import kotlin.math.max
import kotlin.math.min

data class Day20Point(val x: Int, val y: Int)
data class Area(val xMin: Int, val yMin: Int, val xMax: Int, val yMax: Int) {
    fun contains(x: Int, y: Int): Boolean {
        return x in xMin..xMax && y in yMin..yMax
    }
}

val flagPoint = Day20Point(Int.MAX_VALUE / 2, Int.MAX_VALUE / 2)

fun main() {

    fun getAlgorithmIndexForPoint(
        field: Set<Day20Point>,
        centerX: Int,
        centerY: Int,
        area: Area
    ): Int {
        val shrankArea = Area(area.xMin + 1, area.yMin + 1, area.xMax - 1, area.yMax - 1)
        var index = 0
        for (x in centerX - 1..centerX + 1) {
            for (y in centerY - 1..centerY + 1) {
                index *= 2
                if (!shrankArea.contains(x, y)) {
                    if (field.contains(flagPoint)) {
                        index += 1
                    }
                } else if (field.contains(Day20Point(x, y))) {
                    index += 1
                }
            }
        }
        return index
    }

    fun step(field: MutableSet<Day20Point>, algorithm: List<Boolean>, area: Area): Pair<MutableSet<Day20Point>, Area> {
        var xMin = Int.MAX_VALUE
        var xMax = Int.MIN_VALUE
        var yMin = Int.MAX_VALUE
        var yMax = Int.MIN_VALUE
        val updatedField = mutableSetOf<Day20Point>()
        for (x in area.xMin..area.xMax) {
            for (y in area.yMin..area.yMax) {
                val index = getAlgorithmIndexForPoint(field, x, y, area)
                if (algorithm[index]) {
                    updatedField.add(Day20Point(x, y))
                    xMin = min(xMin, x)
                    xMax = max(xMax, x)
                    yMin = min(yMin, x)
                    yMax = max(yMax, x)
                }
            }
        }

        if (algorithm[getAlgorithmIndexForPoint(field, flagPoint.x, flagPoint.y, area)]) {
            updatedField.add(flagPoint)
        }

        return updatedField to Area(xMin - 1, yMin - 1, xMax + 1, yMax + 1)
    }

    fun parseInput(input: List<String>): Triple<List<Boolean>, MutableSet<Day20Point>, Area> {
        val algorithm = input[0].map { it == '#' }

        val field = mutableSetOf<Day20Point>()

        var xMin = Int.MAX_VALUE
        var xMax = Int.MIN_VALUE
        var yMin = Int.MAX_VALUE
        var yMax = Int.MIN_VALUE
        for (i in 2..input.lastIndex) {
            val x = i - 2
            val line = input[i]
            line.forEachIndexed { y, c ->
                if (c == '#') {
                    field.add(Day20Point(x, y))
                    xMin = min(xMin, x)
                    xMax = max(xMax, x)
                    yMin = min(yMin, x)
                    yMax = max(yMax, x)
                }
            }
        }
        val area = Area(xMin - 1, yMin - 1, xMax + 1, yMax + 1)
        return Triple(algorithm, field, area)
    }

    fun part1(input: List<String>): Int {

        val (algorithm, field, area) = parseInput(input)

        var fieldAndArea = step(field, algorithm, area)
        fieldAndArea = step(fieldAndArea.first, algorithm, fieldAndArea.second)

        return fieldAndArea.first.size
    }

    fun part2(input: List<String>): Int {
        var (algorithm, field, area) = parseInput(input)

        var fieldAndArea: Pair<MutableSet<Day20Point>, Area>
        for (i in 0..49) {
            fieldAndArea = step(field, algorithm, area)
            field = fieldAndArea.first
            area = fieldAndArea.second
        }

        return field.size
    }

    check(part1(readInput("Day20_test")) == 35)
    check(part2(readInput("Day20_test")) == 3351)

    val input = readInput("Day20")
    println(part1(input))
    println(part2(input))
}