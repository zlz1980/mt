package com.nantian.nbp.rule.ast;

/**
 * @author Liang Haizhen
 * @date 2025/7/29
 * @description 定义了一个枚举类型TokenType，用于表示查询语法中的不同类型的标记（Token）。
 * 这些标记在解析和执行查询时用于识别和处理不同的逻辑操作符和结构。
 */
public enum TokenType {
    /**
     * 枚举成员包括：
     * - AND: 逻辑与操作符，用于连接两个查询条件，表示两者都需要满足。
     * - OR: 逻辑或操作符，用于连接两个查询条件，表示满足其中任何一个即可。
     * - NOT: 逻辑非操作符，用于否定一个查询条件，表示该条件不满足。
     * - LPAREN: 左括号，用于标记查询表达式中的分组开始。
     * - RPAREN: 右括号，用于标记查询表达式中的分组结束。
     * - ATOMIC: 原子操作符，表示一个不可分割的查询条件或表达式。
     */
    AND, OR, NOT, LPAREN, RPAREN, ATOMIC
}
