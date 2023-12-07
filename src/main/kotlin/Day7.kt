fun main(args: Array<String>) {
    println("Part 1: ${Day7.solvePart1()}")
    println("Part 2: ${Day7.solvePart2()}")
}

object Day7 {
    fun solvePart1(): Long {
        val input = Day7::class.java.getResource("day7.txt")?.readText() ?: error("Can't read input")
        val sortedHands = input.split("\r\n").filter { it.isNotBlank() }
            .map { Hand.parse(it) }
            .sorted()
        return sortedHands
            .mapIndexed { idx, hand -> hand.bid * (sortedHands.size - idx) }
            .sum()
    }

    fun solvePart2(): Long {
        val input = Day7::class.java.getResource("day7.txt")?.readText() ?: error("Can't read input")
        val sortedHands = input.split("\r\n").filter { it.isNotBlank() }
            .map { Hand.parseWithJokers(it) }
            .sorted()
        return sortedHands
            .mapIndexed { idx, hand -> hand.bid * (sortedHands.size - idx) }
            .sum()
    }

    class Hand(
        val cards: List<Card>,
        val bid: Long,
    ) : Comparable<Hand> {

        fun getType(): Type {
            val cardToAmount = cards.groupBy { it }.mapValues { it.value.size }.toMutableMap()
            val numberOfJokers = cardToAmount.getOrDefault(Card.JOKER, 0)
            cardToAmount.remove(Card.JOKER)
            return if (cardToAmount.size == 1 || cardToAmount.any { it.value + numberOfJokers == 5 } || numberOfJokers == 5) {
                Type.FIVE_OF_A_KIND
            } else if (cardToAmount.values.contains(4) || cardToAmount.any { it.value + numberOfJokers == 4 }) {
                Type.FOUR_OF_A_KIND
            } else if (cardToAmount.values.contains(3) || cardToAmount.any { it.value + numberOfJokers == 3 }) {
                val cardWithMaxAmount = cardToAmount.maxBy { it.value }
                val otherCards = cardToAmount.filter { it.key != cardWithMaxAmount.key }
                if (otherCards.values.contains(2)) {
                    Type.FULL_HOUSE
                } else {
                    Type.THREE_OF_A_KIND
                }
            } else if (cardToAmount.values.contains(2) || cardToAmount.any { it.value + numberOfJokers == 2 }) {
                if (cardToAmount.values.count { it == 2 } == 2) {
                    Type.TWO_PAIR
                } else {
                    Type.ONE_PAIR
                }
            } else {
                Type.HIGH_CARD
            }
        }

        override fun compareTo(other: Hand): Int {
            val typeDiff = other.getType().ordinal - this.getType().ordinal
            if (typeDiff != 0) {
                return typeDiff
            }
            for (i in (0..5)) {
                val cardDiff = other.cards[i].ordinal - this.cards[i].ordinal
                if (cardDiff != 0) {
                    return cardDiff
                }
            }
            return 0
        }

        override fun toString(): String {
            return cards.map { it.letter }.joinToString("")
        }

        companion object {
            fun parse(value: String): Hand {
                val (hand, bid) = value.split(" ")
                return Hand(
                    hand.trim().map { Card.fromString(it) },
                    bid.trim().toLong()
                )
            }

            fun parseWithJokers(value: String): Hand {
                val (hand, bid) = value.split(" ")
                return Hand(
                    hand.trim().map { Card.fromStringWithJoker(it) },
                    bid.trim().toLong()
                )
            }
        }

    }

    enum class Type {
        HIGH_CARD,
        ONE_PAIR,
        TWO_PAIR,
        THREE_OF_A_KIND,
        FULL_HOUSE,
        FOUR_OF_A_KIND,
        FIVE_OF_A_KIND
    }

    enum class Card(val letter: Char) {
        JOKER('J'),
        TWO('2'),
        THREE('3'),
        FOUR('4'),
        FIVE('5'),
        SIX('6'),
        SEVEN('7'),
        EIGHT('8'),
        NINE('9'),
        TEN('T'),
        JACK('J'),
        QUEEN('Q'),
        KING('K'),
        ACE('A');

        companion object {
            fun fromString(value: Char): Card {
                return when (value) {
                    'A' -> ACE
                    'K' -> KING
                    'Q' -> QUEEN
                    'J' -> JACK
                    'T' -> TEN
                    '9' -> NINE
                    '8' -> EIGHT
                    '7' -> SEVEN
                    '6' -> SIX
                    '5' -> FIVE
                    '4' -> FOUR
                    '3' -> THREE
                    '2' -> TWO
                    else -> error("Invalid card")
                }
            }

            fun fromStringWithJoker(value: Char): Card {
                return when (value) {
                    'A' -> ACE
                    'K' -> KING
                    'Q' -> QUEEN
                    'J' -> JOKER
                    'T' -> TEN
                    '9' -> NINE
                    '8' -> EIGHT
                    '7' -> SEVEN
                    '6' -> SIX
                    '5' -> FIVE
                    '4' -> FOUR
                    '3' -> THREE
                    '2' -> TWO
                    else -> error("Invalid card")
                }
            }
        }
    }

}