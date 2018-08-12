@file:Suppress("DEPRECATION")

package com.afollestad.materialdialogssample

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.content.SharedPreferences
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebView
import android.widget.CheckBox
import android.widget.EditText
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.checkbox.checkBoxPrompt
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.afollestad.materialdialogs.file.fileChooser
import com.afollestad.materialdialogs.file.folderChooser
import com.afollestad.materialdialogs.input.input
import com.afollestad.materialdialogs.list.listItems
import com.afollestad.materialdialogs.list.listItemsMultiChoice
import com.afollestad.materialdialogs.list.listItemsSingleChoice
import kotlinx.android.synthetic.main.activity_main.basic
import kotlinx.android.synthetic.main.activity_main.basic_buttons
import kotlinx.android.synthetic.main.activity_main.basic_checkbox_titled_buttons
import kotlinx.android.synthetic.main.activity_main.basic_icon
import kotlinx.android.synthetic.main.activity_main.basic_long
import kotlinx.android.synthetic.main.activity_main.basic_long_titled_buttons
import kotlinx.android.synthetic.main.activity_main.basic_titled
import kotlinx.android.synthetic.main.activity_main.basic_titled_buttons
import kotlinx.android.synthetic.main.activity_main.buttons_callbacks
import kotlinx.android.synthetic.main.activity_main.buttons_neutral
import kotlinx.android.synthetic.main.activity_main.buttons_stacked
import kotlinx.android.synthetic.main.activity_main.buttons_stacked_checkboxPrompt
import kotlinx.android.synthetic.main.activity_main.custom_view
import kotlinx.android.synthetic.main.activity_main.custom_view_webview
import kotlinx.android.synthetic.main.activity_main.file_chooser
import kotlinx.android.synthetic.main.activity_main.folder_chooser
import kotlinx.android.synthetic.main.activity_main.input
import kotlinx.android.synthetic.main.activity_main.input_check_prompt
import kotlinx.android.synthetic.main.activity_main.input_counter
import kotlinx.android.synthetic.main.activity_main.input_message
import kotlinx.android.synthetic.main.activity_main.list
import kotlinx.android.synthetic.main.activity_main.list_buttons
import kotlinx.android.synthetic.main.activity_main.list_checkPrompt
import kotlinx.android.synthetic.main.activity_main.list_checkPrompt_buttons
import kotlinx.android.synthetic.main.activity_main.list_long
import kotlinx.android.synthetic.main.activity_main.list_long_buttons
import kotlinx.android.synthetic.main.activity_main.list_long_items
import kotlinx.android.synthetic.main.activity_main.list_long_items_buttons
import kotlinx.android.synthetic.main.activity_main.list_long_items_titled
import kotlinx.android.synthetic.main.activity_main.list_long_items_titled_buttons
import kotlinx.android.synthetic.main.activity_main.list_long_titled
import kotlinx.android.synthetic.main.activity_main.list_long_titled_buttons
import kotlinx.android.synthetic.main.activity_main.list_titled
import kotlinx.android.synthetic.main.activity_main.list_titled_buttons
import kotlinx.android.synthetic.main.activity_main.misc_dialog_callbacks
import kotlinx.android.synthetic.main.activity_main.multiple_choice
import kotlinx.android.synthetic.main.activity_main.multiple_choice_buttons
import kotlinx.android.synthetic.main.activity_main.multiple_choice_long_items
import kotlinx.android.synthetic.main.activity_main.single_choice_buttons_titled
import kotlinx.android.synthetic.main.activity_main.single_choice_long_items
import kotlinx.android.synthetic.main.activity_main.single_choice_titled

/** @author Aidan Follestad (afollestad) */
class MainActivity : AppCompatActivity() {

  companion object {
    const val KEY_PREFS = "prefs"
    const val KEY_DARK_THEME = "dark_theme"
    const val KEY_DEBUG_MODE = "debug_mode"
    const val FILE_REQUEST_CODE = 69
    const val FOLDER_REQUEST_CODE = 69
  }

  private var debugMode = false
  private var postPermissionRunnable: Runnable? = null
  private lateinit var prefs: SharedPreferences

