fun main(args: Array<String>) {
    println("Part 1: ${Day9.solvePart1()}")
    println("Part 2: ${Day9.solvePart2()}")
}

object Day9 {

    fun solvePart1(): Long {
        val input = Day9::class.java.getResource("day9.txt")?.readText()?.split("\r\n") ?: error("Can't read input")
        val predictions = input.map { predictFuture(it.split(" ").mapNotNull { word -> word.trim().toLongOrNull() }) }
        return predictions.sum()
    }

    fun predictFuture(sensorValues: List<Long>): Long {
        val predictionRows = calculateDifferencesUntilZero(sensorValues)
        for (i in 1..predictionRows.size - 1) {
            val diffToReach = predictionRows[predictionRows.size - i].last()
            val seenValue = predictionRows[predictionRows.size - i - 1].last()
            predictionRows[predictionRows.size - i - 1].add(diffToReach + seenValue)
        }
        return predictionRows.first().last()
    }

    private fun calculateDifferencesUntilZero(sensorValues: List<Long>): MutableList<MutableList<Long>> {
        val predictionRows = mutableListOf<MutableList<Long>>()
        predictionRows.add(sensorValues.toMutableList())
        var values = sensorValues
        while (true) {
            val differences = values.windowed(2, 1) { (first, second) ->
                second - first
            }
            predictionRows.add(differences.toMutableList())
            if (differences.all { it == 0L }) {
                break
            }
            values = differences
        }
        return predictionRows
    }

    fun solvePart2(): Long {
        val input = Day9::class.java.getResource("day9.txt")?.readText()?.split("\r\n") ?: error("Can't read input")
        val predictions = input.map { predictPast(it.split(" ").mapNotNull { word -> word.trim().toLongOrNull() }) }
        return predictions.sum()
    }

    fun predictPast(sensorValues: List<Long>): Long {
        val predictionRows = calculateDifferencesUntilZero(sensorValues)
        for (i in 1..predictionRows.size - 1) {
            val diffToReach = predictionRows[predictionRows.size - i].first()
            val seenValue = predictionRows[predictionRows.size - i - 1].first()
            predictionRows[predictionRows.size - i - 1].addFirst(seenValue - diffToReach)
        }
        return predictionRows.first().first()
    }

}