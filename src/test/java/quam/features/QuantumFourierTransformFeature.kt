package quam.features

import org.junit.jupiter.api.Test
import quam.*
import java.lang.Math.PI
import kotlin.test.assertEquals

class QuantumFourierTransformFeature {

    @Test
    fun `qft one qubit`() {
        assertEquals(oneOverSqrt(2) * ComplexMatrix(2,
                1.0, 1.0,
                1.0, -1.0), qft(1))
    }

    @Test
    fun `qft 2 qubits`() {
        assertEquals(0.5 * ComplexMatrix(4,
                ONE, ONE, ONE, ONE,
                ONE, i, -ONE, -i,
                ONE, -ONE, ONE, -ONE,
                ONE, -i, -ONE, i), qft(2))
    }

    @Test
    fun `inverse qft one qubit`() {
        assertEquals(oneOverSqrt(2) * ComplexMatrix(2,
                1.0, 1.0,
                1.0, -1.0), invQft(1))
    }

    @Test
    fun `inverse qft 2 qubits`() {
        assertEquals(0.5 * ComplexMatrix(4,
                ONE, ONE, ONE, ONE,
                ONE, -i, -ONE, i,
                ONE, -ONE, ONE, -ONE,
                ONE, i, -ONE, -i), invQft(2))
    }
}

private fun qft(m: Int): ComplexMatrix {
    val n = pow2(m)
    val nthRootOfUnity = nthRootOfUnity(n)
    return oneOverSqrt(n) * ComplexMatrix(n, { x, y -> pow(nthRootOfUnity, x * y) })
}

private fun invQft(m: Int): ComplexMatrix {
    val n = pow2(m)
    val nthRootOfUnity = nthRootOfUnity(n)
    return oneOverSqrt(n) * ComplexMatrix(n, { x, y -> pow(nthRootOfUnity, y * x).conjugate() })
}

private fun oneOverSqrt(n: Int) = (1 / Math.sqrt(n.toDouble()))

fun pow(complexNumber: ComplexNumber, m: Int): ComplexNumber {
    return (0 until m).fold(ONE) { acc, _ -> acc * complexNumber }
}

private fun nthRootOfUnity(n: Int): ComplexNumber {
    return exp(2 * PI * i / n)
}
