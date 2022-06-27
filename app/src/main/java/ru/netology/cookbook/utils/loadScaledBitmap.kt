package ru.netology.cookbook.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.DisplayMetrics
import androidx.appcompat.widget.AppCompatImageView

fun AppCompatImageView.loadBitmapFromPath(imagePath: String?, height: Int = 200) {
    val bitmap = BitmapFactory.decodeFile(imagePath)
    val bitmapHeight = dpToPx(height, this.context)
    val scaled = bitmap.height * 1.0 / bitmapHeight
    val bitmapWidth = (bitmap.width / scaled).toInt()
    this.setImageBitmap(
        Bitmap.createScaledBitmap(
            bitmap,
            bitmapWidth,
            bitmapHeight,
            false
        )
    )
}

fun dpToPx(dp: Int, context: Context) =
    dp * (context.resources.displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)
