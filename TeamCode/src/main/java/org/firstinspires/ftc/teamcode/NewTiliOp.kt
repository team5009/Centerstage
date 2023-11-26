package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.util.ElapsedTime
import kotlin.math.abs
import kotlin.math.max


@TeleOp(name = "NewTiliOp", group = "Robot")
class NewTiliOp : LinearOpMode() {

    private val runtime = ElapsedTime()
    override fun runOpMode() {
        val bot = TeleOp1(this)
        var triggerIsPressed: Boolean = false
        var ratio = 0.15

        waitForStart()

        while (opModeIsActive()) {
            //telemetry.addLine("Caution! This code is experimental and might not work as well. Please do not use in a competition until you are proficient with the controls")
            //telemetry.addLine("-Emmanuel, the local madman")

            bot.tele(ratio)

            if (gamepad1.a && !gamepad1.start) {
                telemetry.addLine("S  l  o  w    m  o  d  e    e  n  g  a  g  e  d")
                ratio = 0.4
                telemetry.update()
            } else if (gamepad1.y) {
                telemetry.addLine("N o r m a l  s p e e d")
                telemetry.update()
                ratio = 0.6
            }

            if (gamepad2.dpad_down) {
                bot.bot.intake.power = 0.8
                bot.bot.flap.power = -0.7
                telemetry.addLine("ABSORBING :O")
            } else if (gamepad2.dpad_up) {
                bot.bot.intake.power = -0.8
                telemetry.addLine("Ew no Xp")
            } else {
                bot.bot.intake.power = 0.0
            }

            if (gamepad2.right_trigger > 0.1) {
                if (!triggerIsPressed) {
                    triggerIsPressed = true
                    bot.bot.arm.power = 0.5
                }
                bot.armmove()
                telemetry.addLine("LIFTING XI")
            } else if (gamepad2.left_trigger > 0.1) {
                if (!triggerIsPressed) {
                    triggerIsPressed = true
                    bot.bot.arm.power = -0.4
                }
                bot.armback()
                telemetry.addLine("Lowering :u")
            } else {
                triggerIsPressed = false
                bot.bot.arm.power = 0.0
            }

            if (gamepad2.x) {
                bot.bot.lift.power = -1.0
                telemetry.addLine("Arm extending ;)")
            } else if (gamepad2.b) {
                bot.bot.lift.power = 1.0
                telemetry.addLine("Arm Retracting XD")
            } else {
                bot.bot.lift.power = 0.0
            }

            if (gamepad2.a) {
                bot.bot.flap.power = 0.8
            } /*else if (gamepad2.y) {
            }*/ else if (!gamepad2.dpad_down) {
                bot.bot.flap.power = 0.0
            }
            telemetry.addLine("( ノ ・ ∀ ・ ) ノ")
            telemetry.update()
        }
    }
}