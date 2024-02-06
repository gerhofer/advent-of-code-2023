fun main(args: Array<String>) {
    println("Part 1: ${Day25.solvePart1()}")
}

object Day25 {

    fun solvePart1(): Int {
        val input = Day22::class.java.getResource("day25.txt")?.readText() ?: error("Can't read input")
        val nodes = parseNodes(input)
        val firstCluster = mutableSetOf<String>()
        val secondCluster = mutableSetOf<String>()
        val start = nodes.keys.first()
        firstCluster.add(start)
        println(start)

        for (other in nodes.keys - start) {
            if (hasMoreThanThreeConnections(nodes, start, other)) {
                firstCluster.add(other)
            } else {
                secondCluster.add(other)
            }
        }

        println("First:")
        println(firstCluster)
        println("Seond:")
        println(secondCluster)
        return firstCluster.size * secondCluster.size
    }

    private fun hasMoreThanThreeConnections(nodes: Map<String, List<String>>, start: String, goal: String): Boolean {
        val distinctCount = countDistinctConnections(nodes, start, goal)
        return distinctCount > 3
    }

    private fun countDistinctConnections(nodes: Map<String, List<String>>, start: String, goal: String): Int {
        val visitedConnections = mutableSetOf<Path>()
        var count = 0
        while (true) {
            val shortestPath = shortestPath(nodes, start, goal, visitedConnections)
            if (goal == "ntq") {
                println(shortestPath + " to ntq")
            }
            if (shortestPath.isEmpty() || count > 3) {
                return count
            } else {
                visitedConnections.addAll(toPaths(shortestPath))
                count ++
            }
        }
    }

    private fun toPaths(path: List<String>): Set<Path> {
        return path.windowed(2) { (a, b) -> Path(setOf(a, b))}.toSet()
    }

    private fun shortestPath(nodes: Map<String, List<String>>, start: String, goal: String, already: Set<Path>): List<String> {
        var allPaths = listOf(            listOf(start))
        val visitedConnections = already.toMutableSet()
        while (allPaths.isNotEmpty()) {
            val newPaths = mutableListOf<List<String>>()
            for (path in allPaths) {
                val current = path.last()
                if (current == goal) {
                    return path
                }
                val neighbours = nodes[current] ?: error("$current has no neighbours")
                val validNeighbours = neighbours.filter { n -> !visitedConnections.contains(Path(setOf(n, current))) }
                for (vNeighbour in validNeighbours) {
                    visitedConnections.add(Path(setOf(vNeighbour, current)))
                    newPaths.add(path + vNeighbour)
                }
            }
            allPaths = newPaths
        }
        return emptyList()
    }

    data class Path(val nodes: Set<String>)

    private fun parseNodes(input: String): Map<String, List<String>> {
        val nodes = input.split("\r\n").associate { line ->
            val parts = line.trim().split(":")
            val left = parts[0].trim()
            val right = parts[1].trim().split(" ")
            left to right
        }
        val all = nodes.flatMap { it.value + it.key }.toSet()
        return all.map {
            it to nodes.getOrDefault(it, emptyList()) + nodes.filter { a -> a.value.contains(it) }.map { a -> a.key }
        }.toMap()
    }


}