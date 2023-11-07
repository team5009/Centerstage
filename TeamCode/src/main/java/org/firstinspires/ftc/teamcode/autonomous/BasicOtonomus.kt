package org.firstinspires.ftc.teamcode.autonomous

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.Ruboot

@Autonomous(name = "BBO")
class BBO : LinearOpMode() {
    override fun runOpMode() {
        val bot = Ruboot(this)
        val visuon = Visuon(this)
        val mtat = MTAT()
        waitForStart()

        while (opModeIsActive()) {

            bot.move(0.8, 0.8,0.80,0.8)
            sleep (800)
            bot.move(0.0,0.0,0.0,0.0)
            sleep (100)
            bot.move(0.0, -0.8, 0.0, -0.8)
            sleep (50)
            bot.move(0.0,0.0,0.0,0.0)
            sleep (50)
            bot.move(0.8,0.8,0.8,0.8)
            sleep (200)



        }
    }

}