fun main(args: Array<String>) {
    //println("Part 1: ${Day12.solvePart1()}") // 6827
    println("Part 2: ${Day12.solvePart2()}")
}

object Day12 {
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

    fun solvePart2(): Int {
        val input = Day12::class.java.getResource("day12.txt")?.readText() ?: error("Can't read input")
        val conditions = input.split("\r\n")
            .map { line -> SpringCondition.parseComplex(line) }
            .mapIndexed { idx, springCondition ->
                val options = generateOptions2(listOf(Option(springCondition.pattern, 1)), springCondition)
                println("finished option generation for $idx")
                options.filter { it.option.matches(springCondition.regex) }
            }
            .toMutableList()
        return conditions.sumOf { it.sumOf { a -> a.count } }
    }

    fun generateOptions2(startingPatterns: List<Option>, condition: SpringCondition): List<Option> {
        if (startingPatterns.none { it.option.contains("?") }) {
            return startingPatterns
        }
        val options = mutableListOf<Option>()
        for (option in startingPatterns.filter { it.option.contains("?") }) {
            val pattern = option.option
            if (pattern.matches(condition.possibleRegex)) {
                val option1 = pattern.replaceFirst("?", ".")
                val option2 = pattern.replaceFirst("?", "#")
                options.addAll(listOf(Option(option1, option.count), Option(option2, option.count)))
            }
        }
        val mergedOptions = mergeOptions(options)
        return generateOptions2(mergedOptions, condition)
    }

    fun mergeOptions(options: List<Option>): List<Option> {
        val grouped = options.groupBy { toGroups(it.option.substringBefore("?")) }
        return grouped.map { Option(it.value.first().option, it.value.sumOf { x -> x.count }) }
    }

    fun toGroups(pattern: String): Group {
        val patterns = mutableListOf<Int>()
        var currentGroup = 0
        for (letter in pattern) {
            if (letter == '.' && currentGroup > 0) {
                patterns.add(currentGroup)
                currentGroup = 0
            }
            if (letter == '#') {
                currentGroup++
            }
        }
        return Group(patterns, currentGroup)
    }

    data class Group(
        val finishedOptions: List<Int>,
        val openOption: Int,
    )

    data class Option(
        val option: String,
        val count: Int
    )

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
        val possibleRegex: Regex get() = Regex("[\\., \\?]*" + constraints.joinToString("[\\., \\?]+") { "[#, \\?]{${it}}" } + "[\\., \\?]*")


        companion object {
            fun parse(line: String): SpringCondition {
                return SpringCondition(
                    line.substringBefore(" "),
                    line.substringAfter(" ").split(",").map { it.toInt() }
                )
            }

            fun parseComplex(line: String): SpringCondition {
                val originalPattern = line.substringBefore(" ")
                var finalPattern = originalPattern
                repeat(4) { finalPattern += "?$originalPattern" }
                val originalConditions = line.substringAfter(" ").split(",").map { it.toInt() }
                var finalConditions = originalConditions.toMutableList()
                repeat(4) { finalConditions.addAll(originalConditions) }
                return SpringCondition(
                    finalPattern,
                    finalConditions.toList()
                )
            }
        }
    }
}