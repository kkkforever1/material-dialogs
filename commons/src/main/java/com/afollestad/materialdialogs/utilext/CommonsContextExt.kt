/*
 * Licensed under Apache-2.0
 *
 * Designed an developed by Aidan Follestad (afollestad)
 */

package com.afollestad.materialdialogs.utilext

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.support.annotation.AttrRes
import android.support.annotation.DimenRes
import android.support.annotation.DrawableRes
import android.support.v4.content.ContextCompat
import android.view.View
import com.afollestad.materialdialogs.MaterialDialog

internal fun Context.hasPermission(permission: String): Boolean {
  return ContextCompat.checkSelfPermission(this, permission) ==
      PackageManager.PERMISSION_GRANTED
}

internal fun MaterialDialog.hasReadStoragePermission(): Boolean {
  return windowContext.hasPermission(READ_EXTERNAL_STORAGE)
}

internal fun <T : View> T.dimenPx(@DimenRes res: Int): Int {
  return context.resources.getDimensionPixelSize(res)
}

internal fun Context.getDrawable(
  @DrawableRes res: Int? = null,
  @AttrRes attr: Int? = null,
  fallback: Drawable? = null
): Drawable? {
  if (attr != null) {
    val a = theme.obtainStyledAttributes(intArrayOf(attr))
    try {
      var d = a.getDrawable(0)
      if (d == null && fallback != null) {
        d = fallback
      }
      return d
    } finally {
      a.recycle()
    }
  }
  if (res == null) return fallback
  return ContextCompat.getDrawable(this, res)
}