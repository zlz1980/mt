package com.nantian.nbp.rule.ast;

/**
 * @author Liang Haizhen
 * @date 2025/7/29
 * @description Token类表示一个解析单元，用于表示文本中的一个符号及其类型
 */
public class Token {
    // TokenType枚举表示符号的类型
    TokenType type;
    // text表示符号的具体文本内容
    String text;

    /**
     * Token的构造函数，用于创建一个新的Token实例
     *
     * @param type 符号的类型
     * @param text 符号的文本内容
     */
    Token(TokenType type, String text) {
        this.type = type;
        this.text = text;
    }

    /**
     * 返回Token的字符串表示形式
     * @return Token的字符串表示形式，格式为：类型(文本)
     */
    public String toString() {
        return type + "(" + text + ")";
    }
}
