package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotor
import org.firstinspires.ftc.robotcore.external.Telemetry
import java.lang.Math.abs
import kotlin.math.floor

class Autonomous1(Instance: LinearOpMode, tele: Telemetry) {

    val instance = Instance
    val t = tele
    val bot = RobotTest(Instance)

    fun move(dist: Double, speed: Double) {
        /*
        * calculate tics per inch
        * calculates tics in dist
        * set motors to speed
        * wait for encoders to reach target
        * stop motors
        * reset encoder
        */
        val tics = bot.tics_per_inch(dist)
        bot.move(speed, speed, speed, speed)
        while (instance.opModeIsActive() && kotlin.math.abs(bot.fl.currentPosition) < tics) {
            instance.sleep(20)
        }

        bot.move(0.0, 0.0, 0.0, 0.0)
        resetEncoders()
    }

    fun accelerate(dist: Double, speed: Double) {
        val decelSpeed = 0.2
        var cur_speed = 0.3
        val target = bot.tics_per_inch(dist)

        while (instance.opModeIsActive() && abs(bot.fl.currentPosition) < (target / 2) && cur_speed < speed) {
            bot.move(cur_speed, cur_speed, cur_speed, cur_speed)
            cur_speed += 0.05
            instance.sleep(100)
        }
        val startdist = abs(bot.fl.currentPosition)
        val enddist = target - startdist
        val distSteps = floor(startdist / ((speed - 0.2) / decelSpeed))
        bot.move(speed, speed, speed, speed)
        while (abs(bot.fl.currentPosition) < enddist) {
            t.addData("FL Position: ", bot.fl.currentPosition)
            t.addData("End Position: ", enddist)
            t.addData("target: ", target)
            t.update()
        }

        var waypoint = enddist + distSteps
        while (instance.opModeIsActive() && abs(bot.fl.currentPosition) < target) {
            cur_speed -= 0.2
            bot.move(cur_speed, cur_speed, cur_speed, cur_speed)
            t.addData("FL Position: ", bot.fl.currentPosition)
            t.addData("End Position: ", enddist)
            t.addData("target: ", target)
            t.update()
            while (instance.opModeIsActive() && abs(bot.fl.currentPosition) < waypoint && abs(bot.fl.currentPosition) < target) {
            }
            waypoint += distSteps
        }
        bot.move(0.0, 0.0, 0.0, 0.0)
        resetEncoders()

    }


    fun pivot(degrees: Double, speed: Double) {
        val dist = tics_per_degree(degrees)
        val tics = bot.tics_per_inch(dist)
        bot.move(-speed, speed, speed, -speed)
        while (instance.opModeIsActive() && kotlin.math.abs(bot.fl.currentPosition) < tics) {
            t.addData("FL Position", bot.fl.currentPosition)
            t.update()
            instance.sleep(20)
        }

        bot.move(0.0, 0.0, 0.0, 0.0)
        resetEncoders()
    }

    fun tics_per_degree(angle: Double): Double {
        val wheelbase: Double = 6.25
        return bot.tics_per_inch(wheelbase * Math.PI / 180 * angle) // * fudgeFactor


    }

    fun resetEncoders() {
        bot.fl.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        bot.fl.mode = DcMotor.RunMode.RUN_USING_ENCODER
        bot.fr.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        bot.fr.mode = DcMotor.RunMode.RUN_USING_ENCODER
        bot.bl.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        bot.bl.mode = DcMotor.RunMode.RUN_USING_ENCODER
        bot.br.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        bot.br.mode = DcMotor.RunMode.RUN_USING_ENCODER


    }

    fun lifting(dist: Double, power: Double) {
        val target = bot.tics_per_lift(dist)
        bot.lift.power = power
        while (instance.opModeIsActive() && kotlin.math.abs(bot.lift.currentPosition) < target) {
            instance.sleep(20)
        }
        bot.lift.power = 0.0

        bot.lift.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        bot.lift.mode = DcMotor.RunMode.RUN_USING_ENCODER
    }

    fun strafeflbr(dist: Double, power: Double) {
        val tics = bot.tics_per_inch(dist)
        bot.move(power, 0.0, 0.0, power)
        while (instance.opModeIsActive() && kotlin.math.abs(bot.fl.currentPosition) < tics) {
            instance.sleep(20)
        }

        bot.move(0.0, 0.0, 0.0, 0.0)
        resetEncoders()
    }

    fun strafefrbl(dist: Double, power: Double) {
        val tics = bot.tics_per_inch(dist)
        bot.move(0.0, power, power, 0.0)
        while (instance.opModeIsActive() && kotlin.math.abs(bot.bl.currentPosition) < tics) {
            instance.sleep(20)
        }

        bot.move(0.0, 0.0, 0.0, 0.0)
        resetEncoders()
    }

    fun strafeside(dist: Double, power: Double) {
        val tics = bot.tics_per_inch(dist)
        bot.move(-power, -power, power, power)
        while (instance.opModeIsActive() && kotlin.math.abs(bot.fl.currentPosition) < tics) {
            instance.sleep(20)
        }

        bot.move(0.0, 0.0, 0.0, 0.0)
        resetEncoders()
    }
}

//moose jerky
