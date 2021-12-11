import java.util.*

data class Position(val row: Int, val column: Int)

fun main() {

    fun step(field: Array<IntArray>): Int {
        val dimension = field.size - 2
        var flashes = 0
        val toUpdate = LinkedList<Position>()
        for (row in 1..dimension) {
            for (column in 1..dimension) {
                toUpdate.add(Position(row, column))
            }
        }
        while (toUpdate.isNotEmpty()) {
            val (row, column) = toUpdate.removeFirst()
            field[row][column] += 1
            if (field[row][column] == 10 && row in (1..dimension) && column in (1..dimension)) {
                flashes += 1
                toUpdate.add(Position(row - 1, column - 1))
                toUpdate.add(Position(row - 1, column))
                toUpdate.add(Position(row - 1, column + 1))
                toUpdate.add(Position(row, column - 1))
                toUpdate.add(Position(row, column + 1))
                toUpdate.add(Position(row + 1, column - 1))
                toUpdate.add(Position(row + 1, column))
                toUpdate.add(Position(row + 1, column + 1))
            }
        }

        for (row in 1..dimension) {
            for (column in 1..dimension) {
                if (field[row][column] >= 10) {
                    field[row][column] = 0
                }
            }
        }
        return flashes
    }

    fun generateField(input: List<String>): Array<IntArray> {
        val dimension = input.size
        val emptyRow = Array(1) { IntArray(dimension + 2) { 0 } }
        return emptyRow + input.map { line -> intArrayOf(0) + line.map { it.digitToInt() } + intArrayOf(0) }
            .toTypedArray() + emptyRow
    }

    fun part1(input: List<String>): Int {
        val field = generateField(input)
        return (0 until 100).sumOf { step(field) }
    }

    fun part2(input: List<String>): Int {
        val field = generateField(input)
        val totalFields = input.size * input.size
        var flashes: Int
        var step = 0
        do {
            step += 1
            flashes = step(field)
        } while (flashes != totalFields)
        return step
    }

    val input = readInput("Day11")
    check(part1(readInput("Day11_test")) == 1656)
    check(part2(readInput("Day11_test")) == 195)

    println(part1(input))
    println(part2(input))
}
