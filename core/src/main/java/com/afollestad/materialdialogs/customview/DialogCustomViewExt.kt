/*
 * Licensed under Apache-2.0
 *
 * Designed an developed by Aidan Follestad (afollestad)
 */

package com.afollestad.materialdialogs.customview

import android.support.annotation.CheckResult
import android.support.annotation.LayoutRes
import android.view.View
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.utilext.addContentScrollView
import com.afollestad.materialdialogs.utilext.assertOneSet
import com.afollestad.materialdialogs.utilext.inflate

@CheckResult
fun MaterialDialog.getCustomView(): View? {
  return contentCustomView
}

/**
 * Sets a custom view to display in the dialog, below the title and above the action buttons
 * (and checkbox prompt).
 *
 * @param viewRes The layout resource to inflate as the custom view.
 * @param view The view to insert as the custom view.
 * @param scrollable Whether or not the custom view is automatically wrapped in a ScrollView.
 */
@CheckResult
fun MaterialDialog.customView(
  @LayoutRes viewRes: Int? = null,
  view: View? = null,
  scrollable: Boolean = false
): MaterialDialog {
  if ((!scrollable && this.contentScrollView != null) ||
      this.contentRecyclerView != null ||
      this.textInputLayout != null
  ) {
    throw IllegalStateException(
        "This dialog has already been setup with another type " +
            "(e.g. list, message, input, etc.)"
    )
  }
  assertOneSet(viewRes, view)
  if (scrollable) {
    addContentScrollView()
    this.contentCustomView = view ?: inflate(context, viewRes!!, this.contentScrollViewFrame!!)
    this.contentScrollViewFrame!!.addView(this.contentCustomView)
  } else {
    this.contentCustomView = view ?: inflate(context, viewRes!!, this.view)
    this.view.addView(this.contentCustomView, 1)
  }
  return this
}