/*
 * Licensed under Apache-2.0
 *
 * Designed an developed by Aidan Follestad (afollestad)
 */

@file:Suppress("unused")

package com.afollestad.materialdialogs.list

import android.support.annotation.ArrayRes
import android.support.annotation.CheckResult
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.R
import com.afollestad.materialdialogs.WhichButton.POSITIVE
import com.afollestad.materialdialogs.actions.setActionButtonEnabled
import com.afollestad.materialdialogs.internal.list.MultiChoiceDialogAdapter
import com.afollestad.materialdialogs.internal.list.PlainListDialogAdapter
import com.afollestad.materialdialogs.internal.list.SingleChoiceDialogAdapter
import com.afollestad.materialdialogs.shared.getDrawable
import com.afollestad.materialdialogs.utilext.assertOneSet
import com.afollestad.materialdialogs.utilext.getStringArray
import com.afollestad.materialdialogs.utilext.inflate

@CheckResult
fun MaterialDialog.getRecyclerView(): RecyclerView? {
  return this.contentRecyclerView
}

@CheckResult
fun MaterialDialog.getListAdapter(): RecyclerView.Adapter<*>? {
  return this.contentRecyclerView?.adapter
}

/**
 * Sets a custom list adapter to render custom list content.
 *
 * Cannot be used in combination with message, input, and some other types of dialogs.
 */
@CheckResult
fun MaterialDialog.customListAdapter(
  adapter: RecyclerView.Adapter<*>
): MaterialDialog {
  addContentRecyclerView()
  if (this.contentRecyclerView!!.adapter != null)
    throw IllegalStateException("An adapter has already been set to this dialog.")
  this.contentRecyclerView!!.adapter = adapter
  return this
}

/**
 * @param arrayRes The string array resource to populate the list with.
 * @param array The literal string array resource to populate the list with.
 * @param waitForPositiveButton When true, the [selection] listener won't be called until an item
 *    is selected and the positive action button is pressed. Defaults to true if the dialog has buttons.
 * @param selection A listener invoked when an item in the list is selected.
 */
@CheckResult
fun MaterialDialog.listItems(
  @ArrayRes arrayRes: Int? = null,
  array: Array<String>? = null,
  waitForPositiveButton: Boolean = true,
  selection: ItemListener = null
): MaterialDialog {
  assertOneSet(arrayRes, array)
  val items = array ?: getStringArray(arrayRes)
  val adapter = getListAdapter()

  if (adapter is PlainListDialogAdapter) {
    adapter.replaceItems(items, selection)
    return this
  }

  return customListAdapter(
      PlainListDialogAdapter(
          dialog = this,
          items = items,
          waitForActionButton = waitForPositiveButton,
          selection = selection
      )
  )
}

/**
 * @param arrayRes The string array resource to populate the list with.
 * @param array The literal string array resource to populate the list with.
 * @param initialSelection The initially selected item's index.
 * @param waitForPositiveButton When true, the [selection] listener won't be called until
 *    the positive action button is pressed. Defaults to true if the dialog has buttons.
 * @param selection A listener invoked when an item in the list is selected.
 */
@CheckResult
fun MaterialDialog.listItemsSingleChoice(
  @ArrayRes arrayRes: Int? = null,
  array: Array<String>? = null,
  initialSelection: Int = -1,
  waitForPositiveButton: Boolean = true,
  selection: SingleChoiceListener = null
): MaterialDialog {
  assertOneSet(arrayRes, array)
  val items = array ?: getStringArray(arrayRes)
  val adapter = getListAdapter()

  if (adapter is SingleChoiceDialogAdapter) {
    adapter.replaceItems(items, selection)
    return this
  }

  setActionButtonEnabled(POSITIVE, false)
  return customListAdapter(
      SingleChoiceDialogAdapter(
          dialog = this,
          items = items,
          initialSelection = initialSelection,
          waitForActionButton = waitForPositiveButton,
          selection = selection
      )
  )
}

/**
 * @param arrayRes The string array resource to populate the list with.
 * @param array The literal string array resource to populate the list with.
 * @param initialSelection The initially selected item indices.
 * @param waitForPositiveButton When true, the [selection] listener won't be called until
 *    the positive action button is pressed.
 * @param selection A listener invoked when an item in the list is selected.
 */
@CheckResult
fun MaterialDialog.listItemsMultiChoice(
  @ArrayRes arrayRes: Int? = null,
  array: Array<String>? = null,
  initialSelection: Array<Int> = emptyArray(),
  waitForPositiveButton: Boolean = true,
  selection: MultiChoiceListener = null
): MaterialDialog {
  assertOneSet(arrayRes, array)
  val items = array ?: getStringArray(arrayRes)
  val adapter = getListAdapter()

  if (adapter is MultiChoiceDialogAdapter) {
    adapter.replaceItems(items, selection)
    return this
  }

  setActionButtonEnabled(POSITIVE, false)
  return customListAdapter(
      MultiChoiceDialogAdapter(
          dialog = this,
          items = items,
          initialSelection = initialSelection,
          waitForActionButton = waitForPositiveButton,
          selection = selection
      )
  )
}

internal fun MaterialDialog.getItemSelector() =
  getDrawable(
      context = context, attr = R.attr.md_item_selector
  )

private fun MaterialDialog.addContentRecyclerView() {
  if (this.contentScrollView != null || this.contentCustomView != null) {
    throw IllegalStateException(
        "Your dialog has already been setup with a different type " +
            "(e.g. with a message, input field, etc.)"
    )
  }
  if (this.contentRecyclerView != null) {
    return
  }
  this.contentRecyclerView = inflate(
      R.layout.md_dialog_stub_recyclerview, this.view
  )
  this.contentRecyclerView!!.attach(this)
  this.contentRecyclerView!!.layoutManager =
      LinearLayoutManager(windowContext)
  this.view.addView(this.contentRecyclerView, 1)
}