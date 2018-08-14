package com.afollestad.materialdialogs.internal.list

interface DialogAdapter<IT, SL> {

  fun replaceItems(
    items: Array<IT>,
    listener: SL
  )

  fun positiveButtonClicked()
}