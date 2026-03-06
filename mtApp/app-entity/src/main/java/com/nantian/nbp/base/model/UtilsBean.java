package com.nantian.nbp.base.model;

import java.io.Serializable;

public class UtilsBean implements Serializable {

    /** 类型名称 */
    private String beanName;
    /** 类型名称全路径 */
    private String classPath;
    /**  类型说明*/
    private String beanNote;
    /** bean */
    private Object bean;

    public String getClassPath() {
        return classPath;
    }

    public void setClassPath(String classPath) {
        this.classPath = classPath;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setClassName(String beanName) {
        this.beanName = beanName;
    }

    public String getBeanNote() {
        return beanNote;
    }

    public void setBeanNote(String beanNote) {
        this.beanNote = beanNote;
    }
    public Object getBean() {
        return bean;
    }
    public void setBean(Object bean) {
        this.bean = bean;
    }

    @Override
    public String toString() {
        return "UtilsBean{" +
                "beanName='" + beanName + '\'' +
                '}';
    }
}
