package com.sample.edgedetection.scan

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.sample.edgedetection.R
import com.sample.edgedetection.REQUEST_CODE
import com.sample.edgedetection.SCANNED_RESULT
import com.sample.edgedetection.SourceManager
import com.sample.edgedetection.base.BaseActivity
import com.sample.edgedetection.crop.CropActivity
import com.sample.edgedetection.processor.processPicture
import org.opencv.android.OpenCVLoader
import org.opencv.core.Core
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.core.Size
import org.opencv.imgcodecs.Imgcodecs
import org.opencv.imgproc.Imgproc
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.io.InputStream

@RequiresApi(Build.VERSION_CODES.N)
class ScanActivity : BaseActivity()  {

    override fun provideContentViewId(): Int = R.layout.activity_scan

    override fun initPresenter() {

    }

    override fun prepare() {
       if (!OpenCVLoader.initDebug()) {
            Log.i(TAG, "loading opencv error, exit")
            finish()
        }
        val bundle = intent.extras;
        if (bundle != null) {
            startDetectEdge(Uri.fromFile(File(bundle.getString("imagePath").toString())));
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                if (null != data && null != data.extras) {
                    val path = data.extras!!.getString(SCANNED_RESULT)
                    setResult(Activity.RESULT_OK, Intent().putExtra(SCANNED_RESULT, path))
                }
            }
            else if(resultCode == Activity.RESULT_CANCELED) {
                setResult(Activity.RESULT_CANCELED, Intent())
            }
        }
        finish()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun startDetectEdge(imageUri: Uri) {
        val iStream: InputStream = contentResolver.openInputStream(imageUri)!!

        val exif = ExifInterface(iStream);
        var rotation = -1
        val orientation: Int = exif.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_UNDEFINED)
        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotation = Core.ROTATE_90_CLOCKWISE
            ExifInterface.ORIENTATION_ROTATE_180 -> rotation = Core.ROTATE_180
            ExifInterface.ORIENTATION_ROTATE_270 -> rotation = Core.ROTATE_90_COUNTERCLOCKWISE
        }
        Log.i(TAG, "rotation:$rotation")

        var imageWidth = exif.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH, 0).toDouble()
        var imageHeight = exif.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH, 0).toDouble()
        if (rotation == Core.ROTATE_90_CLOCKWISE || rotation == Core.ROTATE_90_COUNTERCLOCKWISE) {
            imageWidth = exif.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH, 0).toDouble()
            imageHeight = exif.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH, 0).toDouble()
        }
        Log.i(TAG, "width:$imageWidth")
        Log.i(TAG, "height:$imageHeight")

        val inputData: ByteArray? = getBytes(contentResolver.openInputStream(imageUri)!!)
        val mat = Mat(Size(imageWidth, imageHeight), CvType.CV_8U)
        mat.put(0, 0, inputData)
        val pic = Imgcodecs.imdecode(mat, Imgcodecs.CV_LOAD_IMAGE_UNCHANGED)
        if (rotation > -1) Core.rotate(pic, pic, rotation)
        mat.release()

        detectEdge(pic);
    }package com.sample.edgedetection.scan

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.sample.edgedetection.R
import com.sample.edgedetection.REQUEST_CODE
import com.sample.edgedetection.SCANNED_RESULT
import com.sample.edgedetection.SourceManager
import com.sample.edgedetection.base.BaseActivity
import com.sample.edgedetection.crop.CropActivity
import com.sample.edgedetection.processor.processPicture
import org.opencv.android.OpenCVLoader
import org.opencv.core.Core
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.core.Size
import org.opencv.imgcodecs.Imgcodecs
import org.opencv.imgproc.Imgproc
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.io.InputStream

@RequiresApi(Build.VERSION_CODES.N)
class ScanActivity : BaseActivity()  {

    override fun provideContentViewId(): Int = R.layout.activity_scan

    override fun initPresenter() {

    }

