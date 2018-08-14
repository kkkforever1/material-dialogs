package com.afollestad.materialdialogs.callbacks

import android.support.annotation.CheckResult
import com.afollestad.materialdialogs.DialogCallback
import com.afollestad.materialdialogs.MaterialDialog

/**
 * Sets a listener that's invoked when the dialog is [show]'n. If this is called
 * multiple times, it appends additional callbacks, rather than overwriting.
 */
@CheckResult
fun MaterialDialog.onShow(callback: DialogCallback): MaterialDialog {
  this.showListeners.add(callback)
  if (this.isShowing) {
    // Already showing, invoke now
    this.showListeners.invokeAll(this)
  }
  if (this.showListeners.isNotEmpty()) {
    // Already set the base show listener
    return this
  }
  setOnShowListener { this.showListeners.invokeAll(this) }
  return this
}

/**
 * Sets a listener that's invoked when the dialog is [dismiss]'d. If this is called
 * multiple times, it appends additional callbacks, rather than overwriting.
 */
@CheckResult
fun MaterialDialog.onDismiss(callback: DialogCallback): MaterialDialog {
  this.dismissListeners.add(callback)
  if (this.dismissListeners.isNotEmpty()) {
    // Already set the base dismiss listener
    return this
  }
  setOnDismissListener { dismissListeners.invokeAll(this) }
  return this
}

/**
 * Sets a listener that's invoked when the dialog is [cancel]'d. If this is called
 * multiple times, it appends additional callbacks, rather than overwriting.
 */
@CheckResult
fun MaterialDialog.onCancel(callback: DialogCallback): MaterialDialog {
  this.cancelListeners.add(callback)
  if (this.cancelListeners.isNotEmpty()) {
    // Already set the base cancel listener
    return this
  }
  setOnCancelListener { cancelListeners.invokeAll(this) }
  return this
}

internal fun MutableList<DialogCallback>.invokeAll(dialog: MaterialDialog) {
  for (callback in this) {
    callback.invoke(dialog)
  }
}