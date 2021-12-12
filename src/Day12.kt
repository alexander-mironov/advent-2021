fun main() {

    fun parseInput(input: List<String>): Map<String, List<String>> {
        val caves = mutableMapOf<String, MutableList<String>>()
        input.forEach { line ->
            val (cave1, cave2) = line.split("-")
            caves.getOrPut(cave1) { mutableListOf() }.add(cave2)
            caves.getOrPut(cave2) { mutableListOf() }.add(cave1)
        }
        return caves
    }

    fun isSmallCave(caveName: String) = caveName == caveName.lowercase()

    fun part1(input: List<String>): Int {

        fun traverse(caveName: String, caves: Map<String, List<String>>, visitedSmallCaves: Set<String>): Int {
            if (caveName == "end") {
                return 1
            }

            val isSmallCave = isSmallCave(caveName)
            val updatedVisitedSmallCaves = visitedSmallCaves + (if (isSmallCave) setOf(caveName) else emptySet())

            return caves[caveName]!!.filterNot { it in visitedSmallCaves }.sumOf { otherCaveName ->
                traverse(otherCaveName, caves, updatedVisitedSmallCaves)
            }
        }

        val caves = parseInput(input)
        return traverse("start", caves, visitedSmallCaves = setOf("start"))
    }

    fun part2(input: List<String>): Int {
        fun traverse(caveName: String, caves: Map<String, List<String>>, numberOfVisits: Map<String, Int>): Int {
            if (caveName == "end") {
                return 1
            }

            val isSmallCave = caveName == caveName.lowercase()
            val updatedVisitedSmallCaves = if (!isSmallCave)
                numberOfVisits
            else
                numberOfVisits.toMutableMap().apply { merge(caveName, 1, Int::plus) }

            val alreadyVisitedACaveTwice = updatedVisitedSmallCaves.values.any { it == 2 }

            return caves[caveName]!!.filterNot { it == "start" }
                .filter { !isSmallCave(it) || updatedVisitedSmallCaves[it] == null || (updatedVisitedSmallCaves[it] == 1 && !alreadyVisitedACaveTwice) }
                .sumOf { otherCaveName ->
                    traverse(otherCaveName, caves, updatedVisitedSmallCaves)
                }
        }

        val caves = parseInput(input)
        return traverse("start", caves, numberOfVisits = emptyMap())
    }

    val input = readInput("Day12")
    check(part1(readInput("Day12_test1")) == 10)
    check(part1(readInput("Day12_test2")) == 19)
    check(part1(readInput("Day12_test3")) == 226)
    check(part2(readInput("Day12_test1")) == 36)
    check(part2(readInput("Day12_test2")) == 103)
    check(part2(readInput("Day12_test3")) == 3509)

    println(part1(input))
    println(part2(input))
}
