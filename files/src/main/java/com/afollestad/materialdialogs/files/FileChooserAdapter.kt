/*
 * Licensed under Apache-2.0
 *
 * Designed an developed by Aidan Follestad (afollestad)
 */

package com.afollestad.materialdialogs.files

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.WhichButton.POSITIVE
import com.afollestad.materialdialogs.shared.getColor
import com.afollestad.materialdialogs.shared.getDrawable
import com.afollestad.materialdialogs.shared.isColorDark
import com.afollestad.materialdialogs.shared.setVisible
import java.io.File

internal class FileChooserViewHolder(
  itemView: View,
  private val adapter: FileChooserAdapter
) : RecyclerView.ViewHolder(itemView), OnClickListener {

  init {
    itemView.setOnClickListener(this)
  }

  val iconView: ImageView = itemView.findViewById(R.id.icon)
  val nameView: TextView = itemView.findViewById(R.id.name)

  override fun onClick(view: View) = adapter.itemClicked(adapterPosition)
}

/** @author Aidan Follestad (afollestad */
internal class FileChooserAdapter(
  private val dialog: MaterialDialog,
  initialFolder: File,
  private val listView: RecyclerView,
  private val emptyView: TextView,
  private val onlyFolders: Boolean,
  private val filter: FileFilter,
  private val callback: FileCallback
) : RecyclerView.Adapter<FileChooserViewHolder>() {

  var selectedFile: File? = null

  private var currentFolder = initialFolder
  private lateinit var contents: List<File>

  private val isLightTheme =
    getColor(dialog.windowContext, attr = android.R.attr.textColorPrimary).isColorDark()

  init {
    loadContents(initialFolder)
  }

  fun itemClicked(index: Int) {
    if (currentFolder.hasParent() && index == 0) {
      // go up
      loadContents(currentFolder.betterParent()!!)
      return
    }

    val actualIndex = if (currentFolder.hasParent()) index - 1 else index
    val selected = contents[actualIndex].jumpOverEmulated()
    callback?.invoke(dialog, selected)

    if (selected.isDirectory) {
      loadContents(selected)
    } else {
      this.selectedFile = selected
      dialog.setActionButtonEnabled(POSITIVE, true)
    }
  }

  private fun loadContents(directory: File) {
    if (onlyFolders) {
      this.selectedFile = this.currentFolder
      dialog.setActionButtonEnabled(POSITIVE, true)
    }

    this.currentFolder = directory
    dialog.title(text = directory.friendlyName())

    val rawContents = directory.listFiles() ?: emptyArray()
    if (onlyFolders) {
      this.contents = rawContents
          .filter { it.isDirectory && filter?.invoke(it) ?: true }
          .sortedBy { it.name }
    } else {
      this.contents = rawContents
          .filter { filter?.invoke(it) ?: true }
          .sortedWith(compareBy(File::isDirectory, File::nameWithoutExtension))
    }

    this.emptyView.setVisible(this.contents.isEmpty())
    notifyDataSetChanged()
  }

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): FileChooserViewHolder {
    val view = LayoutInflater.from(parent.context)
        .inflate(R.layout.md_file_chooser_item, parent, false)
    view.background = getDrawable(dialog.context, attr = R.attr.md_item_selector)
    return FileChooserViewHolder(view, this)
  }

  override fun getItemCount(): Int {
    if (currentFolder.hasParent()) {
      return contents.size + 1
    }
    return contents.size
  }

  override fun onBindViewHolder(
    holder: FileChooserViewHolder,
    position: Int
  ) {
    if (currentFolder.hasParent() && position == 0) {
      holder.iconView.setImageResource(
          if (isLightTheme) R.drawable.icon_return_dark
          else R.drawable.icon_return_light
      )
      holder.nameView.text = "..."
      holder.itemView.isActivated = false
      return
    }

    val actualIndex = if (currentFolder.hasParent()) position - 1 else position
    val item = contents[actualIndex]
    holder.iconView.setImageResource(item.iconRes())
    holder.nameView.text = item.name
    holder.itemView.isActivated = selectedFile?.absolutePath == item.absolutePath ?: false
  }

  private fun File.iconRes(): Int {
    return if (isLightTheme) {
      if (this.isDirectory) R.drawable.icon_folder_dark
      else R.drawable.icon_file_dark
    } else {
      if (this.isDirectory) R.drawable.icon_folder_light
      else R.drawable.icon_file_light
    }
  }
}