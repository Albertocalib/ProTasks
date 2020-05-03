package com.example.protasks.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Environment
import android.widget.ImageView
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

class ImageHandler {

    fun downloadImage(image:ImageView,context: Context) {
        val draw: BitmapDrawable = image.drawable as BitmapDrawable
        var outStream: FileOutputStream? = null
        val sdCard = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val dir = File(sdCard!!.absolutePath + "/ProTasks")
        dir.mkdir()
        val fileName =
            String.format("%d.jpg", System.currentTimeMillis())
        val outFile = File(dir, fileName)
        try {
            outStream = FileOutputStream(outFile)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        draw.bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream)
        try {
            outStream!!.flush()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        try {
            outStream!!.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}