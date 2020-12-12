package ru.sps.utils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class PeriodHelper {

    public static List<BigDecimal> calculatePeriodsInDays(List<Date> dates) {
        if (dates.size() < 2) return Collections.emptyList();
        var soredDates = dates.stream().sorted();
        var periods = new ArrayList<BigDecimal>();
        var iterator = soredDates.iterator();
        var previousDate = iterator.next();
        while (iterator.hasNext()) {
            var nextDate = iterator.next();
            periods.add(new BigDecimal(calculatePeriodBetweenDates(previousDate, nextDate)));
            previousDate = nextDate;
        }
        return periods;
    }

    public static int calculatePeriodBetweenDates(Date startDate, Date endDate) {
        var start = toLocalDateTime(startDate);
        var end = toLocalDateTime(endDate);
        return (int)ChronoUnit.DAYS.between(start, end);
    }

    public static Date addDays(Date startDate, int days) {
        var start = toLocalDateTime(startDate);
        return toDate(ChronoUnit.DAYS.addTo(start, days));
    }

    private static LocalDate toLocalDateTime(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private static Date toDate(LocalDate localDate) {
        return java.util.Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }
}
