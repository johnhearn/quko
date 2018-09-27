package quam

import kotlin.math.pow

fun qft(bits: Int): ComplexMatrix {
    val n = 2 pow bits
    val nthRootOfUnity = nthRootOfUnity(n)
    return oneOverSqrt(n) * ComplexMatrix(n) { x, y -> nthRootOfUnity pow x * y }
}

fun invQft(m: Int): ComplexMatrix {
    val n = 2 pow m
    val nthRootOfUnity = nthRootOfUnity(n)
    return oneOverSqrt(n) * ComplexMatrix(n) { x, y -> (nthRootOfUnity pow y * x).conjugate() }
}

private fun nthRootOfUnity(n: Int)
        = exp(2 * Math.PI * i / n)

fun oneOverSqrt(n: Int)
        = (1 / Math.sqrt(n.toDouble()))

private infix fun ComplexNumber.pow(m: Int)
        = (0 until m).fold(ONE) { acc, _ -> acc * this }

private infix fun Int.pow(n:Int) = this.toDouble().pow(n).toInt()

