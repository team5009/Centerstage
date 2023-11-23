package org.firstinspires.ftc.teamcode

import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Environment
import android.view.Surface
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration
import org.firstinspires.ftc.vision.VisionProcessor
import org.opencv.android.Utils
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc
//import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.core.vision.ImageProcessingOptions
//import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import org.tensorflow.lite.task.vision.classifier.Classifications
import org.tensorflow.lite.task.vision.classifier.ImageClassifier
import java.io.File
import java.nio.ByteBuffer
import java.nio.ByteOrder


class VisionLearn (telemetry : Telemetry) : VisionProcessor {
    //var bitmap: Bitmap? = null
    //var imageClassifier: ImageClassifier? = null
    var imageProcessor: ImageProcessor? = null
    //var results: MutableList<Classifications>? = null
    var lastRgbMat = Mat()
    val t = telemetry
    val obj: Object = Object()

    //var outputs: Model.Outputs? = null
    //var outputFeature0 : TensorBuffer? = null

    override fun init(width:Int, height:Int, calibration: CameraCalibration) {

    }

    override fun processFrame(input: Mat, captureTimeNanos: Long): Mat? {
        val rgbMat = Mat()
        Imgproc.cvtColor(input, rgbMat, Imgproc.COLOR_BGR2RGBA)
        //Imgproc.cvtColor(rgbMat, rgbMat, CvType.CV_8U)
        //bitmap = Bitmap.createBitmap(rgbMat.cols(), rgbMat.rows(), Bitmap.Config.RGB_565)
        //Utils.matToBitmap(rgbMat, bitmap)
        synchronized(obj)  {
            lastRgbMat.release()
            rgbMat.copyTo(lastRgbMat)
            rgbMat.release()
        }

        //if (imageClassifier == null) {
         //   setupClassifier()
        //}
        //imageProcessor = ImageProcessor.Builder().build()
        //val image = Bitmap.createScaledBitmap(bitmap!!, 224, 224, false)
        //val tensorImage = imageProcessor?.process(TensorImage.fromBitmap(bitmap))
        //t.addLine(bitmap.toString() .toString())
        //t.update()
        //val tensorOptions = ImageProcessingOptions.builder().build()

        //results = imageClassifier!!.classify(tensorImage, tensorOptions)

        /*val model = Model.newInstance(hw.appContext.applicationContext)
        val inputFeature0 : TensorBuffer = TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.FLOAT32)
        val byteBuffer : ByteBuffer = ByteBuffer.allocateDirect(4 * 224 * 224 * 3)

        byteBuffer.order(ByteOrder.nativeOrder())

        // get 1D array of 224 * 224 pixels in image

        // get 1D array of 224 * 224 pixels in image
        val intValues = IntArray(224 * 224)
        image.getPixels(intValues, 0, image.width, 0, 0, image.width, image.height)

        // iterate over pixels and extract R, G, and B values. Add to bytebuffer.

        // iterate over pixels and extract R, G, and B values. Add to bytebuffer.
        var pixel = 0
        for (i in 0 until 224) {
            for (j in 0 until 224) {
                val `val` = intValues[pixel++] // RGB
                byteBuffer.putFloat((`val` shr 16 and 0xFF) * (1f / 255f))
                byteBuffer.putFloat((`val` shr 8 and 0xFF) * (1f / 255f))
                byteBuffer.putFloat((`val` and 0xFF) * (1f / 255f))
            }
        }

        inputFeature0.loadBuffer(byteBuffer)

        outputs = model.process(inputFeature0)
        outputFeature0 = outputs!!.outputFeature0AsTensorBuffer

        // Releases model resources if no longer used.
        model.close()*/

        return input
    }

    override fun onDrawFrame(
        canvas: Canvas?,
        onscreenWidth: Int,
        onscreenHeight: Int,
        scaleBmpPxToCanvasPx: Float,
        scaleCanvasDensity: Float,
        userContext: Any?
    ) {
        TODO("Not yet implemented")
    }
    fun setupClassifier() {
        val optionsBuilder = ImageClassifier.ImageClassifierOptions.builder()
            .setScoreThreshold(0.5f)
            .setMaxResults(1)
        val baseOptionsBuilder = BaseOptions.builder().setNumThreads(2)

        optionsBuilder.setBaseOptions(baseOptionsBuilder.build())

        val model : File = File("/storage/emulated/0/FIRST/data/assets/model.tflite")

        //try {
        //    imageClassifier = ImageClassifier.createFromFileAndOptions(model, optionsBuilder.build())
        //} catch (e: IllegalStateException) {
        //    t.addData("ERROR", "Did not create classifier")
        //}
    }

    fun getResults2(): Bitmap? {
        /*var maxScore :Float = -0.1f
        var bestCat : Int = 0
        for (cat in results!![0].categories) {
            if(cat.score > maxScore) {
                bestCat = cat.index
            }
        }
        val resList = arrayListOf("RIGHT", "CENTER", "LEFT")
        return resList[bestCat]*/
        var bitmap : Bitmap? = null
        synchronized(obj) {
            Imgproc.cvtColor(lastRgbMat, lastRgbMat, CvType.CV_8U)
            bitmap = Bitmap.createBitmap(lastRgbMat.cols(), lastRgbMat.rows(), Bitmap.Config.RGB_565)
            Utils.matToBitmap(lastRgbMat, bitmap)
        }
        return bitmap!!//results!![0].categories.size
    }
}