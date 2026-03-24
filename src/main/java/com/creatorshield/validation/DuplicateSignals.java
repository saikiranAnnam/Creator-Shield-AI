package com.creatorshield.validation;

public record DuplicateSignals(
        double duplicateScore, double maxTitleSimilarity, boolean exactDuplicateOtherCreator) {}
