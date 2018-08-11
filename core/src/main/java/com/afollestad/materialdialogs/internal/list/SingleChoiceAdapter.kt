/*
 * Licensed under Apache-2.0
 *
 * Designed an developed by Aidan Follestad (afollestad)
 */

package com.afollestad.materialdialogs.internal.list

import android.support.v7.widget.AppCompatRadioButton
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.R
import com.afollestad.materialdialogs.list.SingleChoiceListener
import com.afollestad.materialdialogs.list.getItemSelector
import com.afollestad.materialdialogs.utilext.hasActionButtons
import com.afollestad.materialdialogs.utilext.inflate

/** @author Aidan Follestad (afollestad) */
internal class MDSingleChoiceViewHolder(
  itemView: View,
  adapter: MDSingleChoiceAdapter,
  dialog: MaterialDialog
) : RecyclerView.ViewHolder(itemView) {
  init {
    itemView.setOnClickListener {
      val canSelect =
        adapter.selectionChanged?.invoke(dialog, adapterPosition, adapter.items[adapterPosition])
            ?: true
      if (canSelect) {
        adapter.currentSelection = adapterPosition
      }
      if (canSelect && dialog.autoDismiss && !dialog.hasActionButtons()) {
        dialog.dismiss()
      }
    }
  }

  val controlView: AppCompatRadioButton = itemView.findViewById(R.id.md_control)
  val titleView: TextView = itemView.findViewById(R.id.md_title)
}

/**
 * The default list adapter for single choice (radio button) list dialogs.
 *
 * @author Aidan Follestad (afollestad)
 */
internal class MDSingleChoiceAdapter(
  private var dialog: MaterialDialog,
  internal var items: Array<CharSequence>,
  initialSelection: Int,
  internal var selectionChanged: SingleChoiceListener
) : RecyclerView.Adapter<MDSingleChoiceViewHolder>() {

  var currentSelection: Int = initialSelection
    set(value) {
      val previousSelection = field
      field = value
      notifyItemChanged(previousSelection)
      notifyItemChanged(value)
    }

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): MDSingleChoiceViewHolder {
    val listItemView: View = parent.inflate(dialog.context, R.layout.md_listitem_singlechoice)
    return MDSingleChoiceViewHolder(
        listItemView, this, dialog
    )
  }

  override fun getItemCount(): Int {
    return items.size
  }

  override fun onBindViewHolder(
    holder: MDSingleChoiceViewHolder,
    position: Int
  ) {
    holder.controlView.isChecked = currentSelection == position
    holder.titleView.text = items[position]
    holder.itemView.background = dialog.getItemSelector()
  }
}