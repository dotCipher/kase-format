package io.dotcipher.kase

enum class CharToken(private val charRanges: Set<CharRange>) {
    HYPHEN('-'..'-'),
    UNDERSCORE('_'..'_'),
    UPPERCASE_LETTER('A'..'Z'),
    LOWERCASE_LETTER('a'..'z'),
    NUMBERS('0'..'9'),
    SPACE(' '..' '),
    OTHER_SPECIALS(
        '!'..',',
        '.'..'/',
        ':'..'@',
        '['..'^',
        '`'..'`',
        '{'..'~');
    // Exclude latin supplement character ranges

    constructor(charRange: CharRange) : this(setOf(charRange))

    constructor(vararg charRange: CharRange): this(setOf(*charRange))

    fun matches(char: Char): Boolean {
        for (charRange in charRanges) {
            if (charRange.contains(char)) {
                return true
            }
        }
        return false
    }

    companion object {

        fun matching(char: Char): CharToken? {
            for (value in values()) {
                if (value.matches(char)) {
                    return value
                }
            }
            return null
        }

    }

}
