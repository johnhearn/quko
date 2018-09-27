package quam.features

import assertk.assert
import org.junit.jupiter.api.Test
import quam.*
import java.lang.Math.PI

class QuantumFourierTransformFeature {

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

private fun qft(bits: Int): ComplexMatrix {
    val n = 2 pow bits
    val nthRootOfUnity = nthRootOfUnity(n)
    return oneOverSqrt(n) * ComplexMatrix(n) { x, y -> nthRootOfUnity pow x * y }
}

private fun invQft(m: Int): ComplexMatrix {
    val n = 2 pow m
    val nthRootOfUnity = nthRootOfUnity(n)
    return oneOverSqrt(n) * ComplexMatrix(n) { x, y -> (nthRootOfUnity pow y * x).conjugate() }
}

private fun oneOverSqrt(n: Int)
        = (1 / Math.sqrt(n.toDouble()))

private infix fun ComplexNumber.pow(m: Int)
        = (0 until m).fold(ONE) { acc, _ -> acc * this }

private fun nthRootOfUnity(n: Int)
        = exp(2 * PI * i / n)
