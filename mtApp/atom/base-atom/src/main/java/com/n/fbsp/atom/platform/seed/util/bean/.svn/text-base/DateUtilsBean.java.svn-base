package com.n.fbsp.atom.platform.seed.util.bean;

import com.nantian.nbp.flow.engine.service.api.Util;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Lianghaizhen
 */
@Util("DateUtils")
public class DateUtilsBean {
    //支持的日期格式
    private static final String [] DATE_FORMATS = {"yyyy-MM-dd","yyyyMMdd"};
    private static final Logger LOGGER = LoggerFactory.getLogger(DateUtilsBean.class);

    /**
     * 比较两个日期字符串的大小
     *
     * @param date1       日期字符串1
     * @param date2       日期字符串2
     * @param dateFormat1 日期格式1
     * @param dateFormat2 日期格式2
     * @return 如果date1大于date2返回1，date1等于date2返回0，date1小于date2返回-1
     * @throws IllegalArgumentException 如果日期字符串格式错误或格式不支持，则抛出此异常
     */
    public int compareDate(String date1, String date2, String dateFormat1, String dateFormat2) {
        int result;
        // 格式校验
        if (!checkFormat(dateFormat1)) {
            throw new IllegalArgumentException("无法识别的日期格式，仅支持yyyyMMdd、yyyy-MM-dd格式：" + dateFormat1);
        }
        if (!checkFormat(dateFormat2)) {
            throw new IllegalArgumentException("无法识别的日期格式，仅支持yyyyMMdd、yyyy-MM-dd格式：" + dateFormat2);
        }
        try {
            // 解析日期
            Date parsedDate1 = DateUtils.parseDateStrictly(date1, dateFormat1);
            Date parsedDate2 = DateUtils.parseDateStrictly(date2, dateFormat2);
            // 比较日期
            result = parsedDate1.compareTo(parsedDate2);
            String resultStr = result > 0 ? "大于" : result == 0 ? "等于" : "小于";
            LOGGER.debug("比较结果:日期1[{}]{}日期2[{}],", parsedDate1, resultStr, parsedDate2);
            return result;
        } catch (ParseException e) {
            LOGGER.error("日期格式错误,请检查输入的日期或格式[{}]", e.getMessage(), e);
            throw new IllegalArgumentException("日期格式错误,请检查输入的日期或格式：" + e.getMessage(), e);
        }
    }

    /**
     * 校验日期格式是否支持
     *
     * @param formatStr 日期格式字符串
     * @return 是否支持的日期格式
     */
    private boolean checkFormat(String formatStr) {
        return Arrays.asList(DATE_FORMATS).contains(formatStr);
    }

    /**
     * 将日期字符串从一种格式转换为另一种格式
     *
     * @param targetFormat   目标日期格式的模式
     * @param originalFormat 原始日期格式的模式
     * @param sourceDate     需要转换的日期字符串
     * @return 转换后的日期字符串
     *
     * @throws IllegalArgumentException 如果源日期字符串与原始格式不匹配，则抛出此异常
     */
    public String formatDate(String targetFormat, String originalFormat, String sourceDate) {
        try {
            // 创建目标格式的SimpleDateFormat对象
            SimpleDateFormat sdfSource = new SimpleDateFormat(originalFormat);
            // 创建原始格式的SimpleDateFormat对象
            SimpleDateFormat sdfTarget = new SimpleDateFormat(targetFormat);
            // 解析原始字符串为Date对象
            Date date = sdfSource.parse(sourceDate);
            // 使用目标格式的SimpleDateFormat对象将Date对象格式化为字符串，并返回
            return sdfTarget.format(date);
        } catch (ParseException e) {
            LOGGER.error("日期格式错误,请检查输入的日期或格式[{}]", e.getMessage(), e);
            throw new IllegalArgumentException("日期格式错误,请检查输入的日期或格式：" + e.getMessage(), e);
        }
    }

    /**
     * 补全日期年份
     *
     * @param sourceDate 待补充年份日期，格式必须为MMdd
     * @return 转换后的日期字符串，格式为yyyyMMdd
     *
     * @throws IllegalArgumentException 如果源日期字符串格式不匹配，则抛出此异常
     */
    public String completeYear(String sourceDate) {
        // 获取当前年份
        Calendar now = Calendar.getInstance();
        int currentYear = now.get(Calendar.YEAR);
        // 获取当前月日
        String mouthAndDay = DateFormatUtils.format(now, "MMdd");
        // 判断是否跨年
        if (sourceDate.compareTo(mouthAndDay) > 0) {
            // 如果输入的日期>当前日期，说明跨年，需要年份减 1
            currentYear = currentYear - 1;
        }
        // 拼接年份到待补全日期
        String completeDateStr = currentYear + sourceDate;
        try {
            // 格式化器：当前日期格式
            SimpleDateFormat currentFormatter = new SimpleDateFormat("yyyyMMdd");
            // 设置不宽容模式，严格解析日期，校验日期合法性
            currentFormatter.setLenient(false);
            currentFormatter.parse(completeDateStr);
            return completeDateStr;
        } catch (ParseException e) {
            LOGGER.error("源日期格式错误，仅支持MMdd格式：[{}]", e.getMessage(), e);
            throw new IllegalArgumentException("源日期格式错误，仅支持MMdd格式：" + sourceDate);
        }
    }

    /**
     * 将日期字符串从一种格式转换为另一种格式只支持yyyy-MM-dd与yyyyMMdd格式
     *
     * @param targetFormat   目标日期格式的模式
     * @param originalFormat 原始日期格式的模式
     * @param sourceDate     需要转换的日期字符串
     * @return 转换后的日期字符串
     *
     * @throws IllegalArgumentException 如果源日期字符串与原始格式不匹配或格式不支持，则抛出此异常
     */
    public String calculateDays(String targetFormat, String originalFormat, String sourceDate, int days) {
        try {
            // 格式校验
            if (!checkFormat(targetFormat)) {
                throw new IllegalArgumentException("无法识别的目标日期格式，仅支持yyyyMMdd、yyyy-MM-dd格式：" + targetFormat);
            }
            if (!checkFormat(originalFormat)) {
                throw new IllegalArgumentException("无法识别的原始日期格式，仅支持yyyyMMdd、yyyy-MM-dd格式：" + originalFormat);
            }
            // 创建原始格式的SimpleDateFormat对象
            SimpleDateFormat sdfSource = new SimpleDateFormat(originalFormat);
            // 创建目标格式的SimpleDateFormat对象
            SimpleDateFormat sdfTarget = new SimpleDateFormat(targetFormat);
            String resultDate;
            // 解析原始字符串为Date对象
            Date date = sdfSource.parse(sourceDate);
            // 使用 Calendar 对日期进行加减
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_MONTH, days);
            return sdfTarget.format(calendar.getTime());
        } catch (ParseException e) {
            LOGGER.error("日期格式错误,请检查输入的日期或格式[{}]", e.getMessage(), e);
            throw new IllegalArgumentException("日期格式错误,请检查输入的日期或格式：" + e.getMessage(), e);
        }
    }

}

