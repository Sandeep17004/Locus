package com.example.locusassignment.utils

import android.app.Activity
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.locusassignment.BuildConfig
import com.example.locusassignment.utils.Constants.IMAGE_PREFIX
import com.example.locusassignment.utils.Constants.IMAGE_SUFFIX
import java.io.File

fun Activity.getTempFileUri(): Uri {
    val tempFile = File.createTempFile(IMAGE_PREFIX, IMAGE_SUFFIX, cacheDir).apply {
        createNewFile()
        deleteOnExit()
    }
    return FileProvider.getUriForFile(this, "${BuildConfig.APPLICATION_ID}.fileProvider", tempFile)
}

