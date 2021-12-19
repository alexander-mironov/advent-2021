sealed class Part(open var parent: Pair?) {
    data class Number(var value: Int, override var parent: Pair?) : Part(parent) {
        override fun toString(): String {
            return value.toString()
        }
    }

    data class Pair(var left: Part, var right: Part, override var parent: Pair?) : Part(parent) {
        override fun toString(): String {
            return "[$left,$right]"
        }
    }

    operator fun plus(number: Part): Part {
        val pair = Pair(this, number, parent = null)
        this.parent = pair
        number.parent = pair
        return pair
    }

    fun magnitude(): Int {
        return when (this) {
            is Number -> {
                value
            }
            is Pair -> {
                3 * left.magnitude() + 2 * right.magnitude()
            }
        }
    }
}

enum class TraverseResult {
    EXPLODED, SPLIT, NO_CHANGE
}

fun main() {

    fun parse(input: String, position: ParserPosition, parent: Part.Pair?): Part {
        var isParsingLeftPart = true
        val fakePart = Part.Number(-1, parent = null)
        val currentPair = Part.Pair(fakePart, fakePart, parent)

        while (position.value < input.length) {
            when (val char = input[position.inc(1)]) {
                ',' -> {
                    isParsingLeftPart = false
                }
                '[' -> {
                    val parsed = parse(input, position, currentPair)
                    if (isParsingLeftPart) {
                        currentPair.left = parsed
                    } else {
                        currentPair.right = parsed
                    }
                }
                ']' -> {
                    return currentPair
                }
                else -> {
                    val number = Part.Number(char.digitToInt(), currentPair)
                    if (isParsingLeftPart) {
                        currentPair.left = number
                    } else {
                        currentPair.right = number
                    }
                }
            }
        }
        return Part.Number(-1, null)
    }


    fun explode(part: Part, depth: Int): TraverseResult {
        fun findRightmost(pair: Part): Part.Number {
            var cur = pair
            while (cur is Part.Pair) {
                cur = cur.right
            }
            return (cur as Part.Number)
        }

        fun findLeftmost(pair: Part): Part.Number {
            var cur = pair

            while (cur is Part.Pair) {
                cur = (cur as Part.Pair).left
            }
            return (cur as Part.Number)
        }

        fun findPrevValue(pair: Part.Pair): Part.Number? {
            var current = pair
            while (current.parent?.left === current) {
                current = current.parent!!
            }
            val parent = current.parent
            if (parent != null) {
                return findRightmost(parent.left)
            }
            return null
        }

        fun findNextValue(pair: Part.Pair): Part.Number? {
            var current = pair
            while (current.parent?.right === current) {
                current = current.parent!!
            }
            val parent = current.parent
            if (parent != null) {
                return findLeftmost(parent.right)
            }
            return null
        }

        if (part is Part.Pair) {
            var traverseResult = explode(part.left, depth + 1)
            if (traverseResult != TraverseResult.NO_CHANGE) {
                return traverseResult
            }
            if (depth >= 4) {
                // println("Explode: $part")
                val prevNumber = findPrevValue(part)
                if (prevNumber != null) {
                    prevNumber.value += (part.left as Part.Number).value
                }
                val nextNumber = findNextValue(part)
                if (nextNumber != null) {
                    nextNumber.value += (part.right as Part.Number).value
                }
                val parent = part.parent
                if (part == parent?.left) {
                    parent.left = Part.Number(0, parent)
                } else if (part == parent?.right) {
                    parent.right = Part.Number(0, parent)
                }
                return TraverseResult.EXPLODED
            }
            traverseResult = explode(part.right, depth + 1)
            if (traverseResult != TraverseResult.NO_CHANGE) {
                return traverseResult
            }
        }
        return TraverseResult.NO_CHANGE
    }

    fun split(part: Part): TraverseResult {
        when (part) {
            is Part.Number -> {
                val parent = part.parent
                if (part.value >= 10) {
                    val left = part.value / 2
                    val right = part.value - left
                    val pair = Part.Pair(Part.Number(left, null), Part.Number(right, null), parent)
                    pair.left.parent = pair
                    pair.right.parent = pair
                    if (part == parent?.left) {
                        parent.left = pair
                    } else if (part == parent?.right) {
                        parent.right = pair
                    }
                    return TraverseResult.SPLIT
                }
            }
            is Part.Pair -> {
                var traverseResult = split(part.left)
                if (traverseResult != TraverseResult.NO_CHANGE) {
                    return traverseResult
                }

                traverseResult = split(part.right)
                if (traverseResult != TraverseResult.NO_CHANGE) {
                    return traverseResult
                }
            }
        }
        return TraverseResult.NO_CHANGE
    }

    fun reduce(part: Part) {
        do {
            do {
                val result = explode(part, 0)
            } while (result != TraverseResult.NO_CHANGE)
            val result = split(part)
        } while (result != TraverseResult.NO_CHANGE)
    }

    fun part1(input: List<String>): Int {
        var prevNumber: Part? = null
        for (line in input) {
            val number = parse(line, ParserPosition(), parent = null)
            prevNumber = if (prevNumber != null) {
                val sum = prevNumber + number
                reduce(sum)
                sum
            } else {
                number
            }
        }
        return prevNumber!!.magnitude()
    }

    fun part2(input: List<String>): Int {
        var maxMagnitude = Int.MIN_VALUE
        for ((index1, line1) in input.withIndex()) {
            for ((index2, line2) in input.withIndex()) {
                if (index1 != index2) {
                    val left = parse(line1, ParserPosition(), parent = null)
                    val right = parse(line2, ParserPosition(), parent = null)
                    val sum = left + right
                    reduce(sum)
                    val magnitude = sum.magnitude()
                    if (magnitude > maxMagnitude) {
                        maxMagnitude = magnitude
                    }
                }
            }
        }
        return maxMagnitude
    }

    check(part1(readInput("Day18_test")) == 4140)
    check(part2(readInput("Day18_test")) == 3993)
    println(part1(readInput("Day18")))
    println(part2(readInput("Day18")))
}
