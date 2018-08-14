/*
 * Licensed under Apache-2.0
 *
 * Designed an developed by Aidan Follestad (afollestad)
 */

package com.afollestad.materialdialogs.files

import android.Manifest.permission
import android.content.Context
import android.content.pm.PackageManager
import android.os.Environment.getExternalStorageDirectory
import android.support.v4.content.ContextCompat
import com.afollestad.materialdialogs.MaterialDialog
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

internal fun Context.hasPermission(permission: String): Boolean {
  return ContextCompat.checkSelfPermission(this, permission) ==
      PackageManager.PERMISSION_GRANTED
}

internal fun MaterialDialog.hasReadStoragePermission(): Boolean {
  return windowContext.hasPermission(permission.READ_EXTERNAL_STORAGE)
}