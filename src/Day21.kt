import kotlin.math.max

data class Params(val pA: Int, val pB: Int, val sA: Int, val sB: Int)

data class Outcome(val pAScore: Long, val pBScore: Long)

fun main() {
    fun part1(player1PositionOriginal: Int, player2PositionOriginal: Int): Int {

        var player1Score = 0
        var player2Score = 0
        var player1Position = player1PositionOriginal - 1
        var player2Position = player2PositionOriginal - 1
        var diceValue = 2
        while (true) {
            player1Position = (player1Position + diceValue * 3) % 10
            player1Score += (player1Position + 1)
            if (player1Score >= 1000) {
                return player2Score * (diceValue + 1)
            }
            diceValue += 3
            player2Position = (player2Position + diceValue * 3) % 10
            player2Score += (player2Position + 1)
            if (player2Score >= 1000) {
                return player1Score * (diceValue + 1)
            }
            diceValue += 3
        }
    }

    fun part2(player1PositionOriginal: Int, player2PositionOriginal: Int): Long {
        val cache = mutableMapOf<Params, Outcome>()
        fun solve(pA: Int, pB: Int, sA: Int, sB: Int): Outcome {
            if (sA >= 21) {
                return Outcome(1, 0)
            }
            if (sB >= 21) {
                return Outcome(0, 1)
            }
            val params = Params(pA, pB, sA, sB)
            if (cache.containsKey(params)) {
                return cache[params]!!
            }
            var answer = Outcome(0, 0)
            for (d1 in (1..3)) {
                for (d2 in (1..3)) {
                    for (d3 in (1..3)) {
                        val newPA = (pA + d1 + d2 + d3) % 10
                        val newSA = sA + newPA + 1

                        val (win1, win2) = solve(pB, newPA, sB, newSA)
                        answer = Outcome(answer.pAScore + win2, answer.pBScore + win1)
                    }
                }
            }
            cache[params] = answer
            return answer
        }


        val solution = solve(player1PositionOriginal - 1, player2PositionOriginal - 1, 0, 0)
        return max(solution.pAScore, solution.pBScore)
    }

    check(part1(4, 8) == 739785)
    println(part1(7, 4))

    check(part2(4, 8) == 444356092776315L)
    println(part2(7, 4))
}