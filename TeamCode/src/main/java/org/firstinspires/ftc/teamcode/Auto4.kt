package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.Disabled
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.util.ElapsedTime

@Autonomous(name = "Auto4", group = "Linear OpMode")
@Disabled
class Auto4 : LinearOpMode() {
    // Declare OpMode members.
    private val runtime = ElapsedTime()
    override fun runOpMode() {
        //val cam = Camera()
        val bot = Autonomous1(this, 2, telemetry)
        waitForStart()

        bot.armmove()
        sleep(500)
        bot.bot.flap.position = 0.3
        sleep(2000)
        //cam.picture2File()
    }
}


