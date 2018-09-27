package quam

import java.util.*

class Qubits(private var state: State = State(), private val random: Random = Random(), private val index: Int) {

    var size = state.size

    constructor(num: Int, random: Random = Random()) : this(composeState(num), random, 0)

    fun apply(k: Int, gate: ComplexMatrix) = apply {
        state = state.apply(k, gate)
    }

    fun apply(k: Int, j: Int, gate: ComplexMatrix) = apply {
        state = state.apply(k, j, gate)
    }

    fun not(index: Int) = apply(index, PAULI_X)

    fun halfNot(index: Int) = apply(index, HALF_NOT)

    fun hadamard(index: Int) = apply(index, HADAMARD)

    fun hadamard(range: IntRange) = apply { range.forEach { hadamard(it) } }

    fun z(index: Int) = apply(index, PAULI_Z)

    fun cnot(k: Int, j: Int) = apply(k, j, CNOT)

    fun swap(k: Int, j: Int) = apply(k, j, SWAP)

    fun cphase(k: Int, j: Int, m: Int) = apply(k, j, cphase(m))

    fun measure(index: Int) = run {
        val prob = state.probFalse(index)
        val result = random.nextDouble() > prob
        state = state.apply(index, if (result) M1 else M0).normalise()
        result
    }

    fun measure(range: IntRange): List<Boolean> = MutableList(range.count()) { i -> measure(range.first + i) }

    fun measureFirst(n: Int) = measure(0 until n)

    fun measureLast(n: Int) = measure((size - n) until size)

    fun measureAll(): List<Boolean> = MutableList(size) { i -> measure(index + i) }

    internal fun compose() = apply {
        state = state kronecker State()
    }

    fun compose(qubits: Qubits) = apply {
        state = state kronecker qubits.state
        qubits.state = state // Systems now share state
    }

    fun ancillary(qubits: Int) = apply {
        state = state kronecker composeState(qubits)
        size += qubits
    }

    fun reset() = apply {
        state = composeState(state.size)
    }

    override fun hashCode() = state.hashCode()

    override fun equals(other: Any?) = when (other) {
        is Qubits -> state == other.state
        else -> false
    }

    override fun toString() = state.toString()
}
