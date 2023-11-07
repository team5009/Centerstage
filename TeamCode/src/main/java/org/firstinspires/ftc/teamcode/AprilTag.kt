package org.firstinspires.ftc.teamcode

import android.annotation.SuppressLint
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName
import org.firstinspires.ftc.teamcode.autonomous.Visuon
import org.firstinspires.ftc.vision.VisionPortal
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor

/*
 * This OpMode illustrates the basics of AprilTag recognition and pose estimation,
 * including Java Builder structures for specifying Vision parameters.
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this OpMode to the Driver Station OpMode list.
 */
@TeleOp(name = "Concept: AprilTag", group = "Concept")
//@Disabled
class AprilTag : LinearOpMode() {
    /**
     * The variable to store our instance of the AprilTag processor.
     */
    private var aprilTag: AprilTagProcessor? = null
    private var frontCam: CameraName? = null
    /**
     * The variable to store our instance of the vision portal.
     */
    private var visionPortal: VisionPortal? = null
    val visuon = Visuon(this)
    override fun runOpMode() {
        initAprilTag()


        // Wait for the DS start button to be touched.
        telemetry.addData("DS preview on/off", "3 dots, Camera Stream")
        telemetry.addData(">", "Touch Play to start OpMode")
        telemetry.update()
        waitForStart()
        if (opModeIsActive()) {
            while (opModeIsActive()) {
                telemetryAprilTag()

                // Push telemetry to the Driver Station.
                telemetry.update()

                // Save CPU resources; can resume streaming when needed.
                if (gamepad1.dpad_down) {
                    visionPortal!!.stopStreaming()
                } else if (gamepad1.dpad_up) {
                    visionPortal!!.resumeStreaming()
                }

                // Share the CPU.
                sleep(20)
            }
        }

        // Save more CPU resources when camera is no longer needed.
        visionPortal!!.close()
    } // end method runOpMode()

    /**
     * Initialize the AprilTag processor.
     */
    private fun initAprilTag() {

        frontCam = hardwareMap.get("FrontCam") as CameraName
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
        if (USE_WEBCAM) {
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
    } // end method initAprilTag()

    /**
     * Add telemetry about AprilTag detections.
     */
    private fun telemetryAprilTag() {
        val currentDetections: List<AprilTagDetection> = aprilTag!!.detections
        telemetry.addData("# AprilTags Detected", currentDetections.size)

        // Step through the list of detections and display info for each one.
        for (detection in currentDetections) {
            if (detection.metadata != null) {
                //"pi = ${pi.format(2)}"
                telemetry.addLine("\n==== (ID ${detection.id}) ${detection.metadata.name}")
                telemetry.addLine("XYZ ${detection.ftcPose.x}.${detection.ftcPose.y}, ${detection.ftcPose.z}, (inch)")
                telemetry.addLine("PRY ${detection.ftcPose.pitch} ${detection.ftcPose.roll} ${detection.ftcPose.yaw}(deg)")
                telemetry.addLine("RBE ${detection.ftcPose.range} ${detection.ftcPose.bearing} ${detection.ftcPose.elevation}")
            } else {
                telemetry.addLine("\n==== (ID ${detection.id}) Unknown")
                telemetry.addLine("Center ${detection.center.x} ${detection.center.y} (pixels)")
            }
        } // end for() loop

        // Add "key" information to telemetry
        telemetry.addLine("\nkey:\nXYZ = X (Right), Y (Forward), Z (Up) dist.")
        telemetry.addLine("PRY = Pitch, Roll & Yaw (XYZ Rotation)")
        telemetry.addLine("RBE = Range, Bearing & Elevation")
    } // end method telemetryAprilTag()

    companion object {
        private const val USE_WEBCAM = true // true for webcam, false for phone camera

    }
    fun Y (){
        val vision: List<AprilTagDetection> = aprilTag!!.detections
        for (item in vision) {
             item.ftcPose
        }


        val currentDetections: List<AprilTagDetection> = aprilTag!!.detections
        for (detection in currentDetections) {
            if (detection.metadata != null && (visuon.tagid() < 0 || detection.id == visuon.tagid())) {
                break // don't look any further.
            } else {
                telemetry.addData("Unknown Target", "Tag ID %d is not in TagLibrary\n", detection.id)
            }
        }
        }

    }




    // amogus toegus
 // end class


