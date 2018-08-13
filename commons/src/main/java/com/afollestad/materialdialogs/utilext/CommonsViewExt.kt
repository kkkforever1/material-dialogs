package com.afollestad.materialdialogs.utilext

import android.view.View
import android.view.View.GONE
import android.view.View.INVISIBLE
import android.view.View.VISIBLE

internal fun <T : View> T.setVisibleOrGone(visible: Boolean) {
  visibility = if (visible) VISIBLE else GONE
}

internal fun <T : View> T.setVisibility(visible: Boolean) {
  visibility = if (visible) VISIBLE else INVISIBLE
}