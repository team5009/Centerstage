package org.firstinspires.ftc.teamcode

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import org.firstinspires.ftc.robotcore.external.tfod.CameraInformation
import org.firstinspires.ftc.robotcore.external.tfod.FrameConsumer
import org.firstinspires.ftc.robotcore.external.tfod.FrameGenerator
import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration
import org.firstinspires.ftc.vision.VisionProcessor
import org.opencv.android.Utils
import org.opencv.core.Mat
import org.opencv.core.MatOfPoint
import org.opencv.core.Scalar


class CameraProcessorBlue : VisionProcessor {
    enum class Position {
        Left, Right, Middle, None, Top, Bottom
    }
    var PosX: Position = Position.Middle
    var PosY: Position = Position.Middle
    val contours = ArrayList<MatOfPoint>()
    private var biggestContour = MatOfPoint()
    var differenceX = 0.0
    var differenceY = 0.0
    object Center {
        var x = 0.0
        var y = 0.0
    }
    var bitmap: Bitmap? = null
    protected val frameConsumerLock = Any()
    protected var frameConsumer: FrameConsumer? = null
    protected var width = 0
    protected var height = 0
    object Screen {
        var width = 0
        var height = 0
    }
    private val white = Scalar(255.0, 255.0, 255.0)
    override fun init(width: Int, height: Int, calibration: CameraCalibration) {
        this.width = width
        this.height = height
    }

    override fun processFrame(frame: Mat?, captureTimeNanos: Long): Any? {
        var frameConsumerSafe: FrameConsumer
        var bitmapSafe: Bitmap?
        synchronized(frameConsumerLock) {
            bitmapSafe = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        }
        Utils.matToBitmap(frame, bitmapSafe)
        return frame
    }

    override fun onDrawFrame(canvas: Canvas, onscreenWidth: Int, onscreenHeight: Int, scaleBmpPxToCanvasPx: Float, scaleCanvasDensity: Float, userContext: Any) {
        val rectPaint = Paint()

        rectPaint.color = Color.RED
        rectPaint.style = Paint.Style.STROKE
        rectPaint.strokeWidth = scaleCanvasDensity * 4
    }

    fun getCenter() : Center {
        return Center
    }
}