fun main(args: Array<String>) {
    println("Part 1: ${Day13.solvePart1()}")
    println("Part 2: ${Day13.solvePart2()}")
}

object Day13 {

    fun solvePart1(): Long {
        val input = Day13::class.java.getResource("day13.txt")?.readText() ?: error("Can't read input")
        return input.split("\r\n\r\n")
            .map { it.split("\r\n").map { it.trim().split("").filter { it.isNotBlank() } }}
            .map { findMirror(it) }
            .sum()
    }

    fun findMirror(lavaPattern: List<List<String>>) : Long {
        val verticalMirror = findVerticalMirror(lavaPattern)
        if (verticalMirror != null) {
            return verticalMirror.toLong()
        }
        val horizontalMirror = findHorizontalMirror(lavaPattern)
        if (horizontalMirror != null) {
            return 100L * horizontalMirror
        }
        error("Lava pattern not having any mirror")
    }

    fun findVerticalMirror(lavaPattern: List<List<String>>): Int? {
        val nrOfColumns = lavaPattern.first().size
        for (mirror in 1..nrOfColumns-1) {
            var allMatch = true
            for (i in 0..mirror-1) {
                if ((mirror + mirror - i - 1) < nrOfColumns && lavaPattern.map { it[i] } != lavaPattern.map { it[mirror + mirror - i - 1]}) {
                    allMatch = false
                    break
                }
            }
            if (allMatch) {
                return mirror
            }
        }
        return null
    }

    fun findHorizontalMirror(lavaPattern: List<List<String>>): Int? {
        val nrOfRows = lavaPattern.size
        for (mirror in 1..nrOfRows) {
            var allMatch = true
            for (i in 0..mirror-1) {
                if ((mirror + mirror - i - 1) < nrOfRows && lavaPattern[i] != lavaPattern[mirror + mirror - i - 1]) {
                    allMatch = false
                    break
                }
            }
            if (allMatch) {
                return mirror
            }
        }
        return null
    }

    fun solvePart2(): Long {
        val input = Day13::class.java.getResource("day13.txt")?.readText() ?: error("Can't read input")
        return input.split("\r\n\r\n")
            .map { it.split("\r\n").map { it.trim().split("").filter { it.isNotBlank() } }}
            .map { findMirrorWithSingleSmudge(it) }
            .sum()
    }

    fun findMirrorWithSingleSmudge(lavaPattern: List<List<String>>) : Long {
        val verticalMirror = findVerticalMirrorWithSingleSmudge(lavaPattern)
        if (verticalMirror != null) {
            return verticalMirror.toLong()
        }
        val horizontalMirror = findHorizontalMirrorWithSingleSmudge(lavaPattern)
        if (horizontalMirror != null) {
            return 100L * horizontalMirror
        }
        error("Lava pattern not having any mirror")
    }

    fun findVerticalMirrorWithSingleSmudge(lavaPattern: List<List<String>>): Int? {
        val nrOfColumns = lavaPattern.first().size
        for (mirror in 1..nrOfColumns-1) {
            var differences = 0
            for (i in 0..mirror-1) {
                if ((mirror + mirror - i - 1) < nrOfColumns) {
                    val one = lavaPattern.map { it[i] }
                    val other = lavaPattern.map { it[mirror + mirror - i - 1]}
                    differences += one.mapIndexed { index, value -> value != other[index] }.count { it }
                    if (differences > 1) {
                        break
                    }
                }
            }
            if (differences == 1) {
                return mirror
            }
        }
        return null
    }

    fun findHorizontalMirrorWithSingleSmudge(lavaPattern: List<List<String>>): Int? {
        val nrOfRows = lavaPattern.size
        for (mirror in 1..nrOfRows-1) {
            var differences = 0
            for (i in 0..mirror-1) {
                if ((mirror + mirror - i - 1) < nrOfRows) {
                    val one = lavaPattern[i]
                    val other = lavaPattern[mirror + mirror - i - 1]
                    differences += one.mapIndexed { index, value -> value != other[index] }.count { it }
                    if (differences > 1) {
                        break
                    }
                }
            }
            if (differences == 1) {
                return mirror
            }
        }
        return null
    }

}