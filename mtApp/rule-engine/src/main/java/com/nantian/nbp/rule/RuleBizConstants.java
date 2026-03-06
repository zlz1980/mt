/**
 * Copyright(c) 2017-2020 NANTIAN Electronic Information Co., Ltd.
 * Finance Business Service Platform (FBSP)
 *
 * @author:NANTIAN
 * @date:pengw at Jul 28, 2020 2:13:59 PM
 *
 */
package com.nantian.nbp.rule;

import java.util.regex.Pattern;

public class RuleBizConstants {

    /**
     * 获取{}内的字符
     */
    public static final Pattern BRACES_PATTERN = Pattern.compile("\\{(.*?)}");
    public static final String APP_RULE_ENGINE_ERR_KEY="APPRULEENGINEERROR: ";

}
