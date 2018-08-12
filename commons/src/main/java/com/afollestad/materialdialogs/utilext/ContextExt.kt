/*
 * Licensed under Apache-2.0
 *
 * Designed an developed by Aidan Follestad (afollestad)
 */

package com.afollestad.materialdialogs.utilext

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.content.Context
import android.content.pm.PackageManager
import android.support.v4.content.ContextCompat
import com.afollestad.materialdialogs.MaterialDialog

internal fun Context.hasPermission(permission: String): Boolean {
  return ContextCompat.checkSelfPermission(this, permission) ==
      PackageManager.PERMISSION_GRANTED
}

internal fun MaterialDialog.hasReadStoragePermission(): Boolean {
  return appContext.hasPermission(READ_EXTERNAL_STORAGE)
}