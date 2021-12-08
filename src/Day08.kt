enum class Segment {
    A, B, C, D, E, F, G
}

fun main() {

    fun part1(input: List<String>): Int {
        val sectors = setOf(2, 3, 4, 7)
        return input.sumOf { line ->
            val digitsOnRightSide = line.trim().split(" | ")[1].split(" ")
            digitsOnRightSide.count { it.length in sectors }
        }
    }

    fun part2(input: List<String>): Int {
        val zero = setOf(Segment.A, Segment.B, Segment.C, Segment.E, Segment.F, Segment.G)
        val one = setOf(Segment.C, Segment.G)
        val two = setOf(Segment.A, Segment.C, Segment.D, Segment.E, Segment.F)
        val three = setOf(Segment.A, Segment.C, Segment.D, Segment.G, Segment.F)
        val four = setOf(Segment.B, Segment.C, Segment.D, Segment.G)
        val five = setOf(Segment.A, Segment.B, Segment.D, Segment.G, Segment.F)
        val six = setOf(Segment.A, Segment.B, Segment.D, Segment.E, Segment.G, Segment.F)
        val seven = setOf(Segment.A, Segment.C, Segment.G)
        val eight = setOf(Segment.A, Segment.B, Segment.C, Segment.D, Segment.E, Segment.F, Segment.G)
        val nine = setOf(Segment.A, Segment.B, Segment.C, Segment.D, Segment.F, Segment.G)
        val segmentsToNumbers = mapOf(
            zero to 0, one to 1, two to 2, three to 3, four to 4,
            five to 5, six to 6, seven to 7, eight to 8, nine to 9
        )

        fun mapPatternToSegments(it: String, mapping: Map<Char, Segment>) = it.map { char -> mapping[char]!! }.toSet()

        fun loop(
            allSignalPatterns: List<String>,
            significantPatterns: List<String>,
            permutation: List<Segment>,
            availableSegments: List<Segment>
        ): Int {
            if (availableSegments.isEmpty()) {
                val mapping = "abcdefg".associateWith { permutation[it - 'a'] }
                if (allSignalPatterns.all { mapPatternToSegments(it, mapping) in segmentsToNumbers.keys }) {
                    return significantPatterns.fold(0) { acc, pattern ->
                        acc * 10 + segmentsToNumbers[mapPatternToSegments(pattern, mapping)]!!
                    }
                }
            }

            for (segment in availableSegments) {
                val result = loop(
                    allSignalPatterns,
                    significantPatterns,
                    permutation = permutation + segment,
                    availableSegments = availableSegments - segment
                )
                if (result >= 0) {
                    return result
                }
            }
            return -1
        }

        return input.sumOf { line ->
            val (left, right) = line.trim().split(" | ")
            val significantPatterns = right.split(" ")
            val signalPatterns = left.split(" ") + significantPatterns
            loop(signalPatterns, significantPatterns, listOf(), Segment.values().toList())
        }
    }


    val testInput = readInput("Day08_test")
    check(part1(testInput) == 26)
    check(part2(testInput) == 61229)

    val input = readInput("Day08")
    println(part1(input))
    println(part2(input))
}
