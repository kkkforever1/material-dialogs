/*
 * Licensed under Apache-2.0
 *
 * Designed an developed by Aidan Follestad (afollestad)
 */

package com.afollestad.materialdialogs.internal.list

import android.support.v7.widget.AppCompatCheckBox
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.TextView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.R
import com.afollestad.materialdialogs.list.MultiChoiceListener
import com.afollestad.materialdialogs.list.getItemSelector
import com.afollestad.materialdialogs.shared.inflate
import com.afollestad.materialdialogs.utilext.hasActionButtons
import com.afollestad.materialdialogs.utilext.pullIndices

/** @author Aidan Follestad (afollestad) */
internal class MultiChoiceViewHolder(
  itemView: View,
  private val adapter: MultiChoiceDialogAdapter,
  private val dialog: MaterialDialog,
  private val waitForActionButton: Boolean
) : RecyclerView.ViewHolder(itemView), OnClickListener {
  init {
    itemView.setOnClickListener(this)
  }

  val controlView: AppCompatCheckBox = itemView.findViewById(R.id.md_control)
  val titleView: TextView = itemView.findViewById(R.id.md_title)

  override fun onClick(view: View) {
    val newSelection = adapter.currentSelection.toMutableList()
    if (newSelection.contains(adapterPosition)) {
      newSelection.remove(adapterPosition)
    } else {
      newSelection.add(adapterPosition)
    }
    adapter.currentSelection = newSelection.toTypedArray()

    if (waitForActionButton && dialog.hasActionButtons()) {
      // Wait for action button, don't call listener
      // so that positive action button press can do so later.
    } else {
      // Don't wait for action button, call listener and dismiss if auto dismiss is applicable
      val selectedItems = adapter.items.pullIndices(adapter.currentSelection)
      adapter.selection?.invoke(dialog, adapter.currentSelection, selectedItems)
      if (dialog.autoDismiss && !dialog.hasActionButtons()) {
        dialog.dismiss()
      }
    }
  }
}

/**
 * The default list adapter for multiple choice (checkbox) list dialogs.
 *
 * @author Aidan Follestad (afollestad)
 */
internal class MultiChoiceDialogAdapter(
  private var dialog: MaterialDialog,
  internal var items: Array<String>,
  initialSelection: Array<Int>,
  private val waitForActionButton: Boolean,
  internal var selection: MultiChoiceListener
) : RecyclerView.Adapter<MultiChoiceViewHolder>(), DialogAdapter<String, MultiChoiceListener> {

  var currentSelection: Array<Int> = initialSelection
    set(value) {
      val previousSelection = field
      field = value
      for (previous in previousSelection) {
        if (!value.contains(previous)) {
          // This value was unselected
          notifyItemChanged(previous)
        }
      }
      for (current in value) {
        if (!previousSelection.contains(current)) {
          // This value was selected
          notifyItemChanged(current)
        }
      }
    }

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): MultiChoiceViewHolder {
    val listItemView: View = parent.inflate(dialog.windowContext, R.layout.md_listitem_multichoice)
    return MultiChoiceViewHolder(
        itemView = listItemView,
        adapter = this,
        dialog = dialog,
        waitForActionButton = waitForActionButton
    )
  }

  override fun getItemCount() = items.size

  override fun onBindViewHolder(
    holder: MultiChoiceViewHolder,
    position: Int
  ) {
    holder.controlView.isChecked = currentSelection.contains(position)
    holder.titleView.text = items[position]
    holder.itemView.background = dialog.getItemSelector()
  }

  override fun positiveButtonClicked() {
    if (currentSelection.isNotEmpty()) {
      val selectedItems = items.pullIndices(currentSelection)
      selection?.invoke(dialog, currentSelection, selectedItems)
    }
  }

  override fun replaceItems(
    items: Array<String>,
    listener: MultiChoiceListener
  ) {
    this.items = items
    this.selection = listener
    this.notifyDataSetChanged()
  }
}