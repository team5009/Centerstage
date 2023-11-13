package org.firstinspires.ftc.teamcode.autonomous

import com.qualcomm.robotcore.hardware.DcMotor
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference
import org.firstinspires.ftc.teamcode.Ruboot
import kotlin.math.abs
import kotlin.math.sign

class Pivut {

    enum class Direction {
        FORWARD, BACKWARD, OPEN, CLOSE, UP, DOWN, MIDDLE, LEFT, RIGHT
    }
    private fun stop(bot:Ruboot, bool: Boolean = true) {
        if (bool) {
            bot.fl.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
            bot.fr.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
            bot.bl.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
            bot.br.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE

        } else {
            bot.fl.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.FLOAT
            bot.fr.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.FLOAT
            bot.bl.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.FLOAT
            bot.br.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.FLOAT
        }
    }

    fun turnToAngle(bot:Ruboot, angle: Double, power: Double) {

        bot.move(-power/100.0, power/100.0, -power/100.0, power/100.0)

        while (bot.Instance.opModeIsActive() &&
            abs(abs(angle) - getAbsoluteHeading(bot)) > 1.0 // used to be 10
        )  {
            bot.Instance.telemetry.addData("Current Angle", getAbsoluteHeading(bot));
            bot.Instance.telemetry.addData("Current", getAbsoluteHeading(bot))
            bot.Instance.telemetry.addData("Target", angle)
            bot.Instance.telemetry.addData("Difference", abs(angle - getAbsoluteHeading(bot)))
            bot.Instance.telemetry.update()
        }
        bot.move(0.0, 0.0, 0.0, 0.0)
    }

    fun pivotTurn(bot: Ruboot, angle: Double, power: Double, direction: Direction) {

        val pow: Double = if (angle < 0) -(power/100) else (power/100)

        when (direction) {
            Direction.LEFT -> {
                turn(bot,-(pow/2.5), pow)
            }
            Direction.RIGHT -> {
                turn(bot,pow, -(pow/2.5))
            }
            else -> {
                turn(bot,0.0,0.0)
            }
        }
        while (bot.Instance.opModeIsActive() &&
            abs(abs(angle) - getAbsoluteHeading(bot)) > 1.0 // used to be 10
        )  {
            bot.Instance.telemetry.addData("Current", getAbsoluteHeading(bot))
            bot.Instance.telemetry.addData("Target", angle)
            bot.Instance.telemetry.addData("Difference", abs(angle) - getAbsoluteHeading(bot))
            bot.Instance.telemetry.update()
        }
        bot.move(0.0, 0.0, 0.0, 0.0)
    }

    fun convertAngle(angleIn: Double): Double{
        if(angleIn < 0)
        {
            return -((-angleIn + 180) % 360 - 180);
        }
        return (angleIn + 180) % 360 - 180;
    }
    fun pivot(bot: Ruboot, angle: Double, telemetry: Telemetry) {
        var heading = angle
        var diff = convertAngle(heading) - convertAngle(bot.rawHeading)

        while (bot.Instance.opModeIsActive() && abs(diff) > 2) {
            //telemetry.addData("difference", diff)
            //telemetry.update()
            //bot.Instance.sleep(2000)
            diff = convertAngle(heading) - convertAngle(bot.absoluteHeading)
            var pow = maxOf(0.8 * ((abs(diff) / 100)), 0.075)
            var value = if (abs(diff) > 180) -sign(diff) else sign(diff)

            if (value < 0) bot.move(-pow, pow, -pow, pow) else bot.move(pow, -pow, pow, -pow)

            telemetry.addData("Target Heading", heading)
            telemetry.addData("Current Heading", bot.absoluteHeading)
            telemetry.addData("", "")
            telemetry.addData("Difference", diff)
            telemetry.addData("Value", value)
            telemetry.addData("Power", pow)
            telemetry.update()
        }
        bot.move(0.0, 0.0, 0.0, 0.0)
    }
    fun getAbsoluteHeading(bot: Ruboot): Float {
        val angle = bot.imu.getRobotOrientation(
            AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle

        return if (angle < 0) {
            360 - abs(angle)
        } else {
            angle
        }
    }

    fun turn(bot: Ruboot, leftPow: Double, rightPow:Double) {
        bot.fl.power = leftPow
        bot.fr.power = rightPow
        bot.bl.power = leftPow
        bot.br.power = rightPow
    }

}