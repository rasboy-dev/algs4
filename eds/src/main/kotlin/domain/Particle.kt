package domain

import edu.princeton.cs.algs4.StdDraw
import java.awt.Color
import kotlin.math.sqrt

/**
 *  The [Particle] class represents a particle moving in the unit box,
 *  with a given position, velocity, radius, and mass.
 */
class Particle(
    private val radius: Double = 0.008,
    private var rx: Double = Math.random() * (1.0 - 2 * radius) + radius,
    private var ry: Double = Math.random() * (1.0 - 2 * radius) + radius,
    private var vx: Double = (Math.random() - 0.5) / 50,
    private var vy: Double = (Math.random() - 0.5) / 50,
    private val mass: Double = 0.01,
    private val color: Color = Color.BLACK
) {

    var count: Int = 0

    fun move(dt: Double) {
        rx += vx * dt
        ry += vy * dt
    }

    fun draw() {
        StdDraw.setPenColor(color)
        StdDraw.filledCircle(rx, ry, radius)
    }

    fun timeToHit(that: Particle): Double {
        if (this == that) return Double.POSITIVE_INFINITY
        val dx = that.rx - this.rx
        val dy = that.ry - this.ry
        val dvx = that.vx - this.vx
        val dvy = that.vy - this.vy
        val dvdr = dx*dvx + dy*dvy
        if (dvdr > 0) return Double.POSITIVE_INFINITY
        val dvdv = dvx*dvx + dvy*dvy
        if (dvdv == 0.0) return Double.POSITIVE_INFINITY
        val drdr = dx*dx + dy*dy
        val sigma = this.radius + that.radius
        val d = (dvdr*dvdr) - dvdv * (drdr - sigma*sigma)
        if (d < 0) return Double.POSITIVE_INFINITY
        return -(dvdr + sqrt(d)) / dvdv
    }

    fun timeToHitHorizontalWall(): Double {
        return if (vy < 0) {
            (radius - ry) / vy
        } else if (vy > 0) {
            (1 - radius - ry) / vy
        } else {
            Double.POSITIVE_INFINITY
        }
    }

    fun timeToHitVerticalWall(): Double {
        return if (vx < 0) {
            (radius - rx) / vx
        } else if (vx > 0) {
            (1.0 - rx - radius) / vx
        } else {
            Double.POSITIVE_INFINITY
        }
    }

    fun bounceOf(that: Particle) {
        val dx = that.rx - this.rx
        val dy = that.ry - this.ry
        val dvx = that.vx - this.vx
        val dvy = that.vy - this.vy
        val dvdr = dx*dvx + dy*dvy
        val dist = this.radius + that.radius

        // magnitude of normal force
        val magnitude = 2 * this.mass * that.mass * dvdr / ((this.mass + that.mass) * dist)
        val fx = magnitude * dx / dist
        val fy = magnitude * dy / dist

        this.vx += fx / this.mass
        this.vy += fy / this.mass
        that.vx -= fx / that.mass
        that.vy -= fy / that.mass
        this.count++
        that.count++
    }

    fun bounceOfHorizontalWall() {
        vy = -vy
        count++
    }

    fun bounceOfVerticalWall() {
        vx = -vx
        count++
    }
}