    override fun prepare() {
       if (!OpenCVLoader.initDebug()) {
            Log.i(TAG, "loading opencv error, exit")
            finish()
        }
        val bundle = intent.extras;
        if (bundle != null) {
            startDetectEdge(Uri.fromFile(File(bundle.getString("imagePath").toString())));
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                if (null != data && null != data.extras) {
                    val path = data.extras!!.getString(SCANNED_RESULT)
                    setResult(Activity.RESULT_OK, Intent().putExtra(SCANNED_RESULT, path))
                }
            }
            else if(resultCode == Activity.RESULT_CANCELED) {
                setResult(Activity.RESULT_CANCELED, Intent())
            }
        }
        finish()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun startDetectEdge(imageUri: Uri) {
        val iStream: InputStream = contentResolver.openInputStream(imageUri)!!

        val exif = ExifInterface(iStream);
        var rotation = -1
        val orientation: Int = exif.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_UNDEFINED)
        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotation = Core.ROTATE_90_CLOCKWISE
            ExifInterface.ORIENTATION_ROTATE_180 -> rotation = Core.ROTATE_180
            ExifInterface.ORIENTATION_ROTATE_270 -> rotation = Core.ROTATE_90_COUNTERCLOCKWISE
        }
        Log.i(TAG, "rotation:$rotation")

        var imageWidth = exif.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH, 0).toDouble()
        var imageHeight = exif.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH, 0).toDouble()
        if (rotation == Core.ROTATE_90_CLOCKWISE || rotation == Core.ROTATE_90_COUNTERCLOCKWISE) {
            imageWidth = exif.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH, 0).toDouble()
            imageHeight = exif.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH, 0).toDouble()
        }
        Log.i(TAG, "width:$imageWidth")
        Log.i(TAG, "height:$imageHeight")

        val inputData: ByteArray? = getBytes(contentResolver.openInputStream(imageUri)!!)
        val mat = Mat(Size(imageWidth, imageHeight), CvType.CV_8U)
        mat.put(0, 0, inputData)
        val pic = Imgcodecs.imdecode(mat, Imgcodecs.CV_LOAD_IMAGE_UNCHANGED)
        if (rotation > -1) Core.rotate(pic, pic, rotation)
        mat.release()

        detectEdge(pic);
    }

    private fun detectEdge(pic: Mat) {
        SourceManager.corners = processPicture(pic)
        Imgproc.cvtColor(pic, pic, Imgproc.COLOR_RGB2BGRA)
        SourceManager.pic = pic
        (this as Activity).startActivityForResult(Intent(this, CropActivity::class.java), REQUEST_CODE)
    }

    @Throws(IOException::class)
    fun getBytes(inputStream: InputStream): ByteArray? {
        val byteBuffer = ByteArrayOutputStream()
        val bufferSize = 1024
        val buffer = ByteArray(bufferSize)
        var len = 0
        while (inputStream.read(buffer).also { len = it } != -1) {
            byteBuffer.write(buffer, 0, len)
        }
        return byteBuffer.toByteArray()
    }
}package com.sample.edgedetection.scan

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.sample.edgedetection.R
import com.sample.edgedetection.REQUEST_CODE
import com.sample.edgedetection.SCANNED_RESULT
import com.sample.edgedetection.SourceManager
import com.sample.edgedetection.base.BaseActivity
import com.sample.edgedetection.crop.CropActivity
import com.sample.edgedetection.processor.processPicture
import org.opencv.android.OpenCVLoader
import org.opencv.core.Core
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.core.Size
import org.opencv.imgcodecs.Imgcodecs
import org.opencv.imgproc.Imgproc
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.io.InputStream

@RequiresApi(Build.VERSION_CODES.N)
class ScanActivity : BaseActivity()  {

    override fun provideContentViewId(): Int = R.layout.activity_scan

    override fun initPresenter() {

    }

    override fun prepare() {
       if (!OpenCVLoader.initDebug()) {
            Log.i(TAG, "loading opencv error, exit")
            finish()
        }
        val bundle = intent.extras;
        if (bundle != null) {
            startDetectEdge(Uri.fromFile(File(bundle.getString("imagePath").toString())));
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                if (null != data && null != data.extras) {
                    val path = data.extras!!.getString(SCANNED_RESULT)
                    setResult(Activity.RESULT_OK, Intent().putExtra(SCANNED_RESULT, path))
                }
            }
            else if(resultCode == Activity.RESULT_CANCELED) {
                setResult(Activity.RESULT_CANCELED, Intent())
            }
        }
        finish()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun startDetectEdge(imageUri: Uri) {
        val iStream: InputStream = contentResolver.openInputStream(imageUri)!!

        val exif = ExifInterface(iStream);
        var rotation = -1
        val orientation: Int = exif.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_UNDEFINED)
        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotation = Core.ROTATE_90_CLOCKWISE
            ExifInterface.ORIENTATION_ROTATE_180 -> rotation = Core.ROTATE_180
            ExifInterface.ORIENTATION_ROTATE_270 -> rotation = Core.ROTATE_90_COUNTERCLOCKWISE
        }
        Log.i(TAG, "rotation:$rotation")

        var imageWidth = exif.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH, 0).toDouble()
        var imageHeight = exif.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH, 0).toDouble()
        if (rotation == Core.ROTATE_90_CLOCKWISE || rotation == Core.ROTATE_90_COUNTERCLOCKWISE) {
            imageWidth = exif.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH, 0).toDouble()
            imageHeight = exif.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH, 0).toDouble()
        }
        Log.i(TAG, "width:$imageWidth")
        Log.i(TAG, "height:$imageHeight")

        val inputData: ByteArray? = getBytes(contentResolver.openInputStream(imageUri)!!)
        val mat = Mat(Size(imageWidth, imageHeight), CvType.CV_8U)
        mat.put(0, 0, inputData)
        val pic = Imgcodecs.imdecode(mat, Imgcodecs.CV_LOAD_IMAGE_UNCHANGED)
        if (rotation > -1) Core.rotate(pic, pic, rotation)
        mat.release()

        detectEdge(pic);
    }

    private fun detectEdge(pic: Mat) {
        SourceManager.corners = processPicture(pic)
        Imgproc.cvtColor(pic, pic, Imgproc.COLOR_RGB2BGRA)
        SourceManager.pic = pic
        (this as Activity).startActivityForResult(Intent(this, CropActivity::class.java), REQUEST_CODE)
    }

    @Throws(IOException::class)
    fun getBytes(inputStream: InputStream): ByteArray? {
        val byteBuffer = ByteArrayOutputStream()
        val bufferSize = 1024
        val buffer = ByteArray(bufferSize)
        var len = 0
        while (inputStream.read(buffer).also { len = it } != -1) {
            byteBuffer.write(buffer, 0, len)
        }
        return byteBuffer.toByteArray()
    }
}package com.sample.edgedetection.scan

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.sample.edgedetection.R
import com.sample.edgedetection.REQUEST_CODE
import com.sample.edgedetection.SCANNED_RESULT
import com.sample.edgedetection.SourceManager
import com.sample.edgedetection.base.BaseActivity
import com.sample.edgedetection.crop.CropActivity
import com.sample.edgedetection.processor.processPicture
import org.opencv.android.OpenCVLoader
import org.opencv.core.Core
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.core.Size
import org.opencv.imgcodecs.Imgcodecs
import org.opencv.imgproc.Imgproc
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.io.InputStream

