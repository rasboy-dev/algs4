package domain

import edu.princeton.cs.algs4.StdDraw

class Ball {
    private val radius: Double = 0.005
    private var rx: Double = Math.random() * (1.0 - 2 * radius) + radius
    private var ry: Double = Math.random() * (1.0 - 2 * radius) + radius
    private var vx: Double = (Math.random() - 0.5) / 50
    private var vy: Double = (Math.random() - 0.5) / 50

    fun move(dt: Double) {
        if (rx + vx * dt < radius || rx + vx * dt > 1.0 - radius) vx = -vx
        if (ry + vy * dt < radius || ry + vy * dt > 1.0 - radius) vy = -vy
        rx += vx * dt
        ry += vy * dt
    }

    fun draw() {
        StdDraw.filledCircle(rx, ry, radius)
    }
}