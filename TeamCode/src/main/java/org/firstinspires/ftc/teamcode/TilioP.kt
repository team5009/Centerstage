package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.Servo
import com.qualcomm.robotcore.util.ElapsedTime
import kotlinx.coroutines.android.awaitFrame
import kotlin.math.abs

@Suppress("UNUSED_EXPRESSION")
@TeleOp(name = "TilioP", group = "Robot")
class TilioP : LinearOpMode() {
    // Declare OpMode members.
    private val runtime = ElapsedTime()
    override fun runOpMode() {
        val bot = Ruboot(this)

        waitForStart()

//fr is a bit slower so once we get better motors well revert back to normal
        while (opModeIsActive()) {
            val left: Double = gamepad1.left_stick_y.toDouble()
            val right: Double = gamepad1.right_stick_y.toDouble()
            val exponantleft: Double = left * left *left
            val exponantright: Double = right * right *right
            if (gamepad1.left_stick_y < -0.1 && gamepad1.right_stick_y < -0.1) {
                bot.move(exponantleft *0.8, exponantright *0.8, exponantleft *0.8, exponantright *0.8)
                telemetry.addLine("Going Forward >:)")
            } else if (gamepad1.left_stick_y > 0.1 && gamepad1.right_stick_y > 0.1) {
                bot.move(exponantleft *0.8, exponantright *0.8, exponantleft *0.8, exponantright *0.8)
                telemetry.addLine("Going Backwards :I")
            } else if (gamepad1.left_stick_y > 0.1 && gamepad1.right_stick_y < -0.1) {
                bot.move(exponantleft *0.8, exponantright *0.8, exponantleft *0.8, exponantright *0.8)
                telemetry.addLine("Turning Left :)")
            } else if (gamepad1.left_stick_y < -0.1 && gamepad1.right_stick_y > 0.1) {
                bot.move(exponantleft *0.8,  exponantright *0.8, exponantleft *0.8, exponantright *0.8)
                telemetry.addLine("Turning Right :)")
            } else if (gamepad1.right_trigger > 0.1) {
                bot.move(0.5, 0.0,  0.0, 0.5)
                telemetry.addLine("Going Diagonal Right :o")
            } else if (gamepad1.left_trigger > 0.1) {
                bot.move(0.0, 0.5, 0.5, 0.0)
                telemetry.addLine("Going Diagonal Left :o")
            } else if (gamepad1.right_bumper) {
                bot.move(0.0, -0.5, -0.5, 0.0)
                telemetry.addLine("Going Backwards Diagonal Right :l")
            } else if (gamepad1.left_bumper) {
                bot.move(-0.5, 0.0, 0.0, -0.5)
                telemetry.addLine("Going Backwards Diagonal Left :l")
            } else if (gamepad1.dpad_right) {
                bot.move(-0.5, 0.5, 0.5, -0.5)
                telemetry.addLine("Going Right >:l")
            } else if (gamepad1.dpad_left) {
                bot.move(0.5, -0.5, -0.5, 0.5)
                telemetry.addLine("Going Left >:l")
            } else if (gamepad1.left_stick_y < -0.1 && abs(gamepad1.right_stick_y) < 0.1) {
                bot.move( -0.8, 0.0,  -0.8, 0.0)
                telemetry.addLine("Turning Left :)")
            } else if (abs(gamepad1.left_stick_y) < 0.1 && gamepad1.right_stick_y < -0.1) {
                bot.move(0.0, -0.8, 0.0,  -0.8)
                telemetry.addLine("Turning Right :)")
            } else if (gamepad1.left_stick_y > 0.1 && abs(gamepad1.right_stick_y) < 0.1) {
                bot.move( 0.8, 0.0,  0.8, 0.0)
                telemetry.addLine("Turning Right :)")
            } else if (abs(gamepad1.left_stick_y) < 0.1 && gamepad1.right_stick_y > 0.1) {
                bot.move(0.0, 0.8, 0.0,  0.8)
                telemetry.addLine("Turning Left :)")
            } else {
                bot.move(0.0, 0.0, 0.0, 0.0)
                telemetry.addLine("Not moving :(")
            }

            if (gamepad1.x) {
                bot.lift.power = 0.5
                telemetry.addLine("Arm extending ;)")
            } else if (gamepad1.b) {
                bot.lift.power = -0.5
                telemetry.addLine("Arm Retracting XD")
            } else {
                bot.lift.power = 0.0
            }
            if (gamepad1.dpad_down && gamepad1.a){
                sleep(100)
                telemetry.addLine("  _.---._"   )
                telemetry.addLine(" /.-   -. )" )
                telemetry.addLine(" ( *)  *)(|" )
                telemetry.addLine("  )  ^' _/()")
                telemetry.addLine("  'V' 'V' |/")
                telemetry.addLine("      <..<'" )
                telemetry.addLine("The punishment for your sin has come")
                telemetry.update()
                sleep(2000)
                telemetry.addLine("Kneel before me, " +
                        " For the angel of death has arrived")
                telemetry.update()
                sleep(1000)

            } else {
                telemetry.addLine("...")
            }

            telemetry.update()

        }
    }
}