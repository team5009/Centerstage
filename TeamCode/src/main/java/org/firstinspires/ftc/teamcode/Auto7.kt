package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.Disabled
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.util.ElapsedTime
import com.qualcomm.robotcore.util.Range
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin
import kotlin.math.sqrt

@Autonomous(name = "BLUE FRONT", group = "Linear OpMode")
//@Disabled
class Auto7 : LinearOpMode() {
    // Declare OpMode members.
    private val runtime = ElapsedTime()
    override fun runOpMode() {
        val bot = Autonomous2(this, 2, telemetry)
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
            telemetry.addData("Porpsee", bot.detectProp(2))
            telemetry.addData("CenterX", bot.bot.cam.camProc!!.getCenter().x)
            telemetry.addData("Size", bot.bot.cam.camProc!!.getSize())
            telemetry.update()
        }
        waitForStart()

        //begining of the autonomous
        val propPos = bot.detectProp(2)
        if(propPos == 3) {
            odoMovement.drive(-2.0, 0.3, dwt)
            bot.halfOdoPivot(27.0)
            sleep(250)
            odoMovement.drive(-16.0, 0.35, dwt)
            odoMovement.strafe(-8.0, 0.35, dwt)
            bot.halfOdoPivot(-0.0)
            odoMovement.drive(-30.0, 0.35, dwt)
            bot.halfOdoPivot(-87.0)
            bot.halfOdoPivot(-95.0)
            sleep(3000)
            odoMovement.strafe(-18.0, 0.35, dwt)
            bot.goToAprilTag(4.0, 5)
            odoMovement.strafe(-10.0, 0.3, dwt)
            odoMovement.drive(-5.0, 0.1, dwt)
            //bot.bot.flap.power = 0.8
            //bot.bot.intake.power = 0.2
            //sleep(900)
            //bot.bot.flap.power = 0.0
            //bot.bot.intake.power = 0.0

            //  odoMovement.drive(-50.0, 0.25, 0.25)
            //  odoMovement.strafe(85.0, 0.5 ,0.25)
        } else if(propPos == 2) {
            odoMovement.drive(-29.0, 0.25, dwt)
            //odoMovement.drive(4.0, 0.3, 0.25)
            odoMovement.strafe(13.0, 0.4, dwt)
            odoMovement.drive(-25.0, 0.3, dwt)
            bot.halfOdoPivot(-87.0)
            sleep(250)
            odoMovement.drive(-95.0, 0.4, dwt)
            bot.halfOdoPivot(-95.0)
            sleep(3000)
            odoMovement.strafe(-14.0, 0.35, dwt)
            bot.goToAprilTag(4.0, 5)
            odoMovement.strafe(-15.0, 0.3, dwt)
            odoMovement.drive(-4.0, 0.1, dwt)
        } else {
            odoMovement.drive(-21.0, 0.3, dwt)
            odoMovement.strafe(10.0, 0.3,dwt)
            odoMovement.drive(-10.0, 0.3, dwt)
            odoMovement.strafe(-15.0, 0.3, dwt)
            odoMovement.strafe(3.0,0.3, dwt)
            odoMovement.drive(-25.0, 0.35, dwt)
            bot.halfOdoPivot(-90.0)
            sleep(250)
            odoMovement.drive(-85.0, 0.4, dwt)
            bot.halfOdoPivot(-95.0)
            sleep(3000)
            odoMovement.strafe(-20.0, 0.3, dwt)
            bot.goToAprilTag(4.0, 5)
            odoMovement.strafe(-10.0, 0.3, dwt)
            odoMovement.drive(-4.0, 0.1, dwt)
        }

        bot.armmove(0.8)
        sleep(250)
        bot.bot.flap.power = 0.8
        sleep(1200)
        bot.armback(0.3)
        bot.bot.flap.power = 0.0
        odoMovement.drive(4.0, 0.2, dwt)
        //odoMovement.strafe(10.0, 0.5, 0.25)
        odoMovement.strafe(40.0/propPos, 0.3, dwt)
        /*
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


