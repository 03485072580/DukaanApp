package com.example.fasih.dukaanapp.utils;

import android.text.TextUtils;

/**
 * Created by Fasih on 01/04/19.
 */

public class StringManipulations {

    public static String toLowerCase(String convertString) {
        return convertString.toLowerCase();
    }

    public static String toLowerCaseUsername(String convertString) {
        String nonSpacedString = convertString.replace(" ", "");
        return nonSpacedString.toLowerCase();
    }

    public static Boolean contains(String originalString, String searchString) {
        if (!TextUtils.isEmpty(originalString)) {
            return originalString.contains(searchString);
        }
        return null;
    }

    public static String getFirstName(String displayName) {
        return displayName.split(" ")[0];
    }

    public static String getLastName(String displayName) {
        return displayName.substring(displayName.indexOf(" ") + 1, displayName.length());
    }
}
