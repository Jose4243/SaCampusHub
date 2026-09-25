package com.mzansi.campushub

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Local (JVM) unit tests for [SecurityUtils]. These run without an Android
 * device/emulator as part of `./gradlew test`, which is what the CI
 * pipeline invokes.
 */
class SecurityUnitTest {

    @Test
    fun hashPassword_producesSixtyFourCharacterHash() {
        val plainTextPassword = "MySecurePassword123"

        val hashed = SecurityUtils.hashPassword(plainTextPassword)

        assertEquals(64, hashed.length)
    }

    @Test
    fun hashPassword_producesOnlyLowercaseHexCharacters() {
        val hashed = SecurityUtils.hashPassword("AnotherPassword!")

        assertTrue(hashed.matches(Regex("^[0-9a-f]{64}$")))
    }

    @Test
    fun hashPassword_isDeterministic_sameInputProducesSameHash() {
        val password = "ConsistentPassword"

        val firstHash = SecurityUtils.hashPassword(password)
        val secondHash = SecurityUtils.hashPassword(password)

        assertEquals(firstHash, secondHash)
    }

    @Test
    fun hashPassword_differentInputsProduceDifferentHashes() {
        val hashOne = SecurityUtils.hashPassword("PasswordOne")
        val hashTwo = SecurityUtils.hashPassword("PasswordTwo")

        assertNotEquals(hashOne, hashTwo)
    }

    @Test
    fun hashPassword_knownValue_matchesExpectedSha256() {
        // SHA-256 of the empty string is a well-known constant value.
        val expectedHash = "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b85"

        val actual = SecurityUtils.hashPassword("")

        assertEquals(expectedHash, actual)
    }
}
