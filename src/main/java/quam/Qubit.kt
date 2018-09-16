package quam

import java.util.*

class Qubit internal constructor(private val random: Random, private val system: Qubits, private val index: Int) {

    @JvmOverloads
    constructor(random: Random = Random()) : this(random, Qubits(State(), random, 0), 0)

    fun measure() = system.measure(index)

    operator fun not() = apply { system.not(index) }

    fun halfNot() = apply { system.halfNot(index) }

    fun hadamard() = apply { system.hadamard(index) }

    fun z() = apply { system.z(0) }

    fun compose(): Qubit {
        system.compose()
        return Qubit(random, system, index + 1)
    }

    fun cnot(second: Qubit): Pair<Qubit, Qubit> {
        require(this.system == second.system)
        require(this != second)
        system.cnot(this.index, second.index)
        return Pair(this, second)
    }
}
