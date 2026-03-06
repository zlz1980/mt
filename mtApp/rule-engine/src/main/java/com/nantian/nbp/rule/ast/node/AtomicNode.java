package com.nantian.nbp.rule.ast.node;

/**
 * @author Liang Haizhen
 * @date 2025/7/29
 * @description 表示一个原子节点的类，原子节点是表达式树中的叶子节点 它包含一个原子（如变量或值）及其复杂度
 */
public class AtomicNode extends ExpressionNode {
    // 原子节点所代表的原子（如变量或值）
    String atom;

    /**
     * 构造函数，用于创建一个原子节点
     *
     * @param atom       原子节点所代表的原子（如变量或值）
     * @param complexity 原子节点的复杂度
     */
    public AtomicNode(String atom, double complexity) {
        this.atom = atom;
        this.complexity = complexity;
    }

    /**
     * 计算并返回原子节点的复杂度
     *
     * @return 原子节点的复杂度
     */
    @Override
    public double computeComplexity() {
        return complexity;
    }

    /**
     * 生成并返回原子节点的字符串表示，用于打印表达式树
     *
     * @param indent 缩进，用于表示节点在树中的层级
     * @param isLast 表示该节点是否为其父节点的最后一个子节点
     * @return 原子节点的字符串表示
     */
    @Override
    public String print(String indent, boolean isLast) {
        return indent + (isLast ? "└── " : "├── ") + "ATOM: ｛" + atom + "｝ = " + complexity + "\n";
    }
}

