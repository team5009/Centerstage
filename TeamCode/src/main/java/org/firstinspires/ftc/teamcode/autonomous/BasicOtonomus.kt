package org.firstinspires.ftc.teamcode.autonomous

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.Ruboot

@Autonomous(name = "BBO")
class BBO : LinearOpMode() {
    override fun runOpMode() {
        val bot = Ruboot(this)
        val mtat = MTAT()
        val pivut = Pivut()
        waitForStart()

        if (opModeIsActive()) {
            telemetry.addData("Info", "Running Pivot")
            telemetry.addLine("moose jerky")
            telemetry.update()
           /* bot.move(0.8, 0.8,0.80,0.8)
            sleep (800)
            bot.move(0.0,0.0,0.0,0.0)
            sleep (100)
            bot.move(0.0, -0.8, 0.0, -0.8)
            sleep (50)
            bot.move(0.0,0.0,0.0,0.0)
            sleep (50)
            bot.move(0.8,0.8,0.8,0.8)
            sleep (200)
*/

            //pivut.pivotTurn(bot,180.0,0.8,Pivut.Direction.LEFT)

            //sleep(1000)

            pivut.pivot(bot,90.0, telemetry)
        }
    }

}