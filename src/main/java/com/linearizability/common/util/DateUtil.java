package com.linearizability.common.util;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

/**
 * 日期时间工具类 提供日期时间格式化、解析、计算、比较等常用方法
 *
 * @author ZhangBoyuan
 * @since  2025-11-07
 */
public class DateUtil {

    /**
     * 默认日期时间格式
     */
    public static final String DEFAULT_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * 默认日期格式
     */
    public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";

    /**
     * 默认时间格式
     */
    public static final String DEFAULT_TIME_PATTERN = "HH:mm:ss";

    /**
     * ISO日期时间格式
     */
    public static final String ISO_DATETIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";

    /**
     * 默认时区
     */
    public static final ZoneId DEFAULT_ZONE = ZoneId.systemDefault();

    /**
     * 默认日期时间格式化器
     */
    private static final DateTimeFormatter DEFAULT_DATETIME_FORMATTER = DateTimeFormatter
            .ofPattern(DEFAULT_DATETIME_PATTERN);

    /**
     * 默认日期格式化器
     */
    private static final DateTimeFormatter DEFAULT_DATE_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_DATE_PATTERN);

    /**
     * 默认时间格式化器
     */
    private static final DateTimeFormatter DEFAULT_TIME_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_TIME_PATTERN);

    // ==================== 格式化 ====================

    /**
     * 格式化LocalDateTime为字符串（默认格式）
     *
     * @param  dateTime 日期时间
     * @return          格式化后的字符串
     */
    public static String format(LocalDateTime dateTime) {
        return dateTime == null ? null : dateTime.format(DEFAULT_DATETIME_FORMATTER);
    }

    /**
     * 格式化LocalDateTime为字符串（指定格式）
     *
     * @param  dateTime 日期时间
     * @param  pattern  格式 pattern
     * @return          格式化后的字符串
     */
    public static String format(LocalDateTime dateTime, String pattern) {
        if (dateTime == null) {
            return null;
        }
        return DateTimeFormatter.ofPattern(pattern).format(dateTime);
    }

    /**
     * 格式化LocalDate为字符串（默认格式）
     *
     * @param  date 日期
     * @return      格式化后的字符串
     */
    public static String format(LocalDate date) {
        return date == null ? null : date.format(DEFAULT_DATE_FORMATTER);
    }

    /**
     * 格式化LocalDate为字符串（指定格式）
     *
     * @param  date    日期
     * @param  pattern 格式 pattern
     * @return         格式化后的字符串
     */
    public static String format(LocalDate date, String pattern) {
        if (date == null) {
            return null;
        }
        return DateTimeFormatter.ofPattern(pattern).format(date);
    }

    /**
     * 格式化LocalTime为字符串（默认格式）
     *
     * @param  time 时间
     * @return      格式化后的字符串
     */
    public static String format(LocalTime time) {
        return time == null ? null : time.format(DEFAULT_TIME_FORMATTER);
    }

    /**
     * 格式化LocalTime为字符串（指定格式）
     *
     * @param  time    时间
     * @param  pattern 格式 pattern
     * @return         格式化后的字符串
     */
    public static String format(LocalTime time, String pattern) {
        if (time == null) {
            return null;
        }
        return DateTimeFormatter.ofPattern(pattern).format(time);
    }

    // ==================== 解析 ====================

    /**
     * 解析字符串为LocalDateTime（默认格式）
     *
     * @param  dateTimeStr 日期时间字符串
     * @return             LocalDateTime对象
     */
    public static LocalDateTime parseDateTime(String dateTimeStr) {
        if (dateTimeStr == null || dateTimeStr.trim().isEmpty()) {
            return null;
        }
        return LocalDateTime.parse(dateTimeStr, DEFAULT_DATETIME_FORMATTER);
    }

    /**
     * 解析字符串为LocalDateTime（指定格式）
     *
     * @param  dateTimeStr 日期时间字符串
     * @param  pattern     格式 pattern
     * @return             LocalDateTime对象
     */
    public static LocalDateTime parseDateTime(String dateTimeStr, String pattern) {
        if (dateTimeStr == null || dateTimeStr.trim().isEmpty()) {
            return null;
        }
        return LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 解析字符串为LocalDate（默认格式）
     *
     * @param  dateStr 日期字符串
     * @return         LocalDate对象
     */
    public static LocalDate parseDate(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return null;
        }
        return LocalDate.parse(dateStr, DEFAULT_DATE_FORMATTER);
    }

    /**
     * 解析字符串为LocalDate（指定格式）
     *
     * @param  dateStr 日期字符串
     * @param  pattern 格式 pattern
     * @return         LocalDate对象
     */
    public static LocalDate parseDate(String dateStr, String pattern) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return null;
        }
        return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 解析字符串为LocalTime（默认格式）
     *
     * @param  timeStr 时间字符串
     * @return         LocalTime对象
     */
    public static LocalTime parseTime(String timeStr) {
        if (timeStr == null || timeStr.trim().isEmpty()) {
            return null;
        }
        return LocalTime.parse(timeStr, DEFAULT_TIME_FORMATTER);
    }

    /**
     * 解析字符串为LocalTime（指定格式）
     *
     * @param  timeStr 时间字符串
     * @param  pattern 格式 pattern
     * @return         LocalTime对象
     */
    public static LocalTime parseTime(String timeStr, String pattern) {
        if (timeStr == null || timeStr.trim().isEmpty()) {
            return null;
        }
        return LocalTime.parse(timeStr, DateTimeFormatter.ofPattern(pattern));
    }

    // ==================== 日期计算 ====================

    /**
     * 增加天数
     *
     * @param  dateTime 日期时间
     * @param  days     天数
     * @return          计算后的日期时间
     */
    public static LocalDateTime plusDays(LocalDateTime dateTime, long days) {
        return dateTime == null ? null : dateTime.plusDays(days);
    }

    /**
     * 减少天数
     *
     * @param  dateTime 日期时间
     * @param  days     天数
     * @return          计算后的日期时间
     */
    public static LocalDateTime minusDays(LocalDateTime dateTime, long days) {
        return dateTime == null ? null : dateTime.minusDays(days);
    }

    /**
     * 增加小时数
     *
     * @param  dateTime 日期时间
     * @param  hours    小时数
     * @return          计算后的日期时间
     */
    public static LocalDateTime plusHours(LocalDateTime dateTime, long hours) {
        return dateTime == null ? null : dateTime.plusHours(hours);
    }

    /**
     * 减少小时数
     *
     * @param  dateTime 日期时间
     * @param  hours    小时数
     * @return          计算后的日期时间
     */
    public static LocalDateTime minusHours(LocalDateTime dateTime, long hours) {
        return dateTime == null ? null : dateTime.minusHours(hours);
    }

    /**
     * 增加月数
     *
     * @param  dateTime 日期时间
     * @param  months   月数
     * @return          计算后的日期时间
     */
    public static LocalDateTime plusMonths(LocalDateTime dateTime, long months) {
        return dateTime == null ? null : dateTime.plusMonths(months);
    }

    /**
     * 减少月数
     *
     * @param  dateTime 日期时间
     * @param  months   月数
     * @return          计算后的日期时间
     */
    public static LocalDateTime minusMonths(LocalDateTime dateTime, long months) {
        return dateTime == null ? null : dateTime.minusMonths(months);
    }

    /**
     * 增加年数
     *
     * @param  dateTime 日期时间
     * @param  years    年数
     * @return          计算后的日期时间
     */
    public static LocalDateTime plusYears(LocalDateTime dateTime, long years) {
        return dateTime == null ? null : dateTime.plusYears(years);
    }

    /**
     * 减少年数
     *
     * @param  dateTime 日期时间
     * @param  years    年数
     * @return          计算后的日期时间
     */
    public static LocalDateTime minusYears(LocalDateTime dateTime, long years) {
        return dateTime == null ? null : dateTime.minusYears(years);
    }

    /**
     * 增加天数（LocalDate）
     *
     * @param  date 日期
     * @param  days 天数
     * @return      计算后的日期
     */
    public static LocalDate plusDays(LocalDate date, long days) {
        return date == null ? null : date.plusDays(days);
    }

    /**
     * 减少天数（LocalDate）
     *
     * @param  date 日期
     * @param  days 天数
     * @return      计算后的日期
     */
    public static LocalDate minusDays(LocalDate date, long days) {
        return date == null ? null : date.minusDays(days);
    }

    /**
     * 增加月数（LocalDate）
     *
     * @param  date   日期
     * @param  months 月数
     * @return        计算后的日期
     */
    public static LocalDate plusMonths(LocalDate date, long months) {
        return date == null ? null : date.plusMonths(months);
    }

    /**
     * 减少月数（LocalDate）
     *
     * @param  date   日期
     * @param  months 月数
     * @return        计算后的日期
     */
    public static LocalDate minusMonths(LocalDate date, long months) {
        return date == null ? null : date.minusMonths(months);
    }

    /**
     * 增加年数（LocalDate）
     *
     * @param  date  日期
     * @param  years 年数
     * @return       计算后的日期
     */
    public static LocalDate plusYears(LocalDate date, long years) {
        return date == null ? null : date.plusYears(years);
    }

    /**
     * 减少年数（LocalDate）
     *
     * @param  date  日期
     * @param  years 年数
     * @return       计算后的日期
     */
    public static LocalDate minusYears(LocalDate date, long years) {
        return date == null ? null : date.minusYears(years);
    }

    // ==================== 日期比较 ====================

    /**
     * 判断日期时间1是否在日期时间2之前
     *
     * @param  dateTime1 日期时间1
     * @param  dateTime2 日期时间2
     * @return           true表示dateTime1在dateTime2之前
     */
    public static boolean isBefore(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        if (dateTime1 == null || dateTime2 == null) {
            return false;
        }
        return dateTime1.isBefore(dateTime2);
    }

    /**
     * 判断日期时间1是否在日期时间2之后
     *
     * @param  dateTime1 日期时间1
     * @param  dateTime2 日期时间2
     * @return           true表示dateTime1在dateTime2之后
     */
    public static boolean isAfter(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        if (dateTime1 == null || dateTime2 == null) {
            return false;
        }
        return dateTime1.isAfter(dateTime2);
    }

    /**
     * 判断两个日期时间是否相等
     *
     * @param  dateTime1 日期时间1
     * @param  dateTime2 日期时间2
     * @return           true表示相等
     */
    public static boolean isEqual(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        if (dateTime1 == null || dateTime2 == null) {
            return false;
        }
        return dateTime1.isEqual(dateTime2);
    }

    /**
     * 判断日期时间是否在指定范围内
     *
     * @param  dateTime 日期时间
     * @param  start    开始时间
     * @param  end      结束时间
     * @return          true表示在范围内
     */
    public static boolean isBetween(LocalDateTime dateTime, LocalDateTime start, LocalDateTime end) {
        if (dateTime == null || start == null || end == null) {
            return false;
        }
        return !dateTime.isBefore(start) && !dateTime.isAfter(end);
    }

    /**
     * 判断日期1是否在日期2之前
     *
     * @param  date1 日期1
     * @param  date2 日期2
     * @return       true表示date1在date2之前
     */
    public static boolean isBefore(LocalDate date1, LocalDate date2) {
        if (date1 == null || date2 == null) {
            return false;
        }
        return date1.isBefore(date2);
    }

    /**
     * 判断日期1是否在日期2之后
     *
     * @param  date1 日期1
     * @param  date2 日期2
     * @return       true表示date1在date2之后
     */
    public static boolean isAfter(LocalDate date1, LocalDate date2) {
        if (date1 == null || date2 == null) {
            return false;
        }
        return date1.isAfter(date2);
    }

    /**
     * 判断日期是否在指定范围内
     *
     * @param  date  日期
     * @param  start 开始日期
     * @param  end   结束日期
     * @return       true表示在范围内
     */
    public static boolean isBetween(LocalDate date, LocalDate start, LocalDate end) {
        if (date == null || start == null || end == null) {
            return false;
        }
        return !date.isBefore(start) && !date.isAfter(end);
    }

    /**
     * 计算两个日期时间之间的天数差
     *
     * @param  start 开始时间
     * @param  end   结束时间
     * @return       天数差
     */
    public static long daysBetween(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) {
            return 0;
        }
        return ChronoUnit.DAYS.between(start, end);
    }

    /**
     * 计算两个日期之间的天数差
     *
     * @param  start 开始日期
     * @param  end   结束日期
     * @return       天数差
     */
    public static long daysBetween(LocalDate start, LocalDate end) {
        if (start == null || end == null) {
            return 0;
        }
        return ChronoUnit.DAYS.between(start, end);
    }

    /**
     * 计算两个日期时间之间的小时差
     *
     * @param  start 开始时间
     * @param  end   结束时间
     * @return       小时差
     */
    public static long hoursBetween(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) {
            return 0;
        }
        return ChronoUnit.HOURS.between(start, end);
    }

    /**
     * 计算两个日期时间之间的分钟差
     *
     * @param  start 开始时间
     * @param  end   结束时间
     * @return       分钟差
     */
    public static long minutesBetween(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) {
            return 0;
        }
        return ChronoUnit.MINUTES.between(start, end);
    }

    /**
     * 计算两个日期时间之间的秒数差
     *
     * @param  start 开始时间
     * @param  end   结束时间
     * @return       秒数差
     */
    public static long secondsBetween(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) {
            return 0;
        }
        return ChronoUnit.SECONDS.between(start, end);
    }

    // ==================== 时间戳转换 ====================

    /**
     * LocalDateTime转时间戳（毫秒）
     *
     * @param  dateTime 日期时间
     * @return          时间戳（毫秒）
     */
    public static long toTimestamp(LocalDateTime dateTime) {
        if (dateTime == null) {
            return 0;
        }
        return dateTime.atZone(DEFAULT_ZONE).toInstant().toEpochMilli();
    }

    /**
     * LocalDateTime转时间戳（秒）
     *
     * @param  dateTime 日期时间
     * @return          时间戳（秒）
     */
    public static long toTimestampSeconds(LocalDateTime dateTime) {
        if (dateTime == null) {
            return 0;
        }
        return dateTime.atZone(DEFAULT_ZONE).toEpochSecond();
    }

    /**
     * 时间戳（毫秒）转LocalDateTime
     *
     * @param  timestamp 时间戳（毫秒）
     * @return           LocalDateTime对象
     */
    public static LocalDateTime fromTimestamp(long timestamp) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), DEFAULT_ZONE);
    }

    /**
     * 时间戳（秒）转LocalDateTime
     *
     * @param  timestamp 时间戳（秒）
     * @return           LocalDateTime对象
     */
    public static LocalDateTime fromTimestampSeconds(long timestamp) {
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), DEFAULT_ZONE);
    }

    /**
     * Date转LocalDateTime
     *
     * @param  date Date对象
     * @return      LocalDateTime对象
     */
    public static LocalDateTime fromDate(Date date) {
        if (date == null) {
            return null;
        }
        return LocalDateTime.ofInstant(date.toInstant(), DEFAULT_ZONE);
    }

    /**
     * LocalDateTime转Date
     *
     * @param  dateTime LocalDateTime对象
     * @return          Date对象
     */
    public static Date toDate(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return Date.from(dateTime.atZone(DEFAULT_ZONE).toInstant());
    }

    // ==================== 时区转换 ====================

    /**
     * 转换时区
     *
     * @param  dateTime 日期时间
     * @param  zoneId   目标时区
     * @return          转换后的日期时间
     */
    public static LocalDateTime convertZone(LocalDateTime dateTime, ZoneId zoneId) {
        if (dateTime == null || zoneId == null) {
            return dateTime;
        }
        return dateTime.atZone(DEFAULT_ZONE).withZoneSameInstant(zoneId).toLocalDateTime();
    }

    /**
     * 转换时区（指定源时区和目标时区）
     *
     * @param  dateTime   日期时间
     * @param  sourceZone 源时区
     * @param  targetZone 目标时区
     * @return            转换后的日期时间
     */
    public static LocalDateTime convertZone(LocalDateTime dateTime, ZoneId sourceZone, ZoneId targetZone) {
        if (dateTime == null || sourceZone == null || targetZone == null) {
            return dateTime;
        }
        return dateTime.atZone(sourceZone).withZoneSameInstant(targetZone).toLocalDateTime();
    }

    /**
     * 获取当前时间戳（毫秒）
     *
     * @return 当前时间戳
     */
    public static long currentTimestamp() {
        return System.currentTimeMillis();
    }

    /**
     * 获取当前时间戳（秒）
     *
     * @return 当前时间戳
     */
    public static long currentTimestampSeconds() {
        return System.currentTimeMillis() / 1000;
    }

    /**
     * 获取当前日期时间
     *
     * @return 当前日期时间
     */
    public static LocalDateTime now() {
        return LocalDateTime.now();
    }

    /**
     * 获取当前日期
     *
     * @return 当前日期
     */
    public static LocalDate today() {
        return LocalDate.now();
    }

    // ==================== 工作日计算 ====================

    /**
     * 判断是否为工作日（周一到周五）
     *
     * @param  date 日期
     * @return      true表示工作日
     */
    public static boolean isWorkday(LocalDate date) {
        if (date == null) {
            return false;
        }
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY;
    }

    /**
     * 判断是否为周末（周六和周日）
     *
     * @param  date 日期
     * @return      true表示周末
     */
    public static boolean isWeekend(LocalDate date) {
        if (date == null) {
            return false;
        }
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
    }

    /**
     * 获取下一个工作日
     *
     * @param  date 日期
     * @return      下一个工作日
     */
    public static LocalDate nextWorkday(LocalDate date) {
        if (date == null) {
            return null;
        }
        LocalDate next = date.plusDays(1);
        while (!isWorkday(next)) {
            next = next.plusDays(1);
        }
        return next;
    }

    /**
     * 获取上一个工作日
     *
     * @param  date 日期
     * @return      上一个工作日
     */
    public static LocalDate previousWorkday(LocalDate date) {
        if (date == null) {
            return null;
        }
        LocalDate previous = date.minusDays(1);
        while (!isWorkday(previous)) {
            previous = previous.minusDays(1);
        }
        return previous;
    }

    /**
     * 计算两个日期之间的工作日数量（不包括开始和结束日期）
     *
     * @param  start 开始日期
     * @param  end   结束日期
     * @return       工作日数量
     */
    public static long workdaysBetween(LocalDate start, LocalDate end) {
        if (start == null || end == null) {
            return 0;
        }
        if (start.isAfter(end)) {
            return 0;
        }
        long count = 0;
        LocalDate current = start.plusDays(1);
        while (current.isBefore(end)) {
            if (isWorkday(current)) {
                count++;
            }
            current = current.plusDays(1);
        }
        return count;
    }

    /**
     * 计算两个日期之间的工作日数量（包括开始和结束日期）
     *
     * @param  start 开始日期
     * @param  end   结束日期
     * @return       工作日数量
     */
    public static long workdaysBetweenInclusive(LocalDate start, LocalDate end) {
        if (start == null || end == null) {
            return 0;
        }
        if (start.isAfter(end)) {
            return 0;
        }
        long count = 0;
        LocalDate current = start;
        while (!current.isAfter(end)) {
            if (isWorkday(current)) {
                count++;
            }
            current = current.plusDays(1);
        }
        return count;
    }

    /**
     * 获取指定日期所在周的开始日期（周一）
     *
     * @param  date 日期
     * @return      周开始日期
     */
    public static LocalDate getWeekStart(LocalDate date) {
        if (date == null) {
            return null;
        }
        return date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
    }

    /**
     * 获取指定日期所在周的结束日期（周日）
     *
     * @param  date 日期
     * @return      周结束日期
     */
    public static LocalDate getWeekEnd(LocalDate date) {
        if (date == null) {
            return null;
        }
        return date.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
    }

    /**
     * 获取指定日期所在月的开始日期
     *
     * @param  date 日期
     * @return      月开始日期
     */
    public static LocalDate getMonthStart(LocalDate date) {
        if (date == null) {
            return null;
        }
        return date.with(TemporalAdjusters.firstDayOfMonth());
    }

    /**
     * 获取指定日期所在月的结束日期
     *
     * @param  date 日期
     * @return      月结束日期
     */
    public static LocalDate getMonthEnd(LocalDate date) {
        if (date == null) {
            return null;
        }
        return date.with(TemporalAdjusters.lastDayOfMonth());
    }

    /**
     * 获取指定日期所在年的开始日期
     *
     * @param  date 日期
     * @return      年开始日期
     */
    public static LocalDate getYearStart(LocalDate date) {
        if (date == null) {
            return null;
        }
        return date.with(TemporalAdjusters.firstDayOfYear());
    }

    /**
     * 获取指定日期所在年的结束日期
     *
     * @param  date 日期
     * @return      年结束日期
     */
    public static LocalDate getYearEnd(LocalDate date) {
        if (date == null) {
            return null;
        }
        return date.with(TemporalAdjusters.lastDayOfYear());
    }
}
