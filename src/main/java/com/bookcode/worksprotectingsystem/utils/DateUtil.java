package com.bookcode.worksprotectingsystem.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.*;
import java.time.format.DateTimeFormatter;

import static java.time.temporal.TemporalAdjusters.firstDayOfMonth;

/**
 * @author 明明如月
 * @date 2018/11/14
 */
public class DateUtil {
    public static DateTimeFormatter defaultDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static DateTimeFormatter simpleDateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");

    public static DateTimeFormatter defaultFormatterWithTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * '前一天的日期
     */
    public static LocalDate lastDay(String date, DateTimeFormatter dateTimeFormatter) {
        LocalDate localDate = LocalDate.parse(date, dateTimeFormatter);
        return localDate.minusDays(1);
    }

    /**
     * 本周一的日期
     */
    public static LocalDate thisWeekFirstDay(String date, DateTimeFormatter dateTimeFormatter) {
        LocalDate localDate = LocalDate.parse(date, dateTimeFormatter);
        return localDate.with(DayOfWeek.MONDAY);
    }

    /**
     * 上周一
     */
    public static LocalDate lastWeekFirstDay(String date, DateTimeFormatter dateTimeFormatter) {
        LocalDate localDate = LocalDate.parse(date, dateTimeFormatter);
        return localDate.minusDays(7).with(DayOfWeek.MONDAY);
    }

    /**
     * 本月第一天
     */
    public static LocalDate thisMonthFirstDay(String date, DateTimeFormatter dateTimeFormatter) {
        LocalDate localDate = LocalDate.parse(date, dateTimeFormatter);
        return localDate.with(firstDayOfMonth());
    }

    /**
     * 上月第一天
     */
    public static LocalDate lastMonthFirstDay(String date, DateTimeFormatter dateTimeFormatter) {
        LocalDate localDate = LocalDate.parse(date, dateTimeFormatter);
        return localDate.minusMonths(1).with(firstDayOfMonth());
    }

    /**
     * 两个时间之间间隔了多少年
     */
    public static BigDecimal year(long low, long high) {
        Instant instant1 = Instant.ofEpochMilli(low);
        Instant instant2 = Instant.ofEpochMilli(high);

        LocalDate localDate1 = LocalDateTime.ofInstant(instant1, ZoneId.systemDefault()).toLocalDate();
        LocalDate localDate2 = LocalDateTime.ofInstant(instant2, ZoneId.systemDefault()).toLocalDate();
        Period period = Period.between(
                localDate1
                , localDate2
        );
        int years = period.getYears();
        int months = period.getMonths();
        BigDecimal monthToYear = new BigDecimal(months).divide(BigDecimal.valueOf(12), 2, RoundingMode.DOWN);
        return BigDecimal.valueOf(years).add(monthToYear);
    }

    /**
     * 是否是同一天
     */
    public static boolean isSameDateByUnixTime(long time1, long time2) {
        LocalDateTime dateTime1 = Instant.ofEpochMilli(time1).atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime dateTime2 = Instant.ofEpochMilli(time2).atZone(ZoneId.systemDefault()).toLocalDateTime();
        return dateTime1.toLocalDate().isEqual(dateTime2.toLocalDate());
    }

}
