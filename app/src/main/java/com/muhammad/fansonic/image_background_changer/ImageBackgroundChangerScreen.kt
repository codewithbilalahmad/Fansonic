package com.muhammad.fansonic.image_background_changer

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import com.muhammad.fansonic.R
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.muhammad.fansonic.image_background_changer.utils.ImageProcessor
import com.muhammad.fansonic.image_background_changer.utils.ImageProcessorListerner

@Composable
fun ImageBackgroundChangerScreen() {
    val context = LocalContext.current
    var originalImage by remember { mutableStateOf<Bitmap?>(null) }
    var processedImage by remember { mutableStateOf<Bitmap?>(null) }

    // Initialize the ImageProcessor with a listener to update the processed image
    val imageProcessor = remember {
        ImageProcessor(object : ImageProcessorListerner {
            override fun onImageUpdated(image: Bitmap?) {
                processedImage = image
            }
        })
    }

    LaunchedEffect(Unit) {
        originalImage = decodeSampledBitmapFromResource(context, R.drawable.pic1, 800, 800)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Show the original image if available
        originalImage?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = "Original Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
        } ?: run {
            Text("No original image to Display")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Button to remove background
        Button(onClick = {
            // Process the image to remove the background
            originalImage?.let { imageProcessor.chooseImage(it, isForeground = true) }
        }) {
            Text("Remove Background")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Show the processed image if available
        processedImage?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = "Processed Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
        } ?: run {
            Text("No processed image to display")
        }
    }
}

fun decodeSampledBitmapFromResource(context: Context, resId: Int, reqWidth: Int, reqHeight: Int): Bitmap? {
    val options = BitmapFactory.Options().apply {
        inJustDecodeBounds = true
    }
    BitmapFactory.decodeResource(context.resources, resId, options)
    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)
    options.inJustDecodeBounds = false
    return BitmapFactory.decodeResource(context.resources, resId, options)
}

fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
    val height = options.outHeight
    val width = options.outWidth
    var inSampleSize = 1

    if (height > reqHeight || width > reqWidth) {
        val halfHeight = height / 2
        val halfWidth = width / 2

        while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
            inSampleSize *= 2
        }
    }
    return inSampleSize
}