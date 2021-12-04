import kotlin.math.pow

fun main() {
    fun part1(input: List<String>): Int {
        val bitCount = input[0].length
        val maxValue = 2.0.pow(bitCount).toInt() - 1
        var gammaRateString = ""
        for (i in 0 until bitCount) {
            val mostFrequent = input.map { it[i] }.groupingBy { it }.eachCount().maxByOrNull { it.value }!!
            gammaRateString += mostFrequent.key
        }
        val gammaRate = gammaRateString.toInt(2)

        return gammaRate * (maxValue - gammaRate)
    }


    fun part2(input: List<String>): Int {
        fun selectCommonOrDefault(frequencies: Map<Char, Int>, common: Common): Char {
            return if (common == Common.MOST_COMMON) {
                if (frequencies['0'] == frequencies['1']) {
                    '1'
                } else {
                    frequencies.maxByOrNull { it.value }!!.key
                }
            } else {
                if (frequencies['0'] == frequencies['1']) {
                    '0'
                } else {
                    frequencies.minByOrNull { it.value }!!.key
                }
            }
        }

        fun getRate(input: List<String>, common: Common): Int {
            val bitCount = input[0].length
            var pool = input.toMutableList()
            var rate = 0
            for (i in 0 until bitCount) {
                val frequencies = pool.map { it[i] }.groupingBy { it }.eachCount()
                val mostCommon = selectCommonOrDefault(frequencies, common)
                pool = pool.filter { it[i] == mostCommon }.toMutableList()
                if (pool.size == 1) {
                    rate = pool[0].toInt(radix = 2)
                    break
                }
            }
            return rate
        }

        val oxygenGeneratorRate = getRate(input, Common.MOST_COMMON)
        val co2ScrubberRate = getRate(input, Common.LEAST_COMMON)

        return oxygenGeneratorRate * co2ScrubberRate
    }

    val test = readInput("Day03_test")
    check(part1(test) == 198)
    check(part2(test) == 230)
    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}

enum class Common {
    MOST_COMMON, LEAST_COMMON
}
