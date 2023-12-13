fun main(args: Array<String>) {
    //println("Part 1: ${Day8.solvePart1()}")
    println("Part 2: ${Day8.solvePart2()}")
}

object Day8 {

    fun solvePart1(): Int? {
        val input = Day8::class.java.getResource("day8.txt")?.readText()?.split("\r\n") ?: error("Can't read input")
        val instructions = input.first().trim()
        val nodeMap = input.drop(2).map { Node.parse(it) }.associateBy { it.current }
        val start = "AAA"
        val goal = "ZZZ"
        return getStepsToGoal(start, goal, instructions, nodeMap)
    }

    private fun getStepsToGoal(
        start: String,
        goal: String,
        instructions: String,
        nodeMap: Map<String, Node>
    ): Int {
        var stepCount = 0
        var current = start
        while (current != goal) {
            val instruction = instructions[stepCount % instructions.length]
            if (instruction == 'L') {
                current = nodeMap[current]?.left ?: error("Node $current not found in map")
            } else if (instruction == 'R') {
                current = nodeMap[current]?.right ?: error("Node $current not found in map")
            } else {
                error("invalid instruction")
            }
            stepCount++
        }

        return stepCount
    }

    private fun getCircle(
        start: String,
        instructions: String,
        nodeMap: Map<String, Node>
    ): Circle {
        var stepCount = 0L
        var current = start
        var circleSize = 0L
        val goalPositions = mutableListOf<Long>()
        val seenPositions = mutableListOf<Position>()
        while (true) {
            if (current.endsWith("Z")) {
                goalPositions.add(stepCount)
            }
            val position = Position(current, (stepCount % instructions.length).toInt())
            if (seenPositions.contains(position)) {
                circleSize = stepCount - seenPositions.indexOf(position)
                break
            } else {
                seenPositions.add(position)
            }
            val instruction = instructions[(stepCount % instructions.length).toInt()]
            current = if (instruction == 'L') {
                nodeMap[current]?.left ?: error("Node $current not found in map")
            } else if (instruction == 'R') {
                nodeMap[current]?.right ?: error("Node $current not found in map")
            } else {
                error("invalid instruction")
            }
            stepCount++
        }

        return Circle(goalPositions, circleSize)
    }

    data class Circle(
        val goalReachings: MutableList<Long>,
        val size: Long,
    )

    data class Position(
        val node: String,
        val instructionIdx: Int,
    )

    fun solvePart2(): Long {
        val input = Day8::class.java.getResource("day8.txt")?.readText()?.split("\r\n") ?: error("Can't read input")
        val instructions = input.first().trim()
        val nodeMap = input.drop(2).map { Node.parse(it) }.associateBy { it.current }
        val circles = nodeMap.keys.filter { it.endsWith("A") }
            .map { getCircle(it, instructions, nodeMap) }
        return findLCMOfListOfNumbers(circles.map { it.goalReachings.first() })
        // val finalCircle = circles.reduceRight { previous, acc -> combineCircles(previous, acc) }
        // return finalCircle.goalReachings.first()
    }

    fun findLCM(a: Long, b: Long): Long {
        val larger = if (a > b) a else b
        val maxLcm = a * b
        var lcm = larger
        while (lcm <= maxLcm) {
            if (lcm % a == 0L && lcm % b == 0L) {
                return lcm
            }
            lcm += larger
        }
        return maxLcm
    }

    fun findLCMOfListOfNumbers(numbers: List<Long>): Long {
        var result = numbers[0]
        for (i in 1 until numbers.size) {
            result = findLCM(result, numbers[i])
        }
        return result
    }

    fun combineCircles(first: Circle, second: Circle): Circle {
        val firstIndices = first.goalReachings
        val secondIndices = second.goalReachings
        while (true) {
            if (firstIndices.intersect(secondIndices).isNotEmpty()) {
                println("combined circles")
                return Circle(firstIndices.intersect(secondIndices).toMutableList(), first.size*second.size)
            }
            firstIndices.addAll(firstIndices.map { it + first.size })
            secondIndices.addAll(secondIndices.map { it + second.size })
        }
    }

    data class Node(
        val current: String,
        val left: String,

        val right: String,
    ) {
        companion object {
            fun parse(input: String): Node {
                val (current, next) = input.split(" = ")
                val (left, right) = input.substringAfter("(").substringBefore(")").split(", ")
                return Node(
                    current.trim(),
                    left.trim(),
                    right.trim()
                )
            }
        }
    }
}