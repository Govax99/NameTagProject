import java.util.Scanner

fun fibonacci() {
    var prev = 0
    var current = 1
    for (i in 0..24) {
        val swap = prev
        prev = current
        current += swap
        println(current)
    }
}

fun main() {
    fibonacci()
}