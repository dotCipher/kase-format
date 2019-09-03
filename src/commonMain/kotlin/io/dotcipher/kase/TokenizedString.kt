package io.dotcipher.kase

internal data class TokenizedString(private val charTokens: List<CharToken>) {

    val uniqueTokens = charTokens.toSet()

    fun startsWith(prefixed: CharToken, ignoredPrefixes: Set<CharToken>): Boolean {
        for (characteristic in charTokens) {
            if (ignoredPrefixes.contains(characteristic)) {
                continue
            } else {
                return characteristic == prefixed
            }
        }
        return false
    }

    fun maxSequentialCount(charToken: CharToken): Int {
        var maxSeen = 0
        var counter = 0
        for (thisCharacteristic in charTokens) {
            when {
                thisCharacteristic == charToken -> counter++
                maxSeen < counter -> {
                    maxSeen = counter
                    counter = 0
                }
                else -> counter = 0
            }
        }
        // Do one last check
        if (counter > 0 && maxSeen < counter) {
            maxSeen = counter
        }
        return maxSeen
    }

    companion object {

        fun of(string: String): TokenizedString {
            val characteristics = mutableListOf<CharToken>()
            for (char in string) {
                val characteristic = CharToken.matching(char)

                if (characteristic != null) {
                    characteristics.add(characteristic)
                }
            }
            return TokenizedString(characteristics.toList())
        }

    }

}
