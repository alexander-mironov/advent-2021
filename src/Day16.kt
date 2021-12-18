const val TYPE_SUM = 0
const val TYPE_PRODUCT = 1
const val TYPE_MINIMUM = 2
const val TYPE_MAXIMUM = 3
const val TYPE_LITERAL = 4
const val TYPE_GREATER_THAN = 5
const val TYPE_LESS_THAN = 6
const val TYPE_EQUAL_TO = 7

sealed class Packet(open val version: Int) {
    data class Literal(override val version: Int, val value: Long) : Packet(version)
    data class Operator(override val version: Int, val packetTypeId: Int, val packets: List<Packet>) :
        Packet(version)

    fun evaluate(): Long {
        return when (this) {
            is Literal -> {
                value
            }
            is Operator -> {
                when (packetTypeId) {
                    TYPE_SUM -> packets.sumOf { it.evaluate() }
                    TYPE_PRODUCT -> packets.fold(1) { acc, packet -> acc * packet.evaluate() }
                    TYPE_MINIMUM -> packets.minOf { it.evaluate() }
                    TYPE_MAXIMUM -> packets.maxOf { it.evaluate() }
                    TYPE_GREATER_THAN -> if (packets[0].evaluate() > packets[1].evaluate()) 1 else 0
                    TYPE_LESS_THAN -> if (packets[0].evaluate() < packets[1].evaluate()) 1 else 0
                    TYPE_EQUAL_TO -> if (packets[0].evaluate() == packets[1].evaluate()) 1 else 0
                    else -> throw IllegalArgumentException("Unknown type: $packetTypeId")
                }
            }
        }
    }
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

    fun part2(input: String): Long {
        val binaryRepresentation = input.map { toBinary[it]!! }.joinToString(separator = "")
        val outermostPacket = parse(binaryRepresentation, InterpreterPosition())
        return outermostPacket.evaluate()
    }

    //part1("D2FE28")
    check(part1("8A004A801A8002F478") == 16)
    check(part1("620080001611562C8802118E34") == 12)
    check(part1("C0015000016115A2E0802F182340") == 23)
    check(part1("A0016C880162017C3686B18A3D4780") == 31)

    check(part2("C200B40A82") == 3L)
    check(part2("04005AC33890") == 54L)
    check(part2("880086C3E88112") == 7L)
    check(part2("CE00C43D881120") == 9L)
    check(part2("D8005AC2A8F0") == 1L)
    check(part2("F600BC2D8F") == 0L)
    check(part2("9C005AC2F8F0") == 0L)
    check(part2("9C0141080250320F1802104A08") == 1L)

    val input = readInputAsString("Day16")
    println(part1(input))
    println(part2(input))
}
