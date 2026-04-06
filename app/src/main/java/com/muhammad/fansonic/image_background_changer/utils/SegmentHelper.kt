package com.muhammad.fansonic.image_background_changer.utils

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Color
import androidx.core.graphics.ColorUtils
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.segmentation.Segmentation
import com.google.mlkit.vision.segmentation.Segmenter
import com.google.mlkit.vision.segmentation.selfie.SelfieSegmenterOptions
import java.nio.ByteBuffer

class SegmentHelper(
    private val listener : ProcessedListener
){
    private val segmenter : Segmenter
    private lateinit var maskBuffer : ByteBuffer
    private var maskWidth = 0
    private var maskHeight = 0
    init {
        val options = SelfieSegmenterOptions.Builder().setDetectorMode(SelfieSegmenterOptions.SINGLE_IMAGE_MODE).build()
        segmenter = Segmentation.getClient(options)
    }
    fun processImage(image : Bitmap){
        val input = InputImage.fromBitmap(image, 0)
        segmenter.process(input).addOnSuccessListener { segmentationMask ->
            maskBuffer = segmentationMask.buffer
            maskWidth = segmentationMask.width
            maskHeight = segmentationMask.height
            listener.imageProcessed()
        }.addOnFailureListener { e ->
            e.printStackTrace()
        }
    }
    @SuppressLint("UseKtx")
    fun generateMaskImage(image : Bitmap) : Bitmap{
        val maskBitmap = Bitmap.createBitmap(maskWidth, maskHeight, Bitmap.Config.ARGB_8888)
        for(x in 0 until maskWidth){
            for(y in 0 until maskHeight){
                if(x < maskBitmap.width && y < maskBitmap.height){
                    val confidence = maskBuffer.float
                    val alpha = (confidence * 255).toInt()
                    val pixelColor = if(alpha > 0) maskBitmap.getPixel(x, y) else Color.TRANSPARENT
                    maskBitmap.setPixel(x, y, Color.argb(alpha, Color.red(pixelColor), Color.green(pixelColor), Color.blue(pixelColor)))
                }
            }
        }
        maskBuffer.rewind()
        return maskBitmap
    }
    @SuppressLint("UseKtx")
    fun generateMaskBgImage(image : Bitmap, bg : Bitmap) : Bitmap{
        val bgBitmap = Bitmap.createBitmap(image.width, image.height, Bitmap.Config.ARGB_8888)
        for(x in 0 until maskWidth){
            for(y in 0 until maskHeight){
                if(x < bg.width && y < bg.height){
                    val bgConfidence = ((1.0 - maskBuffer.float) * 2).toInt()
                    var bgPixel = bg.getPixel(x, y)
                    bgPixel = ColorUtils.setAlphaComponent(bgPixel, bgConfidence)
                    bgBitmap.setPixel(x, y, bgPixel)
                }
            }
        }
        maskBuffer.rewind()
        return mergeBitmaps(image, bgBitmap)
    }
}

interface ProcessedListener {
    fun imageProcessed()
}