import kotlin.math.abs

data class Day19Position(val x: Int, val y: Int, val z: Int) {
    operator fun minus(other: Day19Position): Day19Position {
        return Day19Position(x - other.x, y - other.y, z - other.z)
    }

    override fun toString(): String {
        return "$x,$y,$z"
    }

    operator fun plus(other: Day19Position): Day19Position {
        return Day19Position(x + other.x, y + other.y, z + other.z)
    }
}

fun main() {

    fun roll(v: Day19Position) = Day19Position(v.x, v.z, -v.y)
    fun turn(v: Day19Position) = Day19Position(-v.y, v.x, v.z)

    fun rotations(v: Day19Position): List<Day19Position> {
        val set = mutableListOf<Day19Position>()

        var value = v
        for (cycle in 0..1) {
            for (step in 0..2) {
                value = roll(value)
                set.add(value)
                for (i in 0..2) {
                    value = turn(value)
                    set.add(value)
                }
            }
            value = roll(turn(roll(value)))
        }
        return set
    }

    fun findBeaconPositions(input: List<String>): Pair<List<Day19Position>, List<Day19Position>> {
        val scanners = mutableListOf<Day19Position>(Day19Position(0, 0, 0))
        val map = mutableMapOf<Int, List<Day19Position>>()
        var list = mutableListOf<Day19Position>()
        for (line in input) {
            if (line.startsWith("---")) {
                val scannerId = line.removePrefix("--- scanner ").removeSuffix(" ---").toInt()
                map[scannerId] = list
            } else if (line.isNotBlank()) {
                list.add(line.split(",").map { it.toInt() }.toPosition())
            } else {
                list = mutableListOf()
            }
        }

        val initialMapSize = map.size
        while (map.size > 1) {
            next@ for (scannerId in 1 until initialMapSize) {
                if (!map.containsKey(scannerId)) {
                    continue@next
                }

                val scanner0DiffsById = mutableMapOf<Int, List<Day19Position>>()
                for (j in 0..map[0]!!.lastIndex) {
                    val diffs = (0..map[0]!!.lastIndex).map { k ->
                        val firstPoint = map[0]!![j]
                        val secondPoint = map[0]!![k]
                        firstPoint - secondPoint
                    }
                    scanner0DiffsById[j] = diffs
                }

                for (j in 0..map[scannerId]!!.lastIndex) {
                    val firstPoint = map[scannerId]!![j]
                    val diffs = (0..map[scannerId]!!.lastIndex).map { k ->
                        val secondPoint = map[scannerId]!![k]
                        firstPoint - secondPoint
                    }
                    val rotatedDiffs = diffs.map { rotations(it) }
                    for (rotation in 0..23) {
                        val rotatedBeaconDiff = rotatedDiffs.map { it[rotation] }

                        for (scannerDiff in scanner0DiffsById) {
                            val intersection = HashSet<Day19Position>(scannerDiff.value)
                            intersection.retainAll(rotatedBeaconDiff.toSet())

                            if (intersection.size >= 12) {
                                val index1 = scannerDiff.value.indexOf(Day19Position(0, 0, 0))
                                val coordinatedInScanner0Space = map[0]!![index1]
                                val index2 = rotatedBeaconDiff.indexOf(Day19Position(0, 0, 0))
                                val rotatedCoordinates = map[scannerId]!!.map { rotations(it) }.map { it[rotation] }
                                val offset = coordinatedInScanner0Space - rotatedCoordinates[index2]
                                scanners.add(offset)
                                val alignedOrientation = rotatedCoordinates.map { it + offset }
                                map[0] = (map[0]!! + alignedOrientation).toSet().toList()
                                map.remove(scannerId)
                                continue@next
                            }
                        }
                    }
                }
            }
        }
        return map[0]!! to scanners
    }

    fun part1(input: List<String>): Int {
        val beacons = findBeaconPositions(input).first
        return beacons.size
    }

    fun part2(input: List<String>): Int {
        val scanners = findBeaconPositions(input).second
        var maxDistance = Int.MIN_VALUE
        for (i in scanners) {
            for (j in scanners) {
                val diff = i - j
                val distance = abs(diff.x) + abs(diff.y) + abs(diff.z)
                if (distance > maxDistance) {
                    maxDistance = distance
                }
            }
        }
        return maxDistance
    }

    check(part1(readInput("Day19_test")) == 79)
    check(part2(readInput("Day19_test")) == 3621)

    val input = readInput("Day19")
    println(part1(input))
    println(part2(input))
}

private fun List<Int>.toPosition(): Day19Position {
    return Day19Position(get(0), get(1), get(2))
}
