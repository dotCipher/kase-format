package io.dotcipher.kase

enum class KaseFormat(
    private val boundary: Regex,
    private val delimiter: String
) {

    /** Lower cased hyphenated formatting, ie. `hello-world` **/
    LOWER_HYPHEN(Regex("-"), "-") {
        override val requiredTokens: Set<CharToken> = setOf(
            CharToken.HYPHEN,
            CharToken.LOWERCASE_LETTER
        )
        override val absentTokens: Set<CharToken> = setOf(
            CharToken.UNDERSCORE,
            CharToken.SPACE,
            CharToken.OTHER_SPECIAL
        )
        override val matchMaxSequentialUppercase: (Int) -> Boolean = {
            // There should be no uppercase characters
            it == 0
        }
        override val shouldStartWith: CharToken = CharToken.LOWERCASE_LETTER
        override val shouldStartWithIgnore: Set<CharToken> = setOf(
            CharToken.NUMBER,
            CharToken.HYPHEN
        )

        override fun normalizeWord(word: String, wordIndex: Int): String = word.toLowerCase()
    },

    /** Capital cased hyphenated formatting, ie. `Hello-World` **/
    CAPITAL_HYPHEN(Regex("-"), "-") {
        override val requiredTokens: Set<CharToken> = setOf(
            CharToken.HYPHEN,
            CharToken.UPPERCASE_LETTER,
            CharToken.LOWERCASE_LETTER
        )
        override val absentTokens: Set<CharToken> = setOf(
            CharToken.UNDERSCORE,
            CharToken.SPACE,
            CharToken.OTHER_SPECIAL
        )
        override val matchMaxSequentialUppercase: (Int) -> Boolean = {
            // There should be uppercased characters, and they should be isolated
            it == 1
        }
        override val shouldStartWith: CharToken = CharToken.UPPERCASE_LETTER
        // Ignore prefixed numbers or hyphen when checking `shouldStartWith`
        override val shouldStartWithIgnore: Set<CharToken> = setOf(
            CharToken.NUMBER,
            CharToken.HYPHEN
        )

        override fun normalizeWord(word: String, wordIndex: Int): String = word.capitalize()
    },

    /** Upper cased hyphenated formatting, ie. `HELLO-WORLD` **/
    UPPER_HYPHEN(Regex("-"), "-") {
        override val requiredTokens: Set<CharToken> = setOf(
            CharToken.HYPHEN,
            CharToken.UPPERCASE_LETTER
        )
        override val absentTokens: Set<CharToken> = setOf(
            CharToken.LOWERCASE_LETTER,
            CharToken.UNDERSCORE,
            CharToken.SPACE,
            CharToken.OTHER_SPECIAL
        )
        override val matchMaxSequentialUppercase: (Int) -> Boolean = {
            // Match all circumstances, since we restrict on lower case letters already
            it > 0
        }
        override val shouldStartWith: CharToken = CharToken.UPPERCASE_LETTER
        override val shouldStartWithIgnore: Set<CharToken> = setOf(
            CharToken.NUMBER,
            CharToken.HYPHEN
        )

        override fun normalizeWord(word: String, wordIndex: Int): String = word.toUpperCase()
    },

    /** Lower cased underscore formatting, ie. `hello_world` **/
    LOWER_UNDERSCORE(Regex("_"), "_") {
        override val requiredTokens: Set<CharToken> = setOf(
            CharToken.UNDERSCORE,
            CharToken.LOWERCASE_LETTER
        )
        override val absentTokens: Set<CharToken> = setOf(
            CharToken.HYPHEN,
            CharToken.SPACE,
            CharToken.OTHER_SPECIAL
        )
        override val matchMaxSequentialUppercase: (Int) -> Boolean = {
            // There should be no uppercase characters
            it == 0
        }
        override val shouldStartWith: CharToken = CharToken.LOWERCASE_LETTER
        override val shouldStartWithIgnore: Set<CharToken> = setOf(
            CharToken.NUMBER,
            CharToken.UNDERSCORE
        )

        override fun normalizeWord(word: String, wordIndex: Int): String = word.toLowerCase()
    },

    /** Capital cased underscore formatting, ie. `Hello_World` **/
    CAPITAL_UNDERSCORE(Regex("_"), "_") {
        override val requiredTokens: Set<CharToken> = setOf(
            CharToken.UNDERSCORE,
            CharToken.UPPERCASE_LETTER,
            CharToken.LOWERCASE_LETTER
        )
        override val absentTokens: Set<CharToken> = setOf(
            CharToken.HYPHEN,
            CharToken.SPACE,
            CharToken.OTHER_SPECIAL
        )
        override val matchMaxSequentialUppercase: (Int) -> Boolean = {
            // There should be uppercased characters, and they should be isolated
            it == 1
        }
        override val shouldStartWith: CharToken = CharToken.UPPERCASE_LETTER
        override val shouldStartWithIgnore: Set<CharToken> = setOf(
            CharToken.NUMBER,
            CharToken.UNDERSCORE
        )

        override fun normalizeWord(word: String, wordIndex: Int): String = word.capitalize()
    },

    /** Upper cased underscore formatting, ie. `HELLO_WORLD` **/
    UPPER_UNDERSCORE(Regex("_"), "_") {
        override val requiredTokens: Set<CharToken> = setOf(
            CharToken.UNDERSCORE,
            CharToken.UPPERCASE_LETTER
        )
        override val absentTokens: Set<CharToken> = setOf(
            CharToken.LOWERCASE_LETTER,
            CharToken.HYPHEN,
            CharToken.SPACE,
            CharToken.OTHER_SPECIAL
        )
        override val matchMaxSequentialUppercase: (Int) -> Boolean = {
            // Match all circumstances, since we restrict on lower case letters already
            it > 0
        }
        override val shouldStartWith: CharToken = CharToken.UPPERCASE_LETTER
        override val shouldStartWithIgnore: Set<CharToken> = setOf(
            CharToken.NUMBER,
            CharToken.UNDERSCORE
        )

        override fun normalizeWord(word: String, wordIndex: Int): String = word.toUpperCase()
    },

    /** Lower camel cased formatting, ie. `helloWorld` **/
    LOWER_CAMEL(Regex("[A-Z]"), "") {
        override val requiredTokens: Set<CharToken> = setOf(
            CharToken.LOWERCASE_LETTER
        )
        override val absentTokens: Set<CharToken> = setOf(
            CharToken.UNDERSCORE,
            CharToken.HYPHEN,
            CharToken.SPACE,
            CharToken.OTHER_SPECIAL
        )
        override val matchMaxSequentialUppercase: (Int) -> Boolean = {
            // There could be uppercased characters, but they should be isolated
            it == 1 || it == 0
        }
        override val shouldStartWith: CharToken = CharToken.LOWERCASE_LETTER
        override val shouldStartWithIgnore: Set<CharToken> = setOf(
            CharToken.NUMBER
        )

        override fun normalizeWord(word: String, wordIndex: Int): String =
            when (wordIndex) {
                0 -> word.toLowerCase()
                else -> when {
                    word.isEmpty() -> word
                    word.length == 1 -> word.capitalize()
                    else -> {
                        "${word[0].toUpperCase()}${word.slice(1 until word.length).toLowerCase()}"
                    }
                }
            }
    },

    /** Capital camel cased formatting, ie. `HelloWorld` **/
    CAPITAL_CAMEL(Regex("[A-Z]"), "") {
        override val requiredTokens: Set<CharToken> = setOf(
            CharToken.LOWERCASE_LETTER,
            CharToken.UPPERCASE_LETTER
        )
        override val absentTokens: Set<CharToken> = setOf(
            CharToken.UNDERSCORE,
            CharToken.HYPHEN,
            CharToken.SPACE,
            CharToken.OTHER_SPECIAL
        )
        override val matchMaxSequentialUppercase: (Int) -> Boolean = {
            // There should be uppercased characters, but they don't need to be isolated
            it >= 1
        }
        override val shouldStartWith: CharToken = CharToken.UPPERCASE_LETTER
        override val shouldStartWithIgnore: Set<CharToken> = setOf(
            CharToken.NUMBER
        )
        override fun normalizeWord(word: String, wordIndex: Int): String {
            return when {
                word.isEmpty() -> word
                word.length == 1 -> word.capitalize()
                else -> {
                    "${word[0].toUpperCase()}${word.slice(1 until word.length).toLowerCase()}"
                }
            }
        }
    };

    private fun splitWords(input: String, sourceFormat: KaseFormat): List<String> =
        if (delimiter.isEmpty() && (sourceFormat == CAPITAL_CAMEL || sourceFormat == LOWER_CAMEL)) {
            // Generate lookahead regex for formats without a delimiter (generally camel case)
            val firstSplit = input.split(Regex("(?=${boundary.pattern})"))
            // Then do a rejoin based on the lookahead tokens
            val rejoinedWords = mutableListOf<String>()
            var wordCache = StringBuilder()
            firstSplit.forEachIndexed { wordIndex, string ->
                if (wordIndex < firstSplit.size - 1) {
                    // Use lookahead for handling the non-delimiter words
                    val nextToken = firstSplit.getOrNull(wordIndex + 1)
                    if (nextToken != null) {
                        if (!nextToken.matches(boundary)) {
                            wordCache.append(string)
                            if (wordCache.isNotEmpty()) {
                                rejoinedWords.add(wordCache.toString())
                                wordCache = StringBuilder()
                            }
                        } else if (string.length == 1){
                            wordCache.append(string)
                        } else if (string.isNotEmpty()) {
                            rejoinedWords.add(string)
                        }
                    }
                } else {
                    wordCache.append(string)
                    if (wordCache.isNotEmpty()) {
                        rejoinedWords.add(wordCache.toString())
                    }
                }
            }
            rejoinedWords
        } else {
            input.split(boundary)
        }

    fun convert(format: KaseFormat, input: String): String {
        val string = StringBuilder()
        val split = splitWords(input, this)
        split.forEachIndexed { wordIndex, word ->
            string.append(format.normalizeWord(word, wordIndex))
            if (wordIndex < split.size - 1) {
                string.append(format.delimiter)
            }
        }
        return string.toString()
    }

    // Standard characteristics matching
    internal abstract val requiredTokens: Set<CharToken>
    internal abstract val absentTokens: Set<CharToken>

    // Special / Aggregate characteristics matching
    internal abstract val matchMaxSequentialUppercase: (Int) -> Boolean
    internal open val shouldStartWith: CharToken? = null
    internal open val shouldStartWithIgnore: Set<CharToken> = emptySet()

    private val dynamicConverter by lazy { DynamicConverter(this) }

    abstract fun normalizeWord(word: String, wordIndex: Int = 0): String

    private fun matchesRequiredTokens(tokenized: TokenizedString): Boolean =
        tokenized.uniqueTokens.containsAll(requiredTokens)

    private fun matchesAbsentTokens(tokenized: TokenizedString): Boolean =
        tokenized.uniqueTokens.containsNone(absentTokens)

    private fun matchesMaxSequentialUppercase(tokenized: TokenizedString): Boolean =
        matchMaxSequentialUppercase.invoke(tokenized.maxSequentialCount(CharToken.UPPERCASE_LETTER))

    private fun matchesStartsWith(tokenized: TokenizedString): Boolean =
        shouldStartWith?.let {
            tokenized.startsWith(it, shouldStartWithIgnore)
        } ?: true

    fun converter(): DynamicConverter = dynamicConverter

    fun converter(sourceFormat: KaseFormat): BiDirectionalConverter =
        BiDirectionalConverter(sourceFormat, this)

    internal fun matches(tokenized: TokenizedString): Boolean =
        matchesRequiredTokens(tokenized)
                && matchesAbsentTokens(tokenized)
                && matchesMaxSequentialUppercase(tokenized)
                && matchesStartsWith(tokenized)

    companion object {

        private fun String.containsOnly(vararg charRange: CharRange): Boolean {
            for (char in this) {
                var matchesAtLeastOneRange = false
                for (range in charRange) {
                    if (range.contains(char)) {
                        matchesAtLeastOneRange = true
                    }
                }
                if (!matchesAtLeastOneRange) {
                    return false
                }
            }
            return true
        }

        private fun String.containsOnlyUpperCaseOrNumeric(): Boolean =
            containsOnly('A'..'Z', '0'..'9')

        private fun String.containsOnlyLowerCaseOrNumeric(): Boolean =
            containsOnly('a'..'z', '0'..'9')

        fun determine(string: String): KaseFormat? {
            // Short circuit
            if (string.isBlank()) {
                return null
            }
            // Then parse
            val tokenized = TokenizedString.of(string)
            val maybeFormat = values().firstOrNull { it.matches(tokenized) }
            if (maybeFormat != null) {
                return maybeFormat
            }
            // Best guesses if only all lower or upper
            if (string.containsOnlyUpperCaseOrNumeric()) {
                return UPPER_UNDERSCORE
            }
            if (string.containsOnlyLowerCaseOrNumeric()) {
                return LOWER_CAMEL
            }
            // Give up
            return null
        }
    }

}
