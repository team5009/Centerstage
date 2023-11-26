package org.firstinspires.ftc.teamcode

import com.qualcomm.hardware.bosch.BHI260IMU
import com.qualcomm.hardware.bosch.BNO055IMU
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.CRServoImplEx
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.IMU
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference
import org.firstinspires.ftc.robotcore.external.navigation.Orientation

class RobotTest(Instance: LinearOpMode, alliance : Int) {
    val fl: DcMotorEx = Instance.hardwareMap.get(DcMotorEx::class.java, "FL")
    val fr: DcMotorEx = Instance.hardwareMap.get(DcMotorEx::class.java, "FR")
    val bl: DcMotorEx = Instance.hardwareMap.get(DcMotorEx::class.java, "BL")
    val br: DcMotorEx = Instance.hardwareMap.get(DcMotorEx::class.java, "BR")

    val leftEncoder : DcMotorEx = Instance.hardwareMap.get(DcMotorEx::class.java, "BL")
    val rightEncoder: DcMotorEx = Instance.hardwareMap.get(DcMotorEx::class.java, "BR")
    val backEncoder: DcMotorEx = Instance.hardwareMap.get(DcMotorEx::class.java, "FL")

    val lift: DcMotorEx = Instance.hardwareMap.get(DcMotorEx::class.java, "elevato")
    val arm: DcMotorEx = Instance.hardwareMap.get(DcMotorEx::class.java, "arm")
    val intake: DcMotorEx = Instance.hardwareMap.get(DcMotorEx::class.java, "intake")
    val flap: CRServoImplEx = Instance.hardwareMap.get(CRServoImplEx::class.java,"flap")

    val cam: Camera = Camera(alliance)
    val imu: BHI260IMU = Instance.hardwareMap.get(BHI260IMU::class.java, "imu")
    //val cam1: CameraName = Instance.hardwareMap.get("FrontCam") as WebcamName

    init {
        // Set Each Wheel Direction
        fl.direction = DcMotorSimple.Direction.FORWARD
        fr.direction = DcMotorSimple.Direction.REVERSE
        bl.direction = DcMotorSimple.Direction.REVERSE
        br.direction = DcMotorSimple.Direction.FORWARD

        leftEncoder.direction = DcMotorSimple.Direction.REVERSE
        rightEncoder.direction = DcMotorSimple.Direction.FORWARD
        backEncoder.direction = DcMotorSimple.Direction.FORWARD

        leftEncoder.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        rightEncoder.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        backEncoder.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER


        lift.direction = DcMotorSimple.Direction.FORWARD
        arm.direction = DcMotorSimple.Direction.FORWARD
        intake.direction = DcMotorSimple.Direction.FORWARD

        fl.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        fl.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        fr.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        fr.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        bl.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        bl.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        br.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        br.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER


        intake.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        intake.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
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

        cam.initAprilTag(Instance.hardwareMap)
        val imuParameters : IMU.Parameters = IMU.Parameters(
                RevHubOrientationOnRobot(
                        Orientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES, 0.0f, -45.0f, 0.0f, 0)
                )
        )

        imu.initialize(imuParameters)
        imu.resetYaw()

        //flap = 0.5
    }

    fun tics_per_inch(inches: Double): Double {
        return 384.5 / 4 / Math.PI * inches
    }

    fun tics_per_lift(inches: Double) :Double {
        return 1120 / 1.96 / Math.PI * inches
    }

    fun move(flPower: Double, frPower: Double, blPower: Double, brPower: Double) {
        fl.power = flPower
        fr.power = frPower
        bl.power = blPower
        br.power = brPower
    }

    val rawHeading: Double
        get() = imu.robotYawPitchRollAngles.getYaw(AngleUnit.DEGREES)

}