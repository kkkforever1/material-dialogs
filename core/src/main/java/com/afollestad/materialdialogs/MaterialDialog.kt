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
import com.afollestad.materialdialogs.internal.button.DialogActionButtonLayout.Companion.INDEX_NEGATIVE
import com.afollestad.materialdialogs.internal.button.DialogActionButtonLayout.Companion.INDEX_NEUTRAL
import com.afollestad.materialdialogs.internal.button.DialogActionButtonLayout.Companion.INDEX_POSITIVE
import com.afollestad.materialdialogs.internal.list.DialogRecyclerView
import com.afollestad.materialdialogs.internal.main.DialogLayout
import com.afollestad.materialdialogs.internal.main.DialogScrollView
import com.afollestad.materialdialogs.utilext.addContentScrollView
import com.afollestad.materialdialogs.utilext.assertOneSet
import com.afollestad.materialdialogs.utilext.getString
import com.afollestad.materialdialogs.utilext.inflate
import com.afollestad.materialdialogs.utilext.preShow
import com.afollestad.materialdialogs.utilext.setDefaults
import com.afollestad.materialdialogs.utilext.setIcon
import com.afollestad.materialdialogs.utilext.setText
import com.afollestad.materialdialogs.utilext.setWindowConstraints

typealias DialogCallback = (MaterialDialog) -> Unit

internal const val CONFIG_AUTO_DISMISS = "auto_dismiss"
internal fun MaterialDialog.autoDismiss() = config[CONFIG_AUTO_DISMISS] as Boolean

/** @author Aidan Follestad (afollestad) */
class MaterialDialog(
  val appContext: Context
) : Dialog(appContext, Theme.inferTheme(appContext).styleRes) {

  val config: MutableMap<String, Any> = mutableMapOf(CONFIG_AUTO_DISMISS to true)

  internal val view: DialogLayout = inflate(context, R.layout.md_dialog_base)
  internal var textViewMessage: TextView? = null
  internal var textInputLayout: TextInputLayout? = null
  internal var contentScrollView: DialogScrollView? = null
  internal var contentScrollViewFrame: LinearLayout? = null
  internal var contentRecyclerView: DialogRecyclerView? = null
  internal var contentCustomView: View? = null

  init {
    setContentView(view)
    this.view.dialog = this
    setWindowConstraints()
    setDefaults()
  }

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

  @CheckResult
  fun title(
    @StringRes textRes: Int? = null,
    text: CharSequence? = null
  ): MaterialDialog {
    assertOneSet(textRes, text)
    setText(
        view.titleLayout.titleView,
        textRes = textRes,
        text = text
    )
    return this
  }

  @CheckResult
  fun message(
    @StringRes textRes: Int? = null,
    text: CharSequence? = null
  ): MaterialDialog {
    addContentScrollView()
    addContentMessageView(textRes, text)
    return this
  }

  @CheckResult
  fun positiveButton(
    @StringRes positiveRes: Int? = null,
    positiveText: CharSequence? = null,
    click: ((MaterialDialog) -> (Unit))? = null
  ): MaterialDialog {
    setText(
        view.buttonsLayout.actionButtons[INDEX_POSITIVE],
        textRes = positiveRes,
        text = positiveText,
        fallback = android.R.string.ok,
        click = click
    )
    return this
  }

  @CheckResult
  fun negativeButton(
    @StringRes negativeRes: Int? = null,
    negativeText: CharSequence? = null,
    click: ((MaterialDialog) -> (Unit))? = null
  ): MaterialDialog {
    setText(
        view.buttonsLayout.actionButtons[INDEX_NEGATIVE],
        textRes = negativeRes,
        text = negativeText,
        fallback = android.R.string.cancel,
        click = click
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
    click: ((MaterialDialog) -> (Unit))? = null
  ): MaterialDialog {
    assertOneSet(neutralRes, neutralText)
    setText(
        view.buttonsLayout.actionButtons[INDEX_NEUTRAL],
        textRes = neutralRes,
        text = neutralText,
        click = click
    )
    return this
  }

  @CheckResult
  fun noAutoDismiss(): MaterialDialog {
    this.config[CONFIG_AUTO_DISMISS] = false
    return this
  }

  @CheckResult
  fun debugMode(debugMode: Boolean = true): MaterialDialog {
    this.view.debugMode = debugMode
    return this
  }

  fun setActionButtonEnabled(
    which: WhichButton,
    enabled: Boolean
  ): MaterialDialog {
    view.buttonsLayout.actionButtons[which.index].isEnabled = enabled
    return this
  }

  @CheckResult
  inline fun onShow(crossinline callback: DialogCallback): MaterialDialog {
    setOnShowListener { callback.invoke(this@MaterialDialog) }
    return this
  }

  @CheckResult
  inline fun onDismiss(crossinline callback: DialogCallback): MaterialDialog {
    setOnDismissListener { callback.invoke(this@MaterialDialog) }
    return this
  }

  @CheckResult
  inline fun onCancel(crossinline callback: DialogCallback): MaterialDialog {
    setOnCancelListener { callback.invoke(this@MaterialDialog) }
    return this
  }

  override fun show() {
    preShow()
    super.show()
  }

  inline fun show(func: MaterialDialog.() -> Unit): MaterialDialog {
    this.func()
    this.show()
    return this
  }

  private fun addContentMessageView(@StringRes res: Int?, text: CharSequence?) {
    if (this.textViewMessage == null) {
      this.textViewMessage = inflate(
          context,
          R.layout.md_dialog_stub_message,
          this.contentScrollViewFrame!!
      )
      this.contentScrollViewFrame!!.addView(this.textViewMessage)
    }
    assertOneSet(res, text)
    this.textViewMessage!!.text = text ?: getString(res)
  }
}