/*
 * Licensed under Apache-2.0
 *
 * Designed an developed by Aidan Follestad (afollestad)
 */

package com.afollestad.materialdialogs.internal.list

import android.support.v7.widget.AppCompatCheckBox
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.R
import com.afollestad.materialdialogs.list.MultiChoiceListener
import com.afollestad.materialdialogs.list.getItemSelector
import com.afollestad.materialdialogs.utilext.hasActionButtons
import com.afollestad.materialdialogs.utilext.inflate

/** @author Aidan Follestad (afollestad) */
internal class MDMultiChoiceViewHolder(
  itemView: View,
  adapter: MDMultiChoiceAdapter,
  dialog: MaterialDialog
) : RecyclerView.ViewHolder(itemView) {
  init {
    itemView.setOnClickListener {
      val newSelection = adapter.currentSelection.toMutableList()
      if (newSelection.contains(adapterPosition)) {
        newSelection.remove(adapterPosition)
      } else {
        newSelection.add(adapterPosition)
      }

      val proposedSelection = newSelection.toTypedArray()
      val canSelect =
        adapter.selectionChanged?.invoke(dialog, adapter.currentSelection, adapter.items) ?: true
      if (canSelect) {
        adapter.currentSelection = proposedSelection
      }

      if (canSelect && dialog.autoDismiss && !dialog.hasActionButtons()) {
        dialog.dismiss()
      }
    }
  }

  val controlView: AppCompatCheckBox = itemView.findViewById(R.id.md_control)
  val titleView: TextView = itemView.findViewById(R.id.md_title)
}

/**
 * The default list adapter for multiple choice (checkbox) list dialogs.
 *
 * @author Aidan Follestad (afollestad)
 */
internal class MDMultiChoiceAdapter(
  private var dialog: MaterialDialog,
  internal var items: Array<CharSequence>,
  initialSelection: Array<Int>,
  internal var selectionChanged: MultiChoiceListener
) : RecyclerView.Adapter<MDMultiChoiceViewHolder>() {

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
  ): MDMultiChoiceViewHolder {
    val listItemView: View = parent.inflate(dialog.context, R.layout.md_listitem_multichoice)
    return MDMultiChoiceViewHolder(
        listItemView, this, dialog
    )
  }

  override fun getItemCount(): Int {
    return items.size
  }

  override fun onBindViewHolder(
    holder: MDMultiChoiceViewHolder,
    position: Int
  ) {
    holder.controlView.isChecked = currentSelection.contains(position)
    holder.titleView.text = items[position]
    holder.itemView.background = dialog.getItemSelector()
  }
}