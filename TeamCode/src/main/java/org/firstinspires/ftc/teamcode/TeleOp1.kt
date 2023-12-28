package org.firstinspires.ftc.teamcode
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotor
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.sign

class TeleOp1 (op : LinearOpMode) {

    val instance = op

    //val cam1: CameraName = Instance.hardwareMap.get("FrontCam") as WebcamName
    val bot: RobotTest = RobotTest(op, 0)

    init {

        bot.fl.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        bot.fl.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER

    }

    fun tili(ratio: Double, superfastmode: Boolean) {
        var ratio1: Double = ratio
        val leftY = -instance.gamepad1.left_stick_y.toDouble()
        val leftX = instance.gamepad1.left_stick_x.toDouble() * 0.8
        val rightX = instance.gamepad1.right_stick_x.toDouble()
        var multiplicator: Int = 1
        val denominator = max(abs(leftY) + abs(leftX) + abs(rightX), 1.0)
        if ((abs(leftX) > 0.5 || abs(rightX) > 0.3 || abs(leftY) > 0.3) && !superfastmode) {
            multiplicator = 1
            bot.move(
                ((((leftY + leftX * multiplicator) - rightX) / denominator) * ratio1) - instance.gamepad1.left_trigger,
                ((((leftY - leftX * multiplicator) + rightX) / denominator) * ratio1) - instance.gamepad1.left_trigger,
                ((((leftY - leftX * multiplicator) - rightX) / denominator) * ratio1) - instance.gamepad1.left_trigger,
                ((((leftY + leftX * multiplicator) + rightX) / denominator) * ratio1) - instance.gamepad1.left_trigger
            )
        } else if (((abs(leftX) > 0.5 || abs(leftY) > 0.001) && abs(rightX) < 0.1) && superfastmode) {
            multiplicator = 10
            bot.move(
                ((((leftY + leftX * multiplicator) - rightX) / denominator) * ratio1) - instance.gamepad1.left_trigger,
                ((((leftY - leftX * multiplicator) + rightX) / denominator) * 1) - instance.gamepad1.left_trigger,
                ((((leftY - leftX * multiplicator) - rightX) / denominator) * ratio1) - instance.gamepad1.left_trigger,
                ((((leftY + leftX * multiplicator) + rightX) / denominator) * ratio1) - instance.gamepad1.left_trigger
            )
        } else if (((abs(rightX) > abs(leftY)) || (abs(rightX) > abs(leftX))) && superfastmode && abs(
                rightX
            ) > 0.1
        ) {
            ratio1 = 0.7
            bot.move(
                ((((leftY + leftX) - rightX) / denominator) * ratio1) - instance.gamepad1.left_trigger,
                ((((leftY - leftX) + rightX) / denominator) * ratio1) - instance.gamepad1.left_trigger,
                ((((leftY - leftX) - rightX) / denominator) * ratio1) - instance.gamepad1.left_trigger,
                ((((leftY + leftX) + rightX) / denominator) * ratio1) - instance.gamepad1.left_trigger
            )
        } else {
            bot.move(0.0, 0.0, 0.0, 0.0)
        }

    }

    /*fun armmove() {
        if (bot.arm.velocity > 40) {
            bot.arm.power += max(0.1, bot.arm.power/5)
        } else if (bot.arm.velocity < 40) {
            bot.arm.power -= 0.05
        }
    }*/

    fun armmove(Armstate: Boolean, power: Double) {
        if (Armstate) {
            if (bot.arm.currentPosition < 70) {
            }
        }
    }

    fun armback() {
        if (bot.arm.velocity > -40) {
            bot.arm.power += max(0.1, -bot.arm.power / 5)
        } else if (bot.arm.velocity < -40) {
            bot.arm.power -= 0.05
        }
    }


    fun tili(ratio : Double) {
        val leftY = -instance.gamepad1.left_stick_y.toDouble()
        val leftX = instance.gamepad1.left_stick_x.toDouble() * 0.8
        val rightX = instance.gamepad1.right_stick_x.toDouble()
        val denominator = max(abs(leftY) + abs(leftX) + abs(rightX), 1.0)
        bot.move(
            (((leftY + leftX - rightX) / denominator) - (instance.gamepad1.left_trigger * (sign(leftY + leftX - rightX)))),
            (((leftY - leftX + rightX) / denominator) - (instance.gamepad1.left_trigger * (sign(leftY - leftX + rightX)))),
            (((leftY - leftX - rightX) / denominator)- (instance.gamepad1.left_trigger * (sign(leftY - leftX - rightX)))),
            (((leftY + leftX + rightX) / denominator) - (instance.gamepad1.left_trigger * (sign(leftY + leftX + rightX))))
        )
    }
}