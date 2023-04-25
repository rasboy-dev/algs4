import domain.Ball
import edu.princeton.cs.algs4.StdDraw

fun main(args: Array<String>) {
    // Try adding program arguments via Run/Debug configuration.
    // Learn more about running applications: https://www.jetbrains.com/help/idea/running-applications.html.
    println("Program arguments: ${args.joinToString()}")

    val n = Integer.parseInt(args[0])
    val balls = Array(n) { Ball() }

    while (true) {
        StdDraw.clear()
        balls.map { ball -> ball.move(0.5); ball.draw() }
        StdDraw.show(25)
    }
}