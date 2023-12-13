import kotlin.math.abs

fun main(args: Array<String>) {
    println("Part 1: ${Day11.solvePart1()}")
    println("Part 2: ${Day11.solvePart2()}")
}

object Day11 {
    fun solvePart1(): Long {
        val input = Day11::class.java.getResource("day11.txt")?.readText() ?: error("Can't read input")
        val field = input.split("\r\n")
            .map { line -> line.split("").filter { it.isNotBlank() }.toMutableList() }
            .toMutableList()
        expandTwice(field)
        val galaxies = findGalaxies(field)
        val distances = getAllDistances(galaxies)
        return distances.sumOf { it.distance }
    }

    fun expandTwice(before: MutableList<MutableList<String>>) {
        var addedRows = 0
        for (idx in before.indices) {
            if (before[addedRows + idx].none { it == "#" }) {
                before.add(addedRows + idx, before[addedRows + idx].toMutableList())
                addedRows++
            }
        }
        var addedColumns = 0
        for (idx in before[0].indices) {
            if (before.map { it[addedColumns + idx] }.none { it == "#" }) {
                before.forEach {
                    it.add(addedColumns + idx, ".")
                }
                addedColumns++
            }
        }
        println()
    }

    fun findGalaxies(universe: MutableList<MutableList<String>>): List<Coordinate> {
        val galaxies = mutableListOf<Coordinate>()
        for (row in universe.indices) {
            for (col in universe[row].indices) {
                if (universe[row][col] == "#") {
                    galaxies.add(Coordinate(col.toLong(), row.toLong()))
                }
            }
        }
        return galaxies
    }

    fun getAllDistances(galaxies: List<Coordinate>): List<Distance> {
        val distances = mutableListOf<Distance>()
        for (firstIdx in galaxies.indices) {
            for (otherIdx in firstIdx+1 until galaxies.size) {
                val distance = abs(galaxies[firstIdx].x - galaxies[otherIdx].x) + abs(galaxies[firstIdx].y - galaxies[otherIdx].y)
                distances.add(Distance(firstIdx.toLong(), otherIdx.toLong(), distance))
            }
         }
        return distances
    }

    fun solvePart2(): Long {
        val input = Day11::class.java.getResource("day11.txt")?.readText() ?: error("Can't read input")
        val field = input.split("\r\n")
            .map { line -> line.split("").filter { it.isNotBlank() }.toMutableList() }
            .toMutableList()
        val galaxies = findGalaxiesExpanded(field)
        val distances = getAllDistances(galaxies)
        return distances.sumOf { it.distance }
    }

    fun findGalaxiesExpanded(universe: MutableList<MutableList<String>>): List<Coordinate> {
        val galaxies = mutableListOf<Coordinate>()
        var rowOffset = 0L
        for (row in universe.indices) {
            if (universe[row].none { it == "#" }) {
                rowOffset += 999999
            }
            var colOffset = 0L
            for (col in universe[row].indices) {
                if (universe.map { it[col] }.none { it == "#" }) {
                    colOffset += 999999
                }
                if (universe[row][col] == "#") {
                    galaxies.add(Coordinate(col + colOffset, row + rowOffset))
                }
            }
        }
        return galaxies
    }

    data class Coordinate(
        val x: Long,
        val y: Long
    )

    data class Distance(
        val from: Long,
        val to: Long,
        val distance: Long
    )


}