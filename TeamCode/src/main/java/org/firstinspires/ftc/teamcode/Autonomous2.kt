package org.firstinspires.ftc.teamcode

import android.os.SystemClock
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection
import kotlin.math.abs
import com.qualcomm.robotcore.util.Range
import org.checkerframework.checker.units.qual.Angle
import org.firstinspires.ftc.teamcode.misc.refAngle
import org.firstinspires.ftc.teamcode.misc.refRad
import org.firstinspires.ftc.vision.VisionProcessor
import kotlin.math.PI
import kotlin.math.atan
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.sign
import kotlin.math.sin
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

    private val driveController: ProportionalController = ProportionalController(
        DRIVE_GAIN,
        DRIVE_ACCEL,
        DRIVE_MAX_AUTO,
        DRIVE_TOLERANCE,
        DRIVE_DEADBAND, false)
    private val strafeController: ProportionalController = ProportionalController(
        STRAFE_GAIN,
        STRAFE_ACCEL,
        STRAFE_MAX_AUTO,
        STRAFE_TOLERANCE,
        STRAFE_DEADBAND, false)
    private val yawController: ProportionalController = ProportionalController(
        YAW_GAIN,
        YAW_ACCEL,
        YAW_MAX_AUTO,
        YAW_TOLERANCE,
        YAW_DEADBAND, true)
    private val holdTimer = ElapsedTime()

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
        val encoderToDegree = 180 / 1000.0
        var heading = convertAngle(angle)
        var currentHeading = odo.location.rot * encoderToDegree
        var diff = heading - currentHeading
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
                && abs(currentHeading - heading) > 3) {
                //&& abs(leftTarget - bot.bl.currentPosition) > 10.0
                //&& abs(rightTarget - bot.br.currentPosition) > 10.0*/) {
            pow = max(0.4 * (abs(diff) / 100), 0.2)
            bot.move(pow * sign(diff), pow * -sign(diff), pow * sign(diff), pow * -sign(diff))
            t.addData("Left :", leftTarget - bot.bl.currentPosition)
            t.addData("Expected :", diff)
            t.addData("start angle :", currentHeading)
            t.addData("Target :", leftTarget)
            t.update()
            odo.calculate()
            currentHeading = odo.location.rot * encoderToDegree
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

    fun goTo(x : Double, y : Double, angle: Double, holdTime: Int = 500) {
        var deltaY = y - odo.location.y
        var deltaX = x - odo.location.x
        var theta: Double = atan2(deltaY, deltaX)
        var thetaDifference = refRad(theta - odo.location.rot)
        var thetaError: Double
        var turn: Double

        if (abs(thetaDifference) < PI / 2) {
            if (thetaDifference < 0) {
                driveController.reset(x, 0.2) //  Maintain zero drive drift
                strafeController.reset(y, 0.4) // Achieve desired Strafe distance
            } else {
                driveController.reset(x, 0.2) //  Maintain zero drive drift
                strafeController.reset(y, 0.4) // Achieve desired Strafe distance
            }
        } else {
            if (thetaDifference < 0) {
                driveController.reset(x, 0.2) //  Maintain zero drive drift
                strafeController.reset(y, 0.4) // Achieve desired Strafe distance
            } else {
                driveController.reset(x, 0.2) //  Maintain zero drive drift
                strafeController.reset(y, 0.4) // Achieve desired Strafe distance
            }
        }

        yawController.reset(refAngle(angle), 0.2) // Maintain last turn angle
        holdTimer.reset()

        while (instance.opModeIsActive() && (!driveController.inPosition() || !strafeController.inPosition() || !yawController.inPosition())) {
            odo.calculate()
            deltaY = y - odo.location.y
            deltaX = x - odo.location.x
            theta = atan2(deltaY, deltaX)
            thetaDifference = refRad(theta - odo.location.rot)

            val powers = checkThetaDiff(thetaDifference , deltaX, deltaY)
            turn = -yawController.getOutput(refAngle(angle) - refAngle( odo.location.rot * 180 / PI))

            var flPower = powers[0] - powers[1] - turn
            var frPower = powers[0] + powers[1] + turn
            var blPower = powers[0] + powers[1] - turn
            var brPower = powers[0] - powers[1] + turn

            var max = maxOf(abs(flPower), abs(frPower))
            max = maxOf(max, abs(blPower))
            max = maxOf(max, abs(brPower))

            if (max > 1) {
                flPower /= max
                frPower /= max
                blPower /= max
                brPower /= max
            }
            instance.telemetry.addData("powerFL ", flPower)
            instance.telemetry.addData("powerFR ", frPower)
            instance.telemetry.addData("powerBL ", blPower)
            instance.telemetry.addData("powerBR ", brPower)
            instance.telemetry.addData("x: ", odo.location.x)
            instance.telemetry.addData("y: ", odo.location.y)
            instance.telemetry.addData("rot: ", odo.location.rot)
            instance.telemetry.addData("theta: ", theta)
            instance.telemetry.addData("theta Difference: ", thetaDifference)
            instance.telemetry.addData("Drive", powers[0])
            instance.telemetry.addData("Strafe", powers[1])
            instance.telemetry.addData("turn", turn)
            instance.telemetry.addData("delta X: ", deltaX)
            instance.telemetry.addData("delta Y: ", deltaY)
            instance.telemetry.update()

            bot.move(flPower, frPower, blPower, brPower)
            if (driveController.inPosition() && yawController.inPosition()) {
                if (holdTimer.time() > holdTime) {
                    break // Exit loop if we are in position, and have been there long enough.
                }
            } else {
                holdTimer.reset()
            }
        }
        bot.move(0.0, 0.0, 0.0, 0.0)
    }

    fun checkThetaDiff(theta: Double, deltaX: Double, deltaY: Double): DoubleArray {
        val thetaDifference = refRad(theta - odo.location.rot)
        val drivePower: Double
        val strafePower: Double

        if (abs(thetaDifference) < PI / 4 || abs(thetaDifference) > 3 * PI / 4) {
            drivePower = driveController.getOutput(deltaX) // Comment
            strafePower = strafeController.getOutput(deltaY) // Comment
        } else {
            drivePower = driveController.getOutput(deltaY) // Comment
            strafePower = strafeController.getOutput(deltaX) // Comment
        }

//        if (abs(thetaDifference) < PI / 2) {
//            if (thetaDifference < 0) {
//                drivePower = driveController.getOutput(deltaX) // Comment
//                strafePower = strafeController.getOutput(deltaY) // Comment
//            } else {
//                drivePower = driveController.getOutput(deltaX) // Comment
//                strafePower = strafeController.getOutput(deltaY) // Comment
//            }
//        } else {
//            if (thetaDifference < 0) {
//                drivePower = driveController.getOutput(deltaX) // Comment
//                strafePower = strafeController.getOutput(deltaY) // Comment
//            } else {
//                drivePower = driveController.getOutput(deltaX) // Comment
//                strafePower = strafeController.getOutput(deltaY) // Comment
//            }
//        }

        return doubleArrayOf(
            drivePower,
            -strafePower
        )
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

    fun detectProp(alliance : Int) : Int {
        val centerX : Double = bot.cam.camProc!!.getCenter().x
        val size = bot.cam.camProc!!.getSize()
        if (alliance == 1) { // red side
            if (size > 1000) {
                if (centerX > 200 && centerX < 400) {
                    t.addData("Prop: ", "Center")
                    return 5
                } else if (centerX < 200) {
                    t.addData("Prop: ", "Left")
                    return 4
                }
            } else {
                t.addData("Prop: ", "Right")
                return 6
            }
        } else if (alliance == 2) { //blue side
            if (size > 1000) {
                if (centerX > 200 && centerX < 400) {
                    t.addData("Prop: ", "Center")
                    return 2
                } else if (centerX > 400) {
                    t.addData("Prop: ", "Right")
                    return 3
                }
            }
        }
        //if on the right of red of left of blue
        if ((8 - (alliance * alliance * 2 - (alliance - 1))) > 3) {
            t.addData("Prop: ", "Right")
        } else {
            t.addData("Prop: ", "Left")
        }
        return (8 - (alliance * alliance * 2 - (alliance - 1)))

    }

    fun goToAprilTag(distAway : Double, propPos : Int) : AprilTagDetection? {
        var targetDist : Double = 0.0
        var bearing : Double = 0.0
        var yaw : Double = 0.0
        var emptyTimes : Int = 0
        var timeout : Long = 0L
        var curTime : Long = 0L


        while (instance.opModeIsActive() && timeout < 1000) {

            var detections: List<AprilTagDetection>? = null
            detections = bot.cam.aprilTag!!.detections
            for (det in detections!!) {
                t.addLine("ID: ${det.id}, name: ${det.metadata.name}")
                t.addLine("RBE ${det.ftcPose.range} ${det.ftcPose.bearing} ${det.ftcPose.yaw}  (inch, deg, deg)")

                if (det.id == propPos) {
                    //targetDist = -(det.ftcPose.range - distAway)
                    //bearing = -det.ftcPose.bearing
                    //yaw = det.ftcPose.yaw
                    //break
                    bot.move(0.0,0.0,0.0,0.0)
                    t.update()
                    return det
                } else {
                    targetDist = 0.0
                    bearing = 0.0
                    yaw = (propPos - det.id).toDouble() * 30.0
                }
            }
            t.update()
            if (detections.isEmpty()) {
                emptyTimes += 1
                timeout = SystemClock.uptimeMillis() - curTime
            } else {
                emptyTimes = 0
                timeout = 0L
                curTime = SystemClock.uptimeMillis()
            }

            if (emptyTimes > 2) {
                bot.move(0.0, 0.0, 0.0, 0.0)
            } else {

                val drive: Double = Range.clip((targetDist - 3.0) * 0.03, -0.5, 0.5)
                //val turn: Double = Range.clip(bearing * 0.02, -0.5, 0.5)
                val strafe: Double = Range.clip(yaw * 0.024, -0.5, 0.5)
                bot.move((drive - strafe /*- turn*/) / 1.5,
                        (drive + strafe /*+ turn*/) / 1.5,
                        (drive + strafe /*- turn*/) / 1.5,
                        (drive - strafe /*+ turn*/) / 1.5)

            }

        }
        bot.move(0.0,0.0,0.0,0.0)
        return null
    }


    fun armmove(apow: Double) {
        val time = SystemClock.uptimeMillis()
        bot.arm.power = -apow
        instance.telemetry.addData("Time", SystemClock.uptimeMillis() - time)
        while (instance.opModeIsActive() && abs(bot.arm.currentPosition) < 120.0) { //maybe change the 100 ()
            t.addLine("Armmoving")
            t.update()
            if (abs(bot.arm.velocity) > 60) {
                bot.arm.power += 0.1
            } else if (abs(bot.arm.velocity) < 60) {
                bot.arm.power -= 0.1
            }
        }
        bot.arm.power = 0.0
        bot.arm.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.FLOAT
    }

    fun newarm(apow: Double, dist: Double) {
        bot.arm.power = apow
        val pastpos = bot.arm.currentPosition
        instance.telemetry.addData("arm pos", bot.arm.currentPosition)
        if (pastpos + dist <= (bot.arm.currentPosition)) {
            bot.arm.power = 0.0
        }
    }

    fun armback(apow : Double) {
        bot.arm.power = apow

        while (instance.opModeIsActive() && abs(bot.arm.currentPosition) > 80.0) {
            if (bot.arm.velocity > 40) {
                bot.arm.power -=  0.01
            } else if (bot.arm.velocity < 40) {
                bot.arm.power += 0.02
            }
        }
        bot.arm.power = 0.0
        bot.arm.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.FLOAT
    }
    fun switchProc(proc: VisionProcessor) {
        bot.cam.visionPortal!!.setProcessorEnabled(
                proc,
                !bot.cam.visionPortal!!.getProcessorEnabled(proc)
        )
    }

    companion object {
        private const val DRIVE_GAIN = 0.11 // Strength of axial position control
        private const val DRIVE_ACCEL = 2.0 // Acceleration limit.  Percent Power change per second.  1.0 = 0-100% power in 1 sec.
        private const val DRIVE_TOLERANCE = 0.8 // Controller is is "inPosition" if position error is < +/- this amount
        private const val DRIVE_DEADBAND = 0.2 // Error less than this causes zero output.  Must be smaller than DRIVE_TOLERANCE
        private const val DRIVE_MAX_AUTO = 0.6 // "default" Maximum Axial power limit during autonomous

        private const val STRAFE_GAIN = 0.12 // Strength of lateral position control
        private const val STRAFE_ACCEL = 1.5 // Acceleration limit.  Percent Power change per second.  1.0 = 0-100% power in 1 sec.
        private const val STRAFE_TOLERANCE = 1.0 // Controller is is "inPosition" if position error is < +/- this amount
        private const val STRAFE_DEADBAND = 0.2 // Error less than this causes zero output.  Must be smaller than DRIVE_TOLERANCE
        private const val STRAFE_MAX_AUTO = 0.6 // "default" Maximum Lateral power limit during autonomous

        private const val YAW_GAIN = 0.03 // Strength of Yaw position control
        private const val YAW_ACCEL = 2.0 // Acceleration limit.  Percent Power change per second.  1.0 = 0-100% power in 1 sec.
        private const val YAW_TOLERANCE = 1.0 // Controller is is "inPosition" if position error is < +/- this amount
        private const val YAW_DEADBAND = 0.25 // Error less than this causes zero output.  Must be smaller than DRIVE_TOLERANCE
        private const val YAW_MAX_AUTO = 0.6 // "default" Maximum Yaw power limit during autonomous
    }
}

//moose jerky
