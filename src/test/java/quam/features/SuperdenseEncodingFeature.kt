package quam.features

import org.junit.jupiter.api.Test
import quam.Qubit
import quam.generateQubits
import java.util.*
import kotlin.test.assertTrue

class SuperdenseEncodingFeature {

    private val random = Random()

    private fun prepare(num: Int, random: Random)
            = generateQubits(num, random)
            .map { it.hadamard() }
            .map { it.cnot(it.compose()) }

    private fun decode(qubits: Pair<Qubit, Qubit>): Pair<Boolean, Boolean> {
        qubits.first.cnot(qubits.second)
        qubits.first.hadamard()
        return Pair(qubits.first.measure(), qubits.second.measure())
    }

    @Test
    fun `encode 00`() {
        assertTrue(prepare(100, random)
                .map { it } // I
                .map { decode(it) }
                .all { it == Pair(false, false) })
    }

    @Test
    fun `encode 01`() {
        assertTrue(prepare(100, random)
                .map { Pair(it.first.not(), it.second) } // X
                .map { decode(it) }
               .all { it == Pair(false, true) })
    }

    @Test
    fun `encode 10`() {
        assertTrue(prepare(100, random)
                .map { Pair(it.first.z(), it.second) } // Z
                .map { decode(it) }
                .all { it == Pair(true, false) })
    }

    @Test
    fun `encode 11`() {
        assertTrue(prepare(100, random)
                .map { Pair(it.first.not().z(), it.second) } // X.Z
                .map { decode(it) }
                .all { it == Pair(true, true) })
    }
}
