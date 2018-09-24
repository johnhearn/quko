package quam

import org.junit.jupiter.api.Test
import assertk.assert
import assertk.assertions.*
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class QubitsShould {

    private val random = Random(2L)

    @Test
    fun `compose qubits together and measure first bit`() {
        assertTrue(generateQubits(1000)
                .all { it.measure(0) == false })
    }

    @Test
    fun `compose qubits together and measure second bit`() {
        assertTrue(generateQubits(1000)
                .all { it.measure(0) == false })
    }

    @Test
    fun `apply not operator to first qubit and measure`() {
        assertTrue(generateQubits(1000)
                .map { it.not(0) }
                .all { it.measure(0) == true })
    }

    @Test
    fun `apply not operator to second qubit and measure`() {
        assertTrue(generateQubits(1000)
                .map { it.not(1) }
                .all { it.measure(1) == true })
    }

    @Test
    fun `apply half not operator to first bit and measure`() {
        assert(generateQubits(100)
                .map { it.halfNot(0) }
                .count { it.measure(0) }).isBetween(45, 55)
    }

    @Test
    fun `apply half not operator to second bit and measure`() {
        assert(generateQubits(100)
                .map { it.halfNot(1) }
                .count { it.measure(1) }).isBetween(45, 55)
    }

    @Test
    fun `apply swap to 01`() {
        assertTrue(generateQubits(1000)
                .map { it.not(1) }
                .map { it.swap(0, 1) }
                .all { it.measureAll() == listOf(true, false) })
    }

    @Test
    fun `apply swap to 10`() {
        assertTrue(generateQubits(1000)
                .map { it.not(0) }
                .map { it.swap(0, 1) }
                .all { it.measureAll() == listOf(false, true) })
    }

    @Test
    fun `apply swap to adjacent indices of 100`() {
        assertTrue(generateQubits(1000, 3)
                .map { it.not(0) }
                .map { it.swap(0, 1) }
                .all { it.measureAll() == listOf(false, true, false) })
    }

    @Test
    fun `apply swap to non-adjacent indices 100`() {
        assertTrue(generateQubits(1000, 3)
                .map { it.not(0) }
                .map { it.swap(0, 2) }
                .all { it.measureAll() == listOf(false, false, true) })
    }

    @Test
    fun `apply swap to adjacent indices of 010`() {
        assertTrue(generateQubits(1000, 3)
                .map { it.not(1) }
                .map { it.swap(1, 2) }
                .all { it.measureAll() == listOf(false, false, true) })
    }

    @Test
    fun `apply swap to non-adjacent indices 100 directly`() {
        val it = Qubits(3, random)
        it.not(0)
        it.swap(0, 2)
        assertEquals(listOf(false, false, true), it.measureAll())
    }

    @Test
    fun `not apply conditional not to 00`() {
        assertTrue(generateQubits(1000)
                .map { it.cnot(0, 1) }
                .all { it.measureAll() == listOf(false, false) })
    }

    @Test
    fun `apply conditional not to 10`() {
        assertTrue(generateQubits(1000)
                .map { it.not(0) }
                .map { it.cnot(0, 1) }
                .all { it.measureAll() == listOf(true, true) })
    }

    @Test
    fun `not apply conditional not to 01`() {
        assertTrue(generateQubits(1000)
                .map { it.not(1) }
                .map { it.cnot(0, 1) }
                .all { it.measureAll() == listOf(false, true) })
    }

    @Test
    fun `apply conditional not to 11`() {
        assertTrue(generateQubits(1000)
                .map { it.not(0) }
                .map { it.not(1) }
                .map { it.cnot(0, 1) }
                .all { it.measureAll() == listOf(true, false) })
    }

    @Test
    fun `create entangled state where qubits are always measured the same`() {
        assertTrue(generateQubits(1000)
                .map { it.hadamard(0) }
                .map { it.cnot(0, 1) }
                .all { it.measure(0) == it.measure(1) })
    }

    @Test
    fun `create entangled state where qubits are always measured different`() {
        assertTrue(generateQubits(1000)
                .map { it.hadamard(0) }
                .map { it.not(1) }
                .map { it.cnot(0, 1) }
                .all { it.measure(0) != it.measure(1) })
    }

    @Test
    fun `create 3 qubit state and operate on first bit`() {
        assertTrue(generateQubits(10, 3)
                .map { it.not(0) }
                .all { it.measureAll() == listOf(true, false, false) })
    }

    @Test
    fun `create 3 qubit state and operate on second bit`() {
        assertTrue(generateQubits(10, 3)
                .map { it.not(1) }
                .all { it.measureAll() == listOf(false, true, false) })
    }

    @Test
    fun `create 3 qubit state and operate on third bit`() {
        assertTrue(generateQubits(10, 3)
                .map { it.not(2) }
                .all { it.measureAll() == listOf(false, false, true) })
    }

    @Test
    fun `create 4 qubit state and entangle first and third bits directly`() {
        assertTrue(generateQubits(10, 4)
                .map { it.cnot(0, 2) }
                .all { it.measure(0) == it.measure(2) && it.measure(1) == false && it.measure(3) == false })
    }

    @Test
    fun `create 3 qubit state and operate on third bit directly`() {
        val it = Qubits(3, random)
        it.not(2)
        assertTrue(it.measureAll() == listOf(false, false, true) )
    }

    @Test
    fun `create 4 qubit state and operate on third bit directly`() {
        val it = Qubits(4, random)
        it.not(2)
        assertTrue(it.measureAll() == listOf(false, false, true, false) )
    }

    @Test
    fun `create 10 qubit state and operate on third and 7th bits directly`() {
        val it = Qubits(10, random)
        it.not(2)
        it.not(7)
        assertTrue(it.measureAll() == listOf(false, false, true, false, false, false, false, true, false, false) )
    }

    @Test
    fun `reset to zeroed state`() {
        assert(generateQubits(1000)
                .map { it.not(0) }
                .map { it.not(1) }
                .map { it.reset() }
                .all { it.measureAll() == listOf(false, false) })
    }

    @Test
    fun `compute size of system`() {
        assert(Qubits(1).size).isEqualTo(1)
        assert(Qubits(2).size).isEqualTo(2)
        assert(Qubits(3).size).isEqualTo(3)
        assert(Qubits(4).size).isEqualTo(4)
        assert(Qubits(19).size).isEqualTo(19)
    }


    @Test
    fun `have useful ket toString with single entry`() {
        val v = Qubits(1).hadamard(0)
        assertEquals("|0> 0.707\n|1> 0.707", v.toString())
    }

    @Test
    fun `have useful ket toString with multiple entries`() {
        val v = Qubits(2).hadamard(0..1)
        assertEquals("|00> 0.500\n|01> 0.500\n|10> 0.500\n|11> 0.500", v.toString())
    }

    @Test
    fun `display 0 state correctly`() {
        val v = Qubits(1)
        assertEquals("|0> 1\n|1> 0", v.toString())
    }

    @Test
    fun `display 1 state correctly`() {
        val v = Qubits(1)
        assertEquals("|0> 1\n|1> 0", v.toString())
    }

    @Test
    fun `display 00 state correctly`() {
        val v = Qubits(2)
        assertEquals("|00> 1\n|01> 0\n|10> 0\n|11> 0", v.toString())
    }

    @Test
    fun `display 01 state correctly`() {
        val v = Qubits(2).not(0)
        assertEquals("|00> 0\n|01> 1\n|10> 0\n|11> 0", v.toString())
    }

    @Test
    fun `display 10 state correctly`() {
        val v = Qubits(2).not(1)
        assertEquals("|00> 0\n|01> 0\n|10> 1\n|11> 0", v.toString())
    }

    @Test
    fun `display 11 state correctly`() {
        val v = Qubits(2).not(0).not(1)
        assertEquals("|00> 0\n|01> 0\n|10> 0\n|11> 1", v.toString())
    }

    @Test
    fun `display 011 state correctly`() {
        val v = Qubits(3).not(0).not(1)
        assertEquals(
                "|000> 0\n|001> 0\n|010> 0\n|011> 1\n" +
                        "|100> 0\n|101> 0\n|110> 0\n|111> 0", v.toString())
    }

    @Test
    fun `display 001 state correctly`() {
        val v = Qubits(3).not(0)
        assertEquals(
                "|000> 0\n|001> 1\n|010> 0\n|011> 0\n" +
                        "|100> 0\n|101> 0\n|110> 0\n|111> 0", v.toString())
    }

    private fun generateQubits(num: Int, size: Int = 2)
            = (1..num).map { Qubits(size, random) }
}
