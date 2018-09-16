package quam

import assertk.assert
import assertk.assertions.isEqualTo
import assertk.assertions.isZero
import org.junit.jupiter.api.Test
import java.util.*

class QubitShould {

    private val random = Random(1L)

    @Test
    fun be_in_false_state_by_default() {
        assert(generateQubits(100, random).count { it.measure() }).isZero()
    }

    @Test
    fun be_in_false_state_after_applying_identity() {
        assert(generateQubits(100, random).count { it.measure() }).isZero()
    }

    @Test
    fun be_in_true_state_after_not_operation() {
        assert(generateQubits(100, random).count { it.not().measure() }).isEqualTo(100)
    }

    @Test
    fun be_in_half_true_half_false_state_after_half_not_operation() {
        assert(generateQubits(100, random).prob { it.halfNot().measure() }).isCloseTo(0.5, 0.05)
    }

    @Test
    fun apply_hadamard_operation() {
        assert(generateQubits(100, random).prob { it.hadamard().measure() }).isCloseTo(0.5, 0.05)
    }

    @Test
    fun `compose qubit`() {
        assert(generateQubits(100, random)
                .map { it.compose() }
                .prob { it.measure() }).isCloseTo(0.0)
    }

    @Test
    fun `apply operation to composed qubit`() {
        assert(generateQubits(100, random)
                .map { it.compose() }
                .map { it.not() }
                .prob { it.measure() }).isCloseTo(1.0)
    }

    @Test
    fun `apply operation to original qubit`() {
        assert(generateQubits(100, random)
                .map { Pair(it, it.compose()) }
                .map { it.first.not() }
                .prob { it.measure() }).isCloseTo(1.0)
    }

    @Test
    fun collapse() {
        val qubit = Qubit(random).halfNot()
        val collapsedState = qubit.measure()
        assert((1..100).count { qubit.measure() == collapsedState }).isEqualTo(100)
    }
}
