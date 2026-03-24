package com.creatorshield.util;

import java.util.Locale;

public final class MetadataNormalizer {

    private MetadataNormalizer() {}

    public static String normalize(String value) {
        if (value == null || value.isBlank()) {
            return "";
        }
        return value
                .toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9\\s]", " ")
                .replaceAll("\\s+", " ")
                .trim();
    }
}