  override fun onCreate(savedInstanceState: Bundle?) {
    prefs = getSharedPreferences(KEY_PREFS, MODE_PRIVATE)
    setTheme(
        if (prefs.boolean(KEY_DARK_THEME)) R.style.AppTheme_Dark else R.style.AppTheme
    )
    debugMode = prefs.boolean(KEY_DEBUG_MODE, false)


    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    basic.setOnClickListener {
      MaterialDialog(this).show {
        message(R.string.shareLocationPrompt)
        debugMode(debugMode)
      }
    }

    basic_titled.setOnClickListener {
      MaterialDialog(this).show {
        title(R.string.useGoogleLocationServices)
        message(R.string.useGoogleLocationServicesPrompt)
        debugMode(debugMode)
      }
    }

    basic_buttons.setOnClickListener {
      MaterialDialog(this).show {
        message(R.string.useGoogleLocationServicesPrompt)
        positiveButton(R.string.agree)
        negativeButton(R.string.disagree)
        debugMode(debugMode)
      }
    }

    basic_titled_buttons.setOnClickListener {
      MaterialDialog(this).show {
        title(R.string.useGoogleLocationServices)
        message(R.string.useGoogleLocationServicesPrompt)
        positiveButton(R.string.agree)
        negativeButton(R.string.disagree)
        debugMode(debugMode)
      }
    }

    basic_long.setOnClickListener {
      MaterialDialog(this).show {
        message(R.string.loremIpsum)
        debugMode(debugMode)
      }
    }

    basic_long_titled_buttons.setOnClickListener {
      MaterialDialog(this).show {
        title(R.string.useGoogleLocationServices)
        message(R.string.loremIpsum)
        positiveButton(R.string.agree)
        negativeButton(R.string.disagree)
        debugMode(debugMode)
      }
    }

    basic_icon.setOnClickListener {
      MaterialDialog(this).show {
        title(R.string.useGoogleLocationServices)
        icon(iconRes = R.mipmap.ic_launcher)
        message(R.string.useGoogleLocationServicesPrompt)
        positiveButton(R.string.agree)
        negativeButton(R.string.disagree)
        debugMode(debugMode)
      }
    }

    basic_checkbox_titled_buttons.setOnClickListener {
      MaterialDialog(this).show {
        title(R.string.useGoogleLocationServices)
        message(R.string.loremIpsum)
        positiveButton(R.string.agree)
        negativeButton(R.string.disagree)
        checkBoxPrompt(R.string.checkboxConfirm) { checked ->
          toast("Checked? $checked")
        }
        debugMode(debugMode)
      }
    }

    list.setOnClickListener {
      MaterialDialog(this).show {
        listItems(R.array.socialNetworks) { _, index, text ->
          toast("Selected item $text at index $index")
        }
        debugMode(debugMode)
      }
    }

    list_buttons.setOnClickListener {
      MaterialDialog(this).show {
        listItems(R.array.socialNetworks) { _, index, text ->
          toast("Selected item $text at index $index")
        }
        positiveButton(R.string.agree)
        negativeButton(R.string.disagree)
        debugMode(debugMode)
      }
    }

    list_titled.setOnClickListener {
      MaterialDialog(this).show {
        title(R.string.socialNetworks)
        listItems(R.array.socialNetworks) { _, index, text ->
          toast("Selected item $text at index $index")
        }
        debugMode(debugMode)
      }
    }

    list_titled_buttons.setOnClickListener {
      MaterialDialog(this).show {
        title(R.string.socialNetworks)
        listItems(R.array.socialNetworks) { _, index, text ->
          toast("Selected item $text at index $index")
        }
        positiveButton(R.string.agree)
        negativeButton(R.string.disagree)
        debugMode(debugMode)
      }
    }

    list_long.setOnClickListener {
      MaterialDialog(this).show {
        listItems(R.array.states) { _, index, text ->
          toast("Selected item $text at index $index")
        }
        debugMode(debugMode)
      }
    }

    list_long_buttons.setOnClickListener {
      MaterialDialog(this).show {
        listItems(R.array.states) { _, index, text ->
          toast("Selected item $text at index $index")
        }
        positiveButton(R.string.agree)
        negativeButton(R.string.disagree)
        debugMode(debugMode)
      }
    }

    list_long_titled.setOnClickListener {
      MaterialDialog(this).show {
        title(R.string.states)
        listItems(R.array.states) { _, index, text ->
          toast("Selected item $text at index $index")
        }
        debugMode(debugMode)
      }
    }

    list_long_titled_buttons.setOnClickListener {
      MaterialDialog(this).show {
        title(R.string.states)
        listItems(R.array.states) { _, index, text ->
          toast("Selected item $text at index $index")
        }
        positiveButton(R.string.agree)
        negativeButton(R.string.disagree)
        debugMode(debugMode)
      }
    }

    list_long_items.setOnClickListener {
      MaterialDialog(this).show {
        listItems(R.array.socialNetworks_longItems) { _, index, text ->
          toast("Selected item $text at index $index")
        }
        debugMode(debugMode)
      }
    }

    list_long_items_buttons.setOnClickListener {
      MaterialDialog(this).show {
        listItems(R.array.socialNetworks_longItems) { _, index, text ->
          toast("Selected item $text at index $index")
        }
        positiveButton(R.string.agree)
        negativeButton(R.string.disagree)
        debugMode(debugMode)
      }
    }

    list_long_items_titled.setOnClickListener {
      MaterialDialog(this).show {
        title(R.string.socialNetworks)
        listItems(R.array.socialNetworks_longItems) { _, index, text ->
          toast("Selected item $text at index $index")
        }
        debugMode(debugMode)
      }
    }

    list_long_items_titled_buttons.setOnClickListener {
      MaterialDialog(this).show {
        title(R.string.socialNetworks)
        listItems(R.array.socialNetworks_longItems) { _, index, text ->
          toast("Selected item $text at index $index")
        }
        positiveButton(R.string.agree)
        negativeButton(R.string.disagree)
        debugMode(debugMode)
      }
    }

    list_checkPrompt.setOnClickListener {
      MaterialDialog(this).show {
        title(R.string.socialNetworks)
        listItems(R.array.socialNetworks_longItems) { _, index, text ->
          toast("Selected item $text at index $index")
        }
        checkBoxPrompt(R.string.checkboxConfirm) { checked ->
          toast("Checked? $checked")
        }
        debugMode(debugMode)
      }
    }

    list_checkPrompt_buttons.setOnClickListener {
      MaterialDialog(this).show {
        title(R.string.socialNetworks)
        listItems(R.array.socialNetworks_longItems) { _, index, text ->
          toast("Selected item $text at index $index")
        }
        positiveButton(R.string.agree)
        negativeButton(R.string.disagree)
        checkBoxPrompt(R.string.checkboxConfirm) { checked ->
          toast("Checked? $checked")
        }
        debugMode(debugMode)
      }
    }

    single_choice_titled.setOnClickListener {
      MaterialDialog(this).show {
        title(R.string.socialNetworks)
        listItemsSingleChoice(R.array.socialNetworks, initialSelection = 1) { _, index, text ->
          toast("Selected item $text at index $index")
        }
        debugMode(debugMode)
      }
    }

    single_choice_buttons_titled.setOnClickListener {
      MaterialDialog(this).show {
        title(R.string.socialNetworks)
        listItemsSingleChoice(R.array.socialNetworks, initialSelection = 2) { _, index, text ->
          toast("Selected item $text at index $index")
        }
        positiveButton(R.string.choose)
        debugMode(debugMode)
      }
    }

    single_choice_long_items.setOnClickListener {
      MaterialDialog(this).show {
        title(R.string.socialNetworks)
        listItemsSingleChoice(R.array.socialNetworks_longItems) { _, index, text ->
          toast("Selected item $text at index $index")
        }
        positiveButton(R.string.choose)
        debugMode(debugMode)
      }
    }

    multiple_choice.setOnClickListener {
      MaterialDialog(this).show {
        title(R.string.socialNetworks)
        listItemsMultiChoice(
            R.array.socialNetworks, initialSelection = arrayOf(1, 3)
        ) { _, indices, text ->
          toast("Selected items ${text.joinToString()} at indices ${indices.joinToString()}")
        }
        debugMode(debugMode)
      }
    }

    multiple_choice_buttons.setOnClickListener {
      MaterialDialog(this).show {
        title(R.string.socialNetworks)
        listItemsMultiChoice(
            R.array.socialNetworks, initialSelection = arrayOf(1, 3)
        ) { _, indices, text ->
          toast("Selected items ${text.joinToString()} at indices ${indices.joinToString()}")
        }
        positiveButton(R.string.choose)
        debugMode(debugMode)
      }
    }

    multiple_choice_long_items.setOnClickListener {
      MaterialDialog(this).show {
        title(R.string.socialNetworks)
        listItemsMultiChoice(
            R.array.socialNetworks_longItems, initialSelection = arrayOf(1, 3)
        ) { _, indices, text ->
          toast("Selected items ${text.joinToString()} at indices ${indices.joinToString()}")
        }
        positiveButton(R.string.choose)
        debugMode(debugMode)
      }
    }

    buttons_stacked.setOnClickListener {
      MaterialDialog(this).show {
        title(R.string.useGoogleLocationServices)
        message(R.string.useGoogleLocationServicesPrompt)
        positiveButton(positiveText = "Hello World")
        negativeButton(negativeText = "How are you doing?")
        neutralButton(neutralText = "Testing long buttons")
        debugMode(debugMode)
      }
    }

    buttons_stacked_checkboxPrompt.setOnClickListener {
      MaterialDialog(this).show {
        title(R.string.useGoogleLocationServices)
        message(R.string.useGoogleLocationServicesPrompt)
        positiveButton(positiveText = "Hello World")
        negativeButton(negativeText = "How are you doing?")
        neutralButton(neutralText = "Testing long buttons")
        checkBoxPrompt(R.string.checkboxConfirm) { checked ->
          toast("Checked? $checked")
        }
        debugMode(debugMode)
      }
    }

    buttons_neutral.setOnClickListener {
      MaterialDialog(this).show {
        title(R.string.useGoogleLocationServices)
        message(R.string.useGoogleLocationServicesPrompt)
        positiveButton(R.string.agree)
        negativeButton(R.string.disagree)
        neutralButton(R.string.more_info)
        debugMode(debugMode)
      }
    }

    buttons_callbacks.setOnClickListener {
      MaterialDialog(this).show {
        title(R.string.useGoogleLocationServices)
        message(R.string.useGoogleLocationServicesPrompt)
        positiveButton(R.string.agree) { _ ->
          toast("On positive")
        }
        negativeButton(R.string.disagree) { _ ->
          toast("On negative")
        }
        neutralButton(R.string.more_info) { _ ->
          toast("On neutral")
        }
        debugMode(debugMode)
      }
    }

    misc_dialog_callbacks.setOnClickListener {
      MaterialDialog(this).show {
        title(R.string.useGoogleLocationServices)
        message(R.string.useGoogleLocationServicesPrompt)
        positiveButton(R.string.agree)
        negativeButton(R.string.disagree)
        onShow { _ -> toast("onShow") }
        onCancel { _ -> toast("onCancel") }
        onDismiss { _ -> toast("onDismiss") }
        debugMode(debugMode)
      }
    }

    input.setOnClickListener {
      MaterialDialog(this).show {
        title(R.string.useGoogleLocationServices)
        input(
            hint = "Type something",
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_WORDS
        )
        positiveButton(R.string.agree)
        negativeButton(R.string.disagree)
        debugMode(debugMode)
      }
    }

    input_message.setOnClickListener {
      MaterialDialog(this).show {
        title(R.string.useGoogleLocationServices)
        message(R.string.useGoogleLocationServicesPrompt)
        input(
            hint = "Type something",
            prefill = "Pre-filled!",
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_WORDS
        )
        positiveButton(R.string.agree)
        negativeButton(R.string.disagree)
        debugMode(debugMode)
      }
    }

    input_counter.setOnClickListener {
      MaterialDialog(this).show {
        title(R.string.useGoogleLocationServices)
        input(
            hint = "Type something",
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_WORDS,
            maxLength = 8
        )
        positiveButton(R.string.agree)
        negativeButton(R.string.disagree)
        debugMode(debugMode)
      }
    }

    input_check_prompt.setOnClickListener {
      MaterialDialog(this).show {
        title(R.string.useGoogleLocationServices)
        input(
            hint = "Type something",
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_WORDS
        )
        positiveButton(R.string.agree)
        negativeButton(R.string.disagree)
        checkBoxPrompt(R.string.checkboxConfirm) { checked ->
          toast("Checked? $checked")
        }
        debugMode(debugMode)
      }
    }

    custom_view.setOnClickListener { showCustomViewDialog() }

    custom_view_webview.setOnClickListener { showWebViewDialog() }

    file_chooser.setOnClickListener {
      if (!hasPermission(READ_EXTERNAL_STORAGE)) {
        postPermissionRunnable = Runnable { file_chooser.performClick() }
        ActivityCompat.requestPermissions(
            this@MainActivity, arrayOf(READ_EXTERNAL_STORAGE), FILE_REQUEST_CODE
        )
        return@setOnClickListener
      }

      MaterialDialog(this).show {
        fileChooser { file ->
          toast("Selected file: ${file.absolutePath}")
        }
        negativeButton(android.R.string.cancel)
        debugMode(debugMode)
      }
    }

    folder_chooser.setOnClickListener {
      if (!hasPermission(READ_EXTERNAL_STORAGE)) {
        postPermissionRunnable = Runnable { folder_chooser.performClick() }
        ActivityCompat.requestPermissions(
            this@MainActivity, arrayOf(READ_EXTERNAL_STORAGE), FOLDER_REQUEST_CODE
        )
        return@setOnClickListener
      }

      MaterialDialog(this).show {
        folderChooser { folder ->
          toast("Selected folder: ${folder.absolutePath}")
        }
        negativeButton(android.R.string.cancel)
        debugMode(debugMode)
      }
    }
  }

