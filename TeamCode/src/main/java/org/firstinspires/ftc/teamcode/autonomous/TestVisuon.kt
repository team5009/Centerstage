package org.firstinspires.ftc.teamcode.autonomous

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.util.ElapsedTime

@TeleOp(name = "TestVisuon", group = "Linear OpMode")
class TestVisuon : LinearOpMode() {
    // Declare OpMode members.
    private val runtime = ElapsedTime()
    override fun runOpMode() {
        telemetry.addData("Status", "Initialized")
        telemetry.update()

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        val visuon: Visuon = Visuon(this)

        visuon.initTfod()

        val result = visuon.detectprop()

        // Wait for the game to start (driver presses PLAY)
        waitForStart()
        runtime.reset()

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            val id = visuon.tagid()
            telemetry.addLine(result)

            if (id == 3) {
                telemetry.addLine("you can't hide")
                telemetry.addLine("right")
            }
                else
                    if (id == 2) {
                        telemetry.addLine("ooooo shiny")
                        telemetry.addLine("center")

                    }

                        else if (id == 1) {
                            telemetry.addLine("ooooo shiny")
                            telemetry.addLine("left")
                        }



            telemetry.update()
        }
    }
}