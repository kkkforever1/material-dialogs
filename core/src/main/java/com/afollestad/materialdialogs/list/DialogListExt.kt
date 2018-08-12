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
import com.afollestad.materialdialogs.internal.list.MDListAdapter
import com.afollestad.materialdialogs.internal.list.MDMultiChoiceAdapter
import com.afollestad.materialdialogs.internal.list.MDSingleChoiceAdapter
import com.afollestad.materialdialogs.utilext.assertOneSet
import com.afollestad.materialdialogs.utilext.getDrawable
import com.afollestad.materialdialogs.utilext.getStringArray
import com.afollestad.materialdialogs.utilext.inflate

private fun MaterialDialog.addContentRecyclerView() {
  if (this.contentScrollView != null || this.textInputLayout != null) {
    throw IllegalStateException(
        "Your dialog has already been setup with a different type " +
            "(e.g. with a message, input field, etc.)"
    )
  }
  if (this.contentRecyclerView != null) {
    return
  }
  this.contentRecyclerView = inflate(
      windowContext, R.layout.md_dialog_stub_recyclerview, this.view
  )
  this.contentRecyclerView!!.rootView = this.view
  this.contentRecyclerView!!.layoutManager =
      LinearLayoutManager(context)
  this.view.addView(this.contentRecyclerView, 1)
}

internal fun MaterialDialog.getItemSelector() =
  getDrawable(
      context = context, attr = R.attr.md_item_selector
  )

@CheckResult
fun MaterialDialog.getRecyclerView(): RecyclerView? {
  return this.contentRecyclerView
}

@CheckResult
fun MaterialDialog.getListAdapter(): RecyclerView.Adapter<*>? {
  return this.contentRecyclerView?.adapter
}

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

@CheckResult
fun MaterialDialog.listItems(
  @ArrayRes arrayRes: Int? = null,
  array: Array<String>? = null,
  waitForActionButton: Boolean = true,
  selection: ItemListener = null
): MaterialDialog {
  assertOneSet(arrayRes, array)
  val items = array ?: getStringArray(arrayRes)

  if (this.contentRecyclerView != null &&
      this.contentRecyclerView!!.adapter != null &&
      this.contentRecyclerView!!.adapter is MDListAdapter
  ) {
    val adapter = this.contentRecyclerView!!.adapter as MDListAdapter
    adapter.replaceItems(items, selection)
    return this
  }

  return customListAdapter(
      MDListAdapter(
          dialog = this,
          items = items,
          waitForActionButton = waitForActionButton,
          selection = selection
      )
  )
}

@CheckResult
fun MaterialDialog.listItemsSingleChoice(
  @ArrayRes arrayRes: Int? = null,
  array: Array<String>? = null,
  initialSelection: Int = -1,
  waitForActionButton: Boolean = true,
  selectionChanged: SingleChoiceListener = null
): MaterialDialog {
  assertOneSet(arrayRes, array)
  val items = array ?: getStringArray(arrayRes)

  if (this.contentRecyclerView != null &&
      this.contentRecyclerView!!.adapter != null &&
      this.contentRecyclerView!!.adapter is MDSingleChoiceAdapter
  ) {
    val adapter = this.contentRecyclerView!!.adapter as MDSingleChoiceAdapter
    adapter.replaceItems(items, selectionChanged)
    return this
  }

  return customListAdapter(
      MDSingleChoiceAdapter(
          dialog = this,
          items = items,
          initialSelection = initialSelection,
          waitForActionButton = waitForActionButton,
          selection = selectionChanged
      )
  )
}

@CheckResult
fun MaterialDialog.listItemsMultiChoice(
  @ArrayRes arrayRes: Int? = null,
  array: Array<String>? = null,
  initialSelection: Array<Int> = emptyArray(),
  waitForActionButton: Boolean = true,
  selectionChanged: MultiChoiceListener = null
): MaterialDialog {
  assertOneSet(arrayRes, array)
  val items = array ?: getStringArray(arrayRes)

  if (this.contentRecyclerView != null &&
      this.contentRecyclerView!!.adapter != null &&
      this.contentRecyclerView!!.adapter is MDMultiChoiceAdapter
  ) {
    val adapter = this.contentRecyclerView!!.adapter as MDMultiChoiceAdapter
    adapter.replaceItems(items, selectionChanged)
    return this
  }

  return customListAdapter(
      MDMultiChoiceAdapter(
          dialog = this,
          items = items,
          initialSelection = initialSelection,
          waitForActionButton = waitForActionButton,
          selection = selectionChanged
      )
  )
}