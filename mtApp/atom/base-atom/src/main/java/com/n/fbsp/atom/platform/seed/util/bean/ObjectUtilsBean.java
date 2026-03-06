package com.n.fbsp.atom.platform.seed.util.bean;

import com.nantian.nbp.flow.engine.service.api.Util;
import org.springframework.util.ObjectUtils;

/**
 * @author Liang Haizhen
 * @date 2025/7/28
 * @description TODO
 */
@Util("ObjectUtils")
public class ObjectUtilsBean {

    /**
     * 判断给定对象是否为空
     * <p>
     * 此方法委托给ObjectUtils类的isEmpty方法来执行实际的空检查
     * 使用此方法可以统一空检查逻辑，便于维护和更改
     *
     * @param obj 要检查的对象
     * @return 如果对象为空，则返回true；否则返回false
     */
    public static boolean isEmpty(Object obj) {
        return ObjectUtils.isEmpty(obj);
    }

}
