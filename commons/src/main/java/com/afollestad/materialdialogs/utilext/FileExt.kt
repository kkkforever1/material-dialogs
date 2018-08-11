package com.afollestad.materialdialogs.utilext

import android.os.Environment.getExternalStorageDirectory
import java.io.File

internal fun File.hasParent() = betterParent() != null

internal fun File.isExternalStorage() =
  absolutePath == getExternalStorageDirectory().absolutePath

internal fun File.betterParent(): File? {
  if (this.isExternalStorage()) {
    // Emulated external storage's parent is empty so jump over it
    return getExternalStorageDirectory().parentFile.parentFile
  }
  // Else normal operation
  return parentFile
}

internal fun File.jumpOverEmulated(): File {
  if (absolutePath == getExternalStorageDirectory().parentFile.absolutePath) {
    // Emulated external storage's parent is empty so jump over it
    return getExternalStorageDirectory()
  }
  return this
}

internal fun File.friendlyName() =
  if (isExternalStorage()) "External Storage" else name