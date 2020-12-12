package ru.sps.utils;

import java.math.BigDecimal;
import java.util.List;

public class NumberUtils {

    public static BigDecimal calculateMedianValue(List<BigDecimal> values) {
        BigDecimal result;
        if (values.size() <= 1) {
            result = BigDecimal.ZERO;
        } else {
            var averageIndex = values.size()/2;
            if (values.size() % 2 == 0) {
                result = (values.get(averageIndex - 1).add(values.get(averageIndex)).divide(BigDecimal.valueOf(2)));
            } else {
                result = values.get(averageIndex);
            }
        }
        return result;
    }
}
