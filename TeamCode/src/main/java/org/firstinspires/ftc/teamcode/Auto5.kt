package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.util.ElapsedTime
import com.qualcomm.robotcore.util.Range
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.sqrt

@Autonomous(name = "RED FRONT", group = "Linear OpMode")
//@Disabled
class Auto5: LinearOpMode() {
    // Declare OpMode members.
    private val runtime = ElapsedTime()
    override fun runOpMode() {
        val bot = Autonomous2(this, 0, telemetry)
        var propc : Int
        val odoMovement = SimpleOdoMovement(this, bot.bot, bot.odo)
        odoMovement.initialize(true)
        while (!opModeIsActive()) {
            telemetry.addData("lEnc: ", bot.bot.leftEncoder.currentPosition)
            telemetry.addData("rEnc: ", bot.bot.rightEncoder.currentPosition)
            telemetry.addData("bEnc: ", bot.bot.backEncoder.currentPosition)
            telemetry.addData("imu av", bot.bot.imu.getRobotAngularVelocity(AngleUnit.DEGREES))
            telemetry.addData("x: ", bot.odo.location.x)
            telemetry.addData("y: ", bot.odo.location.y)
            telemetry.addData("rot: ", bot.odo.location.rot)
            telemetry.update()
        }

        waitForStart()
        while(opModeIsActive()) {
            bot.odo.calculate()
            telemetry.addData("lEnc: ",bot.bot.leftEncoder.currentPosition)
            telemetry.addData("rEnc: ",bot.bot.rightEncoder.currentPosition)
            telemetry.addData("bEnc: ",bot.bot.backEncoder.currentPosition)
            telemetry.addData("x: ",bot.odo.location.x)
            telemetry.addData("y: ",bot.odo.location.y)
            telemetry.addData("rot: ",bot.odo.location.rot)
            telemetry.update()
            //bot.switchProc(bot.bot.cam.camProc!!)
            propc = bot.detectProp(1)
            odoMovement.drive(25.0 , 0.5, 0.1)
            if (propc == 4) {
                bot.pivot(90.0, 0.6 )
                odoMovement.drive(-10.0, 0.5, 0.1)
                } else if (propc == 5) {
                    odoMovement.drive(5.0, 0.5, 0.1)
                    odoMovement.drive(-10.0, 0.5, 0.1)
                } else if (propc == 6) {
                    bot.pivot(-90.0, 0.6 )
                    odoMovement.drive(-10.0, 0.5, 0.1)
            }


        }
    }
}


