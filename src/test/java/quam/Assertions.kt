package quam

import assertk.assertions.support.expected
import java.util.*


fun generateQubits(num: Int, random: Random)
        = generateSequence { Qubit(random) }.take(num)

fun generateQubits(width:Int, num: Int, random: Random)
        = generateSequence { Qubits(width, random) }.take(num)

fun <T> Iterable<T>.prob(predicate: (T) -> Boolean): Double {
    var count = 0
    var success = 0
    for (element in this) {
        count++
        if (predicate(element)) success++
    }
    return success.toDouble() / count
}

fun assertk.Assert<Double>.isCloseTo(expect: Double, tol: Double = 0.000001) {
    if (!actual.isCloseTo(expect, tol))
        expected("to be within $tol: ", expect, actual)
}

fun assertk.Assert<ComplexNumber>.isCloseTo(expect: ComplexNumber, tol: Double = 0.000001) {
    if (!actual.isCloseTo(expect, tol))
        expected("to be within $tol: ", expect, actual)
}

fun assertk.Assert<ComplexMatrix>.isCloseTo(expect: ComplexMatrix, tol: Double = 0.000001) {
    if (actual.width != expect.width
            || !actual.components.zip(expect.components).all { it.first.isCloseTo(it.second, tol) })
        expected("to be within $tol: ", expect, actual)
}

private fun ComplexNumber.isCloseTo(expect: ComplexNumber, tol: Double) =
        a.isCloseTo(expect.a, tol) || b.isCloseTo(expect.b, tol)

private fun Double.isCloseTo(expect: Double, tol: Double) = Math.abs(this - expect) < tol

fun <T> Sequence<T>.prob(predicate: (T) -> Boolean): Double {
    var count = 0
    var success = 0
    for (element in this) {
        count++
        if (predicate(element)) success++
    }
    return success.toDouble() / count
}
