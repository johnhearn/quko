package quam

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

fun pow2(n: Int)
        = Math.pow(2.0, n.toDouble()).toInt()

fun convert(input: List<Boolean>) = input.fold(0) { acc, b -> (acc shl 1) or if (b) 0b1 else 0b0 }

class MathsShould {

    @Test
    fun `compute 2^n`() {
        assertEquals(1, pow2(0))
        assertEquals(2, pow2(1))
        assertEquals(4, pow2(2))
        assertEquals(8, pow2(3))
        assertEquals(16, pow2(4))
        assertEquals(32, pow2(5))
    }

    @Test
    fun `convert boolean lists to integers`() {
        assertEquals(0b11001011, convert(listOf(true, true, false, false, true, false, true, true)))
    }
}