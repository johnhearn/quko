package quam.features

import org.junit.jupiter.api.Test
import quam.*
import java.util.*
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class DeutschJozsaAlgorithmFeature {

    private val random = Random(1L)

    @Test
    fun `constant 0`() {
        assertFalse(isBalanced(4) { 0 })
    }

    @Test
    fun `constant 1`() {
        assertFalse(isBalanced(4) { 1 })
    }

    @Test
    fun `balanced complement`() {
        assertTrue(isBalanced(4) { it.inv() })
    }

    @Test
    fun `balanced identity`() {
        assertTrue(isBalanced(4) { it })
    }

    @Test
    fun `balanced xor`() {
        assertTrue(isBalanced(4) { it xor 0 })
    }

    private fun isBalanced(n: Int, f: (Int) -> Int) = prepare(n)
            .map { it.apply(0, oracle(n, f)) }
            .map { decode(it) }.get()

    private fun prepare(n: Int) = Optional.of(Qubits(n + 1, random)
            .not(n)
            .hadamard(0 until n + 1))

    private fun oracle(n: Int, f: (Int) -> Int): ComplexMatrix {
        val map = mutableMapOf<Int, Int>()
        val size = pow2(n + 1)
        for (x in 0 until size)
            for (y in 0..1) {
                val xy = (x shl 1) + y      // |x>|y>
                val fxy = f(x) xor y        // |f(x)⊕y>
                val xfxy = (x shl 1) + fxy  // |x>|f(x)⊕y>
                println("${format(x, n)} ${format(y, n)} | ${format(x, n)} ${format(fxy, n)} | $xy $xfxy")
                map[xy] = xfxy
            }
        return permutationMatrix(size) { map[it] ?: it }
    }

    private fun decode(qubits: Qubits) = convert(qubits
            .hadamard(0 until qubits.size - 1)
            .measureFirst(qubits.size - 1)) != 0

    private fun format(x: Int, n: Int) = (x and (pow2(n + 1) - 1)).toByte().toString(2).padStart(n, '0')
}
