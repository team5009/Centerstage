package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.util.ElapsedTime
import kotlin.math.abs
import kotlin.math.max

@TeleOp(name = "EXPTilioP", group = "Robot")
class EXPTilioP : LinearOpMode() {

    private val runtime = ElapsedTime()
    override fun runOpMode() {
        val bot = Ruboot(this)

        waitForStart()
        var ratio = 0.8
        var fl: Double = 0.0
        var bl: Double = 0.0
        var fr: Double = 0.0
        var br: Double = 0.0



            while (opModeIsActive()) {
                //telemetry.addLine("Caution! This code is experimental and might not work as well. Please do not use in a competition until you are proficient with the controls")
                //telemetry.addLine("-Emmanuel, the local madman")


                if (gamepad1.a) {
                    telemetry.addLine("S  l  o  w    m  o  d  e    e  n  g  a  g  e  d")
                    ratio = 0.2
                    telemetry.update()
                } else if (gamepad1.y) {
                    telemetry.addLine("GOING FAST!")
                    ratio = 1.0
                    telemetry.update()
                } else if (gamepad1.start) {
                    telemetry.addLine("N o r m a l  s p e e d")
                    telemetry.update()
                    ratio = 0.8
                }
                val drive: Double = gamepad1.left_stick_y.toDouble() * ratio
                val strafe: Double = gamepad1.left_stick_x.toDouble() * ratio
                val turn: Double = gamepad2.right_stick_x.toDouble() * ratio
                val denominator = max(abs(drive) + abs(strafe) + abs(turn), 1.0)

                telemetry.addLine()
                telemetry.update()

                /*fl = drive - strafe - turn
                fr = drive + strafe + turn
                br = drive - strafe + turn*/


                if ((abs(drive) - abs(strafe)) > 0.1) { //forward or backwards
                    telemetry.addLine("Congrats! You figured out how to move forwards. Or backwards. I don't really care")
                    fl = (drive) / denominator
                    bl = (drive) - strafe / denominator
                    fr = (drive) - strafe / denominator
                    br = (drive) / denominator
                } else if (abs(strafe) - abs(drive) > 0.1) { //left or right
                    telemetry.addLine("Damn. Didn't know you had the brain power to do this")
                    fl = (strafe) + drive / denominator
                    bl = (strafe) - drive /denominator
                    br = (strafe) + drive / denominator
                    fr = (strafe) - drive / denominator
                } else if (abs(drive - strafe) < 0.1 && abs(drive) > 0.1) { //directly diagonal
                    telemetry.addLine("Congrats! You are slightly annoying me! Your IP address is 10.12.22.156")
                    fl = drive - abs(strafe) / denominator
                    bl = drive / denominator
                    br = drive - abs(strafe) / denominator
                    fr = drive / denominator
                } else if (abs(drive) < 0.1 && abs(strafe) > 0.1) { //directly sideways
                    telemetry.addLine("Why. Just why?")
                    fl = strafe * -5 / denominator
                    fr = strafe * 5 / denominator
                    bl = strafe * 5 / denominator
                    br = strafe * -5 / denominator
                } else if (abs(strafe) < 0.1 && abs(drive) > 0.1) { //directly forward or backwards
                    telemetry.addLine("You have room temperature iq don't you?")
                    fl = drive / denominator
                    fr = drive * -1 / denominator
                    bl = drive * -1 / denominator
                    br = drive / denominator
                } else { //nothing
                    telemetry.addLine("NOTHING IS WORKING AND THIS IS YOUR FAULT")
                    fl = 0.0
                    bl = 0.0
                    fr = 0.0
                    br = 0.0
                }


                if (abs(turn) > 0.1) {
                    telemetry.addLine("Damn bro. I know your little brother is dumb and all but let him play")
                    if (abs(gamepad1.right_stick_y) < 0.1 && abs(gamepad1.right_stick_x) > 0.1) {
                        fl = abs(fl) + turn
                        bl = abs(bl) + turn
                        fr = -abs(fr) - turn
                        br = -abs(br) - turn
                    } else if (abs(gamepad1.right_stick_y) > 0.1 && abs(gamepad1.right_stick_x) > 0.1) {
                        fl = fl - turn
                        bl = bl - turn
                        br = br + turn
                        fr = fr + turn
                    }



                    if (gamepad2.dpad_down) {
                        bot.intake.power = 0.8
                        telemetry.addLine("ABSORBING :O")
                        telemetry.addLine("- ")
                    } else
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
                    if (gamepad2.dpad_left){
                        bot.flap.position = 0.39
                    } else if (gamepad2.dpad_right) {
                        bot.flap.position = 0.7
                    }



                if (gamepad1.a) {
                    sleep(100)
                    telemetry.addLine("   _ . - - - - - . _   ")
                    telemetry.addLine(" /   . -       - .   )  ")
                    telemetry.addLine("  (   * )     * )   ( |  ")
                    telemetry.addLine("  )     ^ '     _ / ( ) ")
                    telemetry.addLine("    ' V '   ' V '   | /  ")
                    telemetry.addLine("          < . . < '    ")
                    telemetry.addLine("             ")
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

                telemetry.addLine("I'm honestly surprised you turned the code on")
                telemetry.addData("fl", fl)
                telemetry.addData("fr", fr)
                telemetry.addData("br", br)
                telemetry.addData("bl", bl)
                bot.move(fl, fr, bl, br)
                telemetry.update()

            }
    }
}

