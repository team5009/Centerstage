package org.firstinspires.ftc.teamcode
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotor
import kotlin.math.max

class TeleOp1 (op : LinearOpMode) {

    val Instance = op

    //val cam1: CameraName = Instance.hardwareMap.get("FrontCam") as WebcamName
    val bot : RobotTest = RobotTest(op, 0)

    init {

        bot.fl.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        bot.fl.mode = DcMotor.RunMode.RUN_USING_ENCODER

    }
    fun fastmove(flPower: Double, frPower: Double, blPower: Double, brPower: Double) {
        bot.fl.power = flPower/2
        bot.fr.power = frPower/2
        bot.bl.power = blPower/2
        bot.br.power = brPower/2
        Instance.sleep(50)
        bot.fl.power = flPower/1.5
        bot.fr.power = frPower/1.5
        bot.bl.power = blPower/1.5
        bot.br.power = brPower/1.5
        Instance.sleep(25)
        bot.fl.power = flPower/1.25
        bot.fr.power = frPower/1.25
        bot.bl.power = blPower/1.25
        bot.br.power = brPower/1.25
        Instance.sleep(10)
        bot.fl.power = flPower
        bot.fr.power = frPower
        bot.bl.power = blPower
        bot.br.power = brPower
    }

    fun move(flPower: Double, frPower: Double, blPower: Double, brPower: Double) {
        bot.fl.power = flPower
        bot.fr.power = frPower
        bot.bl.power = blPower
        bot.br.power = brPower
        Instance.sleep(25)
        bot.fl.power = flPower/1.25
        bot.fr.power = frPower/1.25
        bot.bl.power = blPower/1.25
        bot.br.power = brPower/1.25
        Instance.sleep(50)
        bot.fl.power = flPower/1.5
        bot.fr.power = frPower/1.5
        bot.bl.power = blPower/1.5
        bot.br.power = brPower/1.5
        Instance.sleep(50)
        bot.fl.power = flPower/2
        bot.fr.power = frPower/2
        bot.bl.power = blPower/2
        bot.br.power = brPower/2
    }


    fun armmove() {
        if (bot.arm.velocity > 20) {
            bot.arm.power += max(0.05, bot.arm.power/10)
        } else if (bot.arm.velocity < 20) {
            bot.arm.power -= 0.05
        }
    }

    fun armback() {
        if (bot.arm.velocity > -20) {
            bot.arm.power -= max(0.05, bot.arm.power/10)
        } else if (bot.arm.velocity < -20) {
            bot.arm.power += 0.05
        }
    }
}
