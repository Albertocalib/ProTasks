package com.example.protasks.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.os.Environment
import android.util.Base64
import android.widget.ImageView
import android.widget.Toast
import java.io.*
import com.example.protasks.models.File as File2

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

    fun setTextToImage(color:Int,name:String):Bitmap{
        val image1 =
            Bitmap.createBitmap(250, 250, Bitmap.Config.ARGB_8888)
        image1.eraseColor(color)

        val canvas = Canvas(image1)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = Color.rgb(61, 61, 61)
        paint.textSize = 60F
        paint.textAlign = Paint.Align.CENTER
        val names=name.split(" ")
        var text=""
        for (el in names){
            text+=el[0].toUpperCase()
        }
        canvas.drawText(text, 250F / 2, 250F / 2, paint)
        return image1
    }
    fun downloadFile(file:File2,context: Context) {
        var outStream: FileOutputStream? = null
        val sdCard = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
        val dir = File(sdCard!!.absolutePath)
        dir.mkdir()
        val fileName = file.getName()!!
        val outFile = File(dir, fileName)
        val decodedBytes: ByteArray = Base64.decode(file.getContent(), Base64.DEFAULT)
        try {
            outStream = FileOutputStream(outFile)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        outStream!!.write(decodedBytes)
        try {
            outStream.flush()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        try {
            outStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        Toast.makeText(context, "Download complete", Toast.LENGTH_SHORT).show()
    }
}