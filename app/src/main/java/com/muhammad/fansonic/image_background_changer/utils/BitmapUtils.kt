package com.muhammad.fansonic.image_background_changer.utils

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.net.Uri
import android.provider.MediaStore
import kotlin.math.roundToInt

@SuppressLint("UseKtx")
fun mergeBitmaps(bmp1: Bitmap, bmp2: Bitmap): Bitmap {
    val merged = Bitmap.createBitmap(bmp1.width, bmp1.height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(merged)
    canvas.drawBitmap(bmp1, Matrix(), null)
    canvas.drawBitmap(bmp2, Matrix(), null)
    return merged
}

@SuppressLint("UseKtx")
fun resizeBitmap(bmp: Bitmap, width: Int, height: Int): Bitmap {
    return Bitmap.createScaledBitmap(bmp, width, height, true)
}

fun resizeBitmapWithAspect(bmp: Bitmap, width: Int): Bitmap {
    val aspectRatio = bmp.width / bmp.height.toFloat()
    val height = (width / aspectRatio).roundToInt()
    return resizeBitmap(bmp = bmp, width = width, height = height)
}

fun getBitmapFromUri(contentResolver: ContentResolver, imageUri: Uri, width: Int): Bitmap? {
    return try {
        val bmp = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
        resizeBitmapWithAspect(bmp = bmp, width = width)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}