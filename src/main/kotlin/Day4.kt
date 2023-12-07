import kotlin.math.pow

fun main(args: Array<String>) {
    println("Part 1: ${Day4.solvePart1()}")
    println("Part 2: ${Day4.solvePart2()}")
}

object Day4 {
    fun solvePart1(): Double {
        val input = Day4::class.java.getResource("day4.txt")?.readText() ?: error("Can't read input")
        return input.split("\n").filter { it.isNotBlank() }
            .map { ScratchCard.parse(it) }
            .sumOf { it.getScore() }
    }

    fun solvePart2(): Int {
        val input = Day4::class.java.getResource("day4.txt")?.readText() ?: error("Can't read input")
        val scratchCardsById = input.split("\n").filter { it.isNotBlank() }
            .map { ScratchCard.parse(it) }
            .associateBy { it.id }
            .toMutableMap()
        for (cardId in scratchCardsById.keys) {
            val card = scratchCardsById[cardId]!!
            val winners = card.getWinners()
            val creatingCopies = card.amount
            if (winners > 0) {
                for (copyId in (cardId + 1..cardId + winners)) {
                    scratchCardsById[copyId]!!.amount += creatingCopies
                }
            }
        }

        return scratchCardsById.values.sumOf { it.amount }
    }

    class ScratchCard(
        val winningNumbers: Set<Int>,
        val numbers: Set<Int>,
        var amount: Int,
        val id: Int,
    ) {

        fun getWinners(): Int {
            return winningNumbers.intersect(numbers).size
        }

        fun getScore(): Double {
            val amountOfWinningNumbers = winningNumbers.intersect(numbers).size
            if (amountOfWinningNumbers == 0) {
                return 0.0
            }
            return 2.0.pow((amountOfWinningNumbers - 1).toDouble())
        }

        companion object {
            fun parse(line: String): ScratchCard {
                val id = line.substringAfter("Card ").substringBefore(":").trim().toInt()
                val (winning, actual) = line.substringAfter(":").split("|")
                return ScratchCard(
                    winning.split(" ").filter { it.trim().isNotBlank() }.map { it.trim().toInt() }.toSet(),
                    actual.split(" ").filter { it.trim().isNotBlank() }.map { it.trim().toInt() }.toSet(),
                    1,
                    id
                )
            }
        }

    }
}