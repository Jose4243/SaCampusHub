package com.mzansi.campushub

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import android.widget.AdapterView

/**
 * Lets the user toggle dark mode and switch the app's display language
 * between English and Afrikaans.
 *
 * Both preferences are persisted via [LocaleHelper] into the "UserPrefs"
 * SharedPreferences file. Changing either one calls [recreate] so this
 * screen (and, thanks to [BaseActivity.attachBaseContext] /
 * [MzansiApplication], every other screen the user subsequently visits)
 * immediately reflects the new theme or locale.
 */
class SettingsActivity : BaseActivity() {

    /** Small holder pairing a language code with its human-readable label. */
    private data class LanguageOption(val code: String, val displayName: String)

    private lateinit var tvSettingsTitle: TextView
    private lateinit var switchDarkMode: SwitchCompat
    private lateinit var spinnerLanguage: Spinner
    private lateinit var btnSettingsBack: Button

    private lateinit var languageOptions: List<LanguageOption>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        tvSettingsTitle = findViewById<TextView>(R.id.tvSettingsTitle)
        switchDarkMode = findViewById<SwitchCompat>(R.id.switchDarkMode)
        spinnerLanguage = findViewById<Spinner>(R.id.spinnerLanguage)
        btnSettingsBack = findViewById<Button>(R.id.btnSettingsBack)

        setupDarkModeSwitch()
        setupLanguageSpinner()

        btnSettingsBack.setOnClickListener {
            finish()
        }
    }

    private fun setupDarkModeSwitch() {
        // Set the initial state BEFORE attaching the listener so restoring
        // the saved preference doesn't itself trigger a recreate() loop.
        switchDarkMode.isChecked = LocaleHelper.isDarkModeEnabled(this)

        switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            LocaleHelper.setDarkModeEnabled(this, isChecked)
            recreate()
        }
    }

    private fun setupLanguageSpinner() {
        languageOptions = listOf(
            LanguageOption(LocaleHelper.LANGUAGE_ENGLISH, getString(R.string.language_english)),
            LanguageOption(LocaleHelper.LANGUAGE_AFRIKAANS, getString(R.string.language_afrikaans))
        )

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            languageOptions.map { it.displayName }
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerLanguage.adapter = adapter

        val currentLanguageCode = LocaleHelper.getPersistedLanguage(this)
        val currentIndex = languageOptions
            .indexOfFirst { it.code == currentLanguageCode }
            .takeIf { it >= 0 } ?: 0
        spinnerLanguage.setSelection(currentIndex, false)

        // Attach the listener after the initial selection has been applied
        // so restoring the saved language doesn't itself trigger a change.
        spinnerLanguage.post {
            spinnerLanguage.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val selectedLanguageCode = languageOptions[position].code
                    if (selectedLanguageCode != LocaleHelper.getPersistedLanguage(this@SettingsActivity)) {
                        LocaleHelper.setLocale(this@SettingsActivity, selectedLanguageCode)
                        recreate()
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // No-op
                }
            }
        }
    }
}
