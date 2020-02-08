package security

import java.security.MessageDigest.getInstance
import java.security.NoSuchAlgorithmException

internal object Encrypt {
    fun sha1(password: String, salt: String): ByteArray {
        return sha1(password.toByteArray(), salt.toByteArray())
    }

    private fun sha1(password: ByteArray, salt: ByteArray): ByteArray {
        return try {
            val md = getInstance("SHA-1")
            md.update(salt)
            md.digest(password)
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException(e)
        }
    }
}
