package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.util.ElapsedTime
import com.qualcomm.robotcore.util.Range
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

@Autonomous(name = "Auto4", group = "Linear OpMode")
//@Disabled
class Auto4 : LinearOpMode() {
    // Declare OpMode members.
    private val runtime = ElapsedTime()
    override fun runOpMode() {
        val bot = Autonomous2(this, 0, telemetry)
        val odoMovement = SimpleOdoMovement(this, bot.bot, bot.odo)
        odoMovement.initialize(true)
        while (!opModeIsActive()) {
            telemetry.addData("lEnc: ", bot.bot.leftEncoder.currentPosition)
            telemetry.addData("rEnc: ", bot.bot.rightEncoder.currentPosition)
            telemetry.addData("bEnc: ", bot.bot.backEncoder.currentPosition)
            telemetry.addData("imu av", bot.bot.imu.getRobotAngularVelocity(AngleUnit.DEGREES))
            telemetry.addData("x: ", bot.odo.location.x)
            telemetry.addData("y: ", bot.odo.location.y)
            telemetry.addData("rot: ", bot.odo.location.rot)
            telemetry.update()
        }
        waitForStart()
        odoMovement.drive(5.0, 0.8, 0.5)
        sleep(1000)
        odoMovement.strafe(5.0, 0.8, 0.25)
        sleep(1000)
        bot.halfOdoPivot(90.0)
        sleep(1000)
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
        val pow = 0.4
        val targetX = bot.autoTicsPerInch(x)
        val targetY = bot.autoTicsPerInch(y)
        var deltaY2 = bot.odo.location.y - targetY
        var deltaX2 = bot.odo.location.x - targetX
        var theta = atan2(deltaY2, deltaX2) - angle
        var hypo = sqrt(deltaX2 * deltaX2 + deltaY2 * deltaY2)

        while (opModeIsActive() && abs(deltaX2) > 100) {
            bot.odo.calculate()
            telemetry.addData("locX : ", bot.odo.location.x)
            deltaY2 = bot.odo.location.y - targetY
            deltaX2 = bot.odo.location.x - targetX
            theta = angle - bot.odo.location.rot

            telemetry.addData("x: ", deltaX2)
            telemetry.addData("y: ", deltaY2)
            telemetry.addData("rot: ", theta)
            telemetry.update()

            /*val sinValue = sin(theta - PI / 4)
            val cosValue = cos(theta - PI / 4)
            val maxPower = maxOf(sinValue, cosValue)*/
            hypo = sqrt(deltaX2 * deltaX2 + deltaY2 * deltaY2)
            val drive = Range.clip(hypo * 0.05, 0.5, -0.5)
            val turn: Double = Range.clip(theta  * 0.03, -0.5, 0.5)
            val strafe: Double = Range.clip( deltaY2 * 0.02, -0.5, 0.5)

            telemetry.addData("flpow: ", ((drive - turn - strafe) / 1.5) * pow)
            telemetry.addData("frpow: ", ((drive + turn + strafe) / 1.5) * pow)
            telemetry.addData("blpow: ", ((drive + turn - strafe) / 1.5) * pow)
            telemetry.addData("brpow: ", ((drive - turn + strafe) / 1.5) * pow)
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
        while (opModeIsActive()) {}
    }
}


