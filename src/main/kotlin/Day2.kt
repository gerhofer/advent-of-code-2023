fun main(args: Array<String>) {
    println("Part 1: ${Day2.solvePart1()}")
    println("Part 2: ${Day2.solvePart2()}")
}

object Day2 {
    fun solvePart1(): Long {
        val input = Day1::class.java.getResource("day2.txt")?.readText() ?: error("Can't read input")
        return input.split("\n").filter { it.isNotBlank() }
            .map { it.toGame() }
            .filter { it.maxReds() <= 12 && it.maxBlues() <= 14 && it.maxGreens() <= 13 }
            .sumOf { it.id }
    }

    fun solvePart2(): Long {
        val input = Day1::class.java.getResource("day2.txt")?.readText() ?: error("Can't read input")
        return input.split("\n").filter { it.isNotBlank() }
            .map { it.toGame() }
            .sumOf { (it.maxReds() * it.maxBlues() * it.maxGreens()).toLong() }
    }

    fun String.toGame(): Game {
        val (idPart, contentPart) = this.split(": ")
        val id = idPart.substringAfter(" ").trim().toLong()
        val rounds = contentPart.split(";")
            .map { set ->
                val colourAndAmount = set.split(",")
                val red = getColourAmount(colourAndAmount, "red")
                val blue = getColourAmount(colourAndAmount, "blue")
                val green = getColourAmount(colourAndAmount, "green")
                Round(red, green, blue)
            }
        return Game(id, rounds.toSet())
    }

    private fun getColourAmount(all: List<String>, colour: String) =
        all.firstOrNull { it.trim().endsWith(colour) }?.substringBefore(colour)?.trim()?.toInt()

    class Game(
        val id: Long,
        val sets: Set<Round>,
    ) {

        fun maxReds(): Int {
            return sets.mapNotNull { it.reds }.maxOrNull() ?: 0
        }

        fun maxGreens(): Int {
            return sets.mapNotNull { it.greens }.maxOrNull() ?: 0
        }

        fun maxBlues(): Int {
            return sets.mapNotNull { it.blues }.maxOrNull() ?: 0
        }

        override fun toString(): String {
            return "Game(id=$id, red =${maxReds()}, green = ${maxGreens()}, blue=${maxBlues()})\n"
        }
    }

    data class Round(
        val reds: Int?,
        val greens: Int?,
        val blues: Int?,
    )
}