package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotorEx

@Autonomous (name = "ExpansionHub Test", group = "Robot")
class ExpantionTest : LinearOpMode() {

    override fun runOpMode() {

        val motors : List<DcMotorEx> = hardwareMap.getAll(DcMotorEx::class.java)
        val bot = RobotTest(this, 1)
        for (motor in motors) {
            telemetry.addData( "Motor Name: ", motor.deviceName)
        }
        telemetry.update()
        waitForStart()
        val len = motors.lastIndex
        var i: Int = 0
        while (opModeIsActive()) {
            while (i <= len && opModeIsActive()){
                motors[i].power = 0.5
                sleep(1000)
                motors[i].power = 0.0
                i ++
            }
        }
    }
}