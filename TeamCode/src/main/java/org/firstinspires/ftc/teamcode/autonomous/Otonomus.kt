package org.firstinspires.ftc.teamcode.autonomous

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.teamcode.Ruboot

@Autonomous(name = "Otonomus")
class Otonomus : LinearOpMode() {
        // Declare OpMode members.
        private val runtime = ElapsedTime()
        override fun runOpMode() {
            val bot = Ruboot(this)
            val visuon = Visuon(this)
            val tfod =

            waitForStart()

            while (opModeIsActive()) {

            }
        }
    }
//croatia