package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.util.ElapsedTime
import kotlin.math.abs

@TeleOp(name = "EXPTilioP", group = "Robot")
class EXPTilioP : LinearOpMode() {

    private val runtime = ElapsedTime()
    override fun runOpMode() {
        val bot = Ruboot(this)

        waitForStart()
        var ratio = -0.8
        var fl: Double = 0.0
        var bl: Double = 0.0
        var fr: Double = 0.0
        var br: Double = 0.0



            while (opModeIsActive()) {
                //telemetry.addLine("Caution! This code is experimental and might not work as well. Please do not use in a competition until you are proficient with the controls")
                //telemetry.addLine("-Emmanuel, the local madman")



                val drive: Double = gamepad1.left_stick_y.toDouble() * ratio
                val strafe: Double = gamepad1.left_stick_x.toDouble() * ratio
                val turn: Double = gamepad2.right_stick_x.toDouble() * ratio

                telemetry.addLine()
                telemetry.update()

                /*fl = drive - strafe - turn
                fr = drive + strafe + turn
                br = drive - strafe + turn*/


                if ((abs(drive) - abs(strafe)) > 0.1) {
                    fl = (drive)
                    bl = (drive) - strafe
                    fr = (drive) - strafe
                    br = (drive)
                } else if (abs(strafe) - abs(drive) > 0.1) {
                    fl = (strafe) + abs(drive)
                    bl = (strafe) - abs(drive)
                    br = (strafe) + abs(drive)
                    fr = (strafe) - abs(drive)
                } else if (abs(drive - strafe) < 0.1 && abs(drive) > 0.1) {
                    fl = drive - abs(strafe)
                    bl = drive
                    br = drive - abs(strafe)
                    fr = drive
                } else if (abs(drive) < 0.1 && abs(strafe) > 0.1) {
                    fl = strafe * -1
                    fr = strafe
                    bl = strafe
                    br = strafe * -1
                } else if (abs(strafe) < 0.1 && abs(drive) > 0.1) {
                    fl = drive
                    fr = drive * -1
                    bl = drive * -1
                    br = drive
                } else {
                    fl = 0.0
                    bl = 0.0
                    fr = 0.0
                    br = 0.0
                }


                if (abs(turn) > 0.1) {
                    if (abs(gamepad1.right_stick_y) < 0.1 && abs(gamepad1.right_stick_x) < 0.1) {
                        fl = fl
                        bl = bl
                        fr = fr
                        br = br
                    } else if (abs(gamepad1.right_stick_y) < 0.1 && abs(gamepad1.right_stick_x) > 0.1) {
                        fl = abs(fl) + turn
                        bl = abs(bl) + turn
                        fr = -abs(fr) - turn
                        br = -abs(br) - turn
                    } else if (abs(gamepad1.right_stick_y) > 0.1 && abs(gamepad1.right_stick_x) > 0.1) {
                        fl = fl
                        bl = bl
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

                telemetry.update()
                bot.move(fl, fr, bl, br)
            }


    }
}

