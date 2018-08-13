/*
 * Licensed under Apache-2.0
 *
 * Designed an developed by Aidan Follestad (afollestad)
 */

@file:Suppress("unused")

package com.afollestad.materialdialogs.checkbox

import android.support.annotation.CheckResult
import android.support.annotation.StringRes
import android.view.View
import android.widget.CheckBox
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.utilext.assertOneSet
import com.afollestad.materialdialogs.utilext.getString

typealias BooleanCallback = ((Boolean) -> Unit)?

@CheckResult
fun MaterialDialog.getCheckBoxPrompt(): CheckBox {
  return view.buttonsLayout.checkBoxPrompt
}

@CheckResult
fun MaterialDialog.isCheckPromptChecked() = getCheckBoxPrompt().isChecked

/**
 * @param textRes The string resource to display for the checkbox label.
 * @param text The literal string to display for the checkbox label.
 * @param isCheckedDefault Whether or not the checkbox is initially checked.
 * @param onToggle A listener invoked when the checkbox is checked or unchecked.
 */
@CheckResult
fun MaterialDialog.checkBoxPrompt(
  @StringRes textRes: Int = 0,
  text: String? = null,
  isCheckedDefault: Boolean = false,
  onToggle: BooleanCallback
): MaterialDialog {
  assertOneSet(textRes, text)
  view.buttonsLayout.checkBoxPrompt.apply {
    this.visibility = View.VISIBLE
    this.text = text ?: getString(textRes)
    this.isChecked = isCheckedDefault
    this.setOnCheckedChangeListener { _, checked ->
      onToggle?.invoke(checked)
    }
  }
  return this
}