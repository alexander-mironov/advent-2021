data class Point(val row: Int, val column: Int)

fun main() {
    fun part1(input: List<String>): Int {
        val heightmap = input.map { row -> row.map { it.digitToInt() } }
        val lastRowIndex = heightmap.lastIndex
        val lastColumnIndex = heightmap[0].lastIndex
        var sumOfLowPoints = 0
        for (row in 0..lastRowIndex) {
            for (column in 0..lastColumnIndex) {
                val value = heightmap[row][column]
                val top = if (row > 0) heightmap[row - 1][column] else Int.MAX_VALUE
                val left = if (column > 0) heightmap[row][column - 1] else Int.MAX_VALUE
                val right = if (column < lastColumnIndex) heightmap[row][column + 1] else Int.MAX_VALUE
                val bottom = if (row < lastRowIndex) heightmap[row + 1][column] else Int.MAX_VALUE
                if (value < top && value < left && value < right && value < bottom) {
                    sumOfLowPoints += (value + 1)
                }
            }
        }
        return sumOfLowPoints
    }

    fun part2(input: List<String>): Int {
        val heightmap = input.map { row -> row.map { it.digitToInt() } }
        val lastRowIndex = heightmap.lastIndex
        val lastColumnIndex = heightmap[0].lastIndex

        val processed = Array(lastRowIndex + 1) { BooleanArray(lastColumnIndex + 1) }

        fun neighbours(point: Point): List<Point> {
            val (row, column) = point
            val top = if (row > 0) Point(row - 1, column) else null
            val left = if (column > 0) Point(row, column - 1) else null
            val right = if (column < lastColumnIndex) Point(row, column + 1) else null
            val bottom = if (row < lastRowIndex) Point(row + 1, column) else null
            return listOfNotNull(top, left, right, bottom)
        }

        fun fill(row: Int, column: Int): Int {
            var toFill = setOf(Point(row, column))
            var area = 0
            while (toFill.isNotEmpty()) {
                area += toFill.size
                toFill.forEach { processed[it.row][it.column] = true }
                toFill = toFill.flatMap { neighbours(it) }
                    .filter { heightmap[it.row][it.column] != 9 && !processed[it.row][it.column] }.toSet()
            }
            return area
        }

        val areas = mutableListOf<Int>()
        for (row in 0..lastRowIndex) {
            for (column in 0..lastColumnIndex) {
                val value = heightmap[row][column]
                if (value != 9 && !processed[row][column]) {
                    areas.add(fill(row, column))
                }
            }
        }
        return areas.sortedDescending().subList(0, 3).reduce { acc, value ->
            acc * value
        }
    }

    val input = readInput("Day09")
    check(part1(readInput("Day09_test")) == 15)
    check(part2(readInput("Day09_test")) == 1134)

    println(part1(input))
    println(part2(input))
}
