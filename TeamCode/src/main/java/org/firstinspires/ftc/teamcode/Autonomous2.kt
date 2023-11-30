package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection
import kotlin.math.abs
import com.qualcomm.robotcore.util.Range
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference
import org.firstinspires.ftc.vision.VisionProcessor
import kotlin.math.atan
import kotlin.math.sign
import kotlin.math.sqrt


class Autonomous2(Instance: LinearOpMode, alliance: Int, tele: Telemetry) {

    val instance = Instance
    val t = tele
    val al = alliance
    val bot = RobotTest(Instance, alliance)

    val odo = Odometry(bot)
    var lastX: Double = 0.0
    var lastY: Double = 0.0
    var lastTheta: Double = 0.0

    fun autoTicsPerInch(dist: Double) :Double {
        return 2000.0 / 2.0 / Math.PI * dist
    }

    fun autoTurnDist(angle: Double) :Double{
        return autoTicsPerInch((11 + (0.125/2 * 9.0)) * Math.PI / 360 * angle)
    }

    fun waypoint(x : Double, y: Double, theta: Double) {
        val deltaX = x - lastX
        val deltaY = y - lastY
        val firstAngle = quadrant(deltaX, deltaY, atan(deltaY / deltaX))
        pivot(firstAngle, 0.5)
        move(sqrt(deltaX * deltaX + deltaY * deltaY), 0.5)
        pivot(theta, 0.5)
    }

    fun halfOdoPivot(angle: Double) {
        var heading = angle
        var diff = convertAngle(heading) - convertAngle(bot.rawHeading)
        val leftTarget = bot.br.currentPosition + autoTurnDist(diff)
        val rightTarget = bot.bl.currentPosition - autoTurnDist(diff)
        /*
        while (instance.opModeIsActive() && abs(diff) > 2) {
            //telemetry.addData("difference", diff)
            //telemetry.update()
            //bot.Instance.sleep(2000)
            diff = convertAngle(heading) - convertAngle(bot.rawHeading)
            var pow = maxOf(0.4 * ((abs(diff) / 100)), 0.05)
            var value = if (abs(diff) > 180) -sign(diff) else sign(diff)

            if (value < 0) bot.move(-pow, pow, -pow, pow) else bot.move(pow, -pow, pow, -pow)

            t.addData("Before: ", bot.rawHeading)
            t.update()
        }*/
        var pow = 0.4 * (abs(diff) / 100)
        while (instance.opModeIsActive()
                && abs(leftTarget - bot.bl.currentPosition) > 10.0
                && abs(rightTarget - bot.br.currentPosition) > 10.0) {
            pow = 0.3 * (abs(diff) / 100)
            bot.move(pow * sign(diff), pow * -sign(diff), pow * sign(diff), pow * -sign(diff))
            t.addData("Left :", leftTarget - bot.bl.currentPosition)
            t.update()
        }
        bot.move(0.0, 0.0, 0.0, 0.0)
    }

    fun quadrant(x: Double, y: Double, theta: Double) :Double {
        if (x < 0 && y >= 0) {
            return 180.0 - theta
        } else if (x < 0 && y < 0) {
            return 180.0 + theta
        } else if (x > 0 && y < 0) {
            return 360.0 - theta
        } else {
            return theta
        }
    }
    fun setOrigin(x: Double, y: Double, theta: Double) {
        lastX = x
        lastY = y
        lastTheta = theta
    }

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
        while (instance.opModeIsActive() && abs(bot.fl.currentPosition) < tics) {
            t.addData("Fl Pos", bot.fl.currentPosition)
            t.addData("Tics", tics)
            t.addData("State", abs(bot.fl.currentPosition) < tics)
            t.update()
        }

