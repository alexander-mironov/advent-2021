fun main() {

    fun part1(input: List<String>): Int {
        var template = input[0]

        val rules = (2..input.lastIndex).associate { lineIndex ->
            val (pair, elementToInsert) = input[lineIndex].split(" -> ")
            pair to elementToInsert
        }

        for (i in 1..10) {
            val updates = (0 until template.lastIndex).map { j ->
                rules[template.substring(j, j + 2)]
            }
            template = buildString {
                for (j in 0 until template.lastIndex) {
                    append(template[j] + updates[j]!!)
                }
                append(template.last())
            }
        }
        val result = template.groupingBy { it }.eachCount()
        return result.maxOf { it.value } - result.minOf { it.value }
    }

    fun part2(input: List<String>): Long {
        val template = input[0]

        val rules = (2..input.lastIndex).associate { lineIndex ->
            val (pair, elementToInsert) = input[lineIndex].split(" -> ")
            pair to elementToInsert
        }

        var pairs = mutableMapOf<String, Long>()
        (0 until template.lastIndex).map { index ->
            val substring = template.substring(index, index + 2)
            pairs[substring] = pairs.getOrDefault(substring, 0L) + 1
        }

        for (i in 1..40) {
            val newPairs = mutableMapOf<String, Long>()
            for (pair in pairs) {
                val charToInsert = rules[pair.key]!!
                newPairs[pair.key[0] + charToInsert] = newPairs.getOrDefault(pair.key[0] + charToInsert, 0) + pair.value
                newPairs[charToInsert + pair.key[1]] = newPairs.getOrDefault(charToInsert + pair.key[1], 0) + pair.value
            }
            pairs = newPairs
        }

        val count = mutableMapOf<Char, Long>()
        for (pair in pairs) {
            count[pair.key[0]] = count.getOrDefault(pair.key[0], 0) + pair.value
            count[pair.key[1]] = count.getOrDefault(pair.key[1], 0) + pair.value
        }

        count[template.first()] = count.getOrDefault(template.first(), 0) + 1
        count[template.last()] = count.getOrDefault(template.last(), 0) + 1

        return (count.maxOf { it.value } - count.minOf { it.value }) / 2
    }

    val input = readInput("Day14")
    check(part1(readInput("Day14_test")) == 1588)
    check(part2(readInput("Day14_test")) == 2188189693529L)

    println(part1(input))
    println(part2(input))
}
