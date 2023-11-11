package org.firstinspires.ftc.teamcode.hyperion.misc

import org.opencv.core.Point
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

fun refAngle(angleIn: Double): Double {
    if (angleIn < 0) {
        return -((-angleIn + 180) % 360) + 180
    }
    return (angleIn + 180) % 360 - 180
}

fun refRad(angleIn: Double): Double {
    if (angleIn < 0) {
        return -((-angleIn + Math.PI) % (2 * Math.PI)) + Math.PI
    }
    return (angleIn + Math.PI) % (2 * Math.PI) - Math.PI
}

fun degToRad(degrees: Double): Double {
    return degrees * Math.PI / 180
}

/**
 * This function finds the intersection(s) between a line segment and a circle.
 * The line segment is defined by the points linePoint1 and linePoint2.
 * The circle is defined by a center point and radius.
 *
 * Returns a list of intersection points (may be empty if no intersections).
 * Note that in most cases there will be either 0 or 2 intersections.
 *
 * The algorithm used is explained here:
 * [How do I calculate the intersection(s) of a straight line and a circle?](http://math.stackexchange.com/questions/228841/how-do-i-calculate-the-intersections-of-a-straight-line-and-a-circle)
 * @param center The center of the circle.
 * @param radius The radius of the circle.
 * @param linePoint1 One point on the line.
 * @param linePoint2 Another point on the line.
 *
 * @return A list of points where the line segment intersects the circle.
 */
fun lineCircleIntersec(center: Point, radius: Double, linePoint1: Point, linePoint2: Point): ArrayList<Point> {
    if (abs(linePoint1.y - linePoint2.y) < 0.003)  {
        linePoint1.y = linePoint2.y + 0.003
    }

    if (abs(linePoint1.x - linePoint2.x) < 0.003) {
        linePoint1.x = linePoint2.x + 0.003
    }

    /*
        mx + b = y
        x^2 + y^2 = r^2

        y = sqrt(r^2 - x^2)

        mx + b = sqrt(r^2 - x^2)

        solve for x using quadratic formula

        x = (-mb + sqrt((m^2 * r^2) - (b^2 * r^2))) / m^2 + 1
     */

    val m1 = (linePoint2.y - linePoint1.y) / (linePoint2.x - linePoint1.x)
    val x1 = linePoint1.x - center.x
    val y1 = linePoint1.y - center.y

    val quadraticA = m1.pow(2.0) + 1.0  // m^2 + 1
    val quadraticB = (2.0 * m1 * y1) - (2.0 * m1.pow(2.0) * x1) // 2m(y - mx)
    val quadraticC = ((m1.pow(2.0) * x1.pow(2.0)) - (2.0 * y1 * m1 * x1) + y1.pow(2.0) - radius.pow(2.0))  // m^2x^2 - 2ymx + y^2 - r^2

    val allPoints: ArrayList<Point> = ArrayList()
    // Catch
    try {
        // solve for x using quadratic formula
        var xRoot1 = (-quadraticB + sqrt(quadraticB.pow(2.0) - (4.0 * quadraticA * quadraticC))) / (2.0 * quadraticA)
        var xRoot2 = (-quadraticB - sqrt(quadraticB.pow(2.0) - (4.0 * quadraticA * quadraticC))) / (2.0 * quadraticA)

        // solve for y
        var yRoot1 = m1 * (xRoot1 - x1) + y1
        var yRoot2 = m1 * (xRoot2 - x1) + y1

        // Add the offset back to the solution
        xRoot1 += center.x
        xRoot2 += center.x
        yRoot1 += center.y
        yRoot2 += center.y

        val minX = if (linePoint1.x < linePoint2.x) linePoint1.x else linePoint2.x
        val maxX = if (linePoint1.x > linePoint2.x) linePoint1.x else linePoint2.x

        if (xRoot1 in minX..maxX) {
            allPoints.add(Point(xRoot1, yRoot1))
        }

        if (xRoot2 in minX..maxX) {
            allPoints.add(Point(xRoot2, yRoot2))
        }
    } catch (e: Exception){
        // no real solutions
    }

    return allPoints


}