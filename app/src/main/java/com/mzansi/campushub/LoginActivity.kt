package com.mzansi.campushub

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast

/**
 * Handles returning-user authentication for Mzansi Campus Hub.
 *
 * Flow:
 *  1. User enters email and password.
 *  2. Input is validated locally (non-empty checks).
 *  3. Stored credentials are read from the "UserPrefs" SharedPreferences file
 *     (the same file [RegisterActivity] writes to).
 *  4. The entered password is hashed with [SecurityUtils.hashPassword] and
 *     compared against the stored hash - the plain-text password is never
 *     compared or stored directly.
 *  5. On a match, the user is taken to [MainActivity]. On a mismatch, or if
 *     no account exists yet, an appropriate warning Toast is shown.
 */
class LoginActivity : BaseActivity() {

    companion object {
        private const val TAG = "LoginActivity"
    }

    private lateinit var tvLoginTitle: TextView
    private lateinit var etLoginEmail: EditText
    private lateinit var etLoginPassword: EditText
    private lateinit var btnLoginSubmit: Button
    private lateinit var btnNavRegister: Button
    private lateinit var progressLogin: ProgressBar

    private lateinit var userPrefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        tvLoginTitle = findViewById<TextView>(R.id.tvLoginTitle)
        etLoginEmail = findViewById<EditText>(R.id.etLoginEmail)
        etLoginPassword = findViewById<EditText>(R.id.etLoginPassword)
        btnLoginSubmit = findViewById<Button>(R.id.btnLoginSubmit)
        btnNavRegister = findViewById<Button>(R.id.btnNavRegister)
        progressLogin = findViewById<ProgressBar>(R.id.progressLogin)

        userPrefs = getSharedPreferences(RegisterActivity.PREFS_NAME, MODE_PRIVATE)

        btnLoginSubmit.setOnClickListener {
            attemptLogin()
        }

        btnNavRegister.setOnClickListener {
            navigateToRegister()
        }
    }

    private fun attemptLogin() {
        val email = etLoginEmail.text.toString().trim()
        val password = etLoginPassword.text.toString()

        if (!isInputValid(email, password)) {
            return
        }

        progressLogin.visibility = View.VISIBLE
        btnLoginSubmit.isEnabled = false

        val isRegistered = userPrefs.getBoolean(RegisterActivity.KEY_IS_REGISTERED, false)
        val storedEmail = userPrefs.getString(RegisterActivity.KEY_EMAIL, null)
        val storedPasswordHash = userPrefs.getString(RegisterActivity.KEY_PASSWORD_HASH, null)

        progressLogin.visibility = View.GONE
        btnLoginSubmit.isEnabled = true

        if (!isRegistered || storedEmail.isNullOrEmpty() || storedPasswordHash.isNullOrEmpty()) {
            Log.d(TAG, "Login attempted but no registered user was found.")
            Toast.makeText(
                this,
                getString(R.string.msg_login_no_account),
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val enteredPasswordHash = SecurityUtils.hashPassword(password)

        val credentialsMatch = storedEmail.equals(email, ignoreCase = true) &&
            storedPasswordHash == enteredPasswordHash

        if (credentialsMatch) {
            Toast.makeText(this, getString(R.string.msg_login_success), Toast.LENGTH_SHORT).show()
            Log.d(TAG, "Login successful for user: $storedEmail")
            navigateToMain()
        } else {
            Log.d(TAG, "Login failed: email or password did not match stored credentials.")
            Toast.makeText(
                this,
                getString(R.string.msg_login_invalid),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun isInputValid(email: String, password: String): Boolean {
        if (email.isEmpty()) {
            etLoginEmail.error = "Email is required"
            etLoginEmail.requestFocus()
            return false
        }

        if (password.isEmpty()) {
            etLoginPassword.error = "Password is required"
            etLoginPassword.requestFocus()
            return false
        }

        return true
    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun navigateToRegister() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }
}
