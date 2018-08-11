package com.afollestad.materialdialogs.file

import android.annotation.SuppressLint
import android.os.Environment
import android.support.annotation.CheckResult
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItems
import com.afollestad.materialdialogs.utilext.betterParent
import com.afollestad.materialdialogs.utilext.friendlyName
import com.afollestad.materialdialogs.utilext.hasParent
import com.afollestad.materialdialogs.utilext.hasReadStoragePermission
import com.afollestad.materialdialogs.utilext.jumpOverEmulated
import java.io.File

typealias FileFilter = ((File) -> Boolean)?
typealias FileCallback = ((File) -> Unit)?

private const val CONFIG_CURRENT_FOLDER = "current_folder"

@CheckResult
fun MaterialDialog.fileChooser(
  initialDirectory: File = Environment.getExternalStorageDirectory(),
  filter: FileFilter = { !it.isHidden },
  selection: FileCallback = null
): MaterialDialog {
  if (!hasReadStoragePermission()) {
    throw IllegalStateException("You must have the READ_EXTERNAL_STORAGE permission first.")
  }
  loadFiles(initialDirectory, filter, selection)
  return this
}

@SuppressLint("CheckResult")
private fun MaterialDialog.loadFiles(
  folder: File,
  filter: FileFilter,
  selection: FileCallback = null
) {
  this.config[CONFIG_CURRENT_FOLDER] = folder

  val rawContents = folder.listFiles() ?: emptyArray()
  val contents = rawContents
      .filter { it.isDirectory || filter?.invoke(it) ?: true }
      .sortedWith(compareBy({ it.isDirectory }, { it.nameWithoutExtension }))

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
      loadFiles(currentFolder.betterParent()!!, filter, selection)
      return@listItems
    }

    val actualIndex = if (currentFolder.hasParent()) i - 1 else i
    val selected = contents[actualIndex].jumpOverEmulated()
    if (selected.isDirectory) {
      loadFiles(selected, filter, selection)
    } else {
      selection?.invoke(selected)
      dismiss()
    }
  }
}