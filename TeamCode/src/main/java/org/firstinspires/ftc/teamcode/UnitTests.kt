package org.firstinspires.ftc.teamcode


import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.util.ElapsedTime

@Autonomous(name = "UnitTests", group = "Linear OpMode")
//@Disabled
class UnitTests : LinearOpMode() {
    val PosX = 0
    val PosY = 0
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
            waitForButton()
            bot.accelerate(10.0, 0.7)
            waitForButton()
           // bot.lifting(12.0, 0.5)
           // waitForButton()
          //  bot.lifting(12.0, -0.5)
          //  waitForButton()
           // bot.pivot(180.0, 0.5)
           // waitForButton()
           // bot.strafeside(25.0, 0.7)
          //  waitForButton()
          //  bot.strafefrbl(15.0, 0.7)
          //  waitForButton()
           // bot.strafeflbr(15.0, 0.7)
           // waitForButton()
           // bot.strafefrbl(15.0, -0.7)
          //  waitForButton()
          //  bot.strafeflbr(15.0, -0.7)
        }
        telemetry.addData("Status", "Ended")
        telemetry.update()

    }
    fun waitForButton() {
        telemetryDisplay()
        while(opModeIsActive() && !gamepad1.circle) {
            sleep(500)
        }
    }

    fun telemetryDisplay() {
        telemetry.addData("PosX: ", PosX)
        telemetry.addData("PosY: ", PosY)
    }
}


