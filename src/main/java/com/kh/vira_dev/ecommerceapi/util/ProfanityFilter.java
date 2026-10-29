package com.kh.vira_dev.ecommerceapi.util;

import java.util.Arrays;
import java.util.List;

public class ProfanityFilter {

    private static final List<String> BLACKLIST = Arrays.asList(
            "អាចន៍",
            "អាចម៍",
            "មិនល្អ",
            "not good"
    );

    public static String clean(String input) {
        if (input == null || input.isBlank()) {
            return input;
        }
        String cleaned = input;
        for (String word : BLACKLIST) {
            cleaned = cleaned.replace(word, "***");
        }
        return cleaned;
    }
}
