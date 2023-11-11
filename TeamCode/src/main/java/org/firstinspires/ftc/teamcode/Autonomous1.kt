package org.firstinspires.ftc.teamcode

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import androidx.core.graphics.get
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
import java.lang.Math.abs
import kotlin.math.floor
import kotlin.math.roundToInt
import com.qualcomm.robotcore.util.Range
import org.firstinspires.ftc.vision.VisionProcessor


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
    fun intake(power : Double, time: Long) {
        bot.intake.power = power
        instance.sleep(time)
        bot.intake.power = 0.0
    }
    fun savePicture() : Bitmap{
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
    }
    fun detectProp2(alliance : Int) : Int {
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
                val turn: Double = Range.clip(bearing * 0.015, -0.5, 0.5)
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
