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

/**
 * Shows an input field as the content of the dialog. Can be used with a message and checkbox
 * prompt, but cannot be used with a list.
 *
 * @param hint The literal string to display as the input field hint.
 * @param hintRes The string resource to display as the input field hint.
 * @param prefill The literal string to pre-fill the input field with.
 * @param prefillRes The string resource to pre-fill the input field with.
 * @param inputType The input type for the input field, e.g. phone or email. Defaults to plain text.
 * @param maxLength The max length for the input field, shows a counter and disables the positive
 *    action button if the input length surpasses it.
 * @param waitForPositiveButton When true, the [callback] isn't invoked until the positive button
 *    is clicked. Otherwise, it's invoked every time the input text changes.
 * @param callback A listener to invoke for input text notifications.
 */
@CheckResult
fun MaterialDialog.input(
  hint: String? = null,
  @StringRes hintRes: Int? = null,
  prefill: CharSequence? = null,
  @StringRes prefillRes: Int? = null,
  inputType: Int = InputType.TYPE_CLASS_TEXT,
  maxLength: Int? = null,
  waitForPositiveButton: Boolean = true,
  callback: InputCallback = null
): MaterialDialog {
  addInputField()
  if (callback != null && waitForPositiveButton) {
    // Add an additional callback to invoke the input listener after the positive AB is pressed
    positiveButton { callback.invoke(this@input, getInputField()!!.text) }
  }

  val editText = this.textInputLayout!!.editText!!
  editText.setText(prefill ?: getString(prefillRes))
  editText.hint = hint ?: getString(hintRes)
  editText.inputType = inputType

  if (maxLength != null) {
    this.textInputLayout!!.apply {
      isCounterEnabled = true
      counterMaxLength = maxLength
    }
  }

  if (!waitForPositiveButton || maxLength != null) {
    // Invoke the input listener whenever the text changes, as opposed to with positive AB presses
    editText.textChanged {
      callback?.invoke(this@input, it)
      invalidateInputMaxLength()
    }
    invalidateInputMaxLength()
  }

  return this
}

private fun MaterialDialog.invalidateInputMaxLength() {
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
      layout.md_dialog_stub_input, this.contentScrollViewFrame!!
  )
  this.contentScrollViewFrame!!.addView(this.textInputLayout)
}
