fun main() {

    class BingoCard {

        val contents = Array(5) { IntArray(5) }

        fun addRowContents(rowId: Int, row: List<Int>) {
            row.forEachIndexed { index, value ->
                contents[rowId][index] = value
            }
        }

        fun mark(number: Int) {
            for (row in 0..4) {
                for (column in 0..4) {
                    if (contents[row][column] == number) {
                        contents[row][column] = -1
                    }
                }
            }
        }

        fun completed(): Boolean {
            for (row in 0..4) {
                var completed = true
                for (column in 0..4) {
                    if (contents[row][column] != -1) {
                        completed = false
                        break
                    }
                }
                if (completed) {
                    return true
                }
            }

            for (column in 0..4) {
                var completed = true
                for (row in 0..4) {
                    if (contents[row][column] != -1) {
                        completed = false
                        break
                    }
                }
                if (completed) {
                    return true
                }
            }

            return false
        }

        fun sumOfUnmarked(): Int {
            var sum = 0
            for (row in 0..4) {
                for (column in 0..4) {
                    if (contents[row][column] != -1) {
                        sum += contents[row][column]
                    }
                }
            }
            return sum
        }
    }

    fun parseInput(input: List<String>): Pair<List<Int>, MutableList<BingoCard>> {
        val numbersToDraw = input[0].split(",").map { it.toInt() }
        var row = 0
        val splitRegex = "\\s+".toRegex()
        val bingoCards = mutableListOf<BingoCard>()
        var bingoCard = BingoCard()
        for (i in 2..input.lastIndex) {
            if (row == 5) {
                row = 0
                continue
            }
            val split = input[i].trim().split(splitRegex).map { it.toInt() }
            bingoCard.addRowContents(row, split)
            if (row == 4) {
                bingoCards.add(bingoCard)
                bingoCard = BingoCard()
            }
            row += 1
        }
        return Pair(numbersToDraw, bingoCards)
    }

    fun part1(input: List<String>): Int {
        val (numbersToDraw, bingoCards) = parseInput(input)

        for (number in numbersToDraw) {
            for (card in bingoCards) {
                card.mark(number)
                if (card.completed()) {
                    return card.sumOfUnmarked() * number
                }
            }
        }
        return 0
    }

    fun part2(input: List<String>): Int {
        val (numbersToDraw, bingoCards) = parseInput(input)

        val bingoCardsPool = bingoCards.toMutableList()

        for (number in numbersToDraw) {
            val iterator = bingoCardsPool.iterator()
            while (iterator.hasNext()) {
                val card = iterator.next()
                card.mark(number)
                if (card.completed()) {
                    iterator.remove()
                    if (bingoCardsPool.isEmpty()) {
                        return card.sumOfUnmarked() * number
                    }
                }
            }
        }
        return 0
    }

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}
