sealed class Packet(open val version: Int) {
    data class Literal(override val version: Int, val value: Long) : Packet(version)
    data class Operator(override val version: Int, val packetTypeId: Int, val packets: List<Packet>) :
        Packet(version)
}

class InterpreterPosition {
    var value: Int = 0
        private set

    fun inc(value: Int) {
        this.value += value
    }

    operator fun plusAssign(value: Int) {
        inc(value)
    }

    operator fun plus(value: Int): Int {
        return this.value + value
    }

}

const val TYPE_LITERAL = 4

fun main() {

    val toBinary = mapOf(
        '0' to "0000",
        '1' to "0001",
        '2' to "0010",
        '3' to "0011",
        '4' to "0100",
        '5' to "0101",
        '6' to "0110",
        '7' to "0111",
        '8' to "1000",
        '9' to "1001",
        'A' to "1010",
        'B' to "1011",
        'C' to "1100",
        'D' to "1101",
        'E' to "1110",
        'F' to "1111"
    )

    fun sumOfVersions(packet: Packet): Int {
        return when (packet) {
            is Packet.Literal -> {
                packet.version
            }
            is Packet.Operator -> {
                packet.version + packet.packets.sumOf { sumOfVersions(it) }
            }
        }
    }

    fun parseLiteralValue(input: String, position: InterpreterPosition): Long {
        val lastPart = '0'
        var flag: Char
        val value = buildString {
            do {
                flag = input[position.value]
                position += 1
                append(input.substring(position.value, position + 4))
                position += 4
            } while (flag != lastPart)
        }.toLong(radix = 2)
        return value
    }

    fun parse(input: String, position: InterpreterPosition): Packet {
        val version = input.substring(position.value, position + 3).toInt(radix = 2)
        position += 3
        val packetTypeId = input.substring(position.value, position + 3).toInt(radix = 2)
        position += 3
        if (packetTypeId == TYPE_LITERAL) {
            val value = parseLiteralValue(input, position)
            return Packet.Literal(version, value)
        } else {
            val lengthTypeId = input[position.value]
            position += 1
            if (lengthTypeId == '0') {
                val lengthInBits = input.substring(position.value, position + 15).toInt(radix = 2)
                position += 15
                val currentPosition = position.value
                val packets = mutableListOf<Packet>()
                while (position.value - currentPosition < lengthInBits) {
                    val packet = parse(input, position)
                    packets.add(packet)
                }
                return Packet.Operator(version, packetTypeId, packets)
            } else {
                val numberOfPackets = input.substring(position.value, position + 11).toInt(radix = 2)
                position += 11
                val packets = (0 until numberOfPackets).map { parse(input, position) }
                return Packet.Operator(version, packetTypeId, packets)
            }
        }
    }

    fun part1(input: String): Int {
        val binaryRepresentation = input.map { toBinary[it]!! }.joinToString(separator = "")
        val outermostPacket = parse(binaryRepresentation, InterpreterPosition())
        return sumOfVersions(outermostPacket)
    }

    fun part2(input: String): Int {
        return 0
    }

    //part1("D2FE28")
    check(part1("8A004A801A8002F478") == 16)
    check(part1("620080001611562C8802118E34") == 12)
    check(part1("C0015000016115A2E0802F182340") == 23)
    check(part1("A0016C880162017C3686B18A3D4780") == 31)

    val input = readInputAsString("Day16")
    println(part1(input))
    println(part2(input))
}
