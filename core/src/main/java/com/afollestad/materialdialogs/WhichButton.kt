package com.afollestad.materialdialogs

import com.afollestad.materialdialogs.internal.button.DialogActionButtonLayout.Companion.INDEX_NEGATIVE
import com.afollestad.materialdialogs.internal.button.DialogActionButtonLayout.Companion.INDEX_NEUTRAL
import com.afollestad.materialdialogs.internal.button.DialogActionButtonLayout.Companion.INDEX_POSITIVE

enum class WhichButton(val index: Int) {
  POSITIVE(INDEX_POSITIVE),
  NEGATIVE(INDEX_NEGATIVE),
  NEUTRAL(INDEX_NEUTRAL)
}