package com.nantian.nbp.utils;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateUtil {

    private static final DateTimeFormatter STAND_DATE = DateTimeFormatter.ofPattern("yyyyMMdd");

    private static final DateTimeFormatter STAND_TIME = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");

    private static final DateTimeFormatter STAND_DATE_TIME = DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss");

    private static final DateTimeFormatter MILL_DATE_TIME = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    public static LocalDateTime getNowLocalDateTime() {
        return  LocalDateTime.now(ZoneId.of("Asia/Shanghai"));
    }

    public static String getDayTimeStr(String format) {
        Date date = new Date();
        SimpleDateFormat sf = new SimpleDateFormat(format);
        return sf.format(date);
    }

    /**
     * 当前系统日期时间yyyyMMdd HH:mm:ss
     * @return yyyyMMdd HH:mm:ss字符串
     */
    public static String getNowDateTime() {
        return STAND_DATE_TIME.format(getNowLocalDateTime());
    }

    /**
     * 当前系统日期时间yyyyMMddHHmmss
     * @return yyyyMMddHHmmss字符串
     */
    public static String getNowMillDateTime() {
        return MILL_DATE_TIME.format(getNowLocalDateTime());
    }

    /**
     * 当前系统日期yyyyMMdd
     * @return yyyyMMdd字符串
     */
    public static String getNowDate() {
        return STAND_DATE.format(getNowLocalDateTime());
    }

    /**
     * 当前系统时间HHmmss
     * @return HHmmss字符串
     */
    public static String getNowTime() {
        return STAND_TIME.format(getNowLocalDateTime());
    }

    /**
     * 当前系统日期时间，分别为yyyyMMdd，HHmmss字符串
     * @return SysDateTime
     */
    public static SysDateTime getNowDataTime() {
        LocalDateTime now = getNowLocalDateTime();
        SysDateTime sysDateTime = new SysDateTime();
        sysDateTime.setDate(STAND_DATE.format(now));
        sysDateTime.setTime(STAND_TIME.format(now));
        return sysDateTime;
    }

    public static class SysDateTime{
        private String date;
        private String time;

        public SysDateTime() {
        }

        public SysDateTime(String date, String time) {
            this.date = date;
            this.time = time;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }

}
