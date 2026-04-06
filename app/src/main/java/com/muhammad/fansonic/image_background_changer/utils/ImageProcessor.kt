package com.muhammad.fansonic.image_background_changer.utils

import android.graphics.Bitmap

class ImageProcessor(
    private val listerner: ImageProcessorListerner
){
    private var foregroundImage : Bitmap? = null
    private var maskImage : Bitmap? = null
    private var maskBgImage : Bitmap? = null
    private var bgImage : Bitmap? = null
    private val segmentHelper = SegmentHelper(object : ProcessedListener{
        override fun imageProcessed() {
            updateMaskImage()
        }
    })
    var selectedMode : DisplayMode = DisplayMode.MASK
        set(value){
            field = value
            notifyImageUpdated()
        }
    fun chooseImage(
        bmp : Bitmap, isForeground : Boolean
    ){
        if(bmp.isEmpty()){
            println("Cannot choose an empty image")
            return
        }
        if(isForeground){
            foregroundImage = bmp
            segmentHelper.processImage(foregroundImage!!)
        } else{
            bgImage = bmp
            generateMaskBackgroundImage()
        }
    }
    private fun generateMaskBackgroundImage(){
        if(foregroundImage != null && bgImage != null){
            maskBgImage = segmentHelper.generateMaskBgImage(foregroundImage!!, bgImage!!)
        } else{
            println("Cannot generate mask background image")
        }
        notifyImageUpdated()
    }
    private fun updateMaskImage(){
        foregroundImage?.let { image ->
            maskImage = segmentHelper.generateMaskImage(image)
            notifyImageUpdated()
        }
    }
    private fun notifyImageUpdated(){
        listerner.onImageUpdated(getCurrentImage())
    }
    fun getCurrentImage() : Bitmap?{
        return when(selectedMode){
            DisplayMode.Normal -> foregroundImage
            DisplayMode.MASK -> maskImage
            DisplayMode.CUSTOM_BG -> maskBgImage
        }
    }
}

private fun Bitmap?.isEmpty() : Boolean{
    return this == null || (width == 0 || height == 0)
}

interface ImageProcessorListerner{
    fun onImageUpdated(image : Bitmap?)
}