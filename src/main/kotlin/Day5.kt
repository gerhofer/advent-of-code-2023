import kotlin.math.max
import kotlin.math.min

fun main(args: Array<String>) {
    println("Part 1: ${Day5.solvePart1()}")
    println("Part 2: ${Day5.solvePart2()}")
}

object Day5 {
    fun solvePart1(): Long {
        val input = Day5::class.java.getResource("day5.txt")?.readText() ?: error("Can't read input")
        val groups = input.split("\r\n\r\n").filter { it.isNotBlank() }
        val seeds = groups[0].substringAfter(":").trim().split(" ").mapNotNull { it.toLongOrNull() }
        val almanac = Almanac.parse(groups)
        val locations = seeds.map { almanac.getLocation(it) }
        return locations.min()

    }

    fun solvePart2(): Long {
        val input = Day5::class.java.getResource("day5.txt")?.readText() ?: error("Can't read input")
        val groups = input.split("\r\n\r\n").filter { it.isNotBlank() }
        val seeds = groups[0].substringAfter(":").trim().split(" ").mapNotNull { it.toLongOrNull() }
        val almanac = Almanac.parse(groups)
        val locations = seeds.windowed(2, 2)
            .map {
                val loc = almanac.getLocation(LongRange(it.first, it.first + it.last))
                println(loc)
                loc
            }
        return locations.min()
    }

    class Almanac(
        val seedToSoilMap: AlmanacMap,
        val soilToFertilizer: AlmanacMap,
        val fertilizerToWater: AlmanacMap,
        val waterToLight: AlmanacMap,
        val lightToTemperature: AlmanacMap,
        val temperatureToHumidity: AlmanacMap,
        val humidityToLocation: AlmanacMap
    ) {

        fun getLocation(seedRange: LongRange): Long {
            val soilRanges = seedToSoilMap.convert(mutableListOf(seedRange))
            val fertilizerRanges = soilToFertilizer.convert(soilRanges)
            val water = fertilizerToWater.convert(fertilizerRanges)
            val light = waterToLight.convert(water)
            val temperature = lightToTemperature.convert(light)
            val humidity = temperatureToHumidity.convert(temperature)
            return humidityToLocation.convert(humidity).minOf { it.min() }
        }

        fun getLocation(seed: Long): Long {
            val soil = seedToSoilMap.convert(seed)
            val fertilizer = soilToFertilizer.convert(soil)
            val water = fertilizerToWater.convert(fertilizer)
            val light = waterToLight.convert(water)
            val temperature = lightToTemperature.convert(light)
            val humidity = temperatureToHumidity.convert(temperature)
            return humidityToLocation.convert(humidity)
        }

        companion object {
            fun parse(groupedStrings: List<String>): Almanac {
                return Almanac(
                    AlmanacMap.parse(groupedStrings[1]),
                    AlmanacMap.parse(groupedStrings[2]),
                    AlmanacMap.parse(groupedStrings[3]),
                    AlmanacMap.parse(groupedStrings[4]),
                    AlmanacMap.parse(groupedStrings[5]),
                    AlmanacMap.parse(groupedStrings[6]),
                    AlmanacMap.parse(groupedStrings[7]),
                )
            }
        }

    }

    class AlmanacMap(
        val entries: List<AlmanacMapEntry>
    ) {
        fun convert(source: Long): Long {
            for (entry in entries) {
                if (source >= entry.sourceRangeStart && source <= (entry.sourceRangeStart + entry.length)) {
                    return entry.destinationRangeStart + (source - entry.sourceRangeStart)
                }
            }
            return source
        }

        fun convert(sources: MutableList<LongRange>): MutableList<LongRange> {
            val ranges: MutableList<LongRange> = mutableListOf()
            while (sources.isNotEmpty()) {
                val source = sources.first
                for (entry in entries) {
                    val overlap = LongRange(
                        max(entry.sourceRange.first, source.first),
                        min(entry.sourceRange.last, source.last)
                    )
                    if (!overlap.isEmpty()) {
                        val resultRange = LongRange(
                            entry.destinationRangeStart + (overlap.first - entry.sourceRangeStart),
                            entry.destinationRangeStart + (overlap.last - entry.sourceRangeStart)
                        )
                        ranges.add(resultRange)
                        sources.remove(source)
                        if (overlap.first > source.first) {
                            sources.add(LongRange(source.first, overlap.first - 1))
                        }
                        if (source.endInclusive > overlap.last) {
                            sources.add(LongRange(overlap.last + 1, source.last))
                        }
                        break
                    }
                }
                if (sources.contains(source)) {
                    ranges.add(source)
                    sources.remove(source)
                }
            }
            return ranges
        }

        companion object {
            fun parse(multilines: String): AlmanacMap {
                val entries = multilines.split("\n")
                    .drop(1)
                    .map { it.trim().split(" ").mapNotNull { it.toLongOrNull() } }
                    .map { AlmanacMapEntry(it[0], it[1], it[2]) }
                return AlmanacMap(entries)
            }
        }
    }

    data class AlmanacMapEntry(
        val destinationRangeStart: Long,
        val sourceRangeStart: Long,
        val length: Long
    ) {
        val sourceRange: LongRange
            get() {
                return LongRange(sourceRangeStart, sourceRangeStart + length)
            }
    }
}