        bot.move(0.0, 0.0, 0.0, 0.0)
        resetEncoders()
    }

    fun pivot(degrees: Double, speed: Double) {
        val dist = autoTurnDist(degrees)
        val tics = bot.tics_per_inch(dist)
        bot.move(-speed, speed, speed, -speed)
        while (instance.opModeIsActive() && abs(bot.br.currentPosition) < tics) {
            t.addData("BR Position", bot.br.currentPosition)
            t.update()
            instance.sleep(20)
        }

        bot.move(0.0, 0.0, 0.0, 0.0)
        resetEncoders()
    }

    /*fun tics_per_degree(angle: Double): Double {
        val wheelbase: Double = 6.25
        return bot.tics_per_inch(wheelbase * Math.PI / 180 * angle) // * fudgeFactor
    }*/

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

    fun intake(power : Double, time: Long) {
        bot.intake.power = power
        instance.sleep(time)
        bot.intake.power = 0.0
    }

    fun arm(power : Double, time : Long) {
        bot.arm.power = power
        instance.sleep(time)
        bot.arm.power = 0.0
    }

    enum class Direction {
        FORWARD, BACKWARD, OPEN, CLOSE, UP, DOWN, MIDDLE, LEFT, RIGHT
    }
    private fun stop(bool: Boolean = true) {
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

    fun convertAngle(angleIn: Double): Double{
        if(angleIn < 0)
        {
            return -((-angleIn + 180) % 360 - 180);
        }
        return (angleIn + 180) % 360 - 180;
    }
    /*fun pivot(angle: Double, telemetry: Telemetry) {
        var heading = angle
        var diff = convertAngle(heading) - convertAngle(bot.rawHeading)

        while (instance.opModeIsActive() && abs(diff) > 2) {
            //telemetry.addData("difference", diff)
            //telemetry.update()
            //bot.Instance.sleep(2000)
            diff = convertAngle(heading) - convertAngle(bot.rawHeading)
            var pow = maxOf(0.8 * ((abs(diff) / 100)), 0.075)
            var value = if (abs(diff) > 180) -sign(diff) else sign(diff)

            if (value < 0) bot.move(-pow, pow, -pow, pow) else bot.move(pow, -pow, pow, -pow)

            telemetry.addData("Target Heading", heading)
            telemetry.addData("Current Heading", bot.rawHeading)
            telemetry.addData("", "")
            telemetry.addData("Difference", diff)
            telemetry.addData("Value", value)
            telemetry.addData("Power", pow)
            telemetry.update()
        }
        bot.move(0.0, 0.0, 0.0, 0.0)
    }*/

    fun detectProp() : Int {
        val centerX : Double = bot.cam.camProc!!.getCenter().x
        if (al == 1) {
            if(centerX > 150 && centerX < 200) {
                t.addData("Prop: ", "Center")
                return 5
            } else if(centerX > 225 && centerX < 275) {
                t.addData("Prop: ", "Right")
                return 6
            }//else if(centerX > 75 && centerX < 125) {
                t.addData("Prop: ", "Left")
                return 4
            //}
        } else {
            if (centerX > 180 && centerX < 300) {
                t.addData("Prop: ", "Center")
                return 2
            } else if (centerX > 400 && centerX < 570) {
                t.addData("Prop: ", "Right")
                return 3
            }
            t.addData("Prop: ", "Left")
            return 1
        }
    }
    fun goToAprilTag(distAway : Double, propPos : Int) {
        var targetDist : Double = 0.0
        var bearing : Double = 0.0
        var yaw : Double = 0.0
        var emptyTimes : Int = 0


        while (instance.opModeIsActive() && targetDist >= 0.0) {

            t.update()
            var detections: List<AprilTagDetection>? = null
            detections = bot.cam.aprilTag!!.detections
            for (det in detections!!) {
                t.addLine("ID: ${det.id}, name: ${det.metadata.name}")
                t.addLine("RBE ${det.ftcPose.range} ${det.ftcPose.bearing} ${det.ftcPose.yaw}  (inch, deg, deg)")

                if (det.id == propPos) {
                    targetDist = det.ftcPose.range - distAway
                    bearing = det.ftcPose.bearing
                    yaw = -det.ftcPose.yaw
                    break
                } else {
                    targetDist = 0.0
                    bearing = 0.0
                    yaw = (det.id.toDouble() - propPos.toDouble()) * 30.0
                }
            }

            if (detections.isEmpty()) {
                emptyTimes += 1
            } else {
                emptyTimes = 0
            }

            if (emptyTimes > 2) {
                bot.move(0.0, 0.0, 0.0, 0.0)
            } else {

                val drive: Double = Range.clip((targetDist + 3.0) * 0.03, -0.5, 0.5)
                val turn: Double = Range.clip(bearing * 0.02, -0.5, 0.5)
                val strafe: Double = Range.clip(yaw * 0.01, -0.5, 0.5)
                bot.move((drive - strafe - turn) / 1.5,
                        (drive + strafe + turn) / 1.5,
                        (drive + strafe - turn) / 1.5,
                        (drive - strafe + turn) / 1.5)

            }

        }
    }
    fun switchProc(proc: VisionProcessor) {
        bot.cam.visionPortal!!.setProcessorEnabled(
                proc,
                !bot.cam.visionPortal!!.getProcessorEnabled(proc)
        )
    }
}

//moose jerky
