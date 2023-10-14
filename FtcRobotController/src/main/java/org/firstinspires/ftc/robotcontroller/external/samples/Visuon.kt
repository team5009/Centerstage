package org.firstinspires.ftc.robotcontroller.external.samples

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry
import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName
import org.firstinspires.ftc.vision.VisionPortal
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor
import org.firstinspires.ftc.vision.tfod.TfodProcessor


@Suppress("UNREACHABLE_CODE")
 class Visuon (linearOpMode: LinearOpMode) {

    private var aprilTag: AprilTagProcessor? = null
    private var frontCam: CameraName? = null
    private var visionPortal: VisionPortal? = null
    private var tfod: TfodProcessor? = null

    private val op = linearOpMode
    private fun initAprilTag() {

        frontCam = op.hardwareMap.get("FrontCam") as CameraName
        // Create the AprilTag processor.
        aprilTag = AprilTagProcessor.Builder() //.setDrawAxes(false)
            //.setDrawCubeProjection(false)
            //.setDrawTagOutline(true)
            //.setTagFamily(AprilTagProcessor.TagFamily.TAG_36h11)
            //.setTagLibrary(AprilTagGameDatabase.getCenterStageTagLibrary())
            //.setOutputUnits(DistanceUnit.INCH, AngleUnit.DEGREES)
            // == CAMERA CALIBRATION ==
            // If you do not manually specify calibration parameters, the SDK will attempt
            // to load a predefined calibration for your camera.
            //.setLensIntrinsics(578.272, 578.272, 402.145, 221.506)
            // ... these parameters are fx, fy, cx, cy.
            .build()

        // Create the vision portal by using a builder.
        val builder = VisionPortal.Builder()

        // Set the camera (webcam vs. built-in RC phone camera).
        if (op.opModeInInit()) {
            builder.setCamera(frontCam)
        } else {
            builder.setCamera(BuiltinCameraDirection.BACK)
        }

        // Choose a camera resolution. Not all cameras support all resolutions.
        //builder.setCameraResolution(new Size(640, 480));

        // Enable the RC preview (LiveView).  Set "false" to omit camera monitoring.
        //builder.enableCameraMonitoring(true);

        // Set the stream format; MJPEG uses less bandwidth than default YUY2.
        //builder.setStreamFormat(VisionPortal.StreamFormat.YUY2);

        // Choose whether or not LiveView stops if no processors are enabled.
        // If set "true", monitor shows solid orange screen if no processors enabled.
        // If set "false", monitor shows camera view without annotations.
        //builder.setAutoStopLiveView(false);

        // Set and enable the processor.
        builder.addProcessor(aprilTag)

        // Build the Vision Portal, using the above settings.
        visionPortal = builder.build()

        // Disable or re-enable the aprilTag processor at any time.
        //visionPortal.setProcessorEnabled(aprilTag, true);
    }

    fun detectprop(): String {
        val currentRecognitions = tfod!!.recognitions
        telemetry.addData("# Props Detected", currentRecognitions.size)
        if (currentRecognitions.isNotEmpty()) {
            if (currentRecognitions[0].left < 320) {
                telemetry.addLine("\n==== Left")
                return "left"
            } else if (currentRecognitions[0].right > 320) {
                telemetry.addLine("\n==== Center")
                return "center"

            } else {
                telemetry.addLine("\n==== Right")
                return "right"
            }
        }
//gotta wait til we get the tensorflow working
        else {
            telemetry.addLine("\n==== Right")
            return "right"
        }
    }



     fun tagid(): Int {

        when (detectprop()) {
            "center" -> return 2
            "left" -> return 1
            "right" -> { return 3
            }
        }

         return TODO("Provide the return value")
     }



}