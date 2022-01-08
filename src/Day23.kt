import java.util.*
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

enum class AmphipodType(val price: Int, val destinationRoomColumn: Int) {
    A(1, 2), B(10, 4), C(100, 6), D(1000, 8)
}

data class Amphipod(val type: AmphipodType, val row: Int, val column: Int) {
    val isInHallway = row == 0

    val isInDestinationRoom = column == type.destinationRoomColumn
}

data class Step(val amphipods: Set<Amphipod>, val score: Int)

fun main() {

    fun getArray(step: Step): Array<Array<Amphipod?>> {
        val array = Array<Array<Amphipod?>>(3) { Array(11) { null } }
        step.amphipods.forEach { amphipod ->
            array[amphipod.row][amphipod.column] = amphipod
        }
        println("Score: ${step.score}")
        for (row in 0..2) {
            for (column in 0..10) {
                val atPoistion = array[row][column]
                if (atPoistion != null) {
                    print(atPoistion.type)
                } else if (row == 0 || column in listOf(2, 4, 6, 8)) {
                    print('.')
                } else {
                    print('#')
                }
            }
            println()
        }
        println()


        return array
    }

    fun isPathClear(
        amphipod: Amphipod,
        destinationColumn: Int,
        array: Array<Array<Amphipod?>>
    ): Boolean {
        val pathStart = min(amphipod.column, destinationColumn)
        val pathEnd = max(amphipod.column, destinationColumn)
        val pathIsClear = (pathStart..pathEnd).all { array[0][it] == null || array[0][it] == amphipod }
        return pathIsClear
    }

    fun part1(initialStep: Step): Int {
        val seen = mutableSetOf<Set<Amphipod>>()

        val priorityQueue = PriorityQueue<Step> { s0, s1 ->
            s0.score - s1.score
        }
        //val priorityQueue = LinkedList<Step>()
        priorityQueue.add(initialStep)

        var bestScore = Int.MAX_VALUE
        while (priorityQueue.isNotEmpty()) {
            val step = priorityQueue.poll()
            if (step.score >= bestScore || seen.contains(step.amphipods)) {
                continue
            }
            seen.add(step.amphipods)

            val array = getArray(step)
            val allInPlace = step.amphipods.all { it.isInDestinationRoom }
            if (allInPlace) {
                bestScore = step.score
                println("Score: ${step.score}")
                continue
            }

            for (amphipod in step.amphipods) {
                if (amphipod.isInDestinationRoom && amphipod.row == 1 && array[2][amphipod.column]?.type == amphipod.type) { // the room is full
                    continue
                } else if (amphipod.isInDestinationRoom && amphipod.row == 2) { // on the bottom of the proper room
                    continue
                } else if (amphipod.isInHallway) { // in hallway can go to it's room only (if it's empty or occupied by a specie of the same color)
                    val destinationColumn = amphipod.type.destinationRoomColumn
                    val isBottomPlaceAvailable = array[1][destinationColumn] == null &&
                            array[2][destinationColumn] == null
                    val isTopPlaceAvailable = array[1][destinationColumn] == null &&
                            array[2][destinationColumn]?.type == amphipod.type
                    val pathIsClear = isPathClear(amphipod, destinationColumn, array)
                    if (pathIsClear && isBottomPlaceAvailable) {
                        val newScore =
                            step.score + amphipod.type.price * (abs(amphipod.column - destinationColumn) + 2)
                        val positions = step.amphipods - amphipod + amphipod.copy(row = 2, column = destinationColumn)
                        val newStep = Step(positions, newScore)
                        if (!priorityQueue.contains(newStep)) {
                            priorityQueue.add(newStep)
                        }
                    } else if (pathIsClear && isTopPlaceAvailable) {
                        val newScore =
                            step.score + amphipod.type.price * (abs(amphipod.column - destinationColumn) + 1)
                        val positions = step.amphipods - amphipod + amphipod.copy(row = 1, column = destinationColumn)
                        val newStep = Step(positions, newScore)
                        if (!priorityQueue.contains(newStep)) {
                            priorityQueue.add(newStep)
                        }
                    }
                } else if (amphipod.row == 2 && array[1][amphipod.column] != null) { // the way is blocked
                    continue
                } else {
                    val potentialColumns = listOf(0, 1, 3, 5, 7, 9, 10)
                    potentialColumns.forEach { column ->
                        if (isPathClear(amphipod, column, array)) {
                            val newScore =
                                step.score + amphipod.type.price * (abs(amphipod.column - column) + amphipod.row)
                            val positions = step.amphipods - amphipod + amphipod.copy(row = 0, column = column)
                            val newStep = Step(positions, newScore)
                            if (!priorityQueue.contains(newStep)) {
                                priorityQueue.add(newStep)
                            }
                        }
                    }
                }
            }


        }

        return bestScore
    }

    fun part2(input: List<Int>): Int {
        return 0
    }

    val testConfig =
        Step(
            setOf(
                Amphipod(AmphipodType.B, 1, 2),
                Amphipod(AmphipodType.A, 2, 2),
                Amphipod(AmphipodType.C, 1, 4),
                Amphipod(AmphipodType.D, 2, 4),
                Amphipod(AmphipodType.B, 1, 6),
                Amphipod(AmphipodType.C, 2, 6),
                Amphipod(AmphipodType.D, 1, 8),
                Amphipod(AmphipodType.A, 2, 8)
            ), 0
        )

    val config =
        Step(
            setOf(
                Amphipod(AmphipodType.D, 1, 8),
                Amphipod(AmphipodType.D, 1, 6),
                Amphipod(AmphipodType.C, 1, 2),
                Amphipod(AmphipodType.B, 2, 2),
                Amphipod(AmphipodType.B, 1, 4),
                Amphipod(AmphipodType.C, 2, 4),
                Amphipod(AmphipodType.A, 2, 6),
                Amphipod(AmphipodType.A, 2, 8)
            ), 0
        )

    //check(part1(testConfig) == 12521)
    println(part1(config))
}
