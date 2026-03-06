package com.nantian.nbp.rule.ast.node;

import java.util.List;

/**
 * @author Liang Haizhen
 * @date 2025/7/29
 * @description AndNode类继承自ExpressionNode，用于表示逻辑与操作的节点
 * 它包含一个子节点列表，用于表示与操作的多个条件
 */
public class AndNode extends ExpressionNode {
    // 子节点列表，每个子节点都是一个表达式节点
    List<ExpressionNode> children;

    /**
     * 构造函数，初始化AndNode对象时设置其子节点列表
     *
     * @param children 子节点列表，表示与操作的多个条件
     */
    public AndNode(List<ExpressionNode> children) {
        this.children = children;
    }

    /**
     * 计算当前节点的复杂度
     * 复杂度是所有子节点复杂度的总和
     *
     * @return 当前节点的复杂度
     */
    @Override
    public double computeComplexity() {
        complexity = 0;
        for (ExpressionNode child : children) {
            complexity += child.computeComplexity();
        }
        return complexity;
    }

    /**
     * 生成当前节点及其子节点的字符串表示
     * 用于以树状结构打印表达式
     *
     * @param indent 缩进字符串，用于控制打印格式
     * @param isLast 表示当前节点是否是其父节点的最后一个子节点
     * @return 当前节点及其子节点的字符串表示
     */
    @Override
    public String print(String indent, boolean isLast) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent).append(isLast ? "└── " : "├── ").append("AND\n");
        for (int i = 0; i < children.size(); i++) {
            sb.append(children.get(i).print(indent + (isLast ? "    " : "│   "), i == children.size() - 1));
        }
        return sb.toString();
    }
}

