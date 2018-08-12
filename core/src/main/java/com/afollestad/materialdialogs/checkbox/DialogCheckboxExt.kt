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

@CheckResult
fun MaterialDialog.getCheckBoxPrompt(): CheckBox {
  return view.buttonsLayout.checkBoxPrompt
}

@CheckResult
fun MaterialDialog.checkBoxPrompt(
  @StringRes textRes: Int = 0,
  text: CharSequence? = null,
  isCheckedDefault: Boolean = false,
  onToggle: ((Boolean) -> Unit)?
): MaterialDialog {
  assertOneSet(textRes, text)
  with(view.buttonsLayout.checkBoxPrompt) {
    this.visibility = View.VISIBLE
    this.text = text ?: getString(textRes)
    this.isChecked = isCheckedDefault
    this.setOnCheckedChangeListener { _, checked ->
      onToggle?.invoke(checked)
    }
  }
  return this
}