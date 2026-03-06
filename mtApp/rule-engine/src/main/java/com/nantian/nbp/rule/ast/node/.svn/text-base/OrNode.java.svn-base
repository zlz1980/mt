package com.nantian.nbp.rule.ast.node;

import java.util.List;

/**
 * @author Liang Haizhen
 * @date 2025/7/29
 * @description OrNode类继承自ExpressionNode，用于表示逻辑或操作的节点
 * 它包含一个子节点列表，这些子节点都是ExpressionNode类型
 */
public class OrNode extends ExpressionNode {
    // 子节点列表，用于存储Or操作的各个子表达式
    List<ExpressionNode> children;

    /**
     * 构造函数，初始化子节点列表
     *
     * @param children 子节点列表，包含所有子表达式
     */
    public OrNode(List<ExpressionNode> children) {
        this.children = children;
    }


    /**
     * 计算当前节点的复杂度
     * 复杂度定义为子节点平均复杂度值
     *
     * @return 当前节点的复杂度
     */
    @Override
    public double computeComplexity() {
        complexity = 0;
        // 遍历所有子节点，计算平均复杂度值
        double sumComplexity = 0;
        for (ExpressionNode child : children) {
            sumComplexity += child.computeComplexity();
        }
        if (!children.isEmpty()) {
            complexity = sumComplexity / children.size();
        }
        return complexity;
    }


    /**
     * 生成当前节点及其子节点的字符串表示
     *
     * @param indent 缩进字符串，用于格式化输出
     * @param isLast 表示当前节点是否为其父节点的最后一个子节点
     * @return 当前节点及其子节点的字符串表示
     */
    @Override
    public String print(String indent, boolean isLast) {
        StringBuilder sb = new StringBuilder();
        // 根据节点是否为其父节点的最后一个子节点，使用不同的符号表示
        sb.append(indent).append(isLast ? "└── " : "├── ").append("OR\n");
        // 遍历所有子节点，递归生成它们的字符串表示
        for (int i = 0; i < children.size(); i++) {
            sb.append(children.get(i).print(indent + (isLast ? "    " : "│   "), i == children.size() - 1));
        }
        return sb.toString();
    }
}

