fun main(args: Array<String>) {
    println("Part 1: ${Day18.solvePart1()}")
    println("Part 2: ${Day18.solvePart2()}")
}

object Day18 {

    fun solvePart1(): Int {
        val input = Day18::class.java.getResource("day18.txt")?.readText() ?: error("Can't read input")
        val instructions = input.split("\r\n")
            .map {
                val (direction, length, color) = it.trim().split(" ")
                DigInstruction(direction, length.toInt(), color.substringAfter("(").substringBefore(")"))
            }
        val path = carvePath(instructions)
        print(path)
        return getArea(path)
    }

    fun carvePath(instructions: List<DigInstruction>): List<Coordinate> {
        val path = mutableListOf<Coordinate>()
        var currentX = 0
        var currentY = 0
        path.add(Coordinate(currentX, currentY))
        for (instruction in instructions) {
            val newCoordinates = when(instruction.direction) {
                "R" -> {
                    repeat(instruction.length) {
                        currentX++
                        path.add(Coordinate(currentX, currentY))
                    }
                }
                "L" -> {
                    repeat(instruction.length) {
                        currentX--
                        path.add(Coordinate(currentX, currentY))
                    }
                }
                "D" -> {
                    repeat(instruction.length) {
                        currentY++
                        path.add(Coordinate(currentX, currentY))
                    }
                }
                "U" -> {
                    repeat(instruction.length) {
                        currentY--
                        path.add(Coordinate(currentX, currentY))
                    }
                }
                else -> error("unsupported direction ${instruction.direction}")
            }
        }
        return path
    }

    fun print(surrounding: List<Coordinate>) {
        val minX = surrounding.minOf { it.x }
        val maxX = surrounding.maxOf { it.x }
        val minY = surrounding.minOf { it.y }
        val maxY = surrounding.maxOf { it.y }
        for (y in minY..maxY) {
            for (x in minX .. maxX) {
                val char = if (surrounding.contains(Coordinate(x, y))){
                    "#"
                } else {
                    "."
                }
                print(char)
            }
            println()
        }
    }

    fun getArea(surrounding: List<Coordinate>): Int {
        val minY = surrounding.minOf { it.y }
        val startX = surrounding.filter { it.y == minY }.minOf { it.x } + 1
        val startY = minY + 1

        val insideArea = mutableSetOf<Coordinate>()
        floodFill(Coordinate(startX, startY), surrounding, insideArea)

        return (surrounding + insideArea).toSet().size
    }

    fun floodFill(current: Coordinate, path: List<Coordinate>, inside: MutableSet<Coordinate>) {
        if (!inside.contains(current) && !path.contains(current)) {
            inside.add(current)
            floodFill(Coordinate(current.x - 1, current.y), path, inside)
            floodFill(Coordinate(current.x + 1, current.y), path, inside)
            floodFill(Coordinate(current.x, current.y + 1), path, inside)
            floodFill(Coordinate(current.x, current.y - 1), path, inside)
        }
    }


    fun solvePart2(): Int {
        val input = Day18::class.java.getResource("day18.txt")?.readText() ?: error("Can't read input")
        return 0
    }

    data class DigInstruction(
        val direction: String,
        val length: Int,
        val color: String
    )

    data class Coordinate(
        val x: Int,
        val y: Int,
    )
}