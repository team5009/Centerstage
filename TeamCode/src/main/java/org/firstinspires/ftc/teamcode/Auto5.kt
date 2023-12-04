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
    override fun runOpMode() {
        val bot = Autonomous2(this, 1, telemetry)
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
            telemetry.addData("Porpsee", bot.detectProp(1))
            telemetry.update()
        }
        waitForStart()
        val propPos = bot.detectProp(1)
        if(propPos == 6) {
            odoMovement.drive(-15.0, 0.2, 0.25)
            odoMovement.drive(-5.0, 0.2, 0.25)
            bot.halfOdoPivot(85.0)
            sleep(250)
            odoMovement.drive(5.0, 0.3, 0.25)
            odoMovement.strafe(-15.0, 0.4, 0.25)
            odoMovement.drive(-6.0, 0.3, 0.25)
            odoMovement.drive(6.0, 0.3, 0.25)
            bot.halfOdoPivot(0.0)
            sleep(250)
            odoMovement.drive(-22.0, 0.3, 0.25)
            odoMovement.strafe(85.0, 0.4, 0.25)
            bot.halfOdoPivot(20.0)
            odoMovement.drive(15.0, 0.3, 0.25)
            odoMovement.strafe(5.0, 0.3, 0.25)
        } else if(propPos == 5) {
            odoMovement.drive(-15.0, 0.25, 0.25)
            odoMovement.drive(-12.0, 0.3, 0.25)
            odoMovement.drive(4.0, 0.3, 0.25)
            odoMovement.strafe(-13.0, 0.4, 0.25)
            odoMovement.drive(-32.0, 0.3, 0.25)
            odoMovement.strafe(90.0, 0.4, 0.25)
            bot.halfOdoPivot(20.0)
            sleep(250)
            odoMovement.drive(26.0, 0.35, 0.25)
        } else {
            odoMovement.drive(-2.0, 0.3, 0.25)
            bot.halfOdoPivot(-27.0)
            sleep(250)
            odoMovement.drive(-16.0, 0.35, 0.25)
            odoMovement.drive(12.0, 0.35, 0.25)
            bot.halfOdoPivot(90.0)
            odoMovement.drive(-100.0, 0.2, 0.25)
            bot.bot.flap.power = 0.8
            bot.bot.intake.power = 0.2
            sleep(900)
            bot.bot.flap.power = 0.0
            bot.bot.intake.power = 0.0

          //  odoMovement.drive(-50.0, 0.25, 0.25)
          //  odoMovement.strafe(85.0, 0.5 ,0.25)
        }
        /* bot.halfOdoPivot(97.0)
        odoMovement.strafe(30.0, 0.3, 0.25)
        bot.armmove(0.7)
        sleep(250)
        bot.bot.flap.power = 0.8
        sleep(3000)
        bot.bot.flap.power = 0.0
        bot.armback(0.6)
        odoMovement.strafe(10.0, 0.5, 0.25) */


        /*odoMovement.strafe(5.0, 0.8, 0.25)
       bot.halfOdoPivot(90.0) */
        while(opModeIsActive()) {
            bot.odo.calculate()
            telemetry.addData("lEnc: ",bot.bot.leftEncoder.currentPosition)
            telemetry.addData("rEnc: ",bot.bot.rightEncoder.currentPosition)
            telemetry.addData("bEnc: ",bot.bot.backEncoder.currentPosition)
            telemetry.addData("x: ",bot.odo.location.x)
            telemetry.addData("y: ",bot.odo.location.y)
            telemetry.addData("rot: ",bot.odo.location.rot)
            telemetry.update()
        }
    }
}


