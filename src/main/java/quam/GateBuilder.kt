package quam

import java.lang.Math.PI

class GateBuilder {

    fun lift(n: Int, k: Int, gate: ComplexMatrix): ComplexMatrix {
        val sizeOfGate = log2n(gate.width)
        return (0..n-sizeOfGate)
                .map { if (it == k) gate else identity(2) }
                .reduce { acc, elem -> acc tensor elem }
    }

    fun lift(n: Int, k: Int, j: Int, gateU: ComplexMatrix): ComplexMatrix {
        require(j > k) {"j !> k"}
        require(j > 0) {"j !> 0"}
        require(gateU.width == 4)

        val gateVhat = lift(n, j-1, gateU)

        // Optimisation to avoid calculating empty pi_j_k
        if (j == k + 1)
            return gateVhat

        if (j > k) {
            val pi_j_k = (k..j-2).fold(identity(gateVhat.width)) { acc, i -> acc * tau(n, i) }
            return pi_j_k.transpose() * gateVhat * pi_j_k
        }

        throw UnsupportedOperationException()
    }

    private fun tau(n: Int, i: Int) = lift(n, i, SWAP)

    private val identity = { width : Int ->
        (0 until log2n(width)-1).fold(IDENTITY_2) { acc, _ -> acc tensor IDENTITY_2 }
    }.memoize()
}

private fun pow2(n: Int)
        = Math.pow(2.0, n.toDouble()).toInt()

private fun log2n(size: Int)
        = (Math.log(1.0 * size) / Math.log(2.0)).toInt()

private val ONE_OVER_ROOT_2 = 1.0 / Math.sqrt(2.0)

val IDENTITY_2 = ComplexMatrix(2,
        1.0, 0.0,
        0.0, 1.0)

val PAULI_X      = ComplexMatrix(2,
        0.0, 1.0,
        1.0, 0.0)
val PAULI_Z      = ComplexMatrix(2,
        1.0, 0.0,
        0.0, -1.0)
val HALF_NOT = 0.5 * ComplexMatrix(2,
        1.0 + 1.0 * i, 1.0 - 1.0 * i,
        1.0 - 1.0 * i, 1.0 + 1.0 * i)
val HADAMARD = ONE_OVER_ROOT_2 * ComplexMatrix(2,
        1.0, 1.0,
        1.0, -1.0)

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


val cphase = { m : Int -> ComplexMatrix(4,
        ONE, ZERO, ZERO, ZERO,
        ZERO, ONE, ZERO, ZERO,
        ZERO, ZERO, ONE, ZERO,
        ZERO, ZERO, ZERO, exp((2 * PI / pow2(m)) * i))
}.memoize()

val M1 = TRUE outer TRUE
val M0 = FALSE outer FALSE
