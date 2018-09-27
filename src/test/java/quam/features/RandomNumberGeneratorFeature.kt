package quam.features

import assertk.assert
import org.junit.jupiter.api.Test
import quam.*
import java.util.*

class RandomNumberGeneratorFeature {

    private val random = Random(1L)

    private val tol = 0.05

    @Test
    fun `generate random bit`() {
        assert(generateQubits(100, random)
                .map { it.halfNot() }
                .prob { it.measure() }).isCloseTo(1.0/2, tol)
    }

    @Test
    fun `generate random two bits`() {
        assert(generateQubits(2, 100, random)
                .map { it.hadamard(0..1) }
                .prob { it.measureAll().toInt() == 0 }).isCloseTo(1.0/4, tol)
    }

    @Test
    fun `generate random three bits`() {
        assert(generateQubits(3, 100, random)
                .map { it.hadamard(0..2) }
                .prob { it.measureAll().toInt() == 0 }).isCloseTo(1.0/8, tol)
    }
}
