package com.mzansi.campushub

import java.security.MessageDigest

/**
 * Utility object responsible for cryptographic helper functions used across
 * the Mzansi Campus Hub application. Currently provides SHA-256 based
 * password hashing so that plain-text passwords are never persisted to
 * SharedPreferences or transmitted over the network.
 */
object SecurityUtils {

    private const val SHA_256 = "SHA-256"

    /**
     * Hashes the given plain-text password using the SHA-256 algorithm and
     * returns the result as a lowercase hexadecimal string (64 characters).
     *
     * @param password the plain-text password to hash
     * @return a 64-character lowercase hex string representing the SHA-256 digest
     */
    fun hashPassword(password: String): String {
        val digest = MessageDigest.getInstance(SHA_256)
        val hashBytes = digest.digest(password.toByteArray(Charsets.UTF_8))
        return bytesToHex(hashBytes)
    }

    /**
     * Converts a byte array into its lowercase hexadecimal string representation.
     */
    private fun bytesToHex(bytes: ByteArray): String {
        val hexChars = CharArray(bytes.size * 2)
        val hexDigits = "0123456789abcdef".toCharArray()
        for (i in bytes.indices) {
            val v = bytes[i].toInt() and 0xFF
            hexChars[i * 2] = hexDigits[v ushr 4]
            hexChars[i * 2 + 1] = hexDigits[v and 0x0F]
        }
        return String(hexChars)
    }
}
