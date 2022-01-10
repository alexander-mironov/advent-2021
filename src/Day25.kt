enum class Type(val char: Char) {
    EMPTY('.'), EAST('>'), SOUTH('v');

    companion object {
        fun from(char: Char): Type {
            return values().first { it.char == char }
        }
    }
}

fun main() {
    fun display(field: Array<Array<Type>>) {
        for (row in 0..field.lastIndex) {
            for (column in 0..field[0].lastIndex) {
                print(field[row][column].char)
            }
            println()
        }
    }

    fun part1(input: List<String>): Int {
        val field = input.map { it.map(Type::from).toTypedArray() }.toTypedArray()

        val rows = field.size
        val columns = field[0].size


        var iteration = 0
        do {
            iteration += 1
            val firstColumn = field.map { it[0] }.toList()

            var changes = false
            for (row in 0 until rows) {
                var column = 0
                while (column < columns) {
                    if (field[row][column] == Type.EAST && ((column + 1 < columns && field[row][column + 1] == Type.EMPTY) || (column + 1 == columns && field[row][(column + 1) % columns] == Type.EMPTY && firstColumn[row] == Type.EMPTY))) {
                        field[row][column] = Type.EMPTY
                        field[row][(column + 1) % columns] = Type.EAST
                        column += 2
                        changes = true
                    } else {
                        column += 1
                    }
                }
            }

            val firstRow = field[0].toList()
            for (column in 0 until columns) {
                var row = 0
                while (row < rows) {
                    if (field[row][column] == Type.SOUTH && ((row + 1 < rows && field[row + 1][column] == Type.EMPTY) || (row + 1 == rows && field[(row + 1) % rows][column] == Type.EMPTY && firstRow[column] == Type.EMPTY))) {
                        field[row][column] = Type.EMPTY
                        field[(row + 1) % rows][column] = Type.SOUTH
                        row += 2
                        changes = true
                    } else {
                        row += 1
                    }
                }
            }
            //println("After $iteration steps")
            //display(field)
            //println()

        } while (changes)

        //display(field)

        return iteration
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    check(part1(readInput("Day25_test")) == 58)
    val input = readInput("Day25")
    println(part1(input))
    println(part2(input))
}