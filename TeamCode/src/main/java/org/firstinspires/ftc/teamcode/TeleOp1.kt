package org.firstinspires.ftc.teamcode
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotor
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class TeleOp1 (op : LinearOpMode) {

    val instance = op

    //val cam1: CameraName = Instance.hardwareMap.get("FrontCam") as WebcamName
    val bot : RobotTest = RobotTest(op, 0)

    init {

        bot.fl.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        bot.fl.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER

    }

    fun tele(ratio: Double) {
        val leftY = -instance.gamepad1.left_stick_y.toDouble()
        val leftX = instance.gamepad1.left_stick_x.toDouble()
        val rightX = instance.gamepad1.right_stick_x.toDouble()
        val denominator = max(abs(leftY) + abs(leftX) + abs(rightX), 1.0)
        if (abs(leftX) > 0.3 || abs(rightX) > 0.3 || abs(leftY) > 0.3) {
            bot.move(
                ((leftY - leftX - rightX) / denominator) * ratio,
                ((leftY + leftX + rightX) / denominator) * ratio,
                ((leftY + leftX - rightX) / denominator) * ratio,
                ((leftY - leftX + rightX) / denominator) * ratio
            )
        } else {
            bot.move(0.0, 0.0, 0.0, 0.0)
        }
    }

    fun armmove() {
        if (bot.arm.velocity > 40) {
            bot.arm.power += max(0.05, bot.arm.power/10)
        } else if (bot.arm.velocity < 40) {
            bot.arm.power -= 0.05
        }
    }

    fun armback() {
        if (bot.arm.velocity > -40) {
            bot.arm.power += max(0.05, -bot.arm.power/10)
        } else if (bot.arm.velocity < -40) {
            bot.arm.power -= 0.05
        }
    }
}
