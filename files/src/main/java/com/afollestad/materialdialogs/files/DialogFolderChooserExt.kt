/*
 * Licensed under Apache-2.0
 *
 * Designed an developed by Aidan Follestad (afollestad)
 */

package com.afollestad.materialdialogs.files

import android.annotation.SuppressLint
import android.os.Environment.getExternalStorageDirectory
import android.support.annotation.CheckResult
import android.support.v7.widget.LinearLayoutManager
import android.widget.TextView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.WhichButton.POSITIVE
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.afollestad.materialdialogs.internal.list.DialogRecyclerView
import java.io.File

@SuppressLint("CheckResult")
@CheckResult
fun MaterialDialog.folderChooser(
  initialDirectory: File = getExternalStorageDirectory(),
  filter: FileFilter = { !it.isHidden },
  waitForPositiveButton: Boolean = true,
  emptyTextRes: Int = R.string.files_default_empty_text,
  selection: FileCallback = null
): MaterialDialog {
  if (!hasReadStoragePermission()) {
    throw IllegalStateException("You must have the READ_EXTERNAL_STORAGE permission first.")
  }
  customView(R.layout.md_file_chooser_base)
  setActionButtonEnabled(POSITIVE, false)

  val customView = getCustomView()!!
  val list: DialogRecyclerView = customView.findViewById(R.id.list)
  val emptyText: TextView = customView.findViewById(R.id.empty_text)
  emptyText.setText(emptyTextRes)

  list.attach(this)
  list.layoutManager = LinearLayoutManager(windowContext)
  val adapter = FileChooserAdapter(
      dialog = this,
      initialFolder = initialDirectory,
      listView = list,
      emptyView = emptyText,
      onlyFolders = true,
      filter = filter,
      callback = if (waitForPositiveButton) null else selection
  )
  list.adapter = adapter

  if (waitForPositiveButton && selection != null) {
    positiveButton {
      val selectedFile = adapter.selectedFile
      if (selectedFile != null) {
        selection.invoke(this, selectedFile)
      }
    }
  }

  return this
}