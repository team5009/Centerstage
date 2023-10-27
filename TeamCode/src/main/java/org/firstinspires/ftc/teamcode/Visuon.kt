package org.firstinspires.ftc.teamcode

import androidx.core.graphics.createBitmap
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.robotcontroller.external.samples.ConceptTensorFlowObjectDetection
import org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap
import org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry
import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName
import org.firstinspires.ftc.vision.VisionPortal
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor
import org.firstinspires.ftc.vision.tfod.TfodProcessor


@Suppress("UNREACHABLE_CODE")
 class Visuon (linearOpMode: LinearOpMode) {

    private var aprilTag: AprilTagProcessor? = null
    private var frontCam: CameraName? = null
    private var visionPortal: VisionPortal? = null
    protected var tfod: TfodProcessor? = null

    private val op = linearOpMode
    fun initTfod() {

        tfod = TfodProcessor.Builder()
            .build()

        val builder = VisionPortal.Builder()

        builder.setCamera(op.hardwareMap.get(WebcamName::class.java, "FrontCam"))

        builder.addProcessor(tfod)

        visionPortal = builder.build()

    }
    private fun initAprilTag() {

        frontCam = op.hardwareMap.get("FrontCam") as CameraName
        // Create the AprilTag processor.
        aprilTag = AprilTagProcessor.Builder() //.setDrawAxes(false)

            .build()

        // Create the vision portal by using a builder.
        val builder = VisionPortal.Builder()

        // Set the camera (webcam vs. built-in RC phone camera).
        if (op.opModeInInit()) {
            builder.setCamera(frontCam)
        } else {
            builder.setCamera(BuiltinCameraDirection.BACK)
        }

        // Set and enable the processor.
        builder.addProcessor(aprilTag)

        // Build the Vision Portal, using the above settings.
        visionPortal = builder.build()

    }



    fun detectprop(): String {
        val currentRecognitions = tfod!!.recognitions
        op.telemetry.addData("# Props Detected", currentRecognitions.size)
        if (currentRecognitions.isNotEmpty()) {
            if (currentRecognitions[0].left < 300) {
                op.telemetry.addLine("\n==== Left")
                return "left"
            } else if (currentRecognitions[0].right > 300) {
                op.telemetry.addLine("\n==== Center")
                return "center"
            } else {
                op.telemetry.addLine("\n==== Right")
                return "right"
            }
        }
        return "right"
    }



     fun tagid(): Int {

        when (detectprop()) {
            "center" -> {
                return 2
            }
            "left" -> {
                return 1
            }
            "right" -> {
                return 3
            }
        }
         return -1
     }
//This will return the main rgb
//EOCVsim -> Proccesor -> Image -> Bitmap -> RGB values -> is prop there or not
    fun propse () {


    }



}