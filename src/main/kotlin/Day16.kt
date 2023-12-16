fun main(args: Array<String>) {
    println("Part 1: ${Day16.solvePart1()}")
    println("Part 2: ${Day16.solvePart2()}")
}

object Day16 {

    fun solvePart1(): Int {
        val input = Day16::class.java.getResource("day16.txt")?.readText() ?: error("Can't read input")
        val field = input.split("\r\n").map { it.trim().split("").filter { a -> a.isNotBlank() } }
        return getEnergizedFieldSize(field,Movement(Coordinate(-1, 0), Direction.RIGHT))
    }

    fun getEnergizedFieldSize(field: List<List<String>>, firstMovement: Movement): Int {
        val energizedFields = mutableSetOf<Coordinate>()
        val moves = mutableSetOf<Movement>()
        move(field, firstMovement, energizedFields, moves)
        return energizedFields.toSet().size
    }

    fun move(field: List<List<String>>, currentMove: Movement, energizedFields: MutableSet<Coordinate>, coveredMovements: MutableSet<Movement>) {
        if (coveredMovements.contains(currentMove)) {
            return
        } else {
            coveredMovements.add(currentMove)
        }
        when (currentMove.direction) {
            Direction.LEFT -> moveLeft(field, currentMove, energizedFields, coveredMovements)
            Direction.RIGHT -> moveRight(field, currentMove, energizedFields,coveredMovements)
            Direction.UP -> moveUp(field, currentMove, energizedFields, coveredMovements)
            Direction.DOWN -> moveDown(field, currentMove, energizedFields, coveredMovements)
        }
    }

    fun moveRight(field: List<List<String>>, currentMove: Movement, energizedFields: MutableSet<Coordinate>, coveredMovements: MutableSet<Movement>) {
        if (currentMove.current.x < field[currentMove.current.y].size - 1) {
            val fieldValue = field[currentMove.current.y][currentMove.current.x + 1]
            val coordinateToTheRight = Coordinate(currentMove.current.x + 1, currentMove.current.y)
            energizedFields.add(coordinateToTheRight)
            when (fieldValue) {
                "|" -> {
                    move(field, Movement(coordinateToTheRight, Direction.UP), energizedFields, coveredMovements)
                    move(field, Movement(coordinateToTheRight, Direction.DOWN), energizedFields, coveredMovements)
                }
                "-", "." -> move(field, Movement(coordinateToTheRight, Direction.RIGHT), energizedFields, coveredMovements)
                "/" -> move(field, Movement(coordinateToTheRight, Direction.UP), energizedFields, coveredMovements)
                "\\" -> move(field, Movement(coordinateToTheRight, Direction.DOWN), energizedFields, coveredMovements)
            }
        }
    }

    fun moveLeft(field: List<List<String>>, currentMove: Movement, energizedFields: MutableSet<Coordinate>, coveredMovements: MutableSet<Movement>) {
        if (currentMove.current.x > 0) {
            val fieldValue = field[currentMove.current.y][currentMove.current.x - 1]
            val coordinateToTheRight = Coordinate(currentMove.current.x - 1, currentMove.current.y)
            energizedFields.add(coordinateToTheRight)
            when (fieldValue) {
                "|" -> {
                    move(field, Movement(coordinateToTheRight, Direction.UP), energizedFields, coveredMovements)
                    move(field, Movement(coordinateToTheRight, Direction.DOWN), energizedFields, coveredMovements)
                }
                "-", "."  -> move(field, Movement(coordinateToTheRight, Direction.LEFT), energizedFields, coveredMovements)
                "/" -> move(field, Movement(coordinateToTheRight, Direction.DOWN), energizedFields, coveredMovements)
                "\\" -> move(field, Movement(coordinateToTheRight, Direction.UP), energizedFields, coveredMovements)
            }
        }
    }

    fun moveUp(field: List<List<String>>, currentMove: Movement, energizedFields: MutableSet<Coordinate>, coveredMovements: MutableSet<Movement>) {
        if (currentMove.current.y > 0) {
            val fieldValue = field[currentMove.current.y - 1][currentMove.current.x]
            val coordinateToTheRight = Coordinate(currentMove.current.x, currentMove.current.y - 1)
            energizedFields.add(coordinateToTheRight)
            when (fieldValue) {
                "|" , "." -> move(field, Movement(coordinateToTheRight, Direction.UP), energizedFields, coveredMovements)
                "-" -> {
                    move(field, Movement(coordinateToTheRight, Direction.LEFT), energizedFields, coveredMovements)
                    move(field, Movement(coordinateToTheRight, Direction.RIGHT), energizedFields, coveredMovements)
                }

                "/" -> move(field, Movement(coordinateToTheRight, Direction.RIGHT), energizedFields, coveredMovements)
                "\\" -> move(field, Movement(coordinateToTheRight, Direction.LEFT), energizedFields, coveredMovements)
            }
        }
    }

    fun moveDown(field: List<List<String>>, currentMove: Movement, energizedFields: MutableSet<Coordinate>, coveredMovements: MutableSet<Movement>) {
        if (currentMove.current.y < field.size - 1) {
            val fieldValue = field[currentMove.current.y + 1][currentMove.current.x]
            val coordinateToTheRight = Coordinate(currentMove.current.x, currentMove.current.y + 1)
            energizedFields.add(coordinateToTheRight)
            when (fieldValue) {
                "|" , "." -> move(field, Movement(coordinateToTheRight, Direction.DOWN), energizedFields, coveredMovements)
                "-" -> {
                    move(field, Movement(coordinateToTheRight, Direction.LEFT), energizedFields, coveredMovements)
                    move(field, Movement(coordinateToTheRight, Direction.RIGHT), energizedFields, coveredMovements)
                }

                "/" -> move(field, Movement(coordinateToTheRight, Direction.LEFT), energizedFields, coveredMovements)
                "\\" -> move(field, Movement(coordinateToTheRight, Direction.RIGHT), energizedFields, coveredMovements)
            }
        }
    }

    fun solvePart2(): Int {
        val input = Day16::class.java.getResource("day16.txt")?.readText() ?: error("Can't read input")
        val field = input.split("\r\n").map { it.trim().split("").filter { a -> a.isNotBlank() } }
        val options = field.indices.map { getEnergizedFieldSize(field,Movement(Coordinate(-1, it), Direction.RIGHT)) } +
                field.indices.map { getEnergizedFieldSize(field,Movement(Coordinate(field.first().size, it), Direction.LEFT)) } +
                field.first().indices.map { getEnergizedFieldSize(field,Movement(Coordinate(it, -1), Direction.DOWN)) } +
                field.first().indices.map { getEnergizedFieldSize(field,Movement(Coordinate(it, field.size), Direction.UP)) }
        return options.max()
    }

    data class Movement(
        val current: Coordinate,
        val direction: Direction
    )

    enum class Direction {
        LEFT, RIGHT, UP, DOWN
    }

    data class Coordinate(
        val x: Int,
        val y: Int,
    )
}