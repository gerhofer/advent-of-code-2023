fun main(args: Array<String>) {
    //println("Part 1: ${Day20.solvePart1()}")
    //println("Part 2: ${Day20.solvePart2()}")
    // run endlessly press stop fast, first 4 number of logs + 1 each :clown:
  //  println(Day8.findLCMOfListOfNumbers(listOf(3767L, 3923L, 3931L, 4007L)))
}

object Day20 {

    fun solvePart1(): Long {
        val input = Day20::class.java.getResource("day20.txt")?.readText() ?: error("Can't read input")
        val modules = input.split("\r\n")
            .map { Module.parse(it) }
        val moduleConfiguration = ModuleConfiguration(mutableMapOf(), mutableMapOf(), modules)
        repeat(1000) {
            sendPulses(moduleConfiguration, it)
        }

        println("Low: ${moduleConfiguration.lowCount}")
        println("High: ${moduleConfiguration.highCount}")

        return moduleConfiguration.lowCount * moduleConfiguration.highCount
    }

    fun sendPulses(moduleConfig: ModuleConfiguration, buttonRound: Int) {
        val pulseSendings = mutableListOf<PulseSending>()
        moduleConfig.lowCount++
        pulseSendings.add(PulseSending("broadcaster", PulseStrength.LOW, "button"))
        while (pulseSendings.isNotEmpty()) {
            val sending = pulseSendings.removeFirst()
            val newPulses = sendPulse(sending.destination, sending.pulse, moduleConfig, sending.from, buttonRound)
            pulseSendings.addAll(newPulses)
        }
    }

    fun sendPulse(
        destination: String,
        pulse: PulseStrength,
        moduleConfig: ModuleConfiguration,
        from: String,
        buttonRound: Int,
    ): List<PulseSending> {
        if (destination == "rx" && pulse == PulseStrength.LOW) {
            moduleConfig.rxLowCount++
        }
        val pulseSendings = mutableListOf<PulseSending>()
        val activatedModule = moduleConfig.module(destination)
        if (activatedModule == null) {
            return emptyList()
        } else if (activatedModule.type == Type.FLIP_FLOP) {
            if (pulse == PulseStrength.LOW) {
                val flipFlopState = moduleConfig.flipFlopModules.getOrDefault(destination, FlipFlopState.OFF)
                if (flipFlopState == FlipFlopState.OFF) {
                    moduleConfig.flipFlopModules[destination] = FlipFlopState.ON
                    activatedModule.destinationModules.forEach {
                        moduleConfig.highCount++
                        pulseSendings.add(PulseSending(it, PulseStrength.HIGH, destination))
                    }
                } else {
                    moduleConfig.flipFlopModules[destination] = FlipFlopState.OFF
                    activatedModule.destinationModules.forEach {
                        moduleConfig.lowCount++
                        pulseSendings.add(PulseSending(it, PulseStrength.LOW, destination))
                    }
                }
            }
        } else if (activatedModule.type == Type.CONJUNCTION) {
            moduleConfig.pulseMemories[from] = pulse
            if (activatedModule.name == "kl" && pulse == PulseStrength.HIGH) {
                println("$from got HIGH pulse in $buttonRound")
            }
            val mostRecentPulses = moduleConfig.conjunctionSources(destination)
                .map { moduleConfig.pulseMemories.getOrDefault(it.name, PulseStrength.LOW) }
            if (mostRecentPulses.all { it == PulseStrength.HIGH }) {
                activatedModule.destinationModules.forEach {
                    moduleConfig.lowCount++
                    pulseSendings.add(PulseSending(it, PulseStrength.LOW, destination))
                }
            } else {
                activatedModule.destinationModules.forEach {
                    moduleConfig.highCount++
                    pulseSendings.add(PulseSending(it, PulseStrength.HIGH, destination))
                }
            }
        } else {
            activatedModule.destinationModules.forEach {
                if (pulse == PulseStrength.HIGH) {
                    moduleConfig.highCount++
                } else {
                    moduleConfig.lowCount++
                }
                pulseSendings.add(PulseSending(it, pulse, destination))
            }
        }
        return pulseSendings
    }

    data class PulseSending(
        val destination: String,
        val pulse: PulseStrength,
        val from: String,
    )

    fun solvePart2(): Int {
        val input = Day20::class.java.getResource("day20.txt")?.readText() ?: error("Can't read input")
        val modules = input.split("\r\n").map { Module.parse(it) }
        val moduleConfiguration = ModuleConfiguration(mutableMapOf(), mutableMapOf(), input.split("\r\n").map { Module.parse(it) })
        val source = moduleConfiguration.conjunctionSources("kl")
        source.map {
            val modConf = ModuleConfiguration(mutableMapOf(), mutableMapOf(), input.split("\r\n").map { Module.parse(it) })
            val onTimes = getFirstThreeWhereIsOn(it, modConf)
            println(onTimes)
        }
        return -1
    }

    fun getFirstThreeWhereIsOn(module: Module, moduleConfiguration: ModuleConfiguration): List<Int> {
        val occurances = mutableListOf<Int>()
        var buttonPressCount = 0
        while (true) {
            sendPulses(moduleConfiguration, buttonPressCount)
            if (moduleConfiguration.pulseMemories[module.name] == PulseStrength.HIGH) {
                occurances.add(buttonPressCount)
                if (occurances.size == 1) {
                    return occurances
                }
            }
            buttonPressCount++
        }
    }

    data class ModuleConfiguration(
        val flipFlopModules: MutableMap<String, FlipFlopState>,
        val pulseMemories: MutableMap<String, PulseStrength>,
        val modules: List<Module>,
        var lowCount: Long = 0L,
        var highCount: Long = 0L,
        var rxLowCount: Int = 0,
    ) {
        fun module(name: String): Module? {
            if (name == "output") {
                return null
            }
            return modules.firstOrNull { it.name == name }
        }

        fun conjunctionSources(name: String): List<Module> =
            modules.filter { it.destinationModules.contains(name) }

    }

    data class Module(
        val type: Type,
        val name: String,
        val destinationModules: List<String>,
    ) {
        companion object {
            fun parse(line: String): Module {
                val (from, to) = line.split(" -> ")
                val destinations = to.trim().split(",").map { it.trim() }
                return if (from == "broadcaster") {
                    Module(Type.BROADCASTER, "broadcaster", destinations)
                } else {
                    val type = Type.fromCode(from[0])
                    val name = from.substring(1)
                    Module(type, name, destinations)
                }
            }
        }
    }

    enum class PulseStrength {
        LOW,
        HIGH;
    }

    enum class FlipFlopState {
        ON,
        OFF;
    }

    enum class Type {
        BROADCASTER,
        FLIP_FLOP,
        CONJUNCTION;

        companion object {
            fun fromCode(code: Char): Type =
                when (code) {
                    '%' -> FLIP_FLOP
                    '&' -> CONJUNCTION
                    else -> error("invalid type code for module $code")
                }

        }
    }

}