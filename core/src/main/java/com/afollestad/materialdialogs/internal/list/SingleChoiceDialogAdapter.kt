/*
 * Licensed under Apache-2.0
 *
 * Designed an developed by Aidan Follestad (afollestad)
 */

package com.afollestad.materialdialogs.internal.list

import android.support.v7.widget.AppCompatRadioButton
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.TextView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.R
import com.afollestad.materialdialogs.autoDismiss
import com.afollestad.materialdialogs.list.SingleChoiceListener
import com.afollestad.materialdialogs.list.getItemSelector
import com.afollestad.materialdialogs.utilext.hasActionButtons
import com.afollestad.materialdialogs.utilext.inflate

/** @author Aidan Follestad (afollestad) */
internal class SingleChoiceViewHolder(
  itemView: View,
  private val adapter: SingleChoiceDialogAdapter,
  private val dialog: MaterialDialog,
  private val waitForActionButton: Boolean
) : RecyclerView.ViewHolder(itemView), OnClickListener {

  init {
    itemView.setOnClickListener(this)
  }

  val controlView: AppCompatRadioButton = itemView.findViewById(R.id.md_control)
  val titleView: TextView = itemView.findViewById(R.id.md_title)

  override fun onClick(view: View) {
    adapter.currentSelection = adapterPosition
    if (waitForActionButton && dialog.hasActionButtons()) {
      // Wait for action button, don't call listener
      // so that positive action button press can do so later.
    } else {
      // Don't wait for action button, call listener and dismiss if auto dismiss is applicable
      adapter.selection?.invoke(dialog, adapterPosition, adapter.items[adapterPosition])
      if (dialog.autoDismiss() && !dialog.hasActionButtons()) {
        dialog.dismiss()
      }
    }
  }
}

/**
 * The default list adapter for single choice (radio button) list dialogs.
 *
 * @author Aidan Follestad (afollestad)
 */
internal class SingleChoiceDialogAdapter(
  private var dialog: MaterialDialog,
  internal var items: Array<String>,
  initialSelection: Int,
  private val waitForActionButton: Boolean,
  internal var selection: SingleChoiceListener
) : RecyclerView.Adapter<SingleChoiceViewHolder>(), DialogAdapter<String, SingleChoiceListener> {

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
  ): SingleChoiceViewHolder {
    val listItemView: View = parent.inflate(dialog.windowContext, R.layout.md_listitem_singlechoice)
    return SingleChoiceViewHolder(
        itemView = listItemView,
        adapter = this,
        dialog = dialog,
        waitForActionButton = waitForActionButton
    )
  }

  override fun getItemCount(): Int {
    return items.size
  }

  override fun onBindViewHolder(
    holder: SingleChoiceViewHolder,
    position: Int
  ) {
    holder.controlView.isChecked = currentSelection == position
    holder.titleView.text = items[position]
    holder.itemView.background = dialog.getItemSelector()
  }

  override fun positiveButtonClicked() {
    if (currentSelection > -1) {
      selection?.invoke(dialog, currentSelection, items[currentSelection])
    }
  }

  override fun replaceItems(
    items: Array<String>,
    listener: SingleChoiceListener
  ) {
    this.items = items
    this.selection = listener
    this.notifyDataSetChanged()
  }
}