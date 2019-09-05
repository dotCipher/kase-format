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
                else -> word.capitalize()
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
            // There should be uppercased characters, and they should be isolated
            it == 1
        }
        override val shouldStartWith: CharToken = CharToken.UPPERCASE_LETTER
        override val shouldStartWithIgnore: Set<CharToken> = setOf(
            CharToken.NUMBER
        )
        override fun normalizeWord(word: String, wordIndex: Int): String = word.capitalize()
    };

    fun convert(format: KaseFormat, input: String): String {
        val string = StringBuilder()
        val split = input.split(boundary)
        split.forEachIndexed { wordIndex, word ->
            if (wordIndex < split.size - 1) {
                string.append(format.delimiter)
            }
            string.append(format.normalizeWord(word, wordIndex))
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

    fun converter(): DynamicKaseConverter = DynamicKaseConverter(this)

    fun converter(sourceFormat: KaseFormat): BiDirectionalConverter =
        BiDirectionalConverter(sourceFormat, this)

    internal fun matches(tokenized: TokenizedString): Boolean =
        matchesRequiredTokens(tokenized)
                && matchesAbsentTokens(tokenized)
                && matchesMaxSequentialUppercase(tokenized)
                && matchesStartsWith(tokenized)

    companion object {
        fun determine(string: String): KaseFormat? {
            val tokenized = TokenizedString.of(string)
            return values().firstOrNull { it.matches(tokenized) }
        }
    }

}
