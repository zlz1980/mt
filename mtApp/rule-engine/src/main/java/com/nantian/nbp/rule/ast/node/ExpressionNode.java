package com.nantian.nbp.rule.ast.node;

/**
 * @author Liang Haizhen
 * @date 2025/7/29
 * @description 抽象类 ExpressionNode 用于表示表达式中的节点，
 * 它定义了所有表达式节点共有的属性和行为，但不提供具体实现
 */

public abstract class ExpressionNode {
    // 定义一个用于表示复杂度的变量
    double complexity;

    /**
     * 计算复杂度的抽象方法，具体实现由子类完成
     *
     * @return 返回计算得到的复杂度值
     */
    public abstract double computeComplexity();

    /**
     * 以指定格式打印对象信息的抽象方法，具体实现由子类完成
     *
     * @param prefix 用于在打印时添加到对象信息前的前缀字符串
     * @param isLast 表示是否是最后一个对象的标志，用于调整打印格式
     * @return 返回格式化后的对象信息字符串
     */
    public abstract String print(String prefix, boolean isLast);
}
