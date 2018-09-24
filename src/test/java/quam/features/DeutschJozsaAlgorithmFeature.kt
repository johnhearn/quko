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

    private fun isBalanced(m: Int, f: (Int) -> Int) = prepare(m)
            .map { it.apply(0, oracle(m, 1) { x, y -> f(x) xor y }) }
            .map { decode(it) }.get()

    private fun prepare(m: Int) = Optional.of(Qubits(m + 1, random)
            .not(m)
            .hadamard(0 until m + 1))

    private fun decode(qubits: Qubits) = convert(qubits
            .hadamard(0 until qubits.size - 1)
            .measureFirst(qubits.size - 1)) != 0
}
