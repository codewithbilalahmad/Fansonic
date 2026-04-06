package com.muhammad.fansonic.template

import android.graphics.Bitmap
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asComposePath

@Immutable
data class ColoredPath(
    val path: Path,
    val color: Color
)

fun getColoredPathsFromBitmap(
    bitmap: Bitmap,
    colorTolerance: Int = 8 // merge similar colors (0 = exact, higher = more merging)
): List<ColoredPath> {
    val width = bitmap.width
    val height = bitmap.height
    val pixels = IntArray(width * height)
    bitmap.getPixels(pixels, 0, width, 0, 0, width, height)

    // ── 1. Color quantization ──────────────────────────────────────────────────
    // Reduces thousands of ARGB values → small palette, so we get few paths.
    val quantizedPixels = IntArray(width * height)
    val palette = mutableListOf<Int>() // representative ARGB per bucket

    fun colorDistance(a: Int, b: Int): Int {
        val da = ((a ushr 24) and 0xff) - ((b ushr 24) and 0xff)
        val dr = ((a ushr 16) and 0xff) - ((b ushr 16) and 0xff)
        val dg = ((a ushr 8) and 0xff) - ((b ushr 8) and 0xff)
        val db = (a and 0xff) - (b and 0xff)
        return maxOf(Math.abs(da), Math.abs(dr), Math.abs(dg), Math.abs(db))
    }

    fun quantize(argb: Int): Int {
        // Transparent → skip (index -1)
        if ((argb ushr 24 and 0xff) <= 128) return -1
        val opaque = argb or (0xff shl 24)
        val idx = palette.indexOfFirst { colorDistance(it, opaque) <= colorTolerance }
        return if (idx != -1) idx else {
            palette.add(opaque)
            palette.size - 1
        }
    }

    for (i in pixels.indices) quantizedPixels[i] = quantize(pixels[i])

    // ── 2. Build row-spans per palette index ──────────────────────────────────
    data class Span(val x1: Int, val x2: Int, val colorIdx: Int)
    data class Band(val span: Span, val startY: Int)

    // rowSpans[y] = list of Span on that row
    val rowSpans = Array(height) { y ->
        val spans = mutableListOf<Span>()
        var startX = -1
        var startIdx = -1
        for (x in 0..width) {
            val idx = if (x < width) quantizedPixels[y * width + x] else -1
            when {
                idx >= 0 && startX == -1 -> {          // start new span
                    startX = x; startIdx = idx
                }
                idx != startIdx && startX != -1 -> {   // color changed or ended
                    spans.add(Span(startX, x, startIdx))
                    if (idx >= 0) { startX = x; startIdx = idx }
                    else { startX = -1; startIdx = -1 }
                }
            }
        }
        spans
    }

    // ── 3. Band-merge per color → one android.graphics.Path per palette entry ─
    val androidPaths = Array(palette.size) {
        android.graphics.Path().apply {
            fillType = android.graphics.Path.FillType.EVEN_ODD
        }
    }

    // activeBands is keyed by (Span) — spans carry colorIdx so two equal x1/x2
    // with different colors are distinct naturally via data class equality.
    var activeBands = mutableListOf<Band>()

    for (y in 0..height) {
        val current = if (y < height) rowSpans[y] else emptyList()

        val nextActive = mutableListOf<Band>()
        val currentSet = current.toHashSet()

        for (band in activeBands) {
            if (band.span in currentSet) {
                nextActive.add(band)           // band continues downward
            } else {
                // Flush completed band as a rect into the correct path
                androidPaths[band.span.colorIdx].addRect(
                    band.span.x1.toFloat(), band.startY.toFloat(),
                    band.span.x2.toFloat(), y.toFloat(),
                    android.graphics.Path.Direction.CW
                )
            }
        }

        val existingSpans = nextActive.map { it.span }.toHashSet()
        for (span in current) {
            if (span !in existingSpans) nextActive.add(Band(span, y))
        }

        activeBands = nextActive
    }

    // ── 4. Wrap into ColoredPath list ─────────────────────────────────────────
    return palette.mapIndexedNotNull { idx, argb ->
        val ap = androidPaths[idx]
        if (ap.isEmpty) null                  // skip unused palette entries
        else ColoredPath(ap.asComposePath(), Color(argb))
    }
}