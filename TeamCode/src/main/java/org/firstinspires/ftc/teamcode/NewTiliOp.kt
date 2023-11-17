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
        val bot = Ruboot(this)

        waitForStart()
        var ratio = -0.8
        fun tele() {
            val leftY = -gamepad1.left_stick_y.toDouble()
            val leftX = gamepad1.left_stick_x.toDouble() * 1.1
            val rightX = gamepad1.right_stick_x.toDouble()
            val deadZone = 0.2
            val threshHold = 0.15
            val denominator = max(abs(leftY) + abs(leftX) + abs(rightX), 1.0)

            bot.move(
                ((leftY + leftX + rightX) / denominator) * ratio,
                ((leftY - leftX - rightX) / denominator) * ratio,
                ((leftY - leftX + rightX) / denominator) * ratio,
                ((leftY + leftX - rightX) / denominator) * ratio
            )
        }
        while (opModeIsActive()) {
            //telemetry.addLine("Caution! This code is experimental and might not work as well. Please do not use in a competition until you are proficient with the controls")
            //telemetry.addLine("-Emmanuel, the local madman")

            tele()

            if (gamepad1.a) {
                telemetry.addLine("S  l  o  w    m  o  d  e    e  n  g  a  g  e  d")
                ratio = -0.2
                telemetry.update()
            } else if (gamepad1.y) {
                telemetry.addLine("GOING FAST!")
                ratio = -1.0
                telemetry.update()
            } else if (gamepad1.start) {
                telemetry.addLine("N o r m a l  s p e e d")
                telemetry.update()
                ratio = -0.8
            }
                if (gamepad2.dpad_down) {
                    bot.intake.power = 0.8
                    telemetry.addLine("ABSORBING :O")
                } else if (gamepad2.dpad_up){
                    bot.intake.power = -0.8
                telemetry.addLine("Ew no Xp")
                } else {
                bot.intake.power = 0.0
                }

            if (gamepad2.right_trigger > 0.1) {
                bot.arm.power = gamepad2.right_trigger.toDouble()
                bot.armmove()
                telemetry.addLine("LIFTING XI")
            } else if (gamepad2.left_trigger > 0.1) {
                bot.arm.power = gamepad2.left_trigger.toDouble() * -1.0
                bot.armback()
                telemetry.addLine("Lowering :u")
            } else {
                bot.arm.power = 0.0
            }
            if (gamepad2.x) {
                bot.lift.power = -0.5
                telemetry.addLine("Arm extending ;)")
            } else if (gamepad2.b) {
                bot.lift.power = 0.5
                telemetry.addLine("Arm Retracting XD")
            } else {
                bot.lift.power = 0.0
            }
            if (gamepad2.a){
                bot.flap.position = 0.32
            } else if (gamepad2.y) {
                bot.flap.position = 0.5
            }
            telemetry.addLine("you should kys ( ノ ・ ∀ ・ ) ノ")
            telemetry.update()
        }
    }
}