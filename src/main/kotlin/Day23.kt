fun main(args: Array<String>) {
    println("Part 1: ${Day23.solvePart1()}")
    println("Part 2: ${Day23.solvePart2()}")
}

object Day23 {

    val WOOD = '#'

    fun solvePart1(): Int {
        val input = Day23::class.java.getResource("day23.txt")?.readText() ?: error("Can't read input")
        val map = input.split("\r\n")
            .map { line -> line.trim().toList() }
        return findLongestPath(map) - 1
    }

    fun findLongestPath(map: List<List<Char>>): Int {
        val longestPaths = mutableMapOf<MapKey, Path>()
        val start = getStart(map)
        val goal = getGoal(map)
        performAllMovesConsideringSlopes(map, longestPaths, Path(listOf(start)))
        return longestPaths.filter { it.key.coordinate == goal }
            .maxOf { it.value.size }
    }

    fun getBlockdeNeighbours(map: List<List<Char>>, current: Path): Set<Coordinate> {
        val currentField = current.coordinates.last()
        val neighbours = mutableSetOf<Coordinate>()
        if (canMoveNorth(map, currentField)) {
            val north = Coordinate(currentField.row - 1, currentField.col)
            neighbours.add(north)
        }
        if (canMoveSouth(map, currentField)) {
            val south = Coordinate(currentField.row + 1, currentField.col)
            neighbours.add(south)
        }
        if (canMoveEast(map, currentField)) {
            val east = Coordinate(currentField.row, currentField.col + 1)
            neighbours.add(east)
        }
        if (canMoveWest(map, currentField)) {
            val west = Coordinate(currentField.row, currentField.col - 1)
            neighbours.add(west)
        }
        return neighbours.intersect(current.coordinates.toSet())
    }

    fun performAllMovesConsideringSlopes(map: List<List<Char>>, longestPaths: MutableMap<MapKey, Path>, current: Path) {
        val currentField = current.coordinates.last()
        val currentType = map[currentField.row][currentField.col]
        val currentKey = MapKey(currentField, getBlockdeNeighbours(map, current))
        val longestPathTilNow = longestPaths.getOrDefault(currentKey, Path(emptyList()))
        if (longestPathTilNow.size < current.size) {
            longestPaths[currentKey] = current
            if (currentType == '>') {
                val east = Coordinate(currentField.row, currentField.col + 1)
                if (!current.coordinates.contains(east)) {
                    performAllMovesConsideringSlopes(map, longestPaths, current.copy(coordinates = current.coordinates + listOf(east)))
                }
            } else if (currentType == '<') {
                val west = Coordinate(currentField.row, currentField.col - 1)
                if (!current.coordinates.contains(west)) {
                    performAllMovesConsideringSlopes(map, longestPaths, current.copy(coordinates = current.coordinates + listOf(west)))
                }
            } else if (currentType == '^') {
                val north = Coordinate(currentField.row - 1, currentField.col)
                if (!current.coordinates.contains(north)) {
                    performAllMovesConsideringSlopes(map, longestPaths, current.copy(coordinates = current.coordinates + listOf(north)))
                }
            } else if (currentType == 'v') {
                val south = Coordinate(currentField.row + 1, currentField.col)
                if (!current.coordinates.contains(south)) {
                    performAllMovesConsideringSlopes(map, longestPaths, current.copy(coordinates = current.coordinates + listOf(south)))
                }
            } else {
                if (canMoveNorth(map, currentField)) {
                    val north = Coordinate(currentField.row - 1, currentField.col)
                    if (!current.coordinates.contains(north)) {
                        performAllMovesConsideringSlopes(map, longestPaths, current.copy(coordinates = current.coordinates + listOf(north)))
                    }
                }
                if (canMoveSouth(map, currentField)) {
                    val south = Coordinate(currentField.row + 1, currentField.col)
                    if (!current.coordinates.contains(south)) {
                        performAllMovesConsideringSlopes(map, longestPaths, current.copy(coordinates = current.coordinates + listOf(south)))
                    }
                }
                if (canMoveEast(map, currentField)) {
                    val east = Coordinate(currentField.row, currentField.col + 1)
                    if (!current.coordinates.contains(east)) {
                        performAllMovesConsideringSlopes(map, longestPaths, current.copy(coordinates = current.coordinates + listOf(east)))
                    }
                }
                if (canMoveWest(map, currentField)) {
                    val west = Coordinate(currentField.row, currentField.col - 1)
                    if (!current.coordinates.contains(west)) {
                        performAllMovesConsideringSlopes(map, longestPaths, current.copy(coordinates = current.coordinates + listOf(west)))
                    }
                }
            }
        }
    }

    fun canMoveNorth(map: List<List<Char>>, position: Coordinate): Boolean {
        return position.row > 0 && map[position.row - 1][position.col] != WOOD
    }

    fun canMoveSouth(map: List<List<Char>>, position: Coordinate): Boolean {
        return position.row < map.size - 1 && map[position.row + 1][position.col] != WOOD
    }

    fun canMoveWest(map: List<List<Char>>, position: Coordinate): Boolean {
        return position.col > 0 && map[position.row][position.col - 1] != WOOD
    }

    fun canMoveEast(map: List<List<Char>>, position: Coordinate): Boolean {
        return position.col < map.size - 1 && map[position.row][position.col + 1] != WOOD
    }

    fun solvePart2(): Int {
        val input = Day23::class.java.getResource("day23.txt")?.readText() ?: error("Can't read input")
        val map = input.split("\r\n")
            .map { line -> line.trim()
                .replace('<', '.')
                .replace('>', '.')
                .replace('^', '.')
                .replace('v', '.')
                .toList() }
        return findLongestPath(map) - 1
    }

    fun getStart(map: List<List<Char>>): Coordinate {
        val row = 0
        val col = map[row].indexOf('.')
        return Coordinate(row, col)
    }

    fun getGoal(map: List<List<Char>>): Coordinate {
        val row = map.size - 1
        val col = map[row].indexOf('.')
        return Coordinate(row, col)
    }

    data class MapKey(
        val coordinate: Coordinate,
        val blockedNeighbours: Set<Coordinate>
    )

    enum class Direction {
        NORTH, SOUTH, EAST, WEST;
    }

    data class Path(
        val coordinates: List<Coordinate>
    ) {
        val size get() = coordinates.size
    }

    data class Coordinate(
        val row: Int,
        val col: Int,
    )
}