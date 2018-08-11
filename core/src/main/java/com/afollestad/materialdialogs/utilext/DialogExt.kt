/*
 * Licensed under Apache-2.0
 *
 * Designed an developed by Aidan Follestad (afollestad)
 */

package com.afollestad.materialdialogs.utilext

import android.content.Context
import android.graphics.Point
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.support.annotation.ArrayRes
import android.support.annotation.AttrRes
import android.support.annotation.CheckResult
import android.support.annotation.ColorInt
import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.support.annotation.LayoutRes
import android.support.annotation.StringRes
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.R

@Suppress("UNCHECKED_CAST")
internal fun <T> inflate(
  context: Context,
  @LayoutRes res: Int,
  root: ViewGroup? = null
): T {
  return LayoutInflater.from(context).inflate(res, root, false) as T
}

internal fun assertOneSet(
  a: Int?,
  b: Any?
) {
  if ((a == null || a == 0) && b == null) {
    throw IllegalArgumentException("You must specify a resource ID or literal value.")
  }
}

@CheckResult
internal fun MaterialDialog.colorBackground(
  @ColorInt color: Int? = null,
  @ColorRes colorRes: Int = 0
): MaterialDialog {
  assertOneSet(colorRes, color)
  val colorValue = color ?: getColor(res = colorRes)
  val drawable = GradientDrawable()
  drawable.cornerRadius = context.resources
      .getDimension(R.dimen.md_dialog_bg_corner_radius)
  drawable.setColor(colorValue)
  window!!.setBackgroundDrawable(drawable)
  return this
}

internal fun MaterialDialog.setWindowConstraints() {
  val wm = this.window!!.windowManager
  val display = wm.defaultDisplay
  val size = Point()
  display.getSize(size)
  val windowWidth = size.x
  val windowHeight = size.y

  with(context.resources) {
    val windowVerticalPadding = getDimensionPixelSize(
        R.dimen.md_dialog_vertical_margin
    )
    val windowHorizontalPadding = getDimensionPixelSize(
        R.dimen.md_dialog_horizontal_margin
    )
    val maxWidth = getDimensionPixelSize(R.dimen.md_dialog_max_width)
    val calculatedWidth = windowWidth - windowHorizontalPadding * 2

    this@setWindowConstraints.view.maxHeight = windowHeight - windowVerticalPadding * 2
    val lp = WindowManager.LayoutParams()
    lp.copyFrom(this@setWindowConstraints.window!!.attributes)
    lp.width = Math.min(maxWidth, calculatedWidth)
    this@setWindowConstraints.window!!.attributes = lp
  }
}

internal fun MaterialDialog.setDefaults() {
  val backgroundColor = getColor(attr = R.attr.colorBackgroundFloating)
  colorBackground(color = backgroundColor)
}

internal fun MaterialDialog.addContentScrollView() {
  if (this.contentScrollView != null) {
    return
  }
  this.contentScrollView = inflate(context, R.layout.md_dialog_stub_scrollview, this.view)
  this.contentScrollView!!.rootView = this.view
  this.contentScrollViewFrame = this.contentScrollView!![0]
  this.view.addView(this.contentScrollView, 1)
}

internal fun MaterialDialog.preShow() {
  with(this.view) {
    if (titleLayout.shouldNotBeVisible()) {
      contentView.updatePadding(
          top = frameMarginVerticalLess,
          bottom = frameMarginVerticalLess
      )
    }
    if (textViewMessage != null && textInputLayout != null) {
      textInputLayout?.updateMargin(
          top = frameMarginVerticalLess
      )
    }
  }
}

@ColorInt internal fun MaterialDialog.getColor(
  @ColorRes res: Int? = null,
  @AttrRes attr: Int? = null
): Int = getColor(context, res, attr)

internal fun MaterialDialog.getString(
  @StringRes res: Int? = null,
  @StringRes fallback: Int? = null
): CharSequence? {
  val resourceId = res ?: (fallback ?: 0)
  if (resourceId == 0) return null
  return context.resources.getText(resourceId)
}

internal fun getDrawable(
  context: Context,
  @DrawableRes res: Int? = null,
  @AttrRes attr: Int? = null,
  fallback: Drawable? = null
): Drawable? {
  if (attr != null) {
    val a = context.theme.obtainStyledAttributes(intArrayOf(attr))
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
  return ContextCompat.getDrawable(context, res)
}

internal fun MaterialDialog.getStringArray(@ArrayRes res: Int?): Array<CharSequence> {
  if (res == null) return emptyArray()
  return context.resources.getTextArray(res)
}

internal fun MaterialDialog.hasActionButtons(): Boolean {
  return view.buttonsLayout.visibleButtons
      .isNotEmpty()
}

internal fun MaterialDialog.setIcon(
  imageView: ImageView,
  @DrawableRes iconRes: Int?,
  icon: Drawable?
) {
  val drawable = getDrawable(context, res = iconRes, fallback = icon)
  if (drawable != null) {
    (imageView.parent as View).visibility = View.VISIBLE
    imageView.visibility = View.VISIBLE
    imageView.setImageDrawable(drawable)
  } else {
    imageView.visibility = View.GONE
  }
}

internal fun MaterialDialog.setText(
  textView: TextView,
  @StringRes textRes: Int? = null,
  text: CharSequence? = null,
  @StringRes fallback: Int = 0,
  click: ((MaterialDialog) -> (Unit))? = null,
  allowDismiss: Boolean = true
) {
  val value = text ?: getString(textRes, fallback)
  if (value != null) {
    (textView.parent as View).visibility = View.VISIBLE
    textView.visibility = View.VISIBLE
    textView.text = value
  } else {
    textView.visibility = View.GONE
  }
  if (value != null && (allowDismiss || click != null)) {
    textView.setOnClickListener {
      if (autoDismiss) {
        dismiss()
      }
      if (click != null) {
        click(this@setText)
      }
    }
  }
}
