package io.dotcipher.kase

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class KaseFormatTests {

    private fun assertKaseConvert(
        inputsToOutputs: Map<String, String>,
        inputFormat: KaseFormat,
        outputFormat: KaseFormat
    ) {
        inputsToOutputs.forEach {
            assertKaseConvert(it.key, inputFormat, it.value, outputFormat)
        }
    }

    private fun assertKaseConvert(
        input: String,
        inputFormat: KaseFormat,
        expectedOutput: String,
        expectedOutputFormat: KaseFormat
    ) {
        // Check formats first
        assertKaseFormat(input, inputFormat)
        assertKaseFormat(expectedOutput, expectedOutputFormat)
        // Then the conversion outputs (don't check bidirectional, since they can be diff)
        val biDirectionalConverter = expectedOutputFormat.converter(inputFormat)
        val convertOutput = biDirectionalConverter.convert(input)
        assertEquals(
            expectedOutput, convertOutput,
            "Converted output '$convertOutput' should equal expected output '$expectedOutput'"
        )
        // Also check cases of converted outputs to be sure
        assertKaseFormat(convertOutput, expectedOutputFormat)
        // Then finally verify dynamic conversion
        val dynamicConvertOutput = expectedOutputFormat.converter().convert(input)
        assertEquals(
            expectedOutput, dynamicConvertOutput,
            "Dynamically converted output '$dynamicConvertOutput' should equal expected output '$expectedOutput'"
        )
    }

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
        INPUTS_UNKNOWN.forEach {
            val format = KaseFormat.determine(it)
            assertNull(format, "$it is determined as $format")
        }
    }

    @Test
    fun testDetermine_LowerHyphen() {
        assertKaseFormat(INPUTS_LOWER_HYPHEN, KaseFormat.LOWER_HYPHEN)
    }

    @Test
    fun testDetermine_CapitalHyphen() {
        assertKaseFormat(INPUTS_CAPITAL_HYPHEN, KaseFormat.CAPITAL_HYPHEN)
    }

    @Test
    fun testDetermine_UpperHyphen() {
        assertKaseFormat(INPUTS_UPPER_HYPHEN, KaseFormat.UPPER_HYPHEN)
    }

    @Test
    fun testDetermine_LowerUnderscore() {
        assertKaseFormat(INPUTS_LOWER_UNDERSCORE, KaseFormat.LOWER_UNDERSCORE)
    }

    @Test
    fun testDetermine_CapitalUnderscore() {
        assertKaseFormat(INPUTS_CAPITAL_UNDERSCORE, KaseFormat.CAPITAL_UNDERSCORE)
    }

    @Test
    fun testDetermine_UpperUnderscore() {
        assertKaseFormat(INPUTS_UPPER_UNDERSCORE, KaseFormat.UPPER_UNDERSCORE)
    }

    @Test
    fun testDetermine_LowerCamel() {
        assertKaseFormat(INPUTS_LOWER_CAMEL, KaseFormat.LOWER_CAMEL)
    }

    @Test
    fun testDetermine_CapitalCamel() {
        assertKaseFormat(INPUTS_CAPITAL_CAMEL, KaseFormat.CAPITAL_CAMEL)
    }

    @Test
    fun testConvert_CapitalCamelToLowerCamel() {
        assertKaseConvert(INPUTS_CAPITAL_CAMEL_TO_LOWER_CAMEL,
            KaseFormat.CAPITAL_CAMEL, KaseFormat.LOWER_CAMEL)
    }

    @Test
    fun testConvert_LowerCamelToCapitalCamel() {
        assertKaseConvert(INPUTS_LOWER_CAMEL_TO_CAPITAL_CAMEL,
            KaseFormat.LOWER_CAMEL, KaseFormat.CAPITAL_CAMEL)
    }

    @Test
    fun testConvert_LowerCamelToUpperHyphen() {
        assertKaseConvert(INPUTS_LOWER_CAMEL_TO_UPPER_HYPEN,
            KaseFormat.LOWER_CAMEL, KaseFormat.UPPER_HYPHEN)
    }

    companion object {
        val INPUTS_CAPITAL_CAMEL = listOf(
            "EasyPeasy",
            "123SomethingElse",
            "ItWorks",
            "Hopefully",
            "Should123AlsoWork456",
            // Acronym words should also be supported
            "SomeJSONAcronym",
            "XMLThing"
        )
        val INPUTS_CAPITAL_CAMEL_TO_LOWER_CAMEL = mapOf(
            "XMLThing" to "xmlThing",
            "SomeJSONAcronym" to "someJsonAcronym",
            "Hopefully" to "hopefully",
            "ItWorks" to "itWorks"
        )

        val INPUTS_LOWER_CAMEL = listOf(
            "abcSomethingElse",
            "with123Numbers",
            "what",
            "123somethingElse",
            "some123thingNumbered"
        )
        val INPUTS_LOWER_CAMEL_TO_CAPITAL_CAMEL = mapOf(
            "abcSomethingElse" to "AbcSomethingElse",
            "with123Numbers" to "With123Numbers",
            "what" to "What",
            "some123thingNumbered" to "Some123thingNumbered"
        )
        val INPUTS_LOWER_CAMEL_TO_UPPER_HYPEN = mapOf(
            "abcSomethingElse" to "ABC-SOMETHING-ELSE",
            "with123Numbers" to "WITH123-NUMBERS",
            "some123thingNumbered" to "SOME123THING-NUMBERED",
            "otherThing" to "OTHER-THING"
        )

        val INPUTS_UPPER_UNDERSCORE = listOf(
            "A_",
            "_A",
            "ABC_DEF",
            "123_ABC_WITH_THINGS",
            "ABC",
            "123ABC"
        )

        val INPUTS_CAPITAL_UNDERSCORE = listOf(
            "Ab_Ab",
            "Ac_",
            "_Ab",
            "Something_Else_With_Words"
        )

        val INPUTS_LOWER_UNDERSCORE = listOf(
            "a_",
            "a_b",
            "_abc",
            "something_blah_123_other_stuff"
        )

        val INPUTS_UPPER_HYPHEN = listOf(
            "A-B",
            "A-B-C",
            "A-",
            "-A",
            "ABC-DEF"
        )

        val INPUTS_CAPITAL_HYPHEN = listOf(
            "Ab-Ab",
            "Ac-",
            "-Ab",
            "Whatever-Something-Else"
        )

        val INPUTS_LOWER_HYPHEN = listOf(
            "a-",
            "a-b",
            "-abc",
            "123-abc-rew-34-whatever"
        )

        val INPUTS_UNKNOWN = listOf(
            "abc-abc_abc",
            "ABC_abc",
            "",
            "-_",
            "!",
            "ABC-abc",
            "[]",
            " ",
            "A B C",
            // Other special characters should show as unknown
            "MaybeThough?",
            " Nope",
            "-not&this-either"
        )

    }

}
