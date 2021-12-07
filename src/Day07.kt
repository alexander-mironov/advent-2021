import kotlin.math.abs

fun main() {

    fun part1(input: String): Int {
        val positions = input.split(",").map { it.toInt() }.sorted()
        var minFuelCost = Int.MAX_VALUE
        for (i in positions.first()..positions.last()) {
            val fuelCost = positions.sumOf { abs(it - i) }
            // there is just one minimum to that function
            if (fuelCost > minFuelCost)
                break
            minFuelCost = fuelCost
        }
        return minFuelCost
    }

    fun part2(input: String): Int {
        fun cost(steps: Int): Int {
            return ((1 + steps) * steps / 2.0).toInt()
        }

        val positions = input.split(",").map { it.toInt() }.sorted()
        var minFuelCost = Int.MAX_VALUE
        for (i in positions.first()..positions.last()) {
            val fuelCost = positions.sumOf { cost(abs(it - i)) }
            if (fuelCost > minFuelCost)
                break
            minFuelCost = fuelCost
        }
        return minFuelCost
    }

    val testInput = readInputAsString("Day07_test")
    check(part1(testInput) == 37)
    check(part2(testInput) == 168)

    val input = readInputAsString("Day07")
    println(part1(input))
    println(part2(input))
}
