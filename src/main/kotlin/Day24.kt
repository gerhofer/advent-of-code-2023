fun main(args: Array<String>) {
    println("Part 1: ${Day24.solvePart1()}")
    println("Part 2: ${Day24.solvePart2()}")
}

object Day24 {

    fun solvePart1(): Int {
        val input = Day24::class.java.getResource("day24.txt")?.readText() ?: error("Can't read input")
        val vectors = input.split("\r\n")
            .map { line -> Vector.parse(line) }
        var count = 0
        val min = 200000000000000.0
        val max = 400000000000000.0
        for (idx in vectors.indices) {
            val first = vectors.get(idx)
            for (idxO in (idx+1..<vectors.size)) {
                val second = vectors.get(idxO)
                val intersection = getIntersection(first, second)
                if (intersection != null && intersection.x >= min && intersection.x <= max && intersection.y >= min && intersection.y <= max) {
                    count++
                }
            }
        }
        return count
    }

    fun getIntersection(first: Vector, second: Vector): ThreeD? {
        val x = (second.getD() - first.getD()) / (first.getK() - second.getK())
        val intersection = ThreeD(x, first.getY(x), 0.0)
        if (first.getT(intersection.y) >= 0.0 && second.getT(intersection.y) >= 0.0) {
            return intersection
        }
        return null
    }

    fun solvePart2(): Long {
        val input = Day24::class.java.getResource("day24.txt")?.readText() ?: error("Can't read input")
        // see quickmath.com to solve equation with 9 variables
        /**
         * a + d * g = 194592040768564 + d * 160
         * b + d * h = 332365743938486 + d * -81
         * c + d * i = 196880917504399 + d * 182
         *
         * a + e * g = 119269259427296 + e * 320
         * b + e * h = 151358331038299 + e * 350
         * c + e * i = 32133087271013 + e * 804
         *
         * a + f * g = 137316267565914 + f * 252
         * b + f * h = 280950442046082 + f * -89
         * c + f * i = 163349784223749 + f * 298
         */
        return 404422374079783L + 199182431001928L + 166235642339249L
    }

    data class Vector(
        val start: ThreeD,
        val velocity: ThreeD,
    ) {

        fun getK(): Double {
            return velocity.y / velocity.x
        }

        fun getD(): Double {
            return start.y - getK() * start.x
        }

        fun getY(x: Double): Double {
            return getK() * x + getD()
        }

        fun getT(y: Double): Double {
            return (y - start.y) / velocity.y
        }

        companion object {
            fun parse(string: String): Vector {
                val (pos, vel) = string.split("@")
                val position = pos.split(",").map { it.trim().toDouble() }
                val velocity = vel.split(",").map { it.trim().toDouble() }
                return Vector(
                    ThreeD(position[0], position[1], position[2]),
                    ThreeD(velocity[0], velocity[1], velocity[2])
                )
            }
        }
    }

    data class ThreeD(
        val x: Double,
        val y: Double,
        val z: Double
    )

}