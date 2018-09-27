package quam

import assertk.assert
import org.junit.jupiter.api.Test
import quam.*

class QftShould {

    @Test
    fun `qft one qubit`() {
        assert(qft(1)).isCloseTo(oneOverSqrt(2) * ComplexMatrix(2,
                1.0, 1.0,
                1.0, -1.0))
    }

    @Test
    fun `qft 2 qubits`() {
        assert(qft(2)).isCloseTo(0.5 * ComplexMatrix(4,
                ONE, ONE, ONE, ONE,
                ONE, i, -ONE, -i,
                ONE, -ONE, ONE, -ONE,
                ONE, -i, -ONE, i))
    }

    @Test
    fun `inverse qft one qubit`() {
        assert(invQft(1)).isCloseTo(oneOverSqrt(2) * ComplexMatrix(2,
                1.0, 1.0,
                1.0, -1.0))
    }

    @Test
    fun `inverse qft 2 qubits`() {
        assert(invQft(2)).isCloseTo(0.5 * ComplexMatrix(4,
                ONE, ONE, ONE, ONE,
                ONE, -i, -ONE, i,
                ONE, -ONE, ONE, -ONE,
                ONE, i, -ONE, -i))
    }
}
