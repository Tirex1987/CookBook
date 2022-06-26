package ru.netology.cookbook.utils

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.loader.content.CursorLoader

fun Uri.getRealPath(context: Context): String {
    val proj = arrayOf(MediaStore.Images.Media.DATA)
    val loader =
        CursorLoader(context, this, proj, null, null, null)
    val cursor = checkNotNull(loader.loadInBackground())
    val column_index: Int = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
    cursor.moveToFirst()
    val result: String = cursor.getString(column_index)
    cursor.close()
    return result
}