@RequiresApi(Build.VERSION_CODES.N)
class ScanActivity : BaseActivity()  {

    override fun provideContentViewId(): Int = R.layout.activity_scan

    override fun initPresenter() {

    }

    override fun prepare() {
       if (!OpenCVLoader.initDebug()) {
            Log.i(TAG, "loading opencv error, exit")
            finish()
        }
        val bundle = intent.extras;
        if (bundle != null) {
            startDetectEdge(Uri.fromFile(File(bundle.getString("imagePath").toString())));
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                if (null != data && null != data.extras) {
                    val path = data.extras!!.getString(SCANNED_RESULT)
                    setResult(Activity.RESULT_OK, Intent().putExtra(SCANNED_RESULT, path))
                }
            }
            else if(resultCode == Activity.RESULT_CANCELED) {
                setResult(Activity.RESULT_CANCELED, Intent())
            }
        }
        finish()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun startDetectEdge(imageUri: Uri) {
        val iStream: InputStream = contentResolver.openInputStream(imageUri)!!

        val exif = ExifInterface(iStream);
        var rotation = -1
        val orientation: Int = exif.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_UNDEFINED)
        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotation = Core.ROTATE_90_CLOCKWISE
            ExifInterface.ORIENTATION_ROTATE_180 -> rotation = Core.ROTATE_180
            ExifInterface.ORIENTATION_ROTATE_270 -> rotation = Core.ROTATE_90_COUNTERCLOCKWISE
        }
        Log.i(TAG, "rotation:$rotation")

        var imageWidth = exif.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH, 0).toDouble()
        var imageHeight = exif.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH, 0).toDouble()
        if (rotation == Core.ROTATE_90_CLOCKWISE || rotation == Core.ROTATE_90_COUNTERCLOCKWISE) {
            imageWidth = exif.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH, 0).toDouble()
            imageHeight = exif.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH, 0).toDouble()
        }
        Log.i(TAG, "width:$imageWidth")
        Log.i(TAG, "height:$imageHeight")

        val inputData: ByteArray? = getBytes(contentResolver.openInputStream(imageUri)!!)
        val mat = Mat(Size(imageWidth, imageHeight), CvType.CV_8U)
        mat.put(0, 0, inputData)
        val pic = Imgcodecs.imdecode(mat, Imgcodecs.CV_LOAD_IMAGE_UNCHANGED)
        if (rotation > -1) Core.rotate(pic, pic, rotation)
        mat.release()

        detectEdge(pic);
    }

    private fun detectEdge(pic: Mat) {
        SourceManager.corners = processPicture(pic)
        Imgproc.cvtColor(pic, pic, Imgproc.COLOR_RGB2BGRA)
        SourceManager.pic = pic
        (this as Activity).startActivityForResult(Intent(this, CropActivity::class.java), REQUEST_CODE)
    }

    @Throws(IOException::class)
    fun getBytes(inputStream: InputStream): ByteArray? {
        val byteBuffer = ByteArrayOutputStream()
        val bufferSize = 1024
        val buffer = ByteArray(bufferSize)
        var len = 0
        while (inputStream.read(buffer).also { len = it } != -1) {
            byteBuffer.write(buffer, 0, len)
        }
        return byteBuffer.toByteArray()
    }
}package com.sample.edgedetection.scan

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.sample.edgedetection.R
import com.sample.edgedetection.REQUEST_CODE
import com.sample.edgedetection.SCANNED_RESULT
import com.sample.edgedetection.SourceManager
import com.sample.edgedetection.base.BaseActivity
import com.sample.edgedetection.crop.CropActivity
import com.sample.edgedetection.processor.processPicture
import org.opencv.android.OpenCVLoader
import org.opencv.core.Core
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.core.Size
import org.opencv.imgcodecs.Imgcodecs
import org.opencv.imgproc.Imgproc
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.io.InputStream

