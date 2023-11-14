package org.firstinspires.ftc.teamcode

import android.graphics.Canvas
import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration
import org.firstinspires.ftc.vision.VisionProcessor
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc

class visionLearn: VisionProcessor {
    override fun init(width: Int, height: Int, calibration: CameraCalibration?) {
        TODO("Not yet implemented")
    }

    override fun processFrame(input: Mat?, captureTimeNanos: Long): Mat? {
        val mat = Mat()
        Imgproc.cvtColor(input, mat, Imgproc.COLOR_RGB2HSV)

        mat.copyTo(input)
        mat.release()
        return input
    }

    override fun onDrawFrame(canvas: Canvas?, onscreenWidth: Int, onscreenHeight: Int, scaleBmpPxToCanvasPx: Float, scaleCanvasDensity: Float, userContext: Any?) {
        TODO("Not yet implemented")
    }
}