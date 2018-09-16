package quam.features

import org.junit.jupiter.api.Test
import quam.ComplexMatrix
import quam.Qubits
import quam.permutationMatrix
import java.util.*
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class DeutschsAlgorithmFeature {

    private val random = Random(1L)

    @Test
    fun `constant identity`() {
        assertFalse(isBalanced(oracle()))
    }

    @Test
    fun `constant flip both bits`() {
        assertFalse(isBalanced(oracle(0 to 1, 2 to 3)))
    }

    @Test
    fun `balanced flip first bit`() {
        assertTrue(isBalanced(oracle(0 to 1)))
    }

    @Test
    fun `balanced flip second bit`() {
        assertTrue(isBalanced(oracle(2 to 3)))
    }

    private fun isBalanced(oracle: ComplexMatrix) = prepare()
            .map { it.apply(0, 1, oracle) }
            .map { decode(it) }.get()

    private fun prepare() = Optional.of(Qubits(2, random))
            .map { it.not(1) }
            .map { it.hadamard(0) }
            .map { it.hadamard(1) }

    private fun oracle(vararg pairs: Pair<Int, Int>) = permutationMatrix(4, *pairs)

    private fun decode(qubits: Qubits) = qubits
            .hadamard(0)
            .measure(0)
}
