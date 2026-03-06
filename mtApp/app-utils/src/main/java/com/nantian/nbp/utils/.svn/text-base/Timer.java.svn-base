/**
 * Copyright(c) 2017-2020 NANTIAN Electronic Information Co., Ltd.
 * Finance Business Service Platform (FBSP)
 *
 * @author:NANTIAN
 * @date:pengw at Jul 28, 2020 2:13:59 PM
 */
package com.nantian.nbp.utils;

/**
 * 执行耗时计时工具类
 *
 * @author Administrator
 */
public class Timer {
    /**
     * 获取当前的系统时间（以纳秒为单位）
     * <p>
     * 此方法用于获取一个相对时间戳，主要用于性能测试或测量代码段的执行时间
     * 更适合用于测量时间间隔，尤其是在需要高精度测量的场景下pom.xml
     *
     * @return 返回当前系统时间的纳秒值，这是一个相对时间，通常用于计算时间差
     */
    public static Long getStartTime() {
        return System.nanoTime();
    }

    /**
     * 计算从给定开始时间到当前时间的已用时间（毫秒）.
     * <p>
     * 此方法主要用于测量一段代码或操作的执行时间. 它通过计算当前时间与开始时间之间的差值来实现.
     * 如果计算出的已用时间大于零，则将其转换为毫秒并返回；否则，返回0.
     *
     * @param start 开始时间，以纳秒为单位. 应使用System.nanoTime()获取此值.
     * @return 已用时间，以毫秒为单位. 如果计算结果小于等于零，则返回0.
     */
    public static Long getUsedTime(Long start) {
        // 计算当前时间与开始时间之间的差值，得到已用时间（纳秒）
        long useTime = System.nanoTime() - start;
        // 判断已用时间是否大于零
        if (useTime > 0) {
            // 如果已用时间大于零，将其转换为毫秒并返回
            return useTime / 1000000;
        } else {
            // 如果已用时间小于等于零，返回0
            return 0L;
        }
    }
}
