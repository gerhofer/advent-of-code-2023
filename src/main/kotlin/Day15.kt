fun main(args: Array<String>) {
    println("Part 1: ${Day15.solvePart1()}")
    println("Part 2: ${Day15.solvePart2()}")
}

object Day15 {

    fun solvePart1(): Long {
        val input = Day15::class.java.getResource("day15.txt")?.readText() ?: error("Can't read input")
        return input.trim().split(",")
            .map { getHash(it).toLong() }
            .sum()
    }

    fun getHash(value: String): Int {
        var hash = 0
        for (letter in value) {
            hash += letter.code
            hash *= 17
            hash = hash % 256
        }
        return hash
    }

    fun solvePart2(): Long {
        val input = Day15::class.java.getResource("day15.txt")?.readText() ?: error("Can't read input")
        val boxes = (0..255).associateWith { mutableListOf<Lense>() }
        input.trim().split(",")
            .forEach {
                if (it.contains("=")) {
                    val (label, focalLength) = it.split("=")
                    val hash = getHash(label)
                    val indexOfMatch = boxes[hash]!!.indexOfFirst { lense -> lense.label == label }
                    if (indexOfMatch == -1) {
                        boxes[hash]!!.add(Lense(label, hash, focalLength.toInt()))
                    } else {
                        boxes[hash]!![indexOfMatch] = Lense(label, hash, focalLength.toInt())
                    }
                } else {
                    val label = it.substringBefore("-")
                    val hash = getHash(label)
                    boxes[hash]!!.removeIf { lense -> lense.label == label }
                }
            }
        return boxes.map { getFocusingPower(it.key, it.value) }
            .sum()
    }

    fun getFocusingPower(box: Int, lenses: List<Lense>): Long {
        var focusingPower = 0L
        for (i in lenses.indices) {
            val current = (box+1) * (i+1) * lenses[i].focalLength
            focusingPower += current
            //println("${lenses[i]} has power $current in box $box")
        }
        return focusingPower
    }

    data class Lense(
        val label: String,
        val hash: Int,
        val focalLength: Int,
    )
}