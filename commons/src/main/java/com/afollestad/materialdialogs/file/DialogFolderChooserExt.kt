/*
 * Licensed under Apache-2.0
 *
 * Designed an developed by Aidan Follestad (afollestad)
 */

package com.afollestad.materialdialogs.file

import android.annotation.SuppressLint
import android.os.Environment
import android.os.Environment.getExternalStorageDirectory
import android.support.annotation.CheckResult
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItems
import com.afollestad.materialdialogs.utilext.betterParent
import com.afollestad.materialdialogs.utilext.friendlyName
import com.afollestad.materialdialogs.utilext.hasParent
import com.afollestad.materialdialogs.utilext.hasReadStoragePermission
import com.afollestad.materialdialogs.utilext.jumpOverEmulated
import java.io.File

private const val CONFIG_CURRENT_FOLDER = "current_folder"

@CheckResult
fun MaterialDialog.folderChooser(
  initialDirectory: File = getExternalStorageDirectory(),
  filter: FileFilter = null,
  selection: FileCallback = null
): MaterialDialog {
  if (!hasReadStoragePermission()) {
    throw IllegalStateException("You must have the READ_EXTERNAL_STORAGE permission first.")
  }
  loadFolders(initialDirectory, filter, selection)
  return this
}

@SuppressLint("CheckResult")
private fun MaterialDialog.loadFolders(
  folder: File,
  filter: FileFilter,
  selection: FileCallback = null
) {
  this.config[CONFIG_CURRENT_FOLDER] = folder

  val rawContents = folder.listFiles() ?: emptyArray()
  val contents = rawContents
      .filter { it.isDirectory && filter?.invoke(it) ?: true }
      .sortedBy { it.nameWithoutExtension }

  val contentsWithUp: Array<String> = if (folder.hasParent()) {
    arrayOf("...") + contents.map { it.name }
  } else {
    contents.map { it.name }
        .toTypedArray()
  }

  title(text = folder.friendlyName())
  listItems(array = contentsWithUp) { _, i, _ ->
    val currentFolder = this.config[CONFIG_CURRENT_FOLDER] as File
    if (currentFolder.hasParent() && i == 0) {
      // go up
      loadFolders(currentFolder.betterParent()!!, filter, selection)
      return@listItems
    }

    val actualIndex = if (currentFolder.hasParent()) i - 1 else i
    val selected = contents[actualIndex].jumpOverEmulated()
    if (selected.isDirectory) {
      loadFolders(selected, filter, selection)
    } else {
      selection?.invoke(selected)
      dismiss()
    }
  }
}