package com.shivam.jobportal.utils;

import java.util.Set;

public class StringUtils {
    public static boolean isEmpty(String str){
        return str == null || str.isBlank();
    }

    public static boolean isEmpty(Set<Long> set){
        return set == null || set.isEmpty();
    }
}
