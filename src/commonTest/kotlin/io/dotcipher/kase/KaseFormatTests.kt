package io.dotcipher.kase

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class KaseFormatTests {

    private fun assertKaseFormat(input: String, format: KaseFormat) {
        val output = KaseFormat.determine(input)
        assertNotNull(output, "$input cannot be determined (expected $format)") {
            assertEquals(output, format, "$input is determined as $output (expected $format)")
        }
    }

    private fun assertKaseFormat(inputs: List<String>, format: KaseFormat) {
        inputs.forEach {
            assertKaseFormat(it, format)
        }
    }

    @Test
    fun testDetermine_Unknown() {
        val inputs = listOf(
            "abc-abc_abc",
            "ABC_abc",
            "",
            "-_",
            "!",
            "ABC-abc",
            "ABC",
            "[]",
            " ",
            "A B C",
            // Acronym words aren't supported
            "SomeJSONAcronym",
            "XMLThing",
            // Other special characters should show as unknown
            "MaybeThough?",
            " Nope",
            "-not&this-either"
            )
        inputs.forEach {
            val format = KaseFormat.determine(it)
            assertNull(format, "$it is determined as $format")
        }
    }

    @Test
    fun testDetermine_LowerHyphen() {
        val inputs = listOf(
            "a-",
            "a-b",
            "-abc",
            "123-abc-rew-34-whatever"
        )
        assertKaseFormat(inputs, KaseFormat.LOWER_HYPHEN)
    }

    @Test
    fun testDetermine_CapitalHyphen() {
        val inputs = listOf(
            "Ab-Ab",
            "Ac-",
            "-Ab",
            "Whatever-Something-Else"
        )
        assertKaseFormat(inputs, KaseFormat.CAPITAL_HYPHEN)
    }

    @Test
    fun testDetermine_UpperHyphen() {
        val inputs = listOf(
            "A-B",
            "A-B-C",
            "A-",
            "-A",
            "ABC-DEF"
        )
        assertKaseFormat(inputs, KaseFormat.UPPER_HYPHEN)

    }

    @Test
    fun testDetermine_LowerUnderscore() {
        val inputs = listOf(
            "a_",
            "a_b",
            "_abc",
            "something_blah_123_other_stuff"
        )
        assertKaseFormat(inputs, KaseFormat.LOWER_UNDERSCORE)
    }

    @Test
    fun testDetermine_CapitalUnderscore() {
        val inputs = listOf(
            "Ab_Ab",
            "Ac_",
            "_Ab",
            "Something_Else_With_Words"
        )
        assertKaseFormat(inputs, KaseFormat.CAPITAL_UNDERSCORE)
    }

    @Test
    fun testDetermine_UpperUnderscore() {
        val inputs = listOf(
            "A_",
            "_A",
            "ABC_DEF",
            "123_ABC_WITH_THINGS"
        )
        assertKaseFormat(inputs, KaseFormat.UPPER_UNDERSCORE)
    }

    @Test
    fun testDetermine_LowerCamel() {
        val inputs = listOf(
            "abcSomethingElse",
            "with123Numbers",
            "what",
            "123somethingElse",
            "some123thingNumbered"
        )
        assertKaseFormat(inputs, KaseFormat.LOWER_CAMEL)
    }

    @Test
    fun testDetermine_CapitalCamel() {
        val inputs = listOf(
            "EasyPeasy",
            "123SomethingElse",
            "ItWorks",
            "Hopefully"
        )
        assertKaseFormat(inputs, KaseFormat.CAPITAL_CAMEL)
    }

}
