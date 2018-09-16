package quam.features

import assertk.assert
import org.junit.jupiter.api.Test
import quam.Qubit
import quam.generateQubits
import quam.isCloseTo
import quam.prob
import java.util.*

class TeleportationFeature {

    private val random = Random()

    @Test
    fun `teleport 0`() {
        assert(generateQubits(100, random)
                .map { teleport(it) }
                .prob { it.measure() }).isCloseTo(0.0)
    }

    @Test
    fun `teleport 1`() {
        assert(generateQubits(100, random)
                .map { it.not() }
                .map { teleport(it) }
                .prob{ it.measure() }).isCloseTo(1.0)
    }

    @Test
    fun `teleport +`() {
        assert(generateQubits(1000, random)
                .map { it.hadamard() }
                .map { teleport(it) }
                .prob { it.measure() }).isCloseTo(0.5, 0.05)
    }

    private fun teleport(phi : Qubit) : Qubit {
        val a = phi.compose()
        val b = a.compose()

        a.hadamard()
        a.cnot(b)
        phi.cnot(a)
        phi.hadamard()

        if (a.measure()) b.not()
        if (phi.measure()) b.z()

        return b
    }
}
