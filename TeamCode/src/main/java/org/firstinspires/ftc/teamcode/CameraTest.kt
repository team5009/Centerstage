package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName
import org.firstinspires.ftc.vision.VisionPortal

@Autonomous(name= "CameraTest")
class CameraTest: LinearOpMode() {
    //var visionPortal: VisionPortal? = null

    override fun runOpMode() {

        val builder = VisionPortal.Builder()
        val processor = visionLearn()
        builder.setCamera(hardwareMap.get(WebcamName::class.java, "Webcam 1"))
        builder.enableLiveView(true)
        builder.setStreamFormat(VisionPortal.StreamFormat.YUY2);
        builder.addProcessor(processor)
        telemetry.addData("Camera", "Ready")
        telemetry.update()
        waitForStart()
        val visionPortal = builder.build()
        while (opModeIsActive()) {
            telemetry.addData("Camera", "Running")
            telemetry.update()
        }
        telemetry.addData("Camera", "Stopped")
        telemetry.update()
        visionPortal.close()

    }
}