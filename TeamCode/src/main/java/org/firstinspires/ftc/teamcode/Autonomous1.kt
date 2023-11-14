package org.firstinspires.ftc.teamcode

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.vision.VisionPortal
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc
import java.io.FileOutputStream
import java.io.IOException
import com.qualcomm.robotcore.util.Range
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference
import org.firstinspires.ftc.vision.VisionProcessor
import java.lang.Math.abs


class Autonomous1(Instance: LinearOpMode, tele: Telemetry) {

    val instance = Instance
    val t = tele
    val bot = RobotTest(Instance)
    var bitSave: Bitmap? = null

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
        var flpow : Double = speed
        while (instance.opModeIsActive() && abs(bot.br.currentPosition) < tics) {
            t.addData("Fl Pos", bot.br.currentPosition)
            t.addData("left speed", bot.fl.velocity)
            t.addData("right speed", bot.fr.velocity)
            t.addData("Tics", tics)
            t.addData("State", abs(bot.br.currentPosition) < tics)
            t.update()
        }

        bot.move(0.0, 0.0, 0.0, 0.0)
        resetEncoders()
    }


    fun pivot(degrees: Double, speed: Double) {
        val dist = tics_per_degree(degrees)
        val tics = bot.tics_per_inch(dist)
        bot.move(-speed, speed, speed, -speed)
        while (instance.opModeIsActive() && abs(bot.br.currentPosition) < tics) {
            t.addData("FL Position", bot.br.currentPosition)
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
        bot.fl.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
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
        while (instance.opModeIsActive() && abs(bot.lift.currentPosition) < target) {
            instance.sleep(20)
        }
        bot.lift.power = 0.0

        bot.lift.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        bot.lift.mode = DcMotor.RunMode.RUN_USING_ENCODER
    }

    fun strafeflbr(dist: Double, power: Double) {
        val tics = bot.tics_per_inch(dist)
        bot.move(power, 0.0, 0.0, power)
        while (instance.opModeIsActive() && abs(bot.br.currentPosition) < tics) {
            instance.sleep(20)
        }

        bot.move(0.0, 0.0, 0.0, 0.0)
        resetEncoders()
    }

    fun strafefrbl(dist: Double, power: Double) {
        val tics = bot.tics_per_inch(dist)
        bot.move(0.0, power, power, 0.0)
        while (instance.opModeIsActive() && abs(bot.bl.currentPosition) < tics) {
            instance.sleep(20)
        }

        bot.move(0.0, 0.0, 0.0, 0.0)
        resetEncoders()
    }

    fun strafeside(dist: Double, power: Double) {
        val tics = bot.tics_per_inch(dist)
        t.addData("tics: ", tics)
        t.update()
        bot.move(-power, -power, power, power)
        while (instance.opModeIsActive() && abs(bot.br.currentPosition) < tics) {
            instance.sleep(20)
        }

        bot.move(0.0, 0.0, 0.0, 0.0)
        resetEncoders()
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
    /*fun savePicture() : Bitmap{
        bitSave = BitmapFactory.decodeFile("/sdcard/VisionPortal-current_frame.png")
        var hsv = FloatArray(3)
        Color.colorToHSV(bitSave!!.getPixel(100,100), hsv)
        val hue = ((hsv[0] / 60.0).roundToInt() % 6)
        t.addData("Got frame: ", hue + 1)
        return bitSave!!
    }

    fun checkTarget(alliance : Int, left : Int, top : Int, width : Int, height : Int) : Double {
        var hsv = FloatArray(3)
        val tot = width * height
        var count = 0
        for (x in left..left+width) {
            for (y in top..top+height) {
                Color.colorToHSV(bitSave!!.getPixel(x, y), hsv)
                if (((hsv[0] / 60.0).roundToInt() % 6) + 1 == alliance) {
                    count += 1
                }
            }
        }

        return count.toDouble() / tot.toDouble()
    }*/
    /*fun detectProp2(alliance : Int) : Int {
        var res : Int = 1
        val leftC : Int = 160
        val topC : Int = 300
        val widthC : Int = 70
        val heightC : Int = 70

        val leftR : Int = 460
        val topR : Int = 300
        val widthR : Int = 70
        val heightR : Int = 70


        if (checkTarget(alliance, leftC, topC, widthC, heightC) > 0.4) {
            res = 2
        } else if (checkTarget(alliance, leftR, topR, widthR, heightR) > 0.4) {
            res = 3
        }

        if (alliance == 1) {
            res += 3
        }
        return res
    }*/

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

    fun turnToAngle(angle: Double, power: Double) {

        bot.move(-power/100.0, power/100.0, -power/100.0, power/100.0)

        while (instance.opModeIsActive() &&
                abs(abs(angle) - getAbsoluteHeading()) > 1.0 // used to be 10
        )  {
            instance.telemetry.addData("Current Angle", getAbsoluteHeading());
            instance.telemetry.addData("Current", getAbsoluteHeading())
            instance.telemetry.addData("Target", angle)
            instance.telemetry.addData("Difference", abs(angle - getAbsoluteHeading()))
            instance.telemetry.update()
        }
        bot.move(0.0, 0.0, 0.0, 0.0)
    }

    fun pivotTurn(angle: Double, power: Double, direction: Direction) {

        val pow: Double = if (angle < 0) -(power/100) else (power/100)

        when (direction) {
            Direction.LEFT -> {
                turn(-(pow/2.5), pow)
            }
            Direction.RIGHT -> {
                turn(pow, -(pow/2.5))
            }
            else -> {
                turn(0.0,0.0)
            }
        }
        while (instance.opModeIsActive() &&
                abs(abs(angle) - getAbsoluteHeading()) > 1.0 // used to be 10
        )  {
            t.addData("Current", getAbsoluteHeading())
            t.addData("Target", angle)
            t.addData("Difference", abs(angle) - getAbsoluteHeading())
            t.update()
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

    fun sign(num : Double) : Int {
        if (num < 0.0) {
            return -1
        } else {
            return 1
        }
    }

    fun maxOf(a : Double, b: Double) : Double{
        return if (a > b){
            a
        } else {
            b
        }
    }
    fun pivot(angle: Double, power:Double, telemetry: Telemetry) {
        var heading : Double = convertAngle(angle)
        var diff : Double = heading - convertAngle(bot.rawHeading)

        while (instance.opModeIsActive() && abs(diff) > 2) {
            //telemetry.addData("difference", diff)
            //telemetry.update()
            //bot.Instance.sleep(2000)
            diff = heading - convertAngle(bot.rawHeading)
            var pow = maxOf(power * ((abs(diff) / 100)), 0.075)
            var value = if (abs(diff) > 180) -sign(diff) else sign(diff)

            if (value < 0) bot.move(pow, -pow, pow, -pow) else bot.move(-pow, pow, -pow, pow)

            telemetry.addData("Target Heading", heading)
            telemetry.addData("Current Heading", bot.rawHeading)
            telemetry.addData("left speed", bot.fl.velocity)
            telemetry.addData("left speed", bot.fr.velocity)
            telemetry.addData("Difference", diff)
            telemetry.addData("Value", value)
            telemetry.addData("Power", pow)
            telemetry.update()
        }
        bot.move(0.0, 0.0, 0.0, 0.0)
        resetEncoders()
    }
    fun getAbsoluteHeading(): Double {
        val angle = bot.imu.getRobotOrientation(
                AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle

        return if (angle < 0) {
            (360 - abs(angle)).toDouble()
        } else {
            angle.toDouble()
        }
    }

    fun turn(leftPow: Double, rightPow:Double) {
        bot.fl.power = leftPow
        bot.fr.power = rightPow
        bot.bl.power = leftPow
        bot.br.power = rightPow
    }

    fun showRedCenter() {
        t.addData("x: ", bot.cam.camProc!!.getCenter().x)
        t.addData("y: ", bot.cam.camProc!!.getCenter().y)
    }

    fun detectProp() : Int {
        val centerX : Double = bot.cam.camProc!!.getCenter().x
        if(centerX > 180 && centerX < 300) {
            t.addData("Prop: ", "Center")
            return 5
        } else if(centerX > 400 && centerX < 570) {
            t.addData("Prop: ", "Right")
            return 6
        }
        t.addData("Prop: ", "Left")
        return 4

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
                bot.move((drive - turn - strafe) / 1.5,
                        (drive + turn + strafe) / 1.5,
                        (drive - turn + strafe) / 1.5,
                        (drive + turn - strafe) / 1.5)

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
