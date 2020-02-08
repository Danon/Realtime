package security

import util.StringBytes

class Password(val hash: ByteArray) {
    constructor() : this(ByteArray(0))
    constructor(plain: String, salt: String = "") : this(Encrypt.sha1(plain, salt))

    fun compare(otherPassword: Password): Boolean {
        for (i in hash.indices) {
            if (hash[i] != otherPassword.hash[i]) {
                return false
            }
        }
        return true
    }

    override fun toString(): String {
        return StringBytes.toString(hash)
    }
}
