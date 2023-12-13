fun main(args: Array<String>) {
    // 6827 println("Part 1: ${Day12.solvePart1()}")
    println("Part 2: ${Day12.solvePart1Two()}")
}

object Day12 {
    val generatedOptions: MutableMap<Long, List<String>> = mutableMapOf<Long, List<String>>()

    fun solvePart1(): Int {
        val input = Day12::class.java.getResource("day12.txt")?.readText() ?: error("Can't read input")
        val conditions = input.split("\r\n")
            .map { line -> SpringCondition.parse(line) }
            .map { springCondition ->
                val options = generateOptions(listOf(springCondition.pattern))
                options.filter { it.matches(springCondition.regex) }
            }
            .toMutableList()
        return conditions.sumOf { it.size }
    }

    fun generateOptions(startingPatterns: List<String>): List<String> {
        if (startingPatterns.none { it.contains("?") }) {
            return startingPatterns
        }
        val options = mutableListOf<String>()
        for (pattern in startingPatterns.filter { it.contains("?") }) {
            val option1 = pattern.replaceFirst("?", ".")
            val option2 = pattern.replaceFirst("?", "#")
            options.addAll(listOf(option1, option2))
        }
        return generateOptions(options)
    }

    fun solvePart1Two(): Int {
        val input = Day12::class.java.getResource("day12.txt")?.readText() ?: error("Can't read input")
        val conditions = input.split("\r\n")
            .map { line -> SpringCondition.parse(line) }
        generateOptions(conditions)

        return conditions.sumOf { condition ->
            val options = generatedOptions[condition.pattern.length.toLong()]
                ?: error("No options found generated for length ${condition.pattern.length}")
            val count = options.count { it.matches(condition.patternRegex) && it.matches(condition.regex) }
            println(count)
            count
        }
    }

    private fun generateOptions(conditions: List<SpringCondition>) {
        val maxSize = conditions.maxOf { it.pattern.length }
        for (i in 1L..maxSize) {
            val options = generatedOptions.getOrDefault(i - 1, listOf(""))
            generatedOptions[i] = options.map { it + "." } + options.map { it + "#" }
        }
    }

    fun solvePart2(): Int {
        val input = Day12::class.java.getResource("day12.txt")?.readText() ?: error("Can't read input")
        val conditions = input.split("\r\n")
            .map { line -> SpringCondition.parse(line) }
        val maxSize = conditions.maxBy { it.pattern.length }

        conditions.map { springCondition ->
            val patternRegex = springCondition.patternRegex
            val regex = springCondition.regex
        }
            .toMutableList()
        return 1
    }

    data class SpringCondition(
        val pattern: String,
        val constraints: List<Int>,
    ) {
        val patternRegex: Regex
            get() = Regex(
                pattern.replace(".", "\\.")
                    .replace("?", "[\\., \\#]")
            )

        val regex: Regex get() = Regex("\\.*" + constraints.joinToString("\\.+") { "#{${it}}" } + "\\.*")

        companion object {
            fun parse(line: String): SpringCondition {
                return SpringCondition(
                    line.substringBefore(" "),
                    line.substringAfter(" ").split(",").map { it.toInt() }
                )
            }
        }
    }

}