package quam

val i = ComplexNumber(0.0, 1.0)
val ZERO = ComplexNumber(0.0)
val ONE = ComplexNumber(1.0)

data class ComplexNumber(val a: Double, val b: Double = 0.0) {
    operator fun plus(other: ComplexNumber)
            = ComplexNumber(this.a + other.a, this.b + other.b)

    operator fun minus(other: ComplexNumber)
            = ComplexNumber(this.a - other.a, this.b - other.b)

    operator fun unaryMinus()
            = ComplexNumber(-this.a, -this.b)

    operator fun times(other: ComplexNumber)
            = ComplexNumber(this.a * other.a - this.b * other.b, this.a * other.b + this.b * other.a)

    operator fun times(d: Double)
            = ComplexNumber(this.a * d, this.b * d)

    operator fun div(d: Double)
            = ComplexNumber(this.a / d, this.b / d)

    operator fun div(d: Int)
            = ComplexNumber(this.a / d, this.b / d)

    fun l2norm()
            = Math.sqrt(a * a + b * b)

    fun conjugate()
            = ComplexNumber(a, -b)

    override fun toString()
            = "${format(a)} + i${format(b)}".replace(" + i0", "")

    private fun format(a: Double)
            = String.format("%.3f", a).replace(".000", "")
}

operator fun Double.times(rhs: ComplexNumber)
        = rhs.times(this)

operator fun Double.plus(rhs: ComplexNumber)
        = ComplexNumber(this + rhs.a, rhs.b)

operator fun Double.minus(rhs: ComplexNumber)
        = ComplexNumber(this - rhs.a, -rhs.b)

fun exp(x: ComplexNumber)
        = Math.exp(x.a) * ComplexNumber(Math.cos(x.b), Math.sin(x.b))