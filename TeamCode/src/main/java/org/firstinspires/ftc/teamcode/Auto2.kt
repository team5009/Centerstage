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

        telemetry.addData("Status", "Started")
        telemetry.update()
        // run until the end of the match (driver presses STOP)
        if (opModeIsActive()) {
            bot.move(10.0, 0.7)
            sleep(1000)
            //  bot.pivot(180.0, 0.5)
        }
        telemetry.addData("Status", "Ended")
        telemetry.update()
    }
}


