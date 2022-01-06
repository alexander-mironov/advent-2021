data class Cuboid(val on: Boolean, val x0: Int, val x1: Int, val y0: Int, val y1: Int, val z0: Int, val z1: Int)

fun main() {
    fun processInput(input: List<String>): List<Cuboid> {
        return input.map { line ->
            val split = line.replace(" x=", " ").replace(",y=", " ").replace(",z=", " ").replace("..", " ").split(" ")
            val on = split[0] == "on"
            Cuboid(
                on,
                split[1].toInt(),
                split[2].toInt(),
                split[3].toInt(),
                split[4].toInt(),
                split[5].toInt(),
                split[6].toInt()
            )
        }
    }

    fun part1(input: List<String>): Int {
        val cuboids = processInput(input).filter { cuboid ->
            listOf(cuboid.x0, cuboid.x1, cuboid.y0, cuboid.y1, cuboid.z0, cuboid.z1).all { it in -50..50 }
        }
        val area = Array(101) { Array(101) { BooleanArray(101) } }

        for (cuboid in cuboids) {
            for (x in cuboid.x0..cuboid.x1) {
                for (y in cuboid.y0..cuboid.y1) {
                    for (z in cuboid.z0..cuboid.z1) {
                        area[x + 50][y + 50][z + 50] = cuboid.on
                    }
                }
            }
        }

        var count = 0
        for (x in 0..100) {
            for (y in 0..100) {
                for (z in 0..100) {
                    if (area[x][y][z]) {
                        count += 1
                    }
                }
            }
        }


        return count
    }

    fun part2(input: List<String>): Long {
        val cuboids = processInput(input)
        val uniqueXs = mutableSetOf<Int>()
        val uniqueYs = mutableSetOf<Int>()
        val uniqueZs = mutableSetOf<Int>()
        cuboids.forEach { cuboid ->
            uniqueXs.add(cuboid.x0)
            uniqueXs.add(cuboid.x1 + 1)
            uniqueYs.add(cuboid.y0)
            uniqueYs.add(cuboid.y1 + 1)
            uniqueZs.add(cuboid.z0)
            uniqueZs.add(cuboid.z1 + 1)
        }
        val xs = uniqueXs.sorted()
        val ys = uniqueYs.sorted()
        val zs = uniqueZs.sorted()
        val area = Array(xs.size) { Array(ys.size) { BooleanArray(zs.size) } }
        cuboids.forEach { cuboid ->
            for (x in xs.indexOf(cuboid.x0) until xs.indexOf(cuboid.x1 + 1)) {
                for (y in ys.indexOf(cuboid.y0) until ys.indexOf(cuboid.y1 + 1)) {
                    for (z in zs.indexOf(cuboid.z0) until zs.indexOf(cuboid.z1 + 1)) {
                        area[x][y][z] = cuboid.on
                    }
                }
            }
        }

        var sum = 0L
        for (xIndex in xs.indices) {
            for (yIndex in ys.indices) {
                for (zIndex in zs.indices) {
                    if (area[xIndex][yIndex][zIndex]) {
                        val xDiff = xs[xIndex + 1] - xs[xIndex]
                        val yDiff = ys[yIndex + 1] - ys[yIndex]
                        val zDiff = zs[zIndex + 1] - zs[zIndex]
                        val areaSum = xDiff.toLong() * yDiff * zDiff
                        sum += areaSum
                    }
                }
            }
        }

        return sum
    }

    check(part1(readInput("Day22_test1")) == 590784)
    check(part1(readInput("Day22_test2")) == 474140)
    check(part2(readInput("Day22_test2")) == 2758514936282235L)

    val input = readInput("Day22")
    println(part1(input))
    println(part2(input))
}
