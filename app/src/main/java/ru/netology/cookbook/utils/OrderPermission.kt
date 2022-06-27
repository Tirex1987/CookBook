package ru.netology.cookbook.utils

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

class OrderPermission(
    private val fragment: Fragment
) {

    private var isPermissionStorage: Boolean = checkPermission()
    private lateinit var accessOpenEvent: () -> Unit

    private val requestPermissionLauncher =
        fragment.registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            isPermissionStorage = isGranted
            if (isGranted) accessOpenEvent()
        }

    fun checkPermission() = fragment.context?.let {
        ContextCompat.checkSelfPermission(
            it,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
    } == PackageManager.PERMISSION_GRANTED

    fun requestStoragePermission(accessOpenEvent: () -> Unit) {
        this.accessOpenEvent = accessOpenEvent
        if (checkPermission()) {
            isPermissionStorage = true
            return
        }
        requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    fun requestStoragePermission() {
        requestStoragePermission { }
    }

    fun isPermissionStorage() = isPermissionStorage
}