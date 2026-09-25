package com.mzansi.campushub

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

/**
 * Handles new-user registration for Mzansi Campus Hub.
 *
 * Flow:
 *  1. User enters an email address and password.
 *  2. Input is validated locally.
 *  3. The password is hashed via [SecurityUtils.hashPassword] - the raw
 *     plain-text password is never stored.
 *  4. The email + hashed password are persisted to the "UserPrefs"
 *     SharedPreferences file.
 *  5. A confirmation Toast is shown and the user is navigated to
 *     [MainActivity].
 */
class RegisterActivity : AppCompatActivity() {

    companion object {
        const val PREFS_NAME = "UserPrefs"
        const val KEY_EMAIL = "user_email"
        const val KEY_PASSWORD_HASH = "user_password_hash"
        const val KEY_IS_REGISTERED = "is_registered"
    }

    private lateinit var tvRegisterTitle: TextView
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnRegister: Button
    private lateinit var progressRegister: ProgressBar

    private lateinit var userPrefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        tvRegisterTitle = findViewById<TextView>(R.id.tvRegisterTitle)
        etEmail = findViewById<EditText>(R.id.etEmail)
        etPassword = findViewById<EditText>(R.id.etPassword)
        btnRegister = findViewById<Button>(R.id.btnRegister)
        progressRegister = findViewById<ProgressBar>(R.id.progressRegister)

        userPrefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)

        btnRegister.setOnClickListener {
            attemptRegistration()
        }
    }

    private fun attemptRegistration() {
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString()

        if (!isInputValid(email, password)) {
            return
        }

        progressRegister.visibility = View.VISIBLE
        btnRegister.isEnabled = false

        // Hash the password before ever touching persistent storage.
        val hashedPassword = SecurityUtils.hashPassword(password)

        val saved = saveCredentials(email, hashedPassword)

        progressRegister.visibility = View.GONE
        btnRegister.isEnabled = true

        if (saved) {
            Toast.makeText(
                this,
                "Registration successful! Welcome to Mzansi Campus Hub.",
                Toast.LENGTH_SHORT
            ).show()
            navigateToMain()
        } else {
            Toast.makeText(
                this,
                "Something went wrong while saving your details. Please try again.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun isInputValid(email: String, password: String): Boolean {
        if (email.isEmpty()) {
            etEmail.error = "Email is required"
            etEmail.requestFocus()
            return false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.error = "Enter a valid email address"
            etEmail.requestFocus()
            return false
        }

        if (password.isEmpty()) {
            etPassword.error = "Password is required"
            etPassword.requestFocus()
            return false
        }

        if (password.length < 6) {
            etPassword.error = "Password must be at least 6 characters"
            etPassword.requestFocus()
            return false
        }

        return true
    }

    private fun saveCredentials(email: String, hashedPassword: String): Boolean {
        return userPrefs.edit()
            .putString(KEY_EMAIL, email)
            .putString(KEY_PASSWORD_HASH, hashedPassword)
            .putBoolean(KEY_IS_REGISTERED, true)
            .commit()
    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
