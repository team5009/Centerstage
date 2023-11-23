package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.util.ElapsedTime
import kotlin.math.abs
import kotlin.math.max


@TeleOp(name = "NewTiliOp", group = "Robot")
class NewTiliOp : LinearOpMode() {

    private val runtime = ElapsedTime()
    override fun runOpMode() {
        val bot = TeleOp1(this)
        var triggerIsPressed : Boolean = false

        waitForStart()
        var ratio = 0.15

        while (opModeIsActive()) {
            //telemetry.addLine("Caution! This code is experimental and might not work as well. Please do not use in a competition until you are proficient with the controls")
            //telemetry.addLine("-Emmanuel, the local madman")

            tele(ratio, bot)

            if (gamepad1.a) {
                telemetry.addLine("S  l  o  w    m  o  d  e    e  n  g  a  g  e  d")
                ratio = 0.07
                telemetry.update()
            } else if (gamepad1.y) {
                telemetry.addLine("GOING FAST!")
                ratio = 1.0
                telemetry.update()
            } else if (gamepad1.right_stick_button) {
                telemetry.addLine("N o r m a l  s p e e d")
                telemetry.update()
                ratio = 0.15
            }

            if (gamepad2.dpad_down) {
                bot.bot.intake.power = 0.8
                telemetry.addLine("ABSORBING :O")
            } else if (gamepad2.dpad_up){
                bot.bot.intake.power = -0.8
                telemetry.addLine("Ew no Xp")
            } else {
                bot.bot.intake.power = 0.0
            }

            if (gamepad2.right_trigger > 0.1) {
                if(!triggerIsPressed) {
                    triggerIsPressed = true
                    bot.bot.arm.power = 0.5
                }
                bot.armmove()
                telemetry.addLine("LIFTING XI")
            } else if (gamepad2.left_trigger > 0.1) {
                if(!triggerIsPressed) {
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

            if (gamepad2.a){
                bot.bot.flap.position = 0.32
                bot.bot.flap.position = 0.5
            } else if (gamepad2.y) {
            }
            telemetry.addLine("( ノ ・ ∀ ・ ) ノ")
            telemetry.update()
        }
    }
    fun tele(ratio : Double, bot : TeleOp1) {
        val leftY = -gamepad1.left_stick_y.toDouble()
        val leftX = gamepad1.left_stick_x.toDouble() * 1.1
        val rightX = gamepad1.right_stick_x.toDouble()
        val denominator = max(abs(leftY) + abs(leftX) + abs(rightX), 1.0)
        if (leftX < 0.5|| rightX < 0.5|| leftY < 0.5) {
            bot.move(
                ((leftY + rightX - leftX) / denominator) * ratio,
                ((leftY - rightX + leftX) / denominator) * ratio,
                ((leftY - rightX - leftX) / denominator) * ratio,
                ((leftY + rightX + leftX) / denominator) * ratio
            )
        } else {
            bot.fastmove(
                ((leftY + rightX - leftX) / denominator) * ratio,
                ((leftY - rightX + leftX) / denominator) * ratio,
                ((leftY - rightX - leftX) / denominator) * ratio,
                ((leftY + rightX + leftX) / denominator) * ratio
            )
        }
    }
}