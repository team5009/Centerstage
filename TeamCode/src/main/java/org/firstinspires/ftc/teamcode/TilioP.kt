package org.firstinspires.ftc.teamcode

import android.annotation.SuppressLint
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.teamcode.autonomous.Pivut
import kotlin.math.abs

@Suppress("UNUSED_EXPRESSION")
@TeleOp(name = "TilioP", group = "Robot")
class TilioP : LinearOpMode() {
    // Declare OpMode members.
    private val runtime = ElapsedTime()

    @SuppressLint("SuspiciousIndentation")
    override fun runOpMode() {
        val bot = Ruboot(this)
        val pivut = Pivut()

        waitForStart()
        var ratio = 0.8
        while (opModeIsActive()) {
            telemetry.update()
            val left: Double = gamepad1.left_stick_y.toDouble()
            val right: Double = gamepad1.right_stick_y.toDouble()
            val left_trig: Double = gamepad1.left_trigger.toDouble()
            val right_trig: Double = gamepad1.right_trigger.toDouble()
            val left_trig2: Double = gamepad2.right_trigger.toDouble()
            val right_trig2: Double = gamepad2.right_trigger.toDouble()
            val exponantleft: Double = left * left * left
            val exponantright: Double = right * right * right
            val exponantlefttrig2: Double = left_trig2 * left_trig2 * left_trig2
            val exponantrighttrig2: Double = right_trig2 * right_trig2 * right_trig2
            val exponantlefttrig: Double = left_trig * left_trig * left_trig
            val exponantrighttrig: Double = right_trig * right_trig * right_trig
            if (gamepad2.a) {
                telemetry.addLine("S  l  o  w    m  o  d  e    e  n  g  a  g  e  d")
                ratio = 0.4
                telemetry.update()
            } else if (gamepad2.y) {
                telemetry.addLine("GOING FAST!")
                ratio = 1.0
                telemetry.update()
            } else if (gamepad2.start) {
                telemetry.addLine("N o r m a l  s p e e d")
                telemetry.update()
                ratio = 0.8
            }
            if (gamepad1.left_stick_y < -0.1 && gamepad1.right_stick_y < -0.1) {
                bot.move(
                    exponantleft * ratio,
                    exponantright * ratio,
                    exponantleft * ratio,
                    exponantright * ratio
                )
                telemetry.addLine("Going Forward >:)")
            } else if (gamepad1.left_stick_y > 0.1 && gamepad1.right_stick_y > 0.1) {
                bot.move(
                    exponantleft * ratio,
                    exponantright * ratio,
                    exponantleft * ratio,
                    exponantright * ratio
                )
                telemetry.addLine("Going Backwards :I")
            } else if (gamepad1.left_stick_y > 0.1 && gamepad1.right_stick_y < -0.1) {
                bot.move(
                    exponantleft * ratio,
                    exponantright * ratio,
                    exponantleft * ratio,
                    exponantright * ratio
                )
                telemetry.addLine("Turning Left :)")
            } else if (gamepad1.left_stick_y < -0.1 && gamepad1.right_stick_y > 0.1) {
                bot.move(
                    exponantleft * ratio,
                    exponantright * ratio,
                    exponantleft * ratio,
                    exponantright * ratio
                )
                telemetry.addLine("Turning Right :)")
            } else if (gamepad1.left_bumper) {
                bot.move(0.5, 0.0, 0.0, 0.5)
                telemetry.addLine("Going Diagonal Right :o")
            } else if (gamepad1.right_bumper) {
                bot.move(0.0, 0.5, 0.5, 0.0)
                telemetry.addLine("Going Diagonal Left :o")
            } else if (gamepad1.left_trigger > 0.1) {
                bot.move(0.0, exponantlefttrig * -ratio, exponantlefttrig * -ratio, 0.0)
                telemetry.addLine("Going Backwards Diagonal Right :l")
            } else if (gamepad1.right_trigger > 0.1) {
                bot.move(exponantrighttrig * -ratio, 0.0, 0.0, exponantrighttrig * -ratio)
                telemetry.addLine("Going Backwards Diagonal Left :l")
            } else if (gamepad1.dpad_right) {
                bot.move(-0.5, 0.5, 0.5, -0.5)
                telemetry.addLine("Going Right >:l")
            } else if (gamepad1.dpad_left) {
                bot.move(0.5, -0.5, -0.5, 0.5)
                telemetry.addLine("Going Left >:l")
            } else if (gamepad1.left_stick_y < -0.1 && abs(gamepad1.right_stick_y) < 0.1) {
                bot.move(0.0, exponantright * ratio, 0.0, exponantright * ratio)
                telemetry.addLine("Turning Left :)")
            } else if (abs(gamepad1.left_stick_y) < 0.1 && gamepad1.right_stick_y < -0.1) {
                bot.move(exponantleft * ratio, 0.0, exponantleft * ratio, 0.0)
                telemetry.addLine("Turning Right :)")
            } else if (gamepad1.right_stick_y > 0.1 && abs(gamepad1.left_stick_y) < 0.1) { //this one works
                bot.move(0.0, exponantright * ratio, 0.0, exponantright * ratio)
                telemetry.addLine("Turning Right :)")
            } else if (abs(gamepad1.right_stick_y) < 0.1 && gamepad1.left_stick_y > 0.1) {
                bot.move(exponantleft * ratio, 0.0, exponantleft * ratio, 0.0)
                telemetry.addLine("Turning Left :)")
            } else {
                bot.move(0.0, 0.0, 0.0, 0.0)
                telemetry.addLine("Not moving :(")
            }

            if (gamepad2.dpad_down) {
                bot.intake.power = 0.8
                telemetry.addLine("ABSORBING :O")
                telemetry.addLine("- ")
            } else if (gamepad2.dpad_down) {
                bot.intake.power = -0.8
                telemetry.addLine("Ew no Xp")
                telemetry.addLine("- ")
            } else {
                bot.intake.power = 0.0
            }

            if (gamepad2.right_trigger > 0.1) {
                bot.arm.power = 0.4
                telemetry.addLine("LIFTING XI")
                telemetry.addLine("- ")
            } else if (gamepad2.left_trigger > 0.1) {
                bot.arm.power = -0.4
                telemetry.addLine("Lowering :u")
                telemetry.addLine("-")
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


            if (gamepad2.dpad_down && gamepad1.a) {
                sleep(100)
                telemetry.addLine("   _ . - - - - - . _      ")
                telemetry.addLine(" /   . -       - .   )    ")
                telemetry.addLine("  (   * )     * )   ( |   ")
                telemetry.addLine("  )     ^ '     _ / ( )   ")
                telemetry.addLine("    ' V '   ' V '   | /   ")
                telemetry.addLine("          < . . < '       ")
                telemetry.addLine("                          ")
                telemetry.addLine("The punishment for your sin has come")
                telemetry.update()
                sleep(3000)
                telemetry.addLine(
                    "Kneel before me, " +
                            " For the angel of death has arrived"
                )
                telemetry.update()
                sleep(2000)

            } else {
                telemetry.addLine("you should kys ( ノ ・ ∀ ・ ) ノ")
            }

            var prevact: String = "Nothing"
            var loc: Int = 0
            var play: Boolean = false

            if (gamepad2.ps && !play) {
                play = true
            }
            if (gamepad2.touchpad && play) {
                play = false
            }

            if (play) {
                telemetry.addLine("  o ")
                telemetry.addLine(" /|) ")
                telemetry.addLine(" /| ")
                prevact = "standing"
                telemetry.update()
                loc = 0
                telemetry.update()
                sleep(1000)
                telemetry.addLine("this is you")
                telemetry.update()
                if (gamepad2.dpad_right && loc == 0) {
                    telemetry.clear()
                    telemetry.addLine("   o ")
                    telemetry.addLine("  /|V")
                    telemetry.addLine("   >) ")
                    loc = 1
                    prevact = "run1"
                    telemetry.update()
                    sleep(400)
                    telemetry.clear()
                    telemetry.addLine("     o  ")
                    telemetry.addLine("     V! ")
                    telemetry.addLine("     /> ")
                    loc = 2
                    prevact = "run2"
                    telemetry.update()
                    sleep(400)
                    telemetry.clear()
                    telemetry.addLine("     o ")
                    telemetry.addLine("    /|) ")
                    telemetry.addLine("    /| ")
                    loc = 3
                    prevact = "standing"
                    telemetry.update()

                } else if (gamepad2.dpad_left && loc == 0) {
                    while (gamepad2.dpad_left)
                        telemetry.clear()
                    telemetry.addLine("|<o< ")
                    telemetry.addLine("| | ")
                    telemetry.addLine("|/ ( ")
                    telemetry.update()
                    sleep(400)
                    telemetry.clear()
                    telemetry.addLine("|(o( ")
                    telemetry.addLine("| | ")
                    telemetry.addLine("|( V  ")
                    telemetry.update()
                    sleep(400)
                } else if (gamepad2.dpad_down) {
                    telemetry.clear()
                    telemetry.addLine(" o ")
                    telemetry.addLine("<|>")
                    telemetry.addLine("( )")
                    telemetry.update()
                    sleep(100)
                    while (gamepad2.dpad_down)
                        telemetry.clear()
                    telemetry.addLine("  ")
                    telemetry.addLine("<o>")
                    telemetry.addLine(">>")
                    telemetry.update()

                } else if (loc == 3 && gamepad2.dpad_right) {

                    telemetry.clear()
                telemetry.addLine("   o ")
                telemetry.addLine("  /|V")
                telemetry.addLine("   >) ")
                loc = 4
                prevact = "run1"
                telemetry.update()
                sleep(400)
                telemetry.clear()
                telemetry.addLine("     o  ")
                telemetry.addLine("     V! ")
                telemetry.addLine("     /> ")
                loc = 5
                prevact = "run2"
                telemetry.update()
                sleep(400)
                telemetry.clear()
                telemetry.addLine("     o ")
                telemetry.addLine("    /|) ")
                telemetry.addLine("    /| ")
                loc = 6
                prevact = "standing"
                telemetry.update()
            }

            telemetry.update()


            }
        }
    }
}

