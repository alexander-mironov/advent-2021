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

    fun getArray(step: Step, rows: Int = 3): Array<Array<Amphipod?>> {
        val array = Array<Array<Amphipod?>>(rows) { Array(11) { null } }
        step.amphipods.forEach { amphipod ->
            array[amphipod.row][amphipod.column] = amphipod
        }
//        println("Score: ${step.score}")
//        for (row in 0 until rows) {
//            for (column in 0..10) {
//                val atPoistion = array[row][column]
//                if (atPoistion != null) {
//                    print(atPoistion.type)
//                } else if (row == 0 || column in listOf(2, 4, 6, 8)) {
//                    print('.')
//                } else {
//                    print('#')
//                }
//            }
//            println()
//        }
//        println()

        return array
    }

    fun isHallwayPathClear(
        amphipod: Amphipod,
        destinationColumn: Int,
        array: Array<Array<Amphipod?>>
    ): Boolean {
        val pathStart = min(amphipod.column, destinationColumn)
        val pathEnd = max(amphipod.column, destinationColumn)
        val pathIsClear = (pathStart..pathEnd).all { array[0][it] == null || array[0][it] == amphipod }
        return pathIsClear
    }

    fun findEnergy(initialStep: Step): Int {
        val bigRoom = initialStep.amphipods.size > 8
        val lastRow = if (bigRoom) 4 else 2
        val seen = mutableSetOf<Set<Amphipod>>()

        val priorityQueue = PriorityQueue<Step> { s0, s1 ->
            s0.score - s1.score
        }
        //val priorityQueue = LinkedList<Step>()
        priorityQueue.add(initialStep)

        var bestScore = Int.MAX_VALUE
        while (priorityQueue.isNotEmpty()) {
            val step = priorityQueue.poll()
            seen.add(step.amphipods)

            val array = getArray(step, rows = lastRow + 1)
            val allInPlace = step.amphipods.all { it.isInDestinationRoom }
            if (allInPlace) {
                bestScore = step.score
                println("Score: ${step.score}")
                break
            }

            for (amphipod in step.amphipods) {
                val allTheSameTypeInDestinationRoom =
                    (1..lastRow).all { array[it][amphipod.type.destinationRoomColumn]?.type == amphipod.type || array[it][amphipod.type.destinationRoomColumn]?.type == null }
                val isTopEmpty = (1 until amphipod.row).all { array[it][amphipod.column] == null }
                if (amphipod.isInDestinationRoom && allTheSameTypeInDestinationRoom) { // the room is filled with same type creatures
                    continue
                } else if (amphipod.isInHallway) { // in hallway can go to it's room only (if it's empty or occupied by a specie of the same color)
                    val destinationColumn = amphipod.type.destinationRoomColumn
                    val pathIsClear = isHallwayPathClear(amphipod, destinationColumn, array)
                    if (pathIsClear && allTheSameTypeInDestinationRoom) {
                        val lastEmpty = (1..lastRow).last { array[it][amphipod.type.destinationRoomColumn] == null }
                        val newScore =
                            step.score + amphipod.type.price * (abs(amphipod.column - destinationColumn) + lastEmpty)
                        val positions =
                            step.amphipods - amphipod + amphipod.copy(row = lastEmpty, column = destinationColumn)
                        val newStep = Step(positions, newScore)
                        if (!priorityQueue.contains(newStep) && !seen.contains(newStep.amphipods)) {
                            priorityQueue.add(newStep)
                        }
                    }
                } else if (amphipod.row >= 2 && !isTopEmpty) { // the way is blocked
                    continue
                } else {
                    val potentialColumns = listOf(0, 1, 3, 5, 7, 9, 10)
                    potentialColumns.forEach { column ->
                        if (isHallwayPathClear(amphipod, column, array)) {
                            val newScore =
                                step.score + amphipod.type.price * (abs(amphipod.column - column) + amphipod.row)
                            val positions = step.amphipods - amphipod + amphipod.copy(row = 0, column = column)
                            val newStep = Step(positions, newScore)
                            if (!priorityQueue.contains(newStep) && !seen.contains(newStep.amphipods)) {
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

    val testConfig2 =
        Step(
            setOf(
                Amphipod(AmphipodType.B, 1, 2),
                Amphipod(AmphipodType.D, 2, 2),
                Amphipod(AmphipodType.D, 3, 2),
                Amphipod(AmphipodType.A, 4, 2),
                Amphipod(AmphipodType.C, 1, 4),
                Amphipod(AmphipodType.C, 2, 4),
                Amphipod(AmphipodType.B, 3, 4),
                Amphipod(AmphipodType.D, 4, 4),
                Amphipod(AmphipodType.B, 1, 6),
                Amphipod(AmphipodType.B, 2, 6),
                Amphipod(AmphipodType.A, 3, 6),
                Amphipod(AmphipodType.C, 4, 6),
                Amphipod(AmphipodType.D, 1, 8),
                Amphipod(AmphipodType.A, 2, 8),
                Amphipod(AmphipodType.C, 3, 8),
                Amphipod(AmphipodType.A, 4, 8)
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

    val config2 =
        Step(
            setOf(
                Amphipod(AmphipodType.C, 1, 2),
                Amphipod(AmphipodType.D, 2, 2),
                Amphipod(AmphipodType.D, 3, 2),
                Amphipod(AmphipodType.B, 4, 2),
                Amphipod(AmphipodType.B, 1, 4),
                Amphipod(AmphipodType.C, 2, 4),
                Amphipod(AmphipodType.B, 3, 4),
                Amphipod(AmphipodType.C, 4, 4),
                Amphipod(AmphipodType.D, 1, 6),
                Amphipod(AmphipodType.B, 2, 6),
                Amphipod(AmphipodType.A, 3, 6),
                Amphipod(AmphipodType.A, 4, 6),
                Amphipod(AmphipodType.D, 1, 8),
                Amphipod(AmphipodType.A, 2, 8),
                Amphipod(AmphipodType.C, 3, 8),
                Amphipod(AmphipodType.A, 4, 8)
            ), 0
        )

    // check(part1(testConfig) == 12521)
    //check(part1(testConfig2) == 44169)
    //println(part1(config))
    println(findEnergy(config2))
}
