fun main(args: Array<String>) {
    println("Part 1: ${Day19.solvePart1()}")
    println("Part 2: ${Day19.solvePart2()}")
}

object Day19 {

    fun solvePart1(): Long {
        val input = Day19::class.java.getResource("day19.txt")?.readText() ?: error("Can't read input")
        val (workflowsAsString, partsAsString) = input.split("\r\n\r\n")
        val workflows = workflowsAsString.split("\r\n")
            .map { Workflow.parse(it) }
            .associateBy { it.id }
        return partsAsString.split("\r\n")
            .map { Parts.parse(it) }
            .filter { isAccepted(workflows, it) }
            .sumOf { it.sum }
    }

    fun isAccepted(workflows: Map<String, Workflow>, part: Parts): Boolean {
        val start = "in"
        return isAccepted(workflows, workflows[start]!!, part)
    }

    fun isAccepted(workflows: Map<String, Workflow>, workflow: Workflow, part: Parts): Boolean {
        for (condition in workflow.conditions) {
            if (condition.check(part)) {
                if (condition.isDecision) {
                    return condition.goal == "A"
                } else {
                    return isAccepted(workflows, workflows[condition.goal] ?: error("Can't find workflow ${condition.goal}"), part)
                }
            }
        }

        error("No condition matched for part $part in workflow $workflow")
    }

    fun solvePart2(): Int {
        val input = Day19::class.java.getResource("day19.txt")?.readText() ?: error("Can't read input")
        return 0
    }

    data class Workflow(
        val id: String,
        val conditions: List<Condition>
    ) {
        companion object {
            fun parse(line: String): Workflow {
                val id = line.substringBefore("{")
                val conditions = line.substringAfter("{").substringBefore("}").split(",")
                    .map { Condition.parse(it) }
                return Workflow(id, conditions)
            }
        }
    }

    data class Condition(
        val variableToCheck: String?,
        val operator: Operator?,
        val against: Long?,
        val goal: String
    ) {
        val isDecision get() = goal == "A" || goal == "R"

        companion object {
            fun parse(line: String): Condition {
                return if (line.contains("<")) {
                    val (variable, value, goal) = line.split(Regex("<|:"))
                    Condition(variable, Operator.LESS, value.toLong(), goal)
                } else if (line.contains(">")) {
                    val (variable, value, goal) = line.split(Regex(">|:"))
                    Condition(variable, Operator.GREATER, value.toLong(), goal)
                } else {
                    Condition(null, null, null, line)
                }
            }
        }

        fun check(part: Parts): Boolean {
            return when(operator) {
                null -> true
                Operator.LESS -> part.get(variableToCheck!!) < against!!
                Operator.GREATER -> part.get(variableToCheck!!) > against!!
            }
        }
    }

    enum class Operator {
        LESS, GREATER
    }

    data class Parts(
        val x: Long,
        val m: Long,
        val a: Long,
        val s: Long,
    ) {
        companion object {
            fun parse(line: String): Parts {
                val values = line.substringAfter("{").substringBefore("}").split(",")
                return Parts(
                    values[0].substringAfter("=").toLong(),
                    values[1].substringAfter("=").toLong(),
                    values[2].substringAfter("=").toLong(),
                    values[3].substringAfter("=").toLong()
                )
            }
        }

        val sum get() = x + m + a + s

        fun get(value: String): Long {
            return when (value) {
                "x" -> x
                "m" -> m
                "a" -> a
                "s" -> s
                else -> error("invalid variable")
            }
        }

    }
}