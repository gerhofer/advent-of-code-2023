fun main(args: Array<String>) {
    println("Part 1: ${Day14.solvePart1()}")
    println("Part 2: ${Day14.solvePart2()}")
}

object Day14 {

    fun solvePart1(): Long {
        val input = Day14::class.java.getResource("day14.txt")?.readText() ?: error("Can't read input")
        val rockMap = input.split("\r\n")
            .map { it.trim().split("").filter { l -> l.isNotBlank() }.toMutableList() }
            .toMutableList()
        tiltNorth(rockMap)
        return calculateLoad(rockMap)
    }

    fun tiltNorth(rockMap: MutableList<MutableList<String>>) {
        for (row in 1 .. rockMap.size - 1) {
            for (col in rockMap[row].indices) {
                var currentRow = row
                if (rockMap[currentRow][col] == "O") {
                    while (canMoveNorth(rockMap, currentRow, col)) {
                        rockMap[currentRow][col] = "."
                        rockMap[currentRow - 1][col] = "O"
                        currentRow--
                    }
                }
            }
        }
    }

    fun canMoveNorth(rockMap: MutableList<MutableList<String>>, row: Int, col: Int): Boolean {
        return row-1 >= 0 && rockMap[row-1][col] == "."
    }

    fun tiltSouth(rockMap: MutableList<MutableList<String>>) {
        for (row in rockMap.size - 2 downTo 0) {
            for (col in rockMap[row].indices) {
                var currentRow = row
                if (rockMap[currentRow][col] == "O") {
                    while (canMoveSouth(rockMap, currentRow, col)) {
                        rockMap[currentRow][col] = "."
                        rockMap[currentRow + 1][col] = "O"
                        currentRow++
                    }
                }
            }
        }
    }

    fun canMoveSouth(rockMap: MutableList<MutableList<String>>, row: Int, col: Int): Boolean {
        return row < rockMap.size-1 && rockMap[row+1][col] == "."
    }

    fun tiltWest(rockMap: MutableList<MutableList<String>>) {
        for (col in 0 .. rockMap.first().size - 1) {
            for (row in rockMap.indices) {
                var currentCol = col
                if (rockMap[row][currentCol] == "O") {
                    while (canMoveWest(rockMap, row, currentCol)) {
                        rockMap[row][currentCol] = "."
                        rockMap[row][currentCol-1] = "O"
                        currentCol--
                    }
                }
            }
        }
    }

    fun canMoveWest(rockMap: MutableList<MutableList<String>>, row: Int, col: Int): Boolean {
        return col > 0 && rockMap[row][col-1] == "."
    }

    fun tiltEast(rockMap: MutableList<MutableList<String>>) {
        for (col in rockMap.first().size - 1 downTo 0) {
            for (row in rockMap.indices) {
                var currentCol = col
                if (rockMap[row][currentCol] == "O") {
                    while (canMoveEast(rockMap, row, currentCol)) {
                        rockMap[row][currentCol] = "."
                        rockMap[row][currentCol+1] = "O"
                        currentCol++
                    }
                }
            }
        }
    }

    fun canMoveEast(rockMap: MutableList<MutableList<String>>, row: Int, col: Int): Boolean {
        return col < rockMap.first().size-1 && rockMap[row][col+1] == "."
    }

    fun print(rockMap: MutableList<MutableList<String>>) {
        for (line in rockMap) {
            println(line.joinToString(""))
        }
    }

    fun toString(rockMap: MutableList<MutableList<String>>): String {
        return rockMap.joinToString("\r\n") { it.joinToString("") }
    }

    fun calculateLoad(rockMap: List<List<String>>): Long {
        var load = 0L
        for (i in rockMap.indices) {
            load += rockMap[i].count { it == "O" } * (rockMap.size - i)
        }
        return load
    }

    fun solvePart2(): Long {
        val input = Day14::class.java.getResource("day14.txt")?.readText() ?: error("Can't read input")
        val rockMap = input.split("\r\n")
            .map { it.trim().split("").filter { l -> l.isNotBlank() }.toMutableList() }
            .toMutableList()
        val seenStates = mutableListOf<String>()
        var i = 0
        val goal = 1000000000
        var firstOccurance = 0
        var circleSize = 0
        while(i < goal) {
            tiltNorth(rockMap)
            tiltWest(rockMap)
            tiltSouth(rockMap)
            tiltEast(rockMap)
            val asString = toString(rockMap)
            if (seenStates.contains(asString)) {
                val alreadySeenAt = seenStates.indexOf(asString)
                firstOccurance = alreadySeenAt
                println("Found cycle at $i already seen at $alreadySeenAt")
                circleSize = i - alreadySeenAt
                break
            }
            i++
            seenStates.add(asString)
        }

        repeat((goal - firstOccurance - 1) % circleSize) {
            tiltNorth(rockMap)
            tiltWest(rockMap)
            tiltSouth(rockMap)
            tiltEast(rockMap)
        }

        return calculateLoad(rockMap)
    }
}