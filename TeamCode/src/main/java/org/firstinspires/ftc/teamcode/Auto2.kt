package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.util.ElapsedTime

@Autonomous(name = "Auto2", group = "Linear OpMode")
//@Disabled
class Auto2 : LinearOpMode() {
    // Declare OpMode members.
    private val runtime = ElapsedTime()
    override fun runOpMode() {

        val bot: Autonomous1 = Autonomous1(this, telemetry)
        telemetry.addData("Status", "Initialized")
        telemetry.update()
        // Wait for the game to start (driver presses PLAY)
        waitForStart()
        runtime.reset()
        val propPos:Int = 5 //bot.detectProp()
        telemetry.addData("Status", "Started")
        telemetry.update()
        // run until the end of the match (driver presses STOP)
        if (opModeIsActive()) {
            bot.resetEncoders()
            bot.move(5.0, 0.1)
            sleep(250)
            bot.pivot(90.0,0.8,telemetry)
            sleep(400)
            if(propPos == 5){
                bot.move(20.0, 0.4)
                sleep(250)
                bot.pivot(0.0, 0.8, telemetry)
                sleep(250)
                bot.intake(-0.4, 1500)
                bot.move(20.0, 0.4)
            } else {
                bot.strafeside(5.0, 0.6)
                bot.move(10.0, 0.7)
                if(propPos == 4) {
                    bot.pivot(45.0, -0.7)
                    bot.intake(0.8, 250)
                    bot.pivot(45.0, 0.7)
                } else {
                    bot.pivot(45.0, 0.7)
                    bot.intake(0.8, 250)
                    bot.pivot(45.0, -0.7)
                }
                bot.move(15.0, 0.7)
                bot.strafeside(5.0, -0.6)
            }
            bot.pivot(-90.0, 0.7,telemetry)
            bot.move(60.0, 0.4)
            bot.strafeside(20.0, 0.2)
            bot.goToAprilTag(10.0, propPos)
        }
        telemetry.addData("Status", "Ended")
        telemetry.update()
    }
}

