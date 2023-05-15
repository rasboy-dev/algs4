import domain.Particle
import edu.princeton.cs.algs4.MinPQ
import edu.princeton.cs.algs4.StdDraw
import edu.princeton.cs.algs4.StdRandom
import java.awt.Color

class CollisionSystem(private val particles: Array<Particle>) {

    private val HZ = 0.5 // number of redraw events per clock tick

    private var pq = MinPQ<Event>()
    private var t: Double = 0.0

    private fun predict(a: Particle?, limit: Double) {
        if (a == null) return
        for (p in particles) {
            val dt = a.timeToHit(p)
            if (t + dt <= limit)
                pq.insert(Event(t + dt, a, p))
        }
        if (a.timeToHitVerticalWall() > 0)
            pq.insert(Event(t + a.timeToHitVerticalWall(), a, null))
        if (a.timeToHitHorizontalWall() > 0)
            pq.insert(Event(t + a.timeToHitHorizontalWall(), null, a))
    }

    private fun redraw(limit: Double) {
        StdDraw.clear()
        particles.map { particle -> particle.draw() }
        StdDraw.show()
        StdDraw.pause(20)
        if (t < limit) {
            pq.insert((Event(t + 1.0 / HZ, null, null)))
        }
    }

    fun simulate(limit: Double) {
        for (particle in particles) {
            predict(particle, limit)
        }
        pq.insert(Event(0.0, null, null))

        while (!pq.isEmpty) {
            val event = pq.delMin()
            if (!event.isValid()) continue
            val a: Particle? = event.a
            val b: Particle? = event.b

            for (particle in particles) {
                particle.move(event.time - t)
            }
            t = event.time

            if (a != null && b != null) a.bounceOf(b)
            else if (a != null && b == null) a.bounceOfVerticalWall()
            else if (a == null && b != null) b.bounceOfHorizontalWall()
            else redraw(limit)

            predict(a, limit)
            predict(b, limit)
        }
    }

    private class Event(
        val time: Double,
        val a: Particle?,
        val b: Particle?
    ) : Comparable<Event> {

        private var countA: Int = a?.count ?: -1
        private var countB: Int = b?.count ?: -1

        override fun compareTo(other: Event): Int = this.time.compareTo(other.time)

        fun isValid(): Boolean {
            if (a != null && a.count != countA) return false
            if (b != null && b.count != countB) return false
            return true
        }
    }
}

fun main(args: Array<String>) {

    StdDraw.setCanvasSize(600, 600)
    StdDraw.enableDoubleBuffering()

    // Try adding program arguments via Run/Debug configuration.
    // Learn more about running applications: https://www.jetbrains.com/help/idea/running-applications.html.
    println("Program arguments: ${args.joinToString()}")

    val n = Integer.parseInt(args[0])
    val particles = Array(n) { Particle() }

    val radius = 0.03
    val bigParticle = Particle(
        radius, StdRandom.uniformDouble(radius, 1.0 - radius), StdRandom.uniformDouble(radius, 1.0 - radius),
        StdRandom.uniformDouble(0.0, 0.01), StdRandom.uniformDouble(0.0, 0.01), 0.05, Color.RED
    )
    particles[n-1] = bigParticle

    val collisionSystem = CollisionSystem(particles)
    collisionSystem.simulate(10000.0)
}