@RequiresApi(Build.VERSION_CODES.N)
class ScanActivity : BaseActivity()  {

    override fun provideContentViewId(): Int = R.layout.activity_scan

    override fun initPresenter() {

    }

    override fun prepare() {
       if (!OpenCVLoader.initDebug()) {
            Log.i(TAG, "loading opencv error, exit")
            finish()
        }
        val bundle = intent.extras;
        if (bundle != null) {
            startDetectEdge(Uri.fromFile(File(bundle.getString("imagePath").toString())));
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                if (null != data && null != data.extras) {
                    val path = data.extras!!.getString(SCANNED_RESULT)
                    setResult(Activity.RESULT_OK, Intent().putExtra(SCANNED_RESULT, path))
                }
            }
            else if(resultCode == Activity.RESULT_CANCELED) {
                setResult(Activity.RESULT_CANCELED, Intent())
            }
        }
        finish()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun startDetectEdge(imageUri: Uri) {
        val iStream: InputStream = contentResolver.openInputStream(imageUri)!!

        val exif = ExifInterface(iStream);
        var rotation = -1
        val orientation: Int = exif.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_UNDEFINED)
        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotation = Core.ROTATE_90_CLOCKWISE
            ExifInterface.ORIENTATION_ROTATE_180 -> rotation = Core.ROTATE_180
            ExifInterface.ORIENTATION_ROTATE_270 -> rotation = Core.ROTATE_90_COUNTERCLOCKWISE
        }
        Log.i(TAG, "rotation:$rotation")

        var imageWidth = exif.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH, 0).toDouble()
        var imageHeight = exif.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH, 0).toDouble()
        if (rotation == Core.ROTATE_90_CLOCKWISE || rotation == Core.ROTATE_90_COUNTERCLOCKWISE) {
            imageWidth = exif.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH, 0).toDouble()
            imageHeight = exif.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH, 0).toDouble()
        }
        Log.i(TAG, "width:$imageWidth")
        Log.i(TAG, "height:$imageHeight")

        val inputData: ByteArray? = getBytes(contentResolver.openInputStream(imageUri)!!)
        val mat = Mat(Size(imageWidth, imageHeight), CvType.CV_8U)
        mat.put(0, 0, inputData)
        val pic = Imgcodecs.imdecode(mat, Imgcodecs.CV_LOAD_IMAGE_UNCHANGED)
        if (rotation > -1) Core.rotate(pic, pic, rotation)
        mat.release()

        detectEdge(pic);
    }

    private fun detectEdge(pic: Mat) {
        SourceManager.corners = processPicture(pic)
        Imgproc.cvtColor(pic, pic, Imgproc.COLOR_RGB2BGRA)
        SourceManager.pic = pic
        (this as Activity).startActivityForResult(Intent(this, CropActivity::class.java), REQUEST_CODE)
    }

    @Throws(IOException::class)
    fun getBytes(inputStream: InputStream): ByteArray? {
        val byteBuffer = ByteArrayOutputStream()
        val bufferSize = 1024
        val buffer = ByteArray(bufferSize)
        var len = 0
        while (inputStream.read(buffer).also { len = it } != -1) {
            byteBuffer.write(buffer, 0, len)
        }
        return byteBuffer.toByteArray()
    }
}

    private fun detectEdge(pic: Mat) {
        SourceManager.corners = processPicture(pic)
        Imgproc.cvtColor(pic, pic, Imgproc.COLOR_RGB2BGRA)
        SourceManager.pic = pic
        (this as Activity).startActivityForResult(Intent(this, CropActivity::class.java), REQUEST_CODE)
    }

    @Throws(IOException::class)
    fun getBytes(inputStream: InputStream): ByteArray? {
        val byteBuffer = ByteArrayOutputStream()
        val bufferSize = 1024
        val buffer = ByteArray(bufferSize)
        var len = 0
        while (inputStream.read(buffer).also { len = it } != -1) {
            byteBuffer.write(buffer, 0, len)
        }
        return byteBuffer.toByteArray()
    }
}