/*
 * Licensed under Apache-2.0
 *
 * Designed an developed by Aidan Follestad (afollestad)
 */

package com.afollestad.materialdialogs.color

import android.annotation.SuppressLint
import android.support.annotation.CheckResult
import android.support.annotation.ColorInt
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView

typealias ColorCallback = ((dialog: MaterialDialog, color: Int) -> Unit)?

@SuppressLint("CheckResult")
@CheckResult
fun MaterialDialog.colorChooser(
  colors: IntArray,
  subColors: Array<IntArray>? = null,
  @ColorInt initialSelection: Int? = null,
  waitForPositiveButton: Boolean = true,
  callback: ColorCallback = null
): MaterialDialog {
  customView(R.layout.md_color_chooser_grid)
  val customView = getCustomView() as RecyclerView

  if (subColors != null && colors.size != subColors.size) {
    throw IllegalStateException("Sub-colors array size should match the colors array size.")
  }

  val gridColumnCount = windowContext.resources
      .getInteger(R.integer.color_grid_column_count)
  customView.layoutManager = GridLayoutManager(
      windowContext, gridColumnCount
  )

  val adapter = ColorGridAdapter(
      dialog = this,
      colors = colors,
      subColors = subColors,
      initialSelection = initialSelection,
      callback = if (waitForPositiveButton) null else callback
  )
  customView.adapter = adapter

  if (waitForPositiveButton && callback != null) {
    positiveButton {
      val color = adapter.selectedColor()
      if (color != null) {
        callback.invoke(this, color)
      }
    }
  }

  return this
}