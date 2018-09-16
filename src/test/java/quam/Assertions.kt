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
    if (Math.abs(actual - expect) > tol)
        expected("to be within $tol: ", expect, actual)
}

fun assertk.Assert<ComplexNumber>.isCloseTo(expect: ComplexNumber, tol: Double = 0.000001) {
    if (Math.abs(actual.a - expect.a) > tol || Math.abs(actual.b - expect.b) > tol)
        expected("to be within $tol: ", expect, actual)
}

fun <T> Sequence<T>.prob(predicate: (T) -> Boolean): Double {
    var count = 0
    var success = 0
    for (element in this) {
        count++
        if (predicate(element)) success++
    }
    return success.toDouble() / count
}