  private fun showCustomViewDialog() {
    val dialog = MaterialDialog(this).show {
      title(R.string.googleWifi)
      customView(R.layout.custom_view, scrollable = true)
      positiveButton(R.string.connect) { dialog ->
        val passwordInput = dialog.getCustomView()!!.findViewById<EditText>(R.id.password)
        toast("Password: $passwordInput")
      }
      negativeButton(android.R.string.cancel)
      debugMode(debugMode)
    }

    val passwordInput = dialog.getCustomView()!!.findViewById<EditText>(R.id.password)
    val showPasswordCheck = dialog.getCustomView()!!.findViewById<CheckBox>(R.id.showPassword)
    showPasswordCheck.setOnCheckedChangeListener { _, isChecked ->
      passwordInput.inputType =
          if (!isChecked) InputType.TYPE_TEXT_VARIATION_PASSWORD else InputType.TYPE_CLASS_TEXT
      passwordInput.transformationMethod =
          if (!isChecked) PasswordTransformationMethod.getInstance() else null
    }
  }

  private fun showWebViewDialog() {
    val dialog = MaterialDialog(this).show {
      title(R.string.changelog)
      customView(R.layout.custom_view_webview)
      positiveButton(android.R.string.ok)
      debugMode(debugMode)
    }

    val webView = dialog.getCustomView()!!.findViewById<WebView>(R.id.web_view)
    webView.loadData(
        "<h3>WebView Custom View</h3>\n" +
            "\n" +
            "<ol>\n" +
            "    <li><b>NEW:</b> Hey!</li>\n" +
            "    <li><b>IMPROVE:</b> Hello!</li>\n" +
            "    <li><b>FIX:</b> Hi!</li>\n" +
            "    <li><b>FIX:</b> Hey again!</li>\n" +
            "    <li><b>FIX:</b> What?</li>\n" +
            "    <li><b>FIX:</b> This is an example.</li>\n" +
            "    <li><b>MISC:</b> How are you?</li>\n" +
            "</ol>\n" +
            "<p>Material guidelines for dialogs:\n" +
            "    <a href='http://www.google.com/design/spec/components/dialogs.html'>" +
            "http://www.google.com/design/spec/components/dialogs.html</a>.\n" +
            "</p>",
        "text/html",
        "UTF-8"
    )

  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    menuInflater.inflate(R.menu.main, menu)
    menu.findItem(R.id.dark_theme)
        .isChecked = prefs.boolean(KEY_DARK_THEME)
    menu.findItem(R.id.debug_mode)
        .isChecked = debugMode
    return super.onCreateOptionsMenu(menu)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {
      R.id.dark_theme -> {
        val newIsDark = !prefs.boolean(KEY_DARK_THEME)
        prefs.apply {
          putBoolean(KEY_DARK_THEME, newIsDark)
        }
        recreate()
        return true
      }
      R.id.debug_mode -> {
        debugMode = !debugMode
        prefs.apply {
          putBoolean(KEY_DEBUG_MODE, debugMode)
        }
        invalidateOptionsMenu()
        return true
      }
    }
    return super.onOptionsItemSelected(item)
  }

  override fun onRequestPermissionsResult(
    requestCode: Int,
    permissions: Array<out String>,
    grantResults: IntArray
  ) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    if (grantResults.isNotEmpty() && grantResults[0] == PERMISSION_GRANTED) {
      postPermissionRunnable?.run()
    }
  }
}