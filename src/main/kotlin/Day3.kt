fun main(args: Array<String>) {
    println("Part 1: ${Day3.solvePart1()}")
    println("Part 2: ${Day3.solvePart2()}")
}

object Day3 {
    fun solvePart1(): Long {
        val input = Day3::class.java.getResource("day3.txt")?.readText() ?: error("Can't read input")
        val lines = input.split("\n").map { it.trim() }.filter { it.isNotBlank() }

        var sum = 0L
        for (rowNr in lines.indices) {
            var firstIdxCurrentNr = -1
            var lastIdxCurrentNr = -1
            var currentNumber = ""
            for (colNr in lines[rowNr].indices) {
                if (lines[rowNr][colNr].isDigit()) {
                    if (firstIdxCurrentNr == -1) {
                        firstIdxCurrentNr = colNr
                    }
                    lastIdxCurrentNr = colNr
                    currentNumber += lines[rowNr][colNr].toString()
                } else {
                    if (anySymbolAdjacent(lines, rowNr, firstIdxCurrentNr, lastIdxCurrentNr)) {
                        sum += currentNumber.toLong()
                    }
                    firstIdxCurrentNr = -1
                    lastIdxCurrentNr = -1
                    currentNumber = ""
                }
            }
            if (currentNumber.isNotBlank() && anySymbolAdjacent(lines, rowNr, firstIdxCurrentNr, lastIdxCurrentNr)) {
                sum += currentNumber.toLong()
            }
        }

        return sum
    }

    fun anySymbolAdjacent(lines: List<String>, row: Int, colFrom: Int, colTo: Int): Boolean {
        val left = 0.coerceAtLeast(colFrom - 1)
        val right = (lines[0].length - 1).coerceAtMost(colTo + 1)
        if (row > 0 && lines[row - 1].substring(left, right + 1).any { it.isSymbol() }) {
            return true
        }
        if (row < lines.lastIndex && lines[row + 1].substring(left, right + 1).any { it.isSymbol() }) {
            return true
        }
        return lines[row].substring(left, right + 1).any { it.isSymbol() }
    }

    private fun Char.isSymbol(): Boolean = !this.isDigit() && this != '.'

    private fun Char.isStar(): Boolean = this == '*'

    fun solvePart2(): Long {
        val input = Day3::class.java.getResource("day3.txt")?.readText() ?: error("Can't read input")
        val lines = input.split("\n").map { it.trim() }.filter { it.isNotBlank() }
        val numbersWithStarNeighbours: MutableList<PartNumber> = mutableListOf()
        for (rowNr in lines.indices) {
            var firstIdxCurrentNr = -1
            var lastIdxCurrentNr = -1
            var currentNumber = ""
            for (colNr in lines[rowNr].indices) {
                if (lines[rowNr][colNr].isDigit()) {
                    if (firstIdxCurrentNr == -1) {
                        firstIdxCurrentNr = colNr
                    }
                    lastIdxCurrentNr = colNr
                    currentNumber += lines[rowNr][colNr].toString()
                } else {
                    val adjacentsStars = getAdjacentStars(lines, rowNr, firstIdxCurrentNr, lastIdxCurrentNr)
                    if (adjacentsStars.isNotEmpty()) {
                        numbersWithStarNeighbours.add(PartNumber(currentNumber.toLong(), adjacentsStars))
                    }
                    firstIdxCurrentNr = -1
                    lastIdxCurrentNr = -1
                    currentNumber = ""
                }
            }
            if (currentNumber.isNotBlank()) {
                val adjacentsStars = getAdjacentStars(lines, rowNr, firstIdxCurrentNr, lastIdxCurrentNr)
                if (adjacentsStars.isNotEmpty()) {
                    numbersWithStarNeighbours.add(PartNumber(currentNumber.toLong(), adjacentsStars))
                }
            }
        }

        return mapStarsToNumbers(numbersWithStarNeighbours)
            .filter { it.value.size == 2 }
            .map { it.value[0] * it.value[1] }
            .sum()
    }

    fun mapStarsToNumbers(partNumbers: List<PartNumber>) : MutableMap<Coordinate, MutableList<Long>>{
        val starsToNumbers : MutableMap<Coordinate, MutableList<Long>> = mutableMapOf()
        for (partNumber in partNumbers) {
            for (star in partNumber.starLocations) {
                if (!starsToNumbers.contains(star)) {
                    starsToNumbers[star] = mutableListOf()
                }
                starsToNumbers[star]!!.add(partNumber.value)
            }
        }
        return starsToNumbers
    }

    fun getAdjacentStars(lines: List<String>, row: Int, colFrom: Int, colTo: Int): List<Coordinate> {
        val adjacentStars = mutableListOf<Coordinate>()
        val left = 0.coerceAtLeast(colFrom - 1)
        val right = (lines[0].length - 1).coerceAtMost(colTo + 1)
        if (row > 0) {
            adjacentStars.addAll(getStarIndices(lines, row - 1, left, right))
        }
        adjacentStars.addAll(getStarIndices(lines, row, left, right))
        if (row < lines.lastIndex) {
            adjacentStars.addAll(getStarIndices(lines, row + 1, left, right))
        }
        return adjacentStars
    }

    private fun getStarIndices(
        lines: List<String>,
        row: Int,
        left: Int,
        right: Int
    ) = lines[row].substring(left, right + 1).mapIndexed { idx, it ->
        if (it.isStar()) {
            Coordinate(row, left + idx)
        } else {
            null
        }
    }.filterNotNull()

    data class PartNumber(
        val value: Long,
        val starLocations: List<Coordinate>
    )

    data class Coordinate(
        val row: Int,
        val col: Int,
    )
}