package org.firstinspires.ftc.teamcode.hyperion.odometry

import org.firstinspires.ftc.teamcode.Ruboot


class Odometry(robot: Ruboot) {
    private val bot = robot

    //val location = Point(0.0, 0.0, 0.0)

    private var currentLeft = 0.0
    private var currentRight = 0.0
    private var currentBack = 0.0

    private var lastLeft = 0.0
    private var lastRight = 0.0
    private var lastBack = 0.0
    fun calculate() {
        lastBack = currentBack
        lastLeft = currentLeft
        lastRight = currentRight

        currentLeft = bot.fl.currentPosition.toDouble()
        currentRight = bot.fr.currentPosition.toDouble()
        currentBack = bot.bl.currentPosition.toDouble()

        val deltaLeft = currentLeft - lastLeft
        val deltaRight = currentRight - lastRight
        val deltaBack = currentBack - lastBack

        /* val deltaTheta = ((deltaLeft + deltaRight) / distanceOneTwo) * ConstantEncoderValue
        val deltaX = ((deltaLeft + deltaRight) / 2.0) * ConstantEncoderValue
        val deltaY = (deltaBack - distanceOnetwoThree * ((deltaLeft + deltaRight) / distanceOneTwo)) * ConstantEncoderValue

        val theta = location.rot + deltaTheta / 2.0
        location.x += deltaX * cos(theta) - deltaY * sin(theta)
        location.y += deltaX * sin(theta) + deltaY * cos(theta)
        location.rot += deltaTheta */

    }
}