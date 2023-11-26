package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.robot.Robot
import com.qualcomm.robotcore.util.ElapsedTime
import com.qualcomm.robotcore.util.Range
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.atan
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sign
import kotlin.math.sin

@Autonomous(name = "Auto4", group = "Linear OpMode")
//@Disabled
class Auto4 : LinearOpMode() {
    // Declare OpMode members.
    private val runtime = ElapsedTime()
    override fun runOpMode() {
        val bot = Autonomous2(this, 0, telemetry)
        waitForStart()
        gotoWayPoint(5.0, 0.0, 0.0, bot)
        while(opModeIsActive()) {
            bot.odo.calculate()
            telemetry.addData("lEnc: ",bot.bot.leftEncoder.currentPosition)
            telemetry.addData("rEnc: ",bot.bot.rightEncoder.currentPosition)
            telemetry.addData("bEnc: ",bot.bot.backEncoder.currentPosition)
            telemetry.addData("x: ",bot.odo.location.x)
            telemetry.addData("y: ",bot.odo.location.y)
            telemetry.addData("rot: ",bot.odo.location.rot)
            telemetry.update()
        }
    }

    fun gotoWayPoint(x : Double, y : Double, angle: Double, bot : Autonomous2) {
        val pow = 0.7
        val targetX = bot.autoTicsPerInch(x)
        val targetY = bot.autoTicsPerInch(y)
        var deltaY = bot.odo.location.y - targetY
        var deltaX = bot.odo.location.x - targetX
        var theta = atan2(deltaY, deltaX) - angle

        while (opModeIsActive() && ((deltaX) > 10 || abs(deltaY) > 10 || theta > 0.2)) {
            bot.odo.calculate()
            deltaY = bot.odo.location.y - targetY
            deltaX = bot.odo.location.x - targetX
            theta = bot.odo.location.rot - angle

            telemetry.addData("x: ",deltaX)
            telemetry.addData("y: ",deltaY)
            telemetry.addData("rot: ",theta)
            telemetry.update()

            val sinValue = sin(theta - PI / 4)
            val cosValue = cos(theta - PI / 4)
            val maxPower = maxOf(sinValue, cosValue)
            val hypo = (deltaX * deltaX + deltaY * deltaY)

            val drive = Range.clip(hypo * cos(theta) * 0.05, 0.5, -0.5)
            val turn: Double = Range.clip(theta  * 0.03, -0.5, 0.5)
            val strafe: Double = Range.clip( hypo * sin(theta) * 0.02, -0.5, 0.5)

            bot.bot.move(
                    ((drive - turn - strafe) / 1.5) * pow,
                    ((drive + turn + strafe) / 1.5) * pow,
                    ((drive + turn - strafe) / 1.5) * pow,
                    ((drive - turn + strafe) / 1.5) * pow
            )

            /*bot.bot.move((pow * cosValue / maxPower) + angle,
                    (pow * sinValue / maxPower) - angle,
                    (pow * sinValue / maxPower) + angle,
                    (pow * cosValue / maxPower) - angle)*/
        }
        bot.bot.move(0.0, 0.0, 0.0, 0.0)
    }
}


