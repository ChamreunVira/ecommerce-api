package com.kh.vira_dev.ecommerceapi.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProfanityFilterTest {

    @Test
    void testCleanWithNormalInput() {
        String input = "កុំព្យូទ័រនេះល្អណាស់";
        String expected = "កុំព្យូទ័រនេះល្អណាស់";
        assertEquals(expected, ProfanityFilter.clean(input));
    }

    @Test
    void testCleanWithBlacklistedKhmerVulgarWords() {
        // Test payload comment
        String input = "កុំព្រូទ័រដូចអាចន៍អញ្ចឹងហា";
        String expected = "កុំព្រូទ័រដូច***អញ្ចឹងហា";
        assertEquals(expected, ProfanityFilter.clean(input));

        // Test with other blacklisted word
        String input2 = "កុំនិយាយពាក្យអាចម៍";
        String expected2 = "កុំនិយាយពាក្យ***";
        assertEquals(expected2, ProfanityFilter.clean(input2));
    }

    @Test
    void testCleanWithNullOrBlankInput() {
        assertNull(ProfanityFilter.clean(null));
        assertEquals("", ProfanityFilter.clean(""));
        assertEquals("   ", ProfanityFilter.clean("   "));
    }
}
