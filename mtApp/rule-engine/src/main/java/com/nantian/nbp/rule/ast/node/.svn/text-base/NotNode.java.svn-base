package com.nantian.nbp.rule.ast.node;

/**
 * @author Liang Haizhen
 * @date 2025/7/30
 * @description NotNode类表示逻辑非操作的表达式节点，它是ExpressionNode的一个子类
 * 主要用于构建表达式树，并计算其复杂度
 */
public class NotNode extends ExpressionNode {
    // 子节点，表示NOT操作的输入表达式
    ExpressionNode child;

    /**
     * 构造函数，初始化NotNode对象
     * @param child 表示NOT操作的输入表达式的子节点
     */
    public NotNode(ExpressionNode child) {
        this.child = child;
    }

    /**
     * 计算当前节点及其子节点的复杂度
     * @return 返回计算得到的复杂度
     */
    @Override
    public double computeComplexity() {
        // 基于子节点的复杂度计算当前节点的复杂度，这里直接使用子节点的复杂度
        // 可调整权重以反映NOT操作对复杂度的影响
        complexity = child.computeComplexity();
        return complexity;
    }

    /**
     * 生成当前节点及其子节点的字符串表示，用于调试或日志输出
     *
     * @param indent 缩进字符串，用于控制输出格式
     * @param isLast 表示当前节点是否为其父节点的最后一个子节点
     * @return 返回节点及其子节点的字符串表示
     */
    @Override
    public String print(String indent, boolean isLast) {
        // 根据节点是否为其父节点的最后一个子节点，使用不同的符号表示
        String sb = indent + (isLast ? "└── " : "├── ") + "NOT\n" +
                // 递归调用子节点的print方法，构建子节点的字符串表示
                child.print(indent + (isLast ? "    " : "│   "), true);
        return sb;
    }
}
