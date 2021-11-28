package com.example.pantryparserbackend.Util;

import java.util.Arrays;

public class UnitUtil {
    private static final String[] validUnits = {
            "ounces",
            "fluid ounces",
            "cups",
            "teaspoons",
            "tablespoons",
            "gallons",
            "pints",
            "quarts",
            "liters",
            "grams",
            "count"
    };

    public static boolean isValidUnit(String unit) {
        return Arrays.asList(validUnits).contains(unit);
    }

}
