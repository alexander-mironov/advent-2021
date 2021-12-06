fun main() {

    fun iterate(input: String, days: Int): Long {
        val inputList = input.split(",").map { it.toInt() }
        var state = LongArray(9)
        for (i in inputList) {
            state[i] += 1L
        }

        for (i in 0 until days) {
            val newState = LongArray(9)
            state.forEachIndexed { index, count ->
                if (index == 0) {
                    newState[6] += count
                    newState[8] += count
                } else {
                    newState[index - 1] += count
                }
            }
            state = newState
        }
        return state.sum()
    }

    check(iterate(readInputAsString("Day06_test"), days = 18) == 26L)
    check(iterate(readInputAsString("Day06_test"), days = 80) == 5934L)
    check(iterate(readInputAsString("Day06_test"), days = 256) == 26984457539L)

    val input = readInputAsString("Day06")
    println(iterate(input, days = 80))
    println(iterate(input, days = 256))
}
