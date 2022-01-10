val a = listOf(1, 1, 1, 1, 1, 26, 1, 26, 26, 1, 26, 26, 26, 26)
val b = listOf(10, 12, 10, 12, 11, -16, 10, -11, -13, 13, -8, -1, -4, -14)
val c = listOf(12, 7, 8, 8, 15, 12, 8, 13, 3, 13, 3, 9, 4, 13)

fun run(part2: Boolean): Any {
    val result = MutableList(14) { -1 }
    val buffer = ArrayDeque<Pair<Int, Int>>()
    val best = if (part2) 1 else 9
    for (i in 0..13) {
        if (a[i] == 26) {
            val offset = b[i]
            val (lastIndex, lastOffset) = buffer.removeFirst()
            val difference = offset + lastOffset
            if (difference >= 0) {
                result[lastIndex] = if (part2) best else best - difference
                result[i] = if (part2) best + difference else best
            } else {
                result[lastIndex] = if (part2) best - difference else best
                result[i] = if (part2) best else best + difference
            }
        } else buffer.addFirst(i to c[i])
    }

    return result.joinToString("").toLong()
}

fun main() {
    println(run(part2 = false))
    println(run(part2 = true))
}