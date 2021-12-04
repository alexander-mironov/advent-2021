fun main() {
    fun part1(input: List<String>): Long {
        var horizontalPosition = 0L
        var depth = 0L
        for (statement in input) {
            val (command, strValue) = statement.split(" ")
            val value = strValue.toInt()
            when (command) {
                "forward" -> horizontalPosition += value
                "up" -> depth -= value
                "down" -> depth += value
            }
        }
        return horizontalPosition * depth
    }

    fun part2(input: List<String>): Long {
        var horizontalPosition = 0L
        var depth = 0L
        var aim = 0L
        for (statement in input) {
            val (command, strValue) = statement.split(" ")
            val value = strValue.toInt()
            when (command) {
                "forward" -> {
                    horizontalPosition += value
                    depth += (aim * value)
                }
                "up" -> aim -= value
                "down" -> aim += value
            }
        }
        return horizontalPosition * depth
    }

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}
