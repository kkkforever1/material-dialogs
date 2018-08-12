/*
 * Licensed under Apache-2.0
 *
 * Designed an developed by Aidan Follestad (afollestad)
 */

@file:Suppress("unused")

package com.afollestad.materialdialogs.input

import android.support.annotation.CheckResult
import android.support.annotation.StringRes
import android.support.design.widget.TextInputLayout
import android.text.InputType
import android.widget.EditText
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.R.layout
import com.afollestad.materialdialogs.WhichButton.POSITIVE
import com.afollestad.materialdialogs.utilext.addContentScrollView
import com.afollestad.materialdialogs.utilext.getString
import com.afollestad.materialdialogs.utilext.inflate
import com.afollestad.materialdialogs.utilext.textChanged

typealias InputCallback = ((MaterialDialog, CharSequence) -> Unit)?

@CheckResult
fun MaterialDialog.getInputLayout(): TextInputLayout? {
  return this.textInputLayout
}

@CheckResult
fun MaterialDialog.getInputField(): EditText? {
  return this.textInputLayout?.editText
}

@CheckResult
fun MaterialDialog.input(
  hint: CharSequence? = null,
  @StringRes hintRes: Int? = null,
  prefill: CharSequence? = null,
  @StringRes prefillRes: Int? = null,
  inputType: Int = InputType.TYPE_CLASS_TEXT,
  maxLength: Int? = null,
  callback: InputCallback = null
): MaterialDialog {
  addInputField()
  val editText = this.textInputLayout!!.editText!!
  editText.setText(prefill ?: getString(prefillRes))
  editText.hint = hint ?: getString(hintRes)
  editText.inputType = inputType

  if (maxLength != null) {
    this.textInputLayout!!.isCounterEnabled = true
    this.textInputLayout!!.counterMaxLength = maxLength
  }

  editText.textChanged {
    callback?.invoke(this@input, it)
    invalidateInputMaxLength()
  }
  invalidateInputMaxLength()

  return this
}

internal fun MaterialDialog.invalidateInputMaxLength() {
  val editText = this.textInputLayout!!.editText!!
  val maxLength = this.textInputLayout!!.counterMaxLength
  val currentLength = editText.text.length
  if (maxLength > 0) {
    setActionButtonEnabled(POSITIVE, currentLength <= maxLength)
  }
}

private fun MaterialDialog.addInputField() {
  if (this.contentRecyclerView != null) {
    throw IllegalStateException(
        "Your dialog has already been setup with a unsupported different type " +
            "(e.g. with a list, etc.)"
    )
  }
  addContentScrollView()
  this.textInputLayout = inflate(
      context, layout.md_dialog_stub_input, this.contentScrollViewFrame!!
  )
  this.contentScrollViewFrame!!.addView(this.textInputLayout)
}
