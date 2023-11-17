package org.firstinspires.ftc.teamcode
import android.os.SystemClock
import com.qualcomm.hardware.bosch.BHI260IMU
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.IMU
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import kotlin.math.abs

class Ruboot (op : LinearOpMode) {

    val Instance = op
    val fl: DcMotorEx = Instance.hardwareMap.get(DcMotorEx::class.java, "FL")
    val fr: DcMotorEx = Instance.hardwareMap.get(DcMotorEx::class.java, "FR")
    val bl: DcMotorEx = Instance.hardwareMap.get(DcMotorEx::class.java, "BL")
    val br: DcMotorEx = Instance.hardwareMap.get(DcMotorEx::class.java, "BR")


    val lift: DcMotorEx = Instance.hardwareMap.get(DcMotorEx::class.java, "elevato")
    val arm: DcMotorEx = Instance.hardwareMap.get(DcMotorEx::class.java, "arm")
    val intake: DcMotorEx = Instance.hardwareMap.get(DcMotorEx::class.java, "intake")
    val flap: Servo = Instance.hardwareMap.get(Servo::class.java, "flap")

    val imu: BHI260IMU = Instance.hardwareMap.get(BHI260IMU::class.java, "imu")


    //val cam1: CameraName = Instance.hardwareMap.get("FrontCam") as WebcamName


    init {
        // Set Each Wheel Direction
        fl.direction = DcMotorSimple.Direction.REVERSE
        fr.direction = DcMotorSimple.Direction.FORWARD
        bl.direction = DcMotorSimple.Direction.REVERSE
        br.direction = DcMotorSimple.Direction.FORWARD


        lift.direction = DcMotorSimple.Direction.FORWARD
        arm.direction = DcMotorSimple.Direction.FORWARD
        intake.direction = DcMotorSimple.Direction.FORWARD


        fl.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        fl.mode = DcMotor.RunMode.RUN_USING_ENCODER
        fr.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        fr.mode = DcMotor.RunMode.RUN_USING_ENCODER
        bl.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        bl.mode = DcMotor.RunMode.RUN_USING_ENCODER
        br.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        br.mode = DcMotor.RunMode.RUN_USING_ENCODER


        lift.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        lift.mode = DcMotor.RunMode.RUN_USING_ENCODER
        arm.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        arm.mode = DcMotor.RunMode.RUN_USING_ENCODER


        // Behaviour when Motor Power = 0
        fl.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        fr.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        bl.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        br.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE


        lift.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        arm.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        intake.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE

        val imuParameters: IMU.Parameters = IMU.Parameters(
            RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.UP,
                RevHubOrientationOnRobot.UsbFacingDirection.RIGHT
            )
        )

        imu.initialize(imuParameters)
        imu.resetYaw()

    }

    val rawHeading: Double
        get() = imu.robotYawPitchRollAngles.getYaw(AngleUnit.DEGREES)

    val absoluteHeading: Double
        get() = if (rawHeading < 0) {
            360 - abs(rawHeading)
        } else {
            rawHeading
        }

    val encoderLeft = fl
    val encoderRight = fr
    val encoderBack = bl

    fun tics_per_inch(inches: Double): Double {
        return 384.5 / 4 / Math.PI * inches
    }


    fun tics_per_lift(inches: Double): Double {
        return 1120 / 1.96 / Math.PI * inches
    }

    fun move(flPower: Double, frPower: Double, blPower: Double, brPower: Double) {
        fl.power = flPower / 2
        fr.power = frPower
        bl.power = brPower
        br.power = blPower
    }


    fun armmove() {
        val time = SystemClock.uptimeMillis()
        val timeofloop = SystemClock.uptimeMillis() - time
        Instance.telemetry.addData("Time", SystemClock.uptimeMillis() - time)
        if (timeofloop > 250) {
            if (arm.velocity > 90) {
                arm.power = arm.power - 0.1
            } else if (arm.power < 90) {
                arm.power = arm.power + 0.1
            } else {
                arm.power = arm.power
            }
        }
    }

    fun armback() {
        val time = SystemClock.uptimeMillis()
        while (Instance.gamepad2.left_trigger > 0.1) {
            val timeofloop = SystemClock.uptimeMillis() - time
            Instance.telemetry.addData("Time", SystemClock.uptimeMillis() - time)
            if (timeofloop > 250) {
                if (arm.velocity > 90) {
                    arm.power = arm.power + 0.1
                } else if (arm.power < 90) {
                    arm.power = arm.power - 0.1
                } else {
                    arm.power = arm.power
                }
            }
        }
    }
}
