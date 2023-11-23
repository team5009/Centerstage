package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.util.ElapsedTime
import com.qualcomm.robotcore.util.Range
import org.firstinspires.ftc.robotcontroller.external.samples.ConceptAprilTag
import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName
import org.firstinspires.ftc.vision.VisionPortal
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor

@TeleOp(name = "AprilTagUnitTest", group = "Linear OpMode")
class AprilTagUnitTest : LinearOpMode() {

    private var aprilTag: AprilTagProcessor? = null
    var visionPortal: VisionPortal? = null
    private val runtime = ElapsedTime()
    override fun runOpMode() {

        val bot: Autonomous1 = Autonomous1(this, 1, telemetry)
        telemetry.addData("Status", "Initialized")
        telemetry.update()
        // Wait for the game to start (driver presses PLAY)


        telemetry.addData("Status", "Started")
        telemetry.update()
        // run until the end of the match (driver presses STOP)
        //initAprilTag()
        // Wait for the DS start button to be touched.
        //bot.bot.cam.picture2File()
        telemetry.addData("DS preview on/off", "3 dots, Camera Stream")
        telemetry.addData(">", "Touch Play to start OpMode")
        telemetry.update()
        waitForStart()
        runtime.reset()
        telemetry.update()
        sleep(1000)
        val propPos = 2
        var targetDist: Double = 0.0
        var bearing: Double = 0.0
        var yaw: Double = 0.0
        var emptyTimes: Int = 0
        //bot.bot.cam.picture2File()
        while (opModeIsActive()) {

            //bot.detectProp(1)
            //bot.showRedCenter()
            //bot.detectProp(1)

            telemetry.update()
            /*var detections: List<AprilTagDetection>? = null
            detections = aprilTag!!.detections
            for (det in detections!!) {
                telemetry.addLine("ID: ${det.id}, name: ${det.metadata.name}")
                telemetry.addLine("RBE ${det.ftcPose.range} ${det.ftcPose.bearing} ${det.ftcPose.yaw}  (inch, deg, deg)")

                if (det.id == propPos) {
                    targetDist = det.ftcPose.range - 5.0
                    bearing = det.ftcPose.bearing
                    yaw = -det.ftcPose.yaw
                    break
                } else {
                    targetDist = 0.0
                    bearing = 0.0
                    yaw = (det.id.toDouble() - propPos.toDouble()) * 30.0
                }
            }

            if (detections.isEmpty()) {
                emptyTimes += 1
            } else {
                emptyTimes = 0
            }

            if (emptyTimes > 2 || targetDist < 0.0) {
                bot.bot.move(0.0, 0.0, 0.0, 0.0)
            } else {

                val drive: Double = Range.clip(targetDist * 0.02, -0.5, 0.5)
                val turn: Double = Range.clip(bearing * 0.015, -0.5, 0.5)
                val strafe: Double = Range.clip(yaw * 0.01, -0.5, 0.5)
                bot.bot.move((drive - turn - strafe) / 1.5,
                        (drive + turn + strafe) / 1.5,
                        (drive - turn + strafe) / 1.5,
                        (drive + turn - strafe) / 1.5)

            } /*else {
                val strafe: Double = Range.clip(yaw * 0.05, -0.3, 0.3)
                bot.bot.move(-strafe / 1.5,
                        strafe / 1.5,
                        strafe / 1.5,
                        -strafe / 1.5)
            }*/


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
        */}
        while(opModeIsActive()) {}
        // Save more CPU resources when camera is no longer needed.
        visionPortal!!.close()
        telemetry.addData("Status", "Ended")
        telemetry.update()
    } // end method runOpMode()

    private fun initAprilTag() {

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

        builder.setCamera(hardwareMap.get(WebcamName::class.java, "Webcam 1"))



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
}