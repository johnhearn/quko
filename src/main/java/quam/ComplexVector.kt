package quam

import java.lang.Math.sqrt

/**
 * A vector in a complex n-dimensional Hilbert space.
 */
data class ComplexVector(val components: List<ComplexNumber>) {

    constructor(vararg components: ComplexNumber) :
            this(listOf(*components))

    constructor(vararg components: Double) : this(components.map { it + 0.0 * i })

    infix fun inner(other: ComplexVector) =
            this.components.zip(other.components)
                    .map { it.first.conjugate() * it.second }
                    .fold(ZERO) { a, b -> a + b }

    infix fun outer(other: ComplexVector): ComplexMatrix {
        return ComplexMatrix(components.size, list(other))
    }

    infix fun kronecker(other: ComplexVector) =
            ComplexVector(list(other))

    private fun list(other: ComplexVector) =
            this.components.map { lhs -> other.components.map { rhs -> lhs * rhs } }.flatten()

    fun normalise(): ComplexVector {
        val l2norm = l2norm()
        return if (l2norm != 1.0) (1.0 / l2norm) * this else this
    }

    fun l2norm() = sqrt(components.map { it.l2norm().let { norm -> norm * norm } }.sum())
}

operator fun Double.times(rhs: ComplexVector): ComplexVector {
    return ComplexVector(rhs.components.map { this * it })
}
