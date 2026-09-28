package com.mzansi.campushub

import android.content.Context
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {

    private var appliedLanguage: String? = null

    override fun attachBaseContext(newBase: Context) {
        appliedLanguage = LocaleHelper.getPersistedLanguage(newBase)
        super.attachBaseContext(LocaleHelper.wrapContext(newBase))
    }

    override fun onResume() {
        super.onResume()
        val currentLanguage = LocaleHelper.getPersistedLanguage(this)
        if (appliedLanguage != null && appliedLanguage != currentLanguage) {
            recreate()
        }
    }
}
