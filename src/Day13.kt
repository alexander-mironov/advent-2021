data class Dot(val x: Int, val y: Int)

data class Fold(val axis: String, val position: Int)

fun main() {
    fun parseInput(input: List<String>): Pair<Set<Dot>, List<Fold>> {
        var firstPart = true
        val dots = mutableSetOf<Dot>()
        val folds = mutableListOf<Fold>()
        for (line in input) {
            if (line.isBlank()) {
                firstPart = false
                continue
            }
            if (firstPart) {
                val (x, y) = line.split(",").map { it.toInt() }
                dots.add(Dot(x, y))
            } else {
                val split = line.removePrefix("fold along ").split("=")
                val axis = split[0]
                val position = split[1].toInt()
                folds.add(Fold(axis, position))
            }
        }
        return dots to folds
    }

    fun applyFold(fold: Fold, dots: Set<Dot>): Set<Dot> {
        return if (fold.axis == "y") {
            dots.map { dot ->
                if (dot.y < fold.position)
                    dot
                else
                    Dot(dot.x, (fold.position + fold.position - dot.y) % fold.position)
            }
        } else {
            dots.map { dot ->
                if (dot.x < fold.position)
                    dot
                else
                    Dot((fold.position + fold.position - dot.x) % fold.position, dot.y)
            }
        }.toSet()
    }

    fun printDots(dots: Set<Dot>) {
        val xMax = dots.maxOf { it.x }
        val yMax = dots.maxOf { it.y }
        for (y in 0..yMax) {
            for (x in 0..xMax) {
                if (dots.contains(Dot(x, y)))
                    print("#")
                else
                    print(".")
            }
            println()
        }
    }

    fun part1(input: List<String>): Int {
        val (dots, folds) = parseInput(input)

        val applyFold = applyFold(folds[0], dots)

        return applyFold.size
    }

    fun part2(input: List<String>) {
        val (dots, folds) = parseInput(input)

        val newDots = folds.fold(dots) { acc, fold ->
            applyFold(fold, acc)
        }
        printDots(newDots)
    }

    val input = readInput("Day13")
    check(part1(readInput("Day13_test")) == 17)

    println(part1(input))
    part2(input)
}
