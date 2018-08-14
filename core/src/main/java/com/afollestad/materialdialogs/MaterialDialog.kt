/*
 * Licensed under Apache-2.0
 *
 * Designed an developed by Aidan Follestad (afollestad)
 */

@file:Suppress("unused")

package com.afollestad.materialdialogs

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.Drawable
import android.support.annotation.CheckResult
import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import android.support.design.widget.TextInputLayout
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.afollestad.materialdialogs.WhichButton.NEGATIVE
import com.afollestad.materialdialogs.WhichButton.POSITIVE
import com.afollestad.materialdialogs.internal.button.DialogActionButtonLayout.Companion.INDEX_NEGATIVE
import com.afollestad.materialdialogs.internal.button.DialogActionButtonLayout.Companion.INDEX_NEUTRAL
import com.afollestad.materialdialogs.internal.button.DialogActionButtonLayout.Companion.INDEX_POSITIVE
import com.afollestad.materialdialogs.internal.list.DialogAdapter
import com.afollestad.materialdialogs.internal.list.DialogRecyclerView
import com.afollestad.materialdialogs.internal.main.DialogLayout
import com.afollestad.materialdialogs.internal.main.DialogScrollView
import com.afollestad.materialdialogs.list.getListAdapter
import com.afollestad.materialdialogs.utilext.addContentScrollView
import com.afollestad.materialdialogs.utilext.assertOneSet
import com.afollestad.materialdialogs.utilext.getString
import com.afollestad.materialdialogs.utilext.hideKeyboard
import com.afollestad.materialdialogs.utilext.inflate
import com.afollestad.materialdialogs.utilext.preShow
import com.afollestad.materialdialogs.utilext.setDefaults
import com.afollestad.materialdialogs.utilext.setIcon
import com.afollestad.materialdialogs.utilext.setText
import com.afollestad.materialdialogs.utilext.setWindowConstraints
import com.afollestad.materialdialogs.utilext.showKeyboardIfApplicable

typealias DialogCallback = (MaterialDialog) -> Unit

