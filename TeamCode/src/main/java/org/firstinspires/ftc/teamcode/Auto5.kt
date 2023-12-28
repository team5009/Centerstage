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
        val dwt : Double = 0.12 //default wait time, modify to make the gap between actions shorter or longer
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
            odoMovement.drive(-21.0, 0.3, dwt)
            odoMovement.strafe(-14.0, 0.3,dwt)
            odoMovement.drive(-10.0, 0.3, dwt)
            odoMovement.strafe(17.0, 0.3, dwt)
            odoMovement.strafe(-3.0,0.3, dwt)
            odoMovement.drive(-25.0, 0.35, dwt)
            bot.halfOdoPivot(90.0)
            sleep(250)
            odoMovement.drive(-80.0, 0.4, dwt)
            bot.halfOdoPivot(95.0)
            odoMovement.strafe(20.0, 0.3, dwt)
            bot.goToAprilTag(4.0, 2)
            bot.halfOdoPivot(97.0)
            odoMovement.strafe(12.0, 0.35, dwt)
            odoMovement.drive(-2.0, 0.3, dwt)
        } else if(propPos == 5) {
            odoMovement.drive(-27.0, 0.25, dwt)
            //odoMovement.drive(4.0, 0.3, 0.25)
            odoMovement.strafe(-13.0, 0.4, dwt)
            odoMovement.drive(-24.0, 0.3, dwt)
            bot.halfOdoPivot(87.0)
            sleep(250)
            odoMovement.drive(-93.0, 0.4, dwt)
            bot.halfOdoPivot(90.0)
            odoMovement.strafe(18.0, 0.35, dwt)
            bot.goToAprilTag(4.0, 2)
            bot.halfOdoPivot(93.0)
            odoMovement.strafe(9.0, 0.3, dwt)
            odoMovement.drive(-3.0, 0.3, dwt)
        } else {
            odoMovement.drive(-2.0, 0.3, dwt)
            bot.halfOdoPivot(-27.0)
            sleep(250)
            odoMovement.drive(-14.0, 0.35, dwt)
            odoMovement.strafe(7.0, 0.35, dwt)
            bot.halfOdoPivot(0.0)
            odoMovement.drive(-30.0, 0.35, dwt)
            bot.halfOdoPivot(87.0)
            odoMovement.drive(-85.0, 0.4, dwt)
            bot.halfOdoPivot(90.0)
            odoMovement.strafe(18.0, 0.35, dwt)
            bot.goToAprilTag(4.0, 2)
            bot.halfOdoPivot(93.0)
            odoMovement.strafe(6.0, 0.3, dwt)
            //odoMovement.drive(-3.0, 0.3, 0.25)
            //bot.bot.flap.power = 0.8
            //bot.bot.intake.power = 0.2
            //sleep(900)
            //bot.bot.flap.power = 0.0
            //bot.bot.intake.power = 0.0

          //  odoMovement.drive(-50.0, 0.25, 0.25)
          //  odoMovement.strafe(85.0, 0.5 ,0.25)
        }

        bot.armmove(0.7)
        sleep(250)
        bot.bot.flap.power = 0.8
        sleep(1000)
        bot.armback(0.3)
        bot.bot.flap.power = 0.0
        odoMovement.drive(4.0, 0.2, dwt)
        odoMovement.strafe(-10.0, 0.5, dwt)


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


