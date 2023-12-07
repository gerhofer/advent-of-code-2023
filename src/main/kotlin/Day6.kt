fun main(args: Array<String>) {
    println("Part 1: ${Day6.solvePart1()}")
    println("Part 2: ${Day6.solvePart2()}")
}

object Day6 {
    fun solvePart1(): Long {
        val input = Day6::class.java.getResource("day6.txt")?.readText() ?: error("Can't read input")
        val lines = input.split("\r\n").filter { it.isNotBlank() }
        val times = lines[0].substringAfter("Time:").trim().split(" ").mapNotNull { it.trim().toIntOrNull() }
        val distances = lines[1].substringAfter("Distance:").trim().split(" ").mapNotNull { it.trim().toIntOrNull() }
        return (times.indices)
            .map { idx -> generateRaceOptions(times[idx].toLong(), distances[idx].toLong()) }
            .map { options -> options.size.toLong() }
            .foldRight(1) { a, b -> a * b }
    }

    fun generateRaceOptions(maxTime: Long, reachedWinningDistance: Long): List<Race> {
        val winningRaces = mutableListOf<Race>()
        for (timePressed in 0..maxTime) {
            val timeReleased = maxTime - timePressed
            val reachedDistance = timeReleased * timePressed
            if (reachedDistance > reachedWinningDistance) {
                winningRaces.add(Race(timePressed, reachedDistance))
            } else if (winningRaces.isNotEmpty()) {
                break
            }
        }
        return winningRaces
    }

    data class Race(
        val timePressed: Long,
        val reachedDistance: Long,
    )

    fun solvePart2(): Long {
        val input = Day6::class.java.getResource("day6.txt")?.readText() ?: error("Can't read input")
        val lines = input.split("\r\n").filter { it.isNotBlank() }
        val time = lines[0].substringAfter("Time:").trim().split(" ").joinToString("") { it.trim() }.toLong()
        val distance = lines[1].substringAfter("Distance:").trim().split(" ").joinToString("") { it.trim() }.toLong()
        val raceOptions = generateRaceOptions(time, distance)
        return raceOptions.size.toLong()
    }

}