/** @author Aidan Follestad (afollestad) */
class MaterialDialog(
  val windowContext: Context
) : Dialog(windowContext, inferTheme(windowContext).styleRes) {

  /** A named config map, used like tags for extensions. */
  val config: MutableMap<String, Any> = mutableMapOf()

  internal var autoDismiss: Boolean = true

  internal val view: DialogLayout = inflate(R.layout.md_dialog_base)
  internal var textViewMessage: TextView? = null
  internal var textInputLayout: TextInputLayout? = null
  internal var contentScrollView: DialogScrollView? = null
  internal var contentScrollViewFrame: LinearLayout? = null
  internal var contentRecyclerView: DialogRecyclerView? = null
  internal var contentCustomView: View? = null

  private val positiveListeners = mutableListOf<DialogCallback>()
  private val negativeListeners = mutableListOf<DialogCallback>()
  private val neutralListeners = mutableListOf<DialogCallback>()

  init {
    setContentView(view)
    this.view.dialog = this
    setWindowConstraints()
    setDefaults()
  }

  /**
   * Shows an icon to the left of the dialog title.
   *
   * @param iconRes The drawable resource to display as the icon.
   * @param icon The drawable to display as the icon.
   */
  @CheckResult
  fun icon(
    @DrawableRes iconRes: Int? = null,
    icon: Drawable? = null
  ): MaterialDialog {
    assertOneSet(iconRes, icon)
    setIcon(
        view.titleLayout.iconView,
        iconRes = iconRes,
        icon = icon
    )
    return this
  }

  /**
   * Shows a title, or header, at the top of the dialog.
   *
   * @param textRes The string resource to display as the title.
   * @param text The literal string to display as the title.
   */
  @CheckResult
  fun title(
    @StringRes textRes: Int? = null,
    text: String? = null
  ): MaterialDialog {
    assertOneSet(textRes, text)
    setText(
        view.titleLayout.titleView,
        textRes = textRes,
        text = text
    )
    return this
  }

  /**
   * Shows a message, below the title, and above the action buttons (and checkbox prompt).
   *
   * @param textRes The string resource to display as the message.
   * @param text The literal string to display as the message.
   */
  @CheckResult
  fun message(
    @StringRes textRes: Int? = null,
    text: CharSequence? = null
  ): MaterialDialog {
    addContentScrollView()
    addContentMessageView(textRes, text)
    return this
  }

  /**
   * Shows a positive action button, in the far right at the bottom of the dialog.
   *
   * @param positiveRes The string resource to display on the title.
   * @param positiveText The literal string to display on the button.
   * @param click A listener to invoke when the button is pressed.
   */
  @CheckResult
  fun positiveButton(
    @StringRes positiveRes: Int? = null,
    positiveText: CharSequence? = null,
    click: DialogCallback? = null
  ): MaterialDialog {
    if (click != null) {
      positiveListeners.add(click)
    }

    val btn = view.buttonsLayout.actionButtons[INDEX_POSITIVE]
    if (positiveRes == null && positiveText == null) {
      // Didn't receive text, so just stop with the added listener.
      return this
    }

    setText(
        btn,
        textRes = positiveRes,
        text = positiveText,
        fallback = android.R.string.ok
    )
    return this
  }

  /**
   * Shows a negative action button, to the left of the positive action button (or at the far
   * right if there is no positive action button).
   *
   * @param negativeRes The string resource to display on the title.
   * @param negativeText The literal string to display on the button.
   * @param click A listener to invoke when the button is pressed.
   */
  @CheckResult
  fun negativeButton(
    @StringRes negativeRes: Int? = null,
    negativeText: CharSequence? = null,
    click: DialogCallback? = null
  ): MaterialDialog {
    if (click != null) {
      negativeListeners.add(click)
    }

    val btn = view.buttonsLayout.actionButtons[INDEX_NEGATIVE]
    if (negativeRes == null && negativeText == null) {
      // Didn't receive text, so just stop with the added listener.
      return this
    }

    setText(
        btn,
        textRes = negativeRes,
        text = negativeText,
        fallback = android.R.string.cancel
    )
    return this
  }

  @CheckResult
  @Deprecated(
      "Use of neutral buttons is discouraged, see " +
          "https://material.io/design/components/dialogs.html#actions."
  )
  fun neutralButton(
    @StringRes neutralRes: Int? = null,
    neutralText: CharSequence? = null,
    click: DialogCallback? = null
  ): MaterialDialog {
    if (click != null) {
      neutralListeners.add(click)
    }

    val btn = view.buttonsLayout.actionButtons[INDEX_NEUTRAL]
    if (neutralRes == null && neutralText == null) {
      // Didn't receive text, so just stop with the added listener.
      return this
    }

    setText(
        btn,
        textRes = neutralRes,
        text = neutralText
    )
    return this
  }

  /**
   * Turns off auto dismiss. Action button and list item clicks won't dismiss the dialog on their
   * own. You have to handle dismissing the dialog manually with the [dismiss] method.
   */
  @CheckResult
  fun noAutoDismiss(): MaterialDialog {
    this.autoDismiss = false
    return this
  }

  /** Turns debug mode on or off. Draws spec guides over dialog views. */
  @CheckResult
  fun debugMode(debugMode: Boolean = true): MaterialDialog {
    this.view.debugMode = debugMode
    return this
  }

  /** Enables or disables an action button. */
  fun setActionButtonEnabled(
    which: WhichButton,
    enabled: Boolean
  ): MaterialDialog {
    view.buttonsLayout.actionButtons[which.index].isEnabled = enabled
    return this
  }

  /** Sets a listener that's invoked when the dialog is [show]'n. */
  @CheckResult
  inline fun onShow(crossinline callback: DialogCallback): MaterialDialog {
    setOnShowListener { callback.invoke(this@MaterialDialog) }
    return this
  }

  /** Sets a listener that's invoked when the dialog is [dismiss]'d. */
  @CheckResult
  inline fun onDismiss(crossinline callback: DialogCallback): MaterialDialog {
    setOnDismissListener { callback.invoke(this@MaterialDialog) }
    return this
  }

  /** Sets a listener that's invoked when the dialog is [cancel]'d. */
  @CheckResult
  inline fun onCancel(crossinline callback: DialogCallback): MaterialDialog {
    setOnCancelListener { callback.invoke(this@MaterialDialog) }
    return this
  }

  /** Opens the dialog. */
  override fun show() {
    preShow()
    super.show()
    showKeyboardIfApplicable()
  }

  /** Applies multiple properties to the dialog and opens it. */
  inline fun show(func: MaterialDialog.() -> Unit): MaterialDialog {
    this.func()
    this.show()
    return this
  }

  override fun dismiss() {
    hideKeyboard()
    super.dismiss()
  }

  fun invalidateDividers(
    scrolledDown: Boolean,
    atBottom: Boolean
  ) = view.invalidateDividers(scrolledDown, atBottom)

  internal fun onActionButtonClicked(which: WhichButton) {
    @Suppress("NON_EXHAUSTIVE_WHEN")
    when (which) {
      POSITIVE -> {
        positiveListeners.invokeAll(this)
        val adapter = getListAdapter() as? DialogAdapter<*, *>
        adapter?.positiveButtonClicked()
      }
      NEGATIVE -> negativeListeners.invokeAll(this)
    }
    if (autoDismiss) {
      dismiss()
    }
  }

  private fun addContentMessageView(@StringRes res: Int?, text: CharSequence?) {
    if (this.textViewMessage == null) {
      this.textViewMessage = inflate(
          R.layout.md_dialog_stub_message,
          this.contentScrollViewFrame!!
      )
      this.contentScrollViewFrame!!.addView(this.textViewMessage)
    }
    assertOneSet(res, text)
    this.textViewMessage!!.text = text ?: getString(res)
  }
}

private fun MutableList<DialogCallback>.invokeAll(dialog: MaterialDialog) {
  for (callback in this) {
    callback.invoke(dialog)
  }
}