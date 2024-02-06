fun main(args: Array<String>) {
    println("Part 1: ${Day10.solvePart1()}")
    println("Part 2: ${Day10.solvePart2()}")
}

object Day10 {

    const val eastWest = "-"
    const val northSouth = "|"
    const val northEast = "L"
    const val northWest = "J"
    const val southWest = "7"
    const val southEast = "F"

    fun solvePart1(): Int {
        val input = Day10::class.java.getResource("day10.txt")?.readText()?.split("\r\n") ?: error("Can't read input")
        val map = input.map { it.trim().split("") }
        val startY = map.indexOfFirst { it.contains("S") }
        val startX = map[startY].indexOf("S")
        val path = findPath(map, Coordinate(startX, startY))
        return path.size / 2
    }

    fun findPath(map: List<List<String>>, start: Coordinate): List<Coordinate> {
        val availableMoves = getAvailableMoves(map, start)
        val path = mutableListOf<Coordinate>()
        path.add(start)
        var current = start
        var currentDirection = availableMoves.first()
        when(currentDirection) {
            Movement.EAST -> current = Coordinate(start.x + 1, start.y)
            Movement.WEST -> current = Coordinate(start.x - 1, start.y)
            Movement.NORTH -> current = Coordinate(start.x, start.y - 1)
            Movement.SOUTH -> current = Coordinate(start.x, start.y + 1)
        }

        do {
            path.add(current)
            val moveResult = move(map, currentDirection, current)
            current = moveResult.newCoordinate
            currentDirection = moveResult.direction
        } while (current != start)
        return path
    }

    fun move(map: List<List<String>>, fromDirection: Movement, start: Coordinate): Move {
        when(fromDirection) {
            Movement.SOUTH -> {
                return when(map[start.y][start.x]) {
                    northEast -> Move(Movement.EAST, Coordinate(start.x + 1, start.y))
                    northSouth -> Move(Movement.SOUTH, Coordinate(start.x, start.y + 1))
                    northWest -> Move(Movement.WEST, Coordinate(start.x - 1, start.y))
                    else -> error("Invalid path coming from north")
                }
            }
            Movement.NORTH -> {
                return when(map[start.y][start.x]) {
                    southEast -> Move(Movement.EAST, Coordinate(start.x + 1, start.y))
                    southWest -> Move(Movement.WEST, Coordinate(start.x - 1, start.y))
                    northSouth -> Move(Movement.NORTH, Coordinate(start.x, start.y - 1))
                    else -> error("Invalid path coming from north")
                }
            }
            Movement.WEST -> {
                return when(map[start.y][start.x]) {
                    eastWest -> Move(Movement.WEST, Coordinate(start.x - 1, start.y))
                    northEast -> Move(Movement.NORTH, Coordinate(start.x , start.y - 1))
                    southEast -> Move(Movement.SOUTH, Coordinate(start.x, start.y + 1))
                    else -> error("Invalid path coming from north")
                }
            }
            Movement.EAST -> {
                return when(map[start.y][start.x]) {
                    eastWest -> Move(Movement.EAST, Coordinate(start.x + 1, start.y))
                    northWest -> Move(Movement.NORTH, Coordinate(start.x , start.y - 1))
                    southWest -> Move(Movement.SOUTH, Coordinate(start.x, start.y + 1))
                    else -> error("Invalid path coming from north")
                }
            }
        }
    }

    class Move(
        val direction: Movement,
        val newCoordinate: Coordinate
    )

    fun getAvailableMoves(map: List<List<String>>, start: Coordinate): List<Movement> {
        val movements = mutableListOf<Movement>()
        if (map[start.y-1][start.x] in setOf(southEast, southWest, northSouth)) {
            movements.add(Movement.NORTH)
        }
        if (map[start.y+1][start.x] in setOf(northEast, northSouth, northWest)) {
            movements.add(Movement.SOUTH)
        }
        if (map[start.y][start.x-1] in setOf(eastWest, northEast, southEast)) {
            movements.add(Movement.WEST)
        }
        if (map[start.y][start.x+1] in setOf(eastWest, northWest, southWest)) {
            movements.add(Movement.EAST)
        }
        check(movements.size == 2)
        return movements
    }

    fun solvePart2(): Long {
        val input = Day10::class.java.getResource("day10.txt")?.readText()?.split("\r\n") ?: error("Can't read input")
        val map = input.map { it.trim().split("") }
        val startY = map.indexOfFirst { it.contains("S") }
        val startX = map[startY].indexOf("S")
        val path = findPath(map, Coordinate(startX, startY))
        //printPath(map, path, map.first().size, map.size)
        return count(map, path)
    }

    private fun printPath(map: List<List<String>>, path: List<Coordinate>, width: Int, height: Int) {
        for (i in 0 .. height) {
            for (j in 0 .. width) {
                if (path.contains(Coordinate(j , i))) {
                    print(map[i][j])
                } else {
                    print(".")
                }
            }
            println()
        }
    }

    private fun count(map: List<List<String>>, path: List<Coordinate>): Long {
        var includedFieldsCount = 0L
        for (i in 0 .. map.size-1) {
            var inside = false
            for (j in 0 .. map.first().size - 1) {
                if (path.contains(Coordinate(j , i))) {
                    val pathValue = map[i][j]
                    if (inside) {
                        if (pathValue == "|" || pathValue == "7" || pathValue == "J") {
                            inside = false
                        }
                    } else {
                        if (pathValue == "S" || pathValue == "L" || pathValue == "|" || pathValue == "F") {
                            inside = true
                        }
                    }

                } else {
                    if (inside) {
                        println("row $i / col $j")
                        includedFieldsCount++
                    }
                }
            }
        }
        return includedFieldsCount
    }

    enum class Movement {
        EAST, WEST, NORTH, SOUTH
    }

    data class Coordinate(
        val x: Int,
        val y: Int,
    )

}