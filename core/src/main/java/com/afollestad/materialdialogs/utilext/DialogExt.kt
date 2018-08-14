/*
 * Licensed under Apache-2.0
 *
 * Designed an developed by Aidan Follestad (afollestad)
 */

package com.afollestad.materialdialogs.utilext

import android.content.Context.INPUT_METHOD_SERVICE
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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.view.inputmethod.InputMethodManager.SHOW_IMPLICIT
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.R
import com.afollestad.materialdialogs.shared.getColor
import com.afollestad.materialdialogs.shared.getDrawable
import com.afollestad.materialdialogs.shared.postApply
import com.afollestad.materialdialogs.shared.updateMargin
import com.afollestad.materialdialogs.shared.updatePadding

@Suppress("UNCHECKED_CAST")
internal fun <T> MaterialDialog.inflate(
  @LayoutRes res: Int,
  root: ViewGroup? = null
): T {
  return LayoutInflater.from(windowContext).inflate(res, root, false) as T
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

  context.resources.apply {
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
  this.contentScrollView = inflate(R.layout.md_dialog_stub_scrollview, this.view)
  this.contentScrollView!!.rootView = this.view
  this.contentScrollViewFrame = this.contentScrollView!!.getChildAt(0) as LinearLayout
  this.view.addView(this.contentScrollView, 1)
}

internal fun MaterialDialog.preShow() {
  this.view.apply {
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
): Int = getColor(windowContext, res, attr)

internal fun MaterialDialog.getString(
  @StringRes res: Int? = null,
  @StringRes fallback: Int? = null
): CharSequence? {
  val resourceId = res ?: (fallback ?: 0)
  if (resourceId == 0) return null
  return windowContext.resources.getText(resourceId)
}

internal fun MaterialDialog.getStringArray(@ArrayRes res: Int?): Array<String> {
  if (res == null) return emptyArray()
  return windowContext.resources.getStringArray(res)
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
  val drawable = getDrawable(windowContext, res = iconRes, fallback = icon)
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
  @StringRes fallback: Int = 0
) {
  val value = text ?: getString(textRes, fallback)
  if (value != null) {
    (textView.parent as View).visibility = View.VISIBLE
    textView.visibility = View.VISIBLE
    textView.text = value
  } else {
    textView.visibility = View.GONE
  }
}

internal fun MaterialDialog.showKeyboardIfApplicable() {
  val editText = textInputLayout?.editText ?: return
  editText.postApply {
    requestFocus()
    val imm =
      windowContext.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(editText, SHOW_IMPLICIT)
  }
}

internal fun MaterialDialog.hideKeyboard() {
  val imm =
    windowContext.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
  val currentFocus = currentFocus
  val windowToken = if (currentFocus != null) {
    currentFocus.windowToken
  } else {
    view.windowToken
  }
  if (windowToken != null) {
    imm.hideSoftInputFromWindow(windowToken, 0)
  }
}