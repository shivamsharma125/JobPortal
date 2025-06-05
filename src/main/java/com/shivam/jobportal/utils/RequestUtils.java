package com.shivam.jobportal.utils;

import java.util.List;
import java.util.Set;

public class RequestUtils {
    public static boolean isInvalidId(Long id){
        return id == null || id <= 0;
    }

    public static boolean isEmptyParam(String param){
        return param == null || param.isBlank();
    }

    public static <T> boolean isEmptyParam(List<T> list){
        return list == null || list.isEmpty();
    }

    public static <T> boolean isEmptyParam(Set<T> set){
        return set == null || set.isEmpty();
    }

    public static boolean isNull(Object obj){
        return obj == null;
    }

    public static boolean isInvalidExperience(Integer experience){
        return experience == null || experience < 0;
    }

    public static boolean isInvalidExperienceRange(Integer minExperience, Integer maxExperience){
        return isInvalidExperience(minExperience) ||
                isInvalidExperience(maxExperience) ||
                minExperience > maxExperience;
    }

    public static boolean isInvalidSalary(Double salary){
        return salary == null || salary < 0;
    }

    public static boolean isInvalidSalaryRange(Double minSalary, Double maxSalary){
        return isInvalidSalary(minSalary) ||
                isInvalidSalary(maxSalary) ||
                minSalary > maxSalary;
    }

    public static boolean isInvalidNoticePeriod(Integer noticePeriod){
        return noticePeriod == null || noticePeriod < 0;
    }

    public static boolean isInvalidNumberOfDays(int days) {
        return days <= 0;
    }
}
