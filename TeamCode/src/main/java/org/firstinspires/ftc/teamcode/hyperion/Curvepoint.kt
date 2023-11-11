package org.firstinspires.ftc.teamcode.hyperion

import org.opencv.core.Point

/**
 * A point on a curve
 *
 */
class Curvepoint {
    var x: Double = 0.0
    var y: Double = 0.0
    var moveSpeed: Double = 0.0
    var turnSpeed: Double = 0.0
    var followDistance: Double = 0.0
    var pointLength: Double = 0.0
    var slowDownTurnRadians: Double = 0.0
    var slowDownTurnAmount: Double = 0.0
    constructor(
        x: Double,
        y: Double,
        moveSpeed: Double,
        turnSpeed: Double,
        followDistance: Double,
        pointLength: Double,
        slowDownTurnRadians: Double,
        slowDownTurnAmount: Double
    ) {
        this.x = x
        this.y = y
        this.moveSpeed = moveSpeed
        this.turnSpeed = turnSpeed
        this.followDistance = followDistance
        this.pointLength = pointLength
        this.slowDownTurnRadians = slowDownTurnRadians
        this.slowDownTurnAmount = slowDownTurnAmount
    }

    constructor(point: Curvepoint) {
        x = point.x
        y = point.y
        moveSpeed = point.moveSpeed
        turnSpeed = point.turnSpeed
        followDistance = point.followDistance
        pointLength = point.pointLength
        slowDownTurnRadians = point.slowDownTurnRadians
        slowDownTurnAmount = point.slowDownTurnAmount
    }

    fun toPoint() : Point {
        return Point(x, y)
    }

    fun setPoint(point: Point) {
        x = point.x
        y = point.y
    }
}