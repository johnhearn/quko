package quam.features

import org.junit.jupiter.api.Test
import quam.*
import java.lang.Math.*
import java.util.*
import kotlin.test.assertEquals

class GroversSearchFeature {

    private val random = Random(1L)

    @Test
    fun `search for 3`() {
        assertEquals(0b011, groversSearch(3) {it == 0b011})
    }

    @Test
    fun `search for 13`() {
        assertEquals(0b1011, groversSearch(4) {it == 0b1011})
    }

    @Test
    fun `search in 7 bits`() {
        assertEquals(0b1101011, groversSearch(7) {it == 0b1101011})
    }

    fun groversSearch(n: Int, f: (Int) -> Boolean): Int {
        val oracle = oracle(n) { x -> f(x) }
        val diffusion = diffusion(n)
        val iterations = (PI * sqrt(1.0 * pow2(n)) / 4).toInt()

        val phi = Qubits(n, random).hadamard(0 until n)

        repeat(iterations) {
            phi.apply(0, oracle).hadamard(0 until n).apply(0, diffusion).hadamard(0 until n)
        }

        return convert(phi.measureAll())
    }

    private fun diffusion(n: Int): ComplexMatrix {
        val zero = ComplexVector(1.0, 0.0)
        val zeron = (0 until n-1).fold(zero) { acc, _ -> acc kronecker zero }
        val outer = zeron outer zeron
        return 2.0 * outer - identity(pow2(n))
    }

    private fun identity(width: Int) = ComplexMatrix(width) { x, y ->
        if (x == y) ONE else ZERO
    }

    private fun oracle(n: Int, f: (Int) -> Boolean) = ComplexMatrix(pow2(n)) { x, y ->
        when {
            x != y -> ComplexNumber(0.0)
            f(x) -> ComplexNumber(-1.0)
            else -> ComplexNumber((1.0))
        }
    }
}
