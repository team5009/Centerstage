package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.util.ElapsedTime
import com.qualcomm.robotcore.util.Range
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

@Autonomous(name = "BLUE FRONT", group = "Linear OpMode")
//@Disabled
class Auto6 : LinearOpMode() {
    // Declare OpMode members.
    private val runtime = ElapsedTime()
    override fun runOpMode() {
        val bot = Autonomous2(this, 2, telemetry)
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
        val propPos = bot.detectProp(2)
        if(propPos == 1) {
            odoMovement.drive(-21.0, 0.3, 0.25)
            odoMovement.drive(2.0, 0.3, 0.25)
            odoMovement.strafe(8.0, 0.3,0.25)
            odoMovement.drive(-10.0, 0.3, 0.25)
            odoMovement.strafe(-11.0, 0.3, 0.25)
            odoMovement.strafe(3.0,0.3, 0.25)
            odoMovement.drive(-25.0, 0.35, 0.25)
            odoMovement.strafe(-70.0, 0.5, 0.25)
            bot.halfOdoPivot(-20.0)
           // sleep(250)
            odoMovement.drive(34.0, 0.35, 0.25)
            bot.halfOdoPivot(-90.0)

        } else if(propPos == 2) {
            odoMovement.drive(-15.0, 0.25, 0.25)
            odoMovement.drive(-12.0, 0.4, 0.25)
            odoMovement.drive(4.0, 0.4, 0.25)
            odoMovement.strafe(10.0, 0.4, 0.25)
            odoMovement.drive(-29.0, 0.4, 0.25)
            odoMovement.strafe(-82.0, 0.5, 0.25)
            bot.halfOdoPivot(-20.0)
            sleep(250)
            odoMovement.drive(28.0, 0.35, 0.25)
        } else {
            odoMovement.drive(-3.0, 0.3, 0.25)
            bot.halfOdoPivot(35.0)
            sleep(250)
            odoMovement.drive(-13.0, 0.35, 0.25)
            odoMovement.drive(3.0, 0.35, 0.25)
            odoMovement.strafe(-5.0, 0.35, 0.25)
            bot.halfOdoPivot(-5.0)
            sleep(250)
            odoMovement.drive(-36.0, 0.35, 0.25)
            bot.halfOdoPivot(0.0)
            sleep(250)
            odoMovement.strafe(-75.0, 0.5, 0.25)
            bot.halfOdoPivot(-20.0)
            odoMovement.drive(-21.0, 0.3, 0.25)
            odoMovement.drive(2.0, 0.3, 0.25)
            odoMovement.strafe(-14.0, 0.3,0.25)
            odoMovement.drive(-10.0, 0.3, 0.25)
            odoMovement.strafe(16.0, 0.3, 0.25)
            odoMovement.strafe(-14.0,0.3, 0.25)

            sleep(250)
            odoMovement.drive(20.0, 0.35, 0.25)
            odoMovement.drive(-21.0, 0.3, 0.25)
            odoMovement.drive(2.0, 0.3, 0.25)
            odoMovement.strafe(-14.0, 0.3,0.25)
            odoMovement.drive(-10.0, 0.3, 0.25)
            odoMovement.strafe(16.0, 0.3, 0.25)
            odoMovement.strafe(-14.0,0.3, 0.25)

        }
        bot.halfOdoPivot(-110.0)
        odoMovement.drive(-23.0, 0.3, 0.25)
        bot.armmove()
        bot.bot.flap.power = 0.8
        sleep(3000)
        bot.bot.flap.power = 0.0
        bot.armback()


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


