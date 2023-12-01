package org.firstinspires.ftc.teamcode

import android.os.SystemClock
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotor
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection
import com.qualcomm.robotcore.util.Range
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference
import org.firstinspires.ftc.vision.VisionProcessor
import kotlin.math.abs
import kotlin.math.exp
import kotlin.math.roundToInt

class Autonomous1(Instance: LinearOpMode,alliance : Int, tele: Telemetry) {

    val instance = Instance
    val t = tele
    val bot = RobotTest(Instance, alliance)

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
        while (instance.opModeIsActive() && abs(bot.br.currentPosition) < tics) {
            t.addData("Fl Pos", bot.br.currentPosition)
            t.addData("left speed", bot.fl.velocity)
            t.addData("right speed", bot.fr.velocity)
            t.addData("Tics", tics)
            t.addData("State", abs(bot.br.currentPosition) < tics)
            t.update()
        }

        bot.move(0.0, 0.0, 0.0, 0.0)
        //t.addData("Current Angle: ", bot.rawHeading
        t.update()
        resetEncoders()
    }


    fun pivot(degrees: Double, speed: Double) {
        val dist = tics_per_degree(degrees)
        val tics = bot.tics_per_inch(dist)
        bot.move(-speed, speed, -speed, speed)
        while (instance.opModeIsActive() && abs(bot.br.currentPosition) < tics) {
            t.addData("BR Position", bot.br.currentPosition)
            t.update()
            //instance.sleep(20)
        }

        bot.move(0.0, 0.0, 0.0, 0.0)
        resetEncoders()
    }

    fun tics_per_degree(angle: Double): Double {
        val wheelbase = 6.0
        return bot.tics_per_inch( Math.PI / 180 / wheelbase * angle) * 2.25
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
        if (time > 0L) {
            instance.sleep(time)
            bot.intake.power = 0.0
        }
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
    }*/

    /*fun checkTarget(alliance : Int, left : Int, top : Int, width : Int, height : Int) :Boolean{
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
        t.addData("h counts: ", count)
        return count.toDouble() / tot.toDouble() > 0.4
    }
    fun detectProp2(alliance : Int, proc : VisionLearn) : Int {
        var res : Int = 1
        val leftC : Int = 150
        val topC : Int = 100
        val widthC : Int = 70
        val heightC : Int = 70

        val leftR : Int = 480
        val topR : Int = 100
        val widthR : Int = 70
        val heightR : Int = 70

        bitSave = proc.getResults2()
        if (bitSave == null) {
            return 1
        }
        if (checkTarget(alliance, leftC, topC, widthC, heightC)) {
            res = 2
        } else if (checkTarget(alliance, leftR, topR, widthR, heightR)) {
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

    /*fun turnToAngle(angle: Double, power: Double) {

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
                abs(abs(angle) + getAbsoluteHeading()) > 1.0 // used to be 10
        )  {
            t.addData("Current", getAbsoluteHeading())
            t.addData("Target", angle)
            t.addData("Difference", abs(angle) - getAbsoluteHeading())
            t.update()
        }
        bot.move(0.0, 0.0, 0.0, 0.0)
    }*/

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
        var diff : Double = 0.0
        var times = 0
        while (instance.opModeIsActive() && abs(diff) > 2) {
            //telemetry.addData("difference", diff)
            //telemetry.update()
            //bot.Instance.sleep(2000)
            val pow = maxOf(power * ((abs(diff) / 100)), 0.1)
            val value = if (abs(diff) > 180) -sign(diff) else sign(diff)
            //diff = abs(convertAngle(angle)) - convertAngle(bot.rawHeading)

            if (value < 0) bot.move(pow, -pow, pow, -pow) else bot.move(-pow, pow, -pow, pow)

            telemetry.update()
            telemetry.addData("Target Heading", heading)
            //telemetry.addData("Current Heading", bot.rawHeading)
            telemetry.addData("left speed", bot.fl.velocity)
            telemetry.addData("left speed", bot.fr.velocity)
            telemetry.addData("Difference", diff)
            telemetry.addData("Value", value)
            telemetry.addData("Power", pow)
            times += 1
        }

        bot.move(0.0, 0.0, 0.0, 0.0)
        telemetry.addData("Times Through: ", times)
        telemetry.update()
        instance.sleep(10000)
        resetEncoders()
    }
   /* fun getAbsoluteHeading() {
        val angle = bot.imu.getRobotOrientation(
               AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle

        return if (angle < 0) {
            (360 - abs(angle)).toDouble()
        } else {
            angle.toDouble()
        }
    } */

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

    fun detectProp(alliance : Int) : Int {
        val centerX : Double = bot.cam.camProc!!.getCenter().x
        val size = bot.cam.camProc!!.getSize()
        if (alliance == 1) { // red side
            if (size > 1000) {
                if (centerX > 200 && centerX < 500) {
                    t.addData("Prop: ", "Center")
                    return 5
                } else if (centerX < 200 && centerX > 50) {
                    t.addData("Prop: ", "Left")
                    return 4
                }
            } else {
                t.addData("Prop: ", "Right")
                return 6
            }
        } else { //blue side
            if (size > 1000) {
                if (centerX > 200 && centerX < 500) {
                    t.addData("Prop: ", "Center")
                    return 2
                } else if (centerX < 700 && centerX > 500) {
                    t.addData("Prop: ", "Right")
                    return 3
                }
            }
        }
            //if on the right of red or left of blue
            if ((8 - (alliance * alliance * 2 - (alliance - 1))) > 3) {
                t.addData("Prop: ", "Right")
            } else {
                t.addData("Prop: ", "Left")
            }
            return (8 - (alliance * alliance * 2 - (alliance - 1)))

    }

    fun goToAprilTag(distAway : Double, propPos : Int) : Double {
        var targetDist : Double = 5.0
        var bearing : Double = 0.0
        var yaw : Double = 0.0
        var emptyTimes : Int = 0
        var timeLimit : Long = 0L
        var lastBearing : Double = 0.0


        while (instance.opModeIsActive() && targetDist >= 3.0 && (timeLimit == 0L || SystemClock.uptimeMillis() - timeLimit < 5000L)) {

            t.update()
            var detections: List<AprilTagDetection>? = null
            detections = bot.cam.aprilTag!!.detections
            for (det in detections!!) {
                t.addLine("ID: ${det.id}, name: ${det.metadata.name}")
                t.addLine("RBE ${det.ftcPose.range} ${det.ftcPose.bearing} ${det.ftcPose.yaw}  (inch, deg, deg)")

                if (det.id == propPos) {
                    targetDist = det.ftcPose.range - distAway
                    bearing = det.ftcPose.bearing
                    lastBearing = bearing
                    yaw = -det.ftcPose.yaw
                    break
                } else {
                    targetDist = 3.0
                    bearing = (det.id.toDouble() - propPos.toDouble()) * 10.0
                    yaw = 0.0
                }
            }

            if (detections.isEmpty()) {
                emptyTimes += 1
                if (timeLimit == 0L) {
                    timeLimit = SystemClock.uptimeMillis()
                }
            } else {
                emptyTimes = 0
                timeLimit = 0L
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
        t.addData("Emptytimes: ", emptyTimes)
        t.update()
        bot.move(0.0,0.0,0.0,0.0)

        return lastBearing
    }
    fun switchProc(proc: VisionProcessor) {
        bot.cam.visionPortal!!.setProcessorEnabled(
                proc,
                !bot.cam.visionPortal!!.getProcessorEnabled(proc)
        )
    }

    fun armmove() {
        val time = SystemClock.uptimeMillis()
        bot.arm.power = 0.6
        instance.telemetry.addData("Time", SystemClock.uptimeMillis() - time)
        while (instance.opModeIsActive() && bot.arm.currentPosition < 100.0) {
            if (bot.arm.velocity > 30) {
                bot.arm.power -= 0.02
            } else if (bot.arm.velocity < 30) {
                bot.arm.power += 0.01
            }
        }
        bot.arm.power = 0.0
        bot.arm.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.FLOAT
    }

    fun armback() {
        bot.arm.power = -0.5

        while (instance.opModeIsActive() && bot.arm.currentPosition > 80.0) {
            if (bot.arm.velocity > -30) {
                bot.arm.power -=  0.01
            } else if (bot.arm.velocity < -30) {
                bot.arm.power += 0.02
            }
        }
        bot.arm.power = 0.0
        bot.arm.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.FLOAT
    }
}

//moose jerky
