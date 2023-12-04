package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.Disabled
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.robotcore.external.Telemetry

@Autonomous
class AutoTest : LinearOpMode() {

    val bot = Autonomous2(this, 0, telemetry)
    override fun runOpMode() {
        while (opModeIsActive()) {
            //put the code you want to test here
            bot.armmove(0.7)
        }

    }
}