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

        val bot: Autonomous1 = Autonomous1(this, 2 ,telemetry)
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
          //  bot.move(5.0, 0.7) why is this here
            sleep(1000)
            bot.strafeside(10.0, 0.3)
            if(propPos == 5){
                bot.move(20.0, 0.4)
                bot.strafeside(4.0, -0.3)
                bot.intake(0.8, 250)
                bot.move(5.0, 0.4)
                bot.strafeside(0.6, -0.3)
            } else {
                bot.strafeside(5.0, 0.3)
                bot.move(10.0, 0.4)
                if(propPos == 4) {
                    bot.pivot(45.0, -0.4)
                    bot.intake(0.8, 250)
                    bot.pivot(45.0, 0.4)
                } else {
                    bot.pivot(45.0, 0.4)
                    bot.intake(0.8, 250)
                    bot.pivot(45.0, -0.4)
                }
                bot.move(15.0, 0.4)
                bot.strafeside(5.0, -0.3)
            }
            bot.pivot(90.0, -0.4)
            bot.move(60.0, 0.4)
            bot.strafeside(20.0, 0.3)
            bot.goToAprilTag(10.0, propPos)
        }
        telemetry.addData("Status", "Ended")
        telemetry.update()
    }
}


