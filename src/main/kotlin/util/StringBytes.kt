package util

object StringBytes {
    fun toString(bytes: ByteArray): String {
        val sb = StringBuilder()
        bytes.forEach { hashByte -> sb.append(hex(hashByte)) }
        return sb.toString()
    }

    private fun hex(hashByte: Byte): String {
        return ((hashByte.toInt() and 0xff) + 0x100).toString(16).substring(1)
    }
}
