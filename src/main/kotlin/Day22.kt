fun main(args: Array<String>) {
    println("Part 1: ${Day22.solvePart1()}")
    println("Part 2: ${Day22.solvePart2()}")
}

object Day22 {

    fun solvePart1(): Int {
        val input = Day22::class.java.getResource("day22.txt")?.readText() ?: error("Can't read input")
        val map = input.split("\r\n")
            .map { line -> line.trim().toList() }
        return 0
    }

    fun solvePart2(): Int {
        val input = Day22::class.java.getResource("day22.txt")?.readText() ?: error("Can't read input")
        return 0
    }

}