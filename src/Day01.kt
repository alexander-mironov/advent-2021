fun main() {
    fun part1(input: List<Int>): Int {
        var increments = 0
        for (i in 1..input.lastIndex) {
            if (input[i] > input[i - 1]) {
                increments += 1
            }
        }
        return increments
    }

    fun part2(input: List<Int>): Int {
        var increments = 0
        for (i in 3..input.lastIndex) {
            if (input[i] > input[i - 3]) {
                increments += 1
            }
        }
        return increments
    }

    val input = readInput("Day01").map { it.toInt() }
    println(part1(input))
    println(part2(input))
}
