package quam.features

import org.junit.jupiter.api.Test
import quam.*
import java.util.*
import kotlin.test.assertEquals

/**
 * The problem is that given a mapping
 *
 * f : (List&lt;Boolean>) -> List&lt;Boolean>
 *
 * (as a black box) with the added property that there is a special value, s, such
 * that f(x) = f(x xor s) for all x, then find what s is.
 *
 * Take, for example, the 3-bit case where s = 011. So
 * f(000) = f(011) = 010
 * f(100) = f(111) = 110
 * f(001) = f(010) = 101
 * f(101) = f(110) = 001
 * (https://www.cs.vu.nl/~tcs/ac/ac-11_simon_algorithm.pdf)
 */
class SimonsProblemFeature {

    private val random = Random(35L)

    @Test
    fun `identity function evaluates to 0^n`() {
        assertEquals(0b000, simonsProblem(3) { it })
    }

    @Test
    fun `3-bit case in comment s=011`() {
        // Worked example (s=011) from here - https://www.cs.vu.nl/~tcs/ac/ac-11_simon_algorithm.pdf
        val fppt = mapping(
                0b000 to 0b010, 0b011 to 0b010,
                0b100 to 0b110, 0b111 to 0b110,
                0b001 to 0b101, 0b010 to 0b101,
                0b101 to 0b001, 0b110 to 0b001)

        assertEquals(0b011, simonsProblem(3, fppt))
    }

    @Test
    fun `3-bit case in wikipedia s=110`() {
        // Wikipedia example (s=110) from here - https://en.wikipedia.org/wiki/Simon%27s_problem#Example
        val fWiki = mapping(
                0b000 to 0b101,
                0b001 to 0b010,
                0b010 to 0b000,
                0b011 to 0b110,
                0b100 to 0b000,
                0b101 to 0b110,
                0b110 to 0b101,
                0b111 to 0b010)

        assertEquals(0b110, simonsProblem(3, fWiki))
    }

    private fun simonsProblem(n: Int, f: (Int) -> Int): Int {
        val oracle = oracle(n, n) { x, y -> f(x) xor y }

        val ys = (0..n*2).map { y(n, oracle) }
                .filter { it != listOf(false, false, false) }
                .distinct()

        println(ys.map { format(convert(it), n)})

        // "Solve" by going through possibilities for s and checking xor_sum of y*s is 0 for all y
        for(s in (1 until pow2(n))) {
            if(ys.map { convert(it) }
                    .map { it and s }
                    .map {
                        // xor over i of (y_i * s_i)
                        (0..n).fold(0) { acc, i ->
                            acc xor ((it ushr i and 1) * (s ushr i and 1))
                        }
                    }
                    .all { it == 0 })
                return s
        }
        return 0
    }

    fun y(n: Int, oracle: ComplexMatrix): List<Boolean> {
        return Qubits(n, random)
                .hadamard(0 until n)
                .ancillary(n)
                .apply(0, oracle)
                //.measureLast(width)
                .hadamard(0 until n)
                .measureFirst(n)
    }

    private fun format(x: Int, width: Int) = x.toByte().toString(2).padStart(width, '0')
}
