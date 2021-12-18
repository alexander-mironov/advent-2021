import kotlin.math.max

fun main() {

    fun part1(input: String): Int {
        val (minX, maxX, minY, maxY) = input.removePrefix("target area: x=").split("..", ", y=").map(String::toInt)

        var maxPositionY = Int.MIN_VALUE
        for (initialVelocityX in 2..200) {
            for (initialVelocityY in -200..300) {
                var velocityX = initialVelocityX
                var velocityY = initialVelocityY
                var positionX = 0
                var positionY = 0
                var currentAttemptMaxPositionY = Int.MIN_VALUE
                for (step in 0..1000) {
                    positionX += velocityX
                    positionY += velocityY
                    if (positionY > currentAttemptMaxPositionY) {
                        currentAttemptMaxPositionY = positionY
                    }
                    if (positionX in minX..maxX && positionY in minY..maxY) {
                        if (currentAttemptMaxPositionY > maxPositionY) {
                            maxPositionY = currentAttemptMaxPositionY
                            break
                        }
                    }
                    velocityX = max(0, velocityX - 1)
                    velocityY -= 1
                }
            }
        }

        return maxPositionY
    }

    fun part2(input: String): Int {
        val (minX, maxX, minY, maxY) = input.removePrefix("target area: x=").split("..", ", y=").map(String::toInt)

        var success = 0
        for (initialVelocityX in 2..200) {
            for (initialVelocityY in -200..300) {
                var velocityX = initialVelocityX
                var velocityY = initialVelocityY
                var positionX = 0
                var positionY = 0
                for (step in 0..1000) {
                    positionX += velocityX
                    positionY += velocityY
                    if (positionX in minX..maxX && positionY in minY..maxY) {
                        success += 1
                        break
                    }
                    velocityX = max(0, velocityX - 1)
                    velocityY -= 1
                }
            }
        }

        return success
    }

    check(part1("target area: x=20..30, y=-10..-5") == 45)
    check(part2("target area: x=20..30, y=-10..-5") == 112)

    val input = "target area: x=124..174, y=-123..-86"
    println(part1(input))
    println(part2(input))
}
