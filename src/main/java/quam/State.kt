package quam

import java.lang.Math.PI

class State(initialState: ComplexVector = FALSE) {

    val components = initialState

    val size = log2n(initialState.components.size)

    private fun log2n(size: Int) = (Math.log(1.0 * size) / Math.log(2.0)).toInt()

    fun apply(k : Int, gate: ComplexMatrix) = State(lift(k, gate) * components)

    fun apply(k: Int, j: Int, gate: ComplexMatrix) = State(lift(k, j, gate) * components)

    infix fun kronecker(other: State) = State(components kronecker other.components)

    fun probFalse(k: Int = 0) = (components inner (lift(k, M0) * components)).l2norm()

    private fun lift(k: Int, j: Int, gateU: ComplexMatrix): ComplexMatrix {
        require(j > k) {"j !> k"}
        require(j > 0) {"j !> 0"}
        require(gateU.width == 4)

        val gateVhat = lift(j-1, gateU)

        // Optimisation to avoid calculating empty pi_j_k
        if (j == k + 1)
            return gateVhat

        if (j > k) {
            val pi_j_k = (k..j-2).fold(identity(gateVhat.width)) { acc, i -> acc * tau(i) }
            return pi_j_k.transpose() * gateVhat * pi_j_k
        }

        throw UnsupportedOperationException()
    }

    private val identity = { width : Int -> _identity(width) }.memoize()

    private fun _identity(width: Int)
            = (0 until log2n(width)-1).fold(IDENTITY_2) { acc, _ -> acc tensor IDENTITY_2 }

    private fun tau(i: Int) = lift(i, SWAP)

    private fun lift(k: Int, gate: ComplexMatrix): ComplexMatrix {
        val sizeOfGate = log2n(gate.width)
        return (0..size - sizeOfGate)
                .map { if (it == k) gate else identity(2) }
                .reduce { acc, elem -> acc tensor elem }
    }

    override fun equals(other: Any?) = when(other) {
        is State -> components == other.components
        else -> false
    }

    override fun toString()= components.toString()

    internal fun normalise() = State(components.normalise())

    companion object {
        private val ONE_OVER_ROOT_2 = 1.0 / Math.sqrt(2.0)
        private val FALSE = ComplexVector(1.0, 0.0)
        private val TRUE = ComplexVector(0.0, 1.0)

        val IDENTITY_2 = ComplexMatrix(2, 1.0, 0.0, 0.0, 1.0)
        val PAULI_X      = ComplexMatrix(2, 0.0, 1.0, 1.0, 0.0)
        val PAULI_Z      = ComplexMatrix(2, 1.0, 0.0, 0.0, -1.0)
        val HALF_NOT = 0.5 * ComplexMatrix(2, 1.0 + 1.0 * i, 1.0 - 1.0 * i, 1.0 - 1.0 * i, 1.0 + 1.0 * i)
        val HADAMARD = ONE_OVER_ROOT_2 * ComplexMatrix(2, 1.0, 1.0, 1.0, -1.0)
        val CNOT = ComplexMatrix(4,
                1.0, 0.0, 0.0, 0.0,
                0.0, 1.0, 0.0, 0.0,
                0.0, 0.0, 0.0, 1.0,
                0.0, 0.0, 1.0, 0.0)
        val SWAP = ComplexMatrix(4,
                1.0, 0.0, 0.0, 0.0,
                0.0, 0.0, 1.0, 0.0,
                0.0, 1.0, 0.0, 0.0,
                0.0, 0.0, 0.0, 1.0)

        val M1 = TRUE outer TRUE
        val M0 = FALSE outer FALSE

        val CPHASE_2 = ComplexMatrix(4,
                1.0 + 0.0 * i, 0.0 + 0.0 * i, 0.0 + 0.0 * i, 0.0 + 0.0 * i,
                0.0 + 0.0 * i, 1.0 + 0.0 * i, 0.0 + 0.0 * i, 0.0 + 0.0 * i,
                0.0 + 0.0 * i, 0.0 + 0.0 * i, 1.0 + 0.0 * i, 0.0 + 0.0 * i,
                0.0 + 0.0 * i, 0.0 + 0.0 * i, 0.0 + 0.0 * i, exp((2 * PI / pow2(2)) * i))
    }
}

private fun pow2(n: Int)
        = Math.pow(2.0, n.toDouble()).toInt()

fun composeState(count: Int) = (2..count).fold(State()) { a, _ -> a kronecker State() }

class Memoize1<in T, out R>(val f: (T) -> R) : (T) -> R {
    private val values = mutableMapOf<T, R>()
    override fun invoke(x: T): R {
        return values.getOrPut(x) { f(x) }
    }
}

fun <T, R> ((T) -> R).memoize(): (T) -> R = Memoize1(this)

