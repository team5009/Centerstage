package org.firstinspires.ftc.teamcode

import android.os.Environment
import androidx.core.graphics.get
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.Disabled
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.util.ElapsedTime
import com.qualcomm.robotcore.util.Range
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.vision.VisionPortal
import java.io.File
import kotlin.math.abs

/* Copyright (c) 2017 FIRST. All rights reserved.
*
* Redistribution and use in source and binary forms, with or without modification,
* are permitted (subject to the limitations in the disclaimer below) provided that
* the following conditions are met:
*
* Redistributions of source code must retain the above copyright notice, this list
* of conditions and the following disclaimer.
*
* Redistributions in binary form must reproduce the above copyright notice, this
* list of conditions and the following disclaimer in the documentation and/or
* other materials provided with the distribution.
*
* Neither the name of FIRST nor the names of its contributors may be used to endorse or
* promote products derived from this software without specific prior written permission.
*
* NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
* LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
* "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
* THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
* ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
* FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
* DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
* SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
* CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
* OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
* OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
/*
 * This file contains an minimal example of a Linear "OpMode". An OpMode is a 'program' that runs in either
 * the autonomous or the teleop period of an FTC match. The names of OpModes appear on the menu
 * of the FTC Driver Station. When a selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 *
 * This particular OpMode just executes a basic Tank Drive Teleop for a two wheeled robot
 * It includes all the skeletal structure that all linear OpModes contain.
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this OpMode to the Driver Station OpMode list
 */
@Autonomous(name = "BLUE BACK", group = "Linear OpMode")
//@Disabled
class Auto1 : LinearOpMode() {
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
        if (propPos == 1) {
            odoMovement.drive(-15.0, 0.4, 0.25)
            odoMovement.drive(-5.0, 0.2, 0.25)
            bot.halfOdoPivot(-70.0)
            sleep(250)
            odoMovement.strafe(4.0, 0.3, 0.25)
            odoMovement.drive(-7.0, 0.35, 0.25)
            odoMovement.drive(7.0, 0.35, 0.25)
            bot.halfOdoPivot(0.0)
            sleep(250)
            odoMovement.strafe(-24.0, 0.3, 0.25)
            odoMovement.drive(4.0, 0.35, 0.25)
            bot.halfOdoPivot(-90.0)
            odoMovement.drive(-16.0, 0.3, 0.25)
        } else if (propPos == 2) {
            odoMovement.drive(-15.0, 0.25, 0.25)
            odoMovement.drive(-12.0, 0.3, 0.25)
            odoMovement.drive(2.0, 0.3, 0.25)
            bot.halfOdoPivot(-90.0)
            sleep(250)
            odoMovement.drive(-41.0, 0.3, 0.25)
        } else {
            odoMovement.drive(-21.0, 0.3, 0.25)
            odoMovement.drive(2.0, 0.3, 0.25)
            odoMovement.strafe(-14.0, 0.3,0.25)
            odoMovement.drive(-10.0, 0.3, 0.25)
            odoMovement.strafe(16.0, 0.3, 0.25)
            odoMovement.strafe(-14.0,0.3, 0.25)
            odoMovement.drive(-3.0, 0.3, 0.25)
            bot.halfOdoPivot(-90.0)
            odoMovement.drive(-26.0, 0.3, 0.25)
        }
        bot.armmove()
        bot.bot.flap.power = 0.8
        sleep(3000)
        bot.bot.flap.power = 0.0
        bot.armback()


        /*odoMovement.strafe(5.0, 0.8, 0.25)
   bot.halfOdoPivot(90.0) */
        while (opModeIsActive()) {
            bot.odo.calculate()
            telemetry.addData("lEnc: ", bot.bot.leftEncoder.currentPosition)
            telemetry.addData("rEnc: ", bot.bot.rightEncoder.currentPosition)
            telemetry.addData("bEnc: ", bot.bot.backEncoder.currentPosition)
            telemetry.addData("x: ", bot.odo.location.x)
            telemetry.addData("y: ", bot.odo.location.y)
            telemetry.addData("rot: ", bot.odo.location.rot)
            telemetry.update()
        }
    }
}



