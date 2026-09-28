package com.mzansi.campushub

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import java.util.Locale

/**
 * Central helper for two persisted user preferences that affect the whole
 * app: display language (locale) and dark mode.
 *
 * Both are stored in the same "UserPrefs" SharedPreferences file already
 * used for the user's session, so nothing new needs to be wired up.
 *
 * Language changes are applied by wrapping each Activity's base [Context]
 * with a [Configuration] carrying the persisted locale (see [wrapContext],
 * used from [BaseActivity.attachBaseContext]). This approach works back to
 * minSdk 24 without relying on the API 33+ per-app language APIs.
 */
object LocaleHelper {

    const val PREFS_NAME = "UserPrefs"
    const val KEY_LANGUAGE = "app_language"
    const val KEY_DARK_MODE = "dark_mode_enabled"

    const val LANGUAGE_ENGLISH = "en"
    const val LANGUAGE_AFRIKAANS = "af"

    /**
     * Persists [languageCode] as the user's chosen language.
     */
    fun persistLanguage(context: Context, languageCode: String) {
        prefs(context).edit().putString(KEY_LANGUAGE, languageCode).apply()
    }

    /**
     * Returns the persisted language code, defaulting to English if the
     * user has never made a selection.
     */
    fun getPersistedLanguage(context: Context): String {
        return prefs(context).getString(KEY_LANGUAGE, LANGUAGE_ENGLISH) ?: LANGUAGE_ENGLISH
    }

    /**
     * Wraps [context] with a Configuration that applies the persisted
     * language. Called from every Activity's attachBaseContext so the
     * correct localized resources are loaded as soon as it is created.
     */
    fun wrapContext(context: Context): Context {
        val languageCode = getPersistedLanguage(context)
        return applyLocaleToContext(context, languageCode)
    }

    /**
     * Persists [languageCode] and returns a Context configured to use it
     * immediately, for callers (e.g. SettingsActivity) that want to react
     * to a fresh selection right away.
     */
    fun setLocale(context: Context, languageCode: String): Context {
        persistLanguage(context, languageCode)
        return applyLocaleToContext(context, languageCode)
    }

    private fun applyLocaleToContext(context: Context, languageCode: String): Context {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val configuration = Configuration(context.resources.configuration)
        configuration.setLocale(locale)
        configuration.setLayoutDirection(locale)

        return context.createConfigurationContext(configuration)
    }

    /**
     * Returns whether the user has previously enabled dark mode.
     */
    fun isDarkModeEnabled(context: Context): Boolean {
        return prefs(context).getBoolean(KEY_DARK_MODE, false)
    }

    /**
     * Persists the dark mode preference and applies it immediately via
     * [AppCompatDelegate], which triggers a recreation of active activities.
     */
    fun setDarkModeEnabled(context: Context, enabled: Boolean) {
        prefs(context).edit().putBoolean(KEY_DARK_MODE, enabled).apply()
        AppCompatDelegate.setDefaultNightMode(
            if (enabled) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        )
    }

    /**
     * Applies the persisted dark mode preference without changing it.
     * Intended to be called once, at application startup.
     */
    fun applyPersistedNightMode(context: Context) {
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkModeEnabled(context)) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    private fun prefs(context: Context) =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
}
