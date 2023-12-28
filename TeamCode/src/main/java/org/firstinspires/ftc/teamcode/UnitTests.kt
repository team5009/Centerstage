package org.firstinspires.ftc.teamcode


import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.util.ElapsedTime
import kotlin.math.abs
import kotlin.math.max

@Autonomous(name = "UnitTests", group = "Linear OpMode")
//@Disabled
class UnitTests : LinearOpMode() {
    val PosX = 0
    val PosY = 0
    val bot: Autonomous1 = Autonomous1(this, 1,telemetry)
    // Declare OpMode members.
    private val runtime = ElapsedTime()
   // val odoMovement = SimpleOdoMovement(this, bot.bot, bot.odo)
    val dwt : Double = 0.12
    override fun runOpMode() {

        telemetry.addData("Status", "Initialized")
        telemetry.update()
        // Wait for the game to start (driver presses PLAY)
        waitForStart()
        runtime.reset()

        telemetry.addData("Status", "Started")
        telemetry.update()
        // run until the end of the match (driver presses STOP)
        if (opModeIsActive()) {
            waitForButton()
           multimovements(1.0, 10.0, 4.0, 0.0)
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

    fun multimovements(ratio: Double, drive: Double, strafe: Double, turn: Double) {
        var ratio1 : Double = ratio
        var multiplicator : Int = 1
        val denominator = max(abs(drive) + abs(strafe) + abs(turn), 1.0)
        bot.bot.move(
            (((drive + strafe * multiplicator) - turn) / denominator) * ratio1,
            (((drive - strafe * multiplicator) + turn) / denominator) * ratio1,
            (((drive - strafe * multiplicator) - turn) / denominator) * ratio1,
            (((drive + strafe * multiplicator) + turn) / denominator) * ratio1
        )

    }
}


