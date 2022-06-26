package ru.netology.cookbook.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.fragment.app.Fragment

class OpenImageIntent(
    private val fragment: Fragment
) {

    fun registerForAvitvityResult(eventForResult: (String?) -> Unit): ActivityResultLauncher<Unit> {
        return fragment.registerForActivityResult(ResultContract) {
            val imagePath = it?.getRealPath(fragment.requireContext())
            eventForResult(imagePath)
        }
    }

    object ResultContract : ActivityResultContract<(Unit), Uri?>() {
        override fun createIntent(context: Context, input: Unit): Intent =
            Intent().apply {
                action = Intent.ACTION_PICK
                type = "image/*"
            }

        override fun parseResult(resultCode: Int, intent: Intent?): Uri? =
            if (resultCode == Activity.RESULT_OK) {
                intent?.data
            } else null
    }
}