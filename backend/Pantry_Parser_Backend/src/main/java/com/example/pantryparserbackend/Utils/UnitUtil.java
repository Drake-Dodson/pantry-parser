package com.example.pantryparserbackend.Utils;

import java.util.Arrays;
import java.util.List;

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

    public static List<String> getValidUnits() {
        return Arrays.asList(validUnits);
    }
}
