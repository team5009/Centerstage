package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.util.ElapsedTime
import kotlin.math.abs

@Autonomous(name = "RED BACK", group = "Linear OpMode")
//@Disabled
class Auto2 : LinearOpMode() {
    // Declare OpMode members.
    private val runtime = ElapsedTime()
    override fun runOpMode() {
        val bot: Autonomous1 = Autonomous1(this, 1, telemetry)
        var heading: Double = 0.0
        telemetry.addData("Status", "Initialized")
        telemetry.update()
        // Wait for the game to start (driver presses PLAY)
        while (!opModeIsActive()) {
            telemetry.addData("Pos: ", bot.detectProp(1))
            telemetry.addData("Size", bot.bot.cam.camProc!!.getSize())
            telemetry.addData("centerX", bot.bot.cam.camProc!!.getCenter().x)
            telemetry.update()
        }
        waitForStart()
        runtime.reset()
        val propPos: Int = bot.detectProp(1)
        telemetry.addData("Status", "Started")
        telemetry.update()
        // run until the end of the match (driver presses STOP)


        if (opModeIsActive()) {
            val propPos: Int = bot.detectProp(1)
            bot.switchProc(bot.bot.cam.camProc!!)
            if (propPos == 5) {
                bot.move(49.0, 0.4)
                //bot.intake(-0.6, 1000)
                bot.move(7.0, 0.5)
                sleep(500)
                bot.move(3.0, -0.4)
                sleep(500)
                bot.pivot(100.0, -0.3)
                sleep(500)
                bot.move(15.0, 0.4)
                sleep(500)
                bot.pivot(25.0, -0.3)
                sleep(500)
                heading = bot.goToAprilTag(6.0, 5)
            } else {
                bot.move(30.0, 0.4)
                if (propPos == 4) {
                    bot.move(1.0, 0.4)
                    bot.pivot(120.0, 0.3)
                    bot.intake(-0.6, 1000)
                    bot.pivot(120.0, -0.3)
                    sleep(500)
                    bot.move(15.0, 0.4)
                    sleep(500)
                    bot.pivot(130.0, -0.3)
                    sleep(500)
                    bot.move(25.0, 0.4)
                    sleep(500)
                    bot.pivot(20.0, 0.4)
                    sleep(500)
                    heading = bot.goToAprilTag(5.0, 6)
                    bot.strafeside(5.0, 0.3)
                    bot.move(10.0, 0.4)
                } else {
                    bot.move(2.0, 0.4)
                    bot.pivot(80.0, -0.5)
                    bot.intake(-0.8, 1000)
                    bot.move(25.0, 0.4)
                    heading = bot.goToAprilTag(6.5, 4)
                    bot.pivot(45.0, 0.4)
                    bot.intake(0.8, 250)
                    bot.pivot(45.0, -0.4)
                }

            }
            telemetry.update()
            telemetry.addLine("Finished go to Apriltag")
            telemetry.update()
            bot.switchProc(bot.bot.cam.aprilTag!!)
            bot.pivot(abs(heading) * 10, 0.3 * bot.sign(heading).toDouble())
            sleep(500)
            bot.intake(0.6, -1L)
            bot.armmove()
            bot.intake(0.0, 100L)
            sleep(500)
            //bot.bot.flap.position = 0.3
            sleep(500)
            bot.armback()
            sleep(1000)
              bot.pivot(80.0, -0.4)
            sleep(500)
            bot.move(20.0, 0.4)
            bot.pivot(90.0, -0.4)
            bot.move(60.0, 0.4)
            bot.strafeside(20.0, 0.3)
            bot.goToAprilTag(10.0, propPos)
        }
        bot.bot.cam.visionPortal!!.close()
        telemetry.addData("Status", "Ended")
        telemetry.update()
    }
}


