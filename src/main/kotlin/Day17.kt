fun main(args: Array<String>) {
    println("Part 1: ${Day17.solvePart1()}")
    println("Part 2: ${Day17.solvePart2()}")
}

object Day17 {

    fun solvePart1(): Int {
        val input = Day17::class.java.getResource("day17.txt")?.readText() ?: error("Can't read input")
        val field = input.split("\r\n").map { it.trim().split("").filter { a -> a.isNotBlank() }.map { it.toInt() } }
        val shortestPathsPerField: List<List<Move>> = field.map {
            it.map { Move.empty() }.toList()
        }
        shortestPathsPerField.first().first().minimumEnergyLossToHere = field.getEnergyLoss(Coordinate(0, 0))
        return getSmallestEnergyLoss(field, shortestPathsPerField)
    }

    fun getSmallestEnergyLoss(field: List<List<Int>>, pathsPerField: List<List<Move>>): Int {
        val start = Coordinate(0, 0)
        performMoves(field, start, pathsPerField)
        return pathsPerField.last().last().minimumEnergyLossToHere
    }

    fun performMoves(field: List<List<Int>>, current: Coordinate, pathsPerField: List<List<Move>>) {
        val energyLossSoFar = pathsPerField.getMinEnergyLoss(current)
        if (current.x < field.size - 1 && pathsPerField.canMove(current, Direction.RIGHT)) {
            val right = Coordinate(current.x + 1, current.y)
            val newEnergyLoss = energyLossSoFar + field.getEnergyLoss(right)
            if (newEnergyLoss < pathsPerField.getMinEnergyLoss(right)) {
                pathsPerField[right.y][right.x].consecutiveStepsInRow = if (pathsPerField.getLatestDirection(current) == Direction.RIGHT) {
                    pathsPerField.getNrOfConsecutiveMoves(current) + 1
                } else {
                    1
                }
                pathsPerField[right.y][right.x].latestDirection = Direction.RIGHT
                pathsPerField[right.y][right.x].minimumEnergyLossToHere = newEnergyLoss
                pathsPerField[right.y][right.x].path = pathsPerField[current.y][current.x].path.toList() + listOf(current)
                performMoves(field, right, pathsPerField)
            }
        }
        if (current.y < field.first().size - 1 && pathsPerField.canMove(current, Direction.DOWN)) {
            val down = Coordinate(current.x, current.y + 1)
            val newEnergyLoss = energyLossSoFar + field.getEnergyLoss(down)
            if (newEnergyLoss < pathsPerField.getMinEnergyLoss(down)) {
                pathsPerField[down.y][down.x].consecutiveStepsInRow = if (pathsPerField.getLatestDirection(current) == Direction.DOWN) {
                    pathsPerField.getNrOfConsecutiveMoves(current) + 1
                } else {
                    1
                }
                pathsPerField[down.y][down.x].latestDirection = Direction.DOWN
                pathsPerField[down.y][down.x].minimumEnergyLossToHere = newEnergyLoss
                pathsPerField[down.y][down.x].path = pathsPerField[current.y][current.x].path.toList() + listOf(current)
                performMoves(field, down, pathsPerField)
            }
        }
        if (current.x > 0 && pathsPerField.canMove(current, Direction.LEFT)) {
            val left = Coordinate(current.x - 1, current.y)
            val newEnergyLoss = energyLossSoFar + field.getEnergyLoss(left)
            if (newEnergyLoss < pathsPerField.getMinEnergyLoss(left)) {
                pathsPerField[left.y][left.x].consecutiveStepsInRow = if (pathsPerField.getLatestDirection(current) == Direction.LEFT) {
                    pathsPerField.getNrOfConsecutiveMoves(current) + 1
                } else {
                    1
                }
                pathsPerField[left.y][left.x].latestDirection = Direction.LEFT
                pathsPerField[left.y][left.x].minimumEnergyLossToHere = newEnergyLoss
                pathsPerField[left.y][left.x].path = pathsPerField[current.y][current.x].path.toList() + listOf(current)
                performMoves(field, left, pathsPerField)
            }
        }
        if (current.y > 0 && pathsPerField.canMove(current, Direction.UP)) {
            val up = Coordinate(current.x, current.y - 1)
            val newEnergyLoss = energyLossSoFar + field.getEnergyLoss(up)
            if (newEnergyLoss < pathsPerField.getMinEnergyLoss(up)) {
                pathsPerField[up.y][up.x].consecutiveStepsInRow = if (pathsPerField.getLatestDirection(current) == Direction.UP) {
                    pathsPerField.getNrOfConsecutiveMoves(current) + 1
                } else {
                    1
                }
                pathsPerField[up.y][up.x].latestDirection = Direction.UP
                pathsPerField[up.y][up.x].minimumEnergyLossToHere = newEnergyLoss
                pathsPerField[up.y][up.x].path = pathsPerField[current.y][current.x].path.toList() + listOf(current)
                performMoves(field, up, pathsPerField)
            }
        }
    }

    fun solvePart2(): Int {
        val input = Day17::class.java.getResource("day17.txt")?.readText() ?: error("Can't read input")
        return 0
    }

    data class Move(
        var minimumEnergyLossToHere: Int,
        var path: List<Coordinate>,
        var consecutiveStepsInRow: Int,
        var latestDirection: Direction,
    ) {
        companion object {
            fun empty(): Move {
                return Move(Int.MAX_VALUE, emptyList(), 0, Direction.RIGHT)
            }
        }
    }

    data class Step(
        val current: Coordinate,
        val energyLoss: Int,
        val consecutiveStepsInRow: Int,
        val currentDirection: Direction,
    )

    fun List<List<Int>>.getEnergyLoss(coordinate: Coordinate) : Int {
        return this[coordinate.y][coordinate.x]
    }

    fun List<List<Move>>.getMinEnergyLoss(coordinate: Coordinate) : Int {
        return this[coordinate.y][coordinate.x].minimumEnergyLossToHere
    }

    fun List<List<Move>>.getLatestDirection(coordinate: Coordinate) : Direction {
        return this[coordinate.y][coordinate.x].latestDirection
    }

    fun List<List<Move>>.getNrOfConsecutiveMoves(coordinate: Coordinate) : Int {
        return this[coordinate.y][coordinate.x].consecutiveStepsInRow
    }

    fun List<List<Move>>.canMove(coordinate: Coordinate, direction: Direction) : Boolean {
        val value = this[coordinate.y][coordinate.x]
        return value.latestDirection != direction || value.consecutiveStepsInRow < 3
    }

    enum class Direction {
        LEFT, RIGHT, UP, DOWN
    }

    data class Coordinate(
        val x: Int,
        val y: Int,
    )
}