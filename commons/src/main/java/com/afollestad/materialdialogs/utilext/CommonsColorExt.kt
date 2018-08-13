package com.afollestad.materialdialogs.utilext

import android.content.Context
import android.graphics.Color
import android.support.annotation.AttrRes
import android.support.annotation.ColorInt
import android.support.annotation.ColorRes
import android.support.v4.content.ContextCompat

@ColorInt internal fun getColor(
  context: Context,
  @ColorRes res: Int? = null,
  @AttrRes attr: Int? = null
): Int {
  if (attr != null) {
    val a = context.theme.obtainStyledAttributes(intArrayOf(attr))
    try {
      return a.getColor(0, Color.BLACK)
    } finally {
      a.recycle()
    }
  }
  return ContextCompat.getColor(context, res ?: 0)
}

internal fun Int.isColorDark(): Boolean {
  val darkness =
    1 - (0.299 * Color.red(this) + 0.587 * Color.green(this) + 0.114 * Color.blue(this)) / 255
  return darkness >= 0.5
}

internal fun Int.toHex() =
  String.format("%06X", 0xFFFFFF and this)

internal fun String.toColor() = Color.parseColor(
    if (!this.startsWith("#")) "#$this"
    else this
)

internal fun Int.red() = Color.red(this)
internal fun Int.green() = Color.green(this)
internal fun Int.blue() = Color.blue(this)