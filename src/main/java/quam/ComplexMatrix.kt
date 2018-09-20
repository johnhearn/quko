package quam

class ComplexMatrix(val width : Int, internal val components: List<ComplexNumber>) {

    constructor(width : Int, vararg components: ComplexNumber) : this(width, listOf(*components))

    constructor(width : Int, vararg components: Double) : this(width, components.map { it + 0.0 * i })

    constructor(width: Int, vararg components: Int) : this(width, components.map { it.toDouble() + 0.0 * i })

    constructor(width: Int, f: (Int, Int) -> ComplexNumber) : this(width, (0 until width).map { x -> (0 until width).map{ y -> f(x,y) } }.flatten().toList())

    fun transpose() = ComplexMatrix(width, columns().flatten())

    operator fun minus(other: ComplexMatrix): ComplexMatrix {
        require(this.width == other.width)
        return ComplexMatrix(width,
                components.zip(other.components).map {
                    ComplexNumber(it.first.a - it.second.a, it.first.b - it.second.b)
                })
    }

    operator fun times(d: Double) = ComplexMatrix(width, components.map { d * it })

    operator fun times(other: ComplexVector): ComplexVector {
        require(this.width == other.components.size) { "Matrix($width) not compatible with Vector(${other.components.size})" }
        return ComplexVector(
                this.rows().map {
                    it.zip(other.components)
                            .map { it.first * it.second }
                            .fold(ZERO) { a, b -> a + b }
                })
    }

    operator fun times(other: ComplexMatrix): ComplexMatrix {
        require(this.rows().size == other.columns().size) { "Matrix(${rows().size}) not compatible with Matrix(${other.columns().size})" }
        return ComplexMatrix(components.size/width,
                this.rows().map { lhs -> other.columns()
                        .map { rhs -> lhs.zip(rhs) }
                        .map { it.map { it.first * it.second } }
                        .map { it.fold(ZERO) { a, b -> a + b } }
                }
                        .flatten())
    }

    infix fun directSum(rhs: ComplexMatrix): ComplexMatrix {
        return ComplexMatrix(this.width + rhs.width,
                listOf(
                        rows().map { concat(it, listOfZeros(rhs.width)) },
                        rhs.rows().map { concat(listOfZeros(width), it) }
                ).flatten().flatten())
    }

    private fun concat(it: List<ComplexNumber>, elements: List<ComplexNumber>) =
            listOf(it, elements).flatten()

    private fun listOfZeros(length: Int) = (1..length).map { ZERO }

    infix fun tensor(other: ComplexMatrix): ComplexMatrix {
        val top = rows().flatMap { dup(other.width, it) }.flatten().flatMap { dup(other.width, it) }
        val bottom = dup(width, other.rows().flatMap { dup(width, it) }.flatten()).flatten()
        val result = top.zip(bottom).map { it.first * it.second }

        return ComplexMatrix(width * other.width, result)
    }

    private fun columns()
            = (0 until width).map { this.components.slice(it until this.components.size step this.width) }

    private fun rows()
            = components.chunked(width)

    private fun <T> dup(times: Int, element: T)
            = MutableList(times) { element }

    override fun toString(): String {
        return rows().joinToString("\n") { it.joinToString(" | ") { it.toString() } }
    }

    override fun hashCode() = width.hashCode() + 31 * components.hashCode()

    override fun equals(other: Any?) = when (other) {
        is ComplexMatrix -> width == other.width && components == other.components
        else -> false
    }
}

operator fun Double.times(rhs: ComplexMatrix)
        = rhs.times(this)

fun permutationMatrix(width: Int, mapping: (Int) -> Int)
        = ComplexMatrix(width, (0 until width).map { oneHot(width, mapping(it)) }.flatten())

fun permutationMatrix(width: Int, vararg pairs : Pair<Int, Int>)
        = permutationMatrix(width, mapping(*pairs))

fun mapping(vararg pairs : Pair<Int, Int>) = { x:Int -> mapOf(*pairs)[x] ?: x }

private fun oneHot(length: Int, index: Int)
        = (0 until length).map { if (it == index) ONE else ZERO }

