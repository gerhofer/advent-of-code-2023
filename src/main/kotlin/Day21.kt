fun main(args: Array<String>) {
    println("Part 1: ${Day21.solvePart1()}")
    println("Part 2: ${Day21.solvePart2()}")
}

object Day21 {

    fun solvePart1(): Int {
        val input = Day21::class.java.getResource("day21.txt")?.readText() ?: error("Can't read input")
        val map = input.split("\r\n")
            .map { line -> line.trim().toList() }
        val start = getStart(map)
        var possibleLocations = setOf(start)
        repeat(64) {
            possibleLocations = performPossibleSteps(possibleLocations, map)
        }
        return possibleLocations.size
    }

    fun performPossibleSteps(locations: Set<Coordinate>, map: List<List<Char>>): Set<Coordinate> {
        return locations.flatMap {
            performPossibleSteps(it, map)
        }.toSet()
    }

    fun performPossibleSteps(position: Coordinate, map: List<List<Char>>): Set<Coordinate> {
        val newPositions = mutableSetOf<Coordinate>()
        if (canMoveNorthInField(map, position)) {
            newPositions.add(Coordinate(position.row - 1, position.col))
        }
        if (canMoveSouthInField(map, position)) {
            newPositions.add(Coordinate(position.row + 1, position.col))
        }
        if (canMoveWestInField(map, position)) {
            newPositions.add(Coordinate(position.row, position.col - 1))
        }
        if (canMoveEastInField(map, position)) {
            newPositions.add(Coordinate(position.row, position.col + 1))
        }
        return newPositions
    }

    fun canMoveNorthInField(map: List<List<Char>>, position: Coordinate): Boolean {
        return position.row > 0 && map[position.row - 1][position.col] in setOf('.', 'S')
    }

    fun canMoveSouthInField(map: List<List<Char>>, position: Coordinate): Boolean {
        return position.row < map.size - 1 && map[position.row + 1][position.col] in setOf('.', 'S')
    }

    fun canMoveWestInField(map: List<List<Char>>, position: Coordinate): Boolean {
        return position.col > 0 && map[position.row][position.col - 1] in setOf('.', 'S')
    }

    fun canMoveEastInField(map: List<List<Char>>, position: Coordinate): Boolean {
        return position.col < map.size - 1 && map[position.row][position.col + 1] in setOf('.', 'S')
    }

    fun solvePart2(): Int {
        val input = Day21::class.java.getResource("day21.txt")?.readText() ?: error("Can't read input")
        val map = input.split("\r\n")
            .map { line -> line.trim().toList() }
        val start = getStart(map)
        var possibleLocations = setOf(BigCoordinate(start, 0, 0, 0))
        val visited = mutableSetOf<BigCoordinate>()
        val tiles = mutableSetOf<Coordinate>()
        tiles.add(Coordinate(0, 0))
        val goal = 1000
        val sequenceCounts = mutableListOf<Long>()
        sequenceCounts.add(1)
        repeat(goal) {step ->
            possibleLocations = performPossibleSteps2(possibleLocations - visited, map, visited, step)
            val reachedTiles = possibleLocations.map { x -> Coordinate(x.row, x.col) }.toSet()
            if (!tiles.containsAll(reachedTiles)) {
                println("Reached new tile ${(reachedTiles - tiles).size} in round $step")
                tiles.addAll(reachedTiles)
            }
            val reachableCount = ((possibleLocations) + visited.filter { it.firstSeen % 2 == (step - 1) % 2 }).size + 1
            sequenceCounts.add(reachableCount.toLong())
        }
        println(sequenceCounts.take(100).joinToString(","))

        val (even, uneven) = visited.filter { it.row == 0 && it.col == 0 }.partition { it.firstSeen % 2 == 0 }
        return ((possibleLocations) + visited.filter { it.firstSeen % 2 == (goal - 1) % 2 }).size + 1
    }

    fun performPossibleSteps2(
        locations: Set<BigCoordinate>,
        map: List<List<Char>>,
        visited: MutableSet<BigCoordinate>,
        itCount: Int,
    ): Set<BigCoordinate> {
        return locations.flatMap {
            if (!visited.contains(it)) {
                val news = performPossibleSteps2(it, map, itCount)
                visited.add(it)
                news
            } else {
                setOf(it)
            }
        }.toSet()
    }

    fun performPossibleSteps2(
        position: BigCoordinate,
        map: List<List<Char>>,
        itCount: Int,
    ): Set<BigCoordinate> {
        val newPositions = mutableSetOf<BigCoordinate>()
        if (canMoveNorthInField(map, position.coordinate)) {
            newPositions.add(
                BigCoordinate(
                    Coordinate(position.coordinate.row - 1, position.coordinate.col),
                    position.row,
                    position.col,
                    itCount
                )
            )
        } else if (position.coordinate.row == 0) {
            //    println("Moving to copy north from ${position.coordinate}" )
            newPositions.add(
                BigCoordinate(
                    Coordinate(map.size - 1, position.coordinate.col),
                    position.row - 1,
                    position.col,
                    itCount
                )
            )
        }
        if (canMoveSouthInField(map, position.coordinate)) {
            newPositions.add(
                BigCoordinate(
                    Coordinate(position.coordinate.row + 1, position.coordinate.col),
                    position.row,
                    position.col,
                    itCount
                )
            )
        } else if (position.coordinate.row == map.size - 1) {
            //    println("Moving to copy south from ${position.coordinate}" )
            newPositions.add(BigCoordinate(Coordinate(0, position.coordinate.col), position.row + 1, position.col, itCount))
        }
        if (canMoveWestInField(map, position.coordinate)) {
            newPositions.add(
                BigCoordinate(
                    Coordinate(position.coordinate.row, position.coordinate.col - 1),
                    position.row,
                    position.col,
                    itCount
                )
            )
        } else if (position.coordinate.col == 0) {
            // println("Moving to copy west from ${position.coordinate}" )
            newPositions.add(
                BigCoordinate(
                    Coordinate(position.coordinate.row, map.first().size - 1),
                    position.row,
                    position.col - 1,
                    itCount
                )
            )
        }
        if (canMoveEastInField(map, position.coordinate)) {
            newPositions.add(
                BigCoordinate(
                    Coordinate(position.coordinate.row, position.coordinate.col + 1),
                    position.row,
                    position.col,
                    itCount
                )
            )
        } else if (position.coordinate.col == map.first().size - 1) {
            //  println("Moving to copy east from ${position.coordinate}" )
            newPositions.add(BigCoordinate(Coordinate(position.coordinate.row, 0), position.row, position.col + 1, itCount))
        }
        return newPositions
    }

    fun getStart(map: List<List<Char>>): Coordinate {
        val row = map.indexOfFirst { it.contains('S') }
        val col = map[row].indexOf('S')
        return Coordinate(row, col)
    }

    data class BigCoordinate(
        val coordinate: Coordinate,
        val row: Int,
        val col: Int,
        val firstSeen: Int,
    ) {

        fun isEdge(maxRow: Int, maxCol: Int): Boolean =
            coordinate.row == 0 || coordinate.row == maxRow || coordinate.col == 0 || coordinate.col == maxCol

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as BigCoordinate

            if (coordinate != other.coordinate) return false
            if (row != other.row) return false
            if (col != other.col) return false

            return true
        }

        override fun hashCode(): Int {
            var result = coordinate.hashCode()
            result = 31 * result + row
            result = 31 * result + col
            return result
        }
    }

    data class Coordinate(
        val row: Int,
        val col: Int,
    )
}