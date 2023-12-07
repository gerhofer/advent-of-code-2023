fun main(args: Array<String>) {
    println("Part 1: ${Day1.solvePart1()}")
    println("Part 2: ${Day1.solvePart2()}")
}

object Day1 {
    fun solvePart1(): Int {
        val input = Day1::class.java.getResource("day1.txt")?.readText() ?: error("Can't read input")
        val lines = input.split("\n").filter { it.isNotBlank() }
        return lines.sumOf { line ->
            val firstDigit = line.first { letter -> letter.isDigit() }
            val lastDigit = line.last { letter -> letter.isDigit() }
            (firstDigit.toString() + lastDigit.toString()).toInt()
        }
    }

    val digits: List<String> = listOf(
        "0",
        "1",
        "2",
        "3",
        "4",
        "5",
        "6",
        "7",
        "8",
        "9",
        "one",
        "two",
        "three",
        "four",
        "five",
        "six",
        "seven",
        "eight",
        "nine")

    fun solvePart2(): Int {
        val input = Day1::class.java.getResource("day1.txt")?.readText() ?: error("Can't read input")
        val lines = input.split("\n").filter { it.isNotBlank() }
        return lines.sumOf { line ->
            val firstDigit = digits.filter { line.contains(it) }.minBy { line.indexOf(it) }
            val lastDigit = digits.maxBy { line.lastIndexOf(it) }
            (toDigitString(firstDigit) + toDigitString(lastDigit)).toInt()
        }
    }

    fun toDigitString(string: String): String {
        return when(string) {
            "zero" -> "0"
            "one" -> "1"
            "two" -> "2"
            "three" -> "3"
            "four" -> "4"
            "five" -> "5"
            "six" -> "6"
            "seven" -> "7"
            "eight" -> "8"
            "nine" -> "9"
            else -> string
        }
    }
}