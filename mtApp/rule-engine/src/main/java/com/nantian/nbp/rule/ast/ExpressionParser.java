package com.nantian.nbp.rule.ast;

import com.nantian.nbp.rule.ast.node.AndNode;
import com.nantian.nbp.rule.ast.node.AtomicNode;
import com.nantian.nbp.rule.ast.node.ExpressionNode;
import com.nantian.nbp.rule.ast.node.NotNode;
import com.nantian.nbp.rule.ast.node.OrNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Map;

/**
 * @author Liang Haizhen
 * @date 2025/7/29
 * @description TODO
 */
public class ExpressionParser {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExpressionParser.class);
    private final Map<String, Double> atomicScoreMap;

    public ExpressionParser(Map<String, Double> atomicScoreMap) {
        this.atomicScoreMap = atomicScoreMap;
    }

    /**
     * 解析输入的字符串表达式为表达式树
     *
     * @param input 输入的字符串表达式
     * @return 表达式树的根节点
     *
     * @throws RuntimeException 如果表达式解析未结束，但输入字符串已结束，则抛出运行时异常
     */
    public ExpressionNode parse(String input) {
        // 将输入字符串分解为标记（Token）
        List<Token> tokens = tokenize(input);
        // 创建一个双端队列来存储标记流，以便从输入中解析表达式
        Deque<Token> stream = new ArrayDeque<>(tokens);
        // 解析标记流中的表达式，并获取表达式树的根节点
        ExpressionNode root = parseExpression(stream);
        // 如果标记流未被完全解析，说明输入的表达式有误，抛出异常
        if (!stream.isEmpty()) {
            throw new RuntimeException("表达式解析未结束，剩余 token: " + tokens);
        }
        // 返回解析得到的表达式树的根节点
        return root;
    }

    /**
     * 解析表达式，处理逻辑或（OR）操作
     * 该方法首先解析一个项，然后检查后续是否有逻辑或操作符以及更多的项
     * 如果有，它会将所有通过逻辑或连接的项收集到一个列表中，并返回一个表示逻辑或操作的OrNode
     * 如果没有逻辑或操作符，则返回最初解析的项
     *
     * @param stream 一个双端队列，包含按解析顺序排列的Token
     * @return 返回一个ExpressionNode，如果解析了多个通过逻辑或连接的项，则返回OrNode，否则返回最初解析的项
     */
    private ExpressionNode parseExpression(Deque<Token> stream) {
        // 解析第一个项
        ExpressionNode node = parseTerm(stream);
        // 初始化一个列表，用于存储通过逻辑或操作符连接的项
        List<ExpressionNode> orList = new ArrayList<>();
        orList.add(node);
        // 循环检查是否有逻辑或操作符以及更多的项
        while (!stream.isEmpty() && stream.peek().type == TokenType.OR) {
            stream.poll(); // consume ||
            // 解析下一个项，并添加到列表中
            orList.add(parseTerm(stream));
        }
        // 如果列表中只有一个项，则返回该项；否则返回表示逻辑或操作的OrNode
        return orList.size() == 1 ? node : new OrNode(orList);
    }

    /**
     * 解析表达式中的项
     * 该方法处理逻辑与（AND）操作符的优先级，通过解析因子并组合它们
     * 当遇到AND操作符时，会继续解析后续因子，并将它们存储起来
     * 如果只有一个因子，则直接返回该因子；否则，创建并返回一个包含所有因子的AndNode
     *
     * @param stream 一个双端队列，包含待解析的令牌流
     * @return 返回解析后的表达式节点，可能是一个单一的因子或多个因子组成的AndNode
     */
    private ExpressionNode parseTerm(Deque<Token> stream) {
        // 解析并获取当前项的第一个因子
        ExpressionNode node = parseFactor(stream);
        // 初始化一个列表，用于存储通过AND操作符连接的表达式节点
        List<ExpressionNode> andList = new ArrayList<>();
        andList.add(node);
        // 当令牌流不为空且下一个令牌是AND操作符时，继续解析
        while (!stream.isEmpty() && stream.peek().type == TokenType.AND) {
            stream.poll(); // consume &&
            // 解析并添加下一个因子到列表中
            andList.add(parseFactor(stream));
        }
        // 如果列表中只有一个因子，则返回该因子；否则，返回包含所有因子的AndNode
        return andList.size() == 1 ? node : new AndNode(andList);
    }

    /**
     * 解析表达式中的因子
     * 该方法处理表达式中的不同类型的因子，如NOT操作符、原子表达式或嵌套表达式
     * 它通过检查输入流中的下一个标记来决定要解析的因子类型
     *
     * @param stream 表达式标记的双端队列，用于向前看和消耗标记
     * @return 解析后的表达式节点
     *
     * @throws RuntimeException 如果遇到意外的表达式结束或未知标记，则抛出运行时异常
     */
    private ExpressionNode parseFactor(Deque<Token> stream) {
        // 获取当前标记
        Token token = stream.peek();
        // 如果当前标记为空，表示表达式意外结束
        if (token == null) {
            throw new RuntimeException("Unexpected end of expression");
        }
        // 如果当前标记是NOT操作符
        if (token.type == TokenType.NOT) {
            stream.poll(); // 消耗当前标记（!）
            // 递归解析下一个因子，NOT操作符绑定紧邻的子表达式
            ExpressionNode child = parseFactor(stream);
            // 返回表示NOT操作的节点
            return new NotNode(child);
        } else if (token.type == TokenType.LPAREN) {
            // 如果当前标记是左括号
            stream.poll(); // 消耗当前标记（(）
            // 解析括号内的表达式
            ExpressionNode node = parseExpression(stream);
            // 期望下一个标记是右括号
            expect(stream, TokenType.RPAREN);
            // 返回括号内的表达式节点
            return node;
        } else if (token.type == TokenType.ATOMIC) {
            // 如果当前标记是原子表达式
            stream.poll(); // 消耗当前标记（原子表达式）
            // 获取原子表达式的文本
            String atom = token.text;
            // 获取原子表达式的分数，如果未找到则默认为1.0
            double score = atomicScoreMap.getOrDefault(atom, 1.0);
            // 返回表示原子表达式的节点
            return new AtomicNode(atom, score);
        } else {
            // 如果当前标记类型未知
            throw new RuntimeException("Unexpected token: " + token.text);
        }
    }


    /**
     * 检查并移除Token流中的下一个Token，确保其类型符合预期
     * 如果Token流为空或下一个Token的类型不符合预期，则抛出异常
     *
     * @param stream   Token流，表示一系列的Token
     * @param expected 预期的Token类型
     */
    private void expect(Deque<Token> stream, TokenType expected) {
        // 从Token流中移除并获取下一个Token
        Token token = stream.poll();
        // 如果Token为空或其类型不符合预期，则抛出异常
        if (ObjectUtils.isEmpty(token) || token.type != expected) {
            throw new RuntimeException("Expected " + expected + " but got " + (ObjectUtils.isEmpty(token) ? "EOF" : token.text));
        }
    }

    /**
     * 将输入字符串解析为Token列表
     * 该方法实现了简单的词法分析，用于解析特定格式的输入字符串
     * 它识别括号、逻辑运算符、原子表达式和非运算符，并将它们转换为相应的Token
     *
     * @param input 要解析的输入字符串
     * @return 解析后的Token列表
     *
     * @throws RuntimeException 如果遇到不匹配的'}'或无效字符
     */
    private List<Token> tokenize(String input) {
        // 存储解析后的Token
        List<Token> tokens = new ArrayList<>();
        // 遍历输入字符串的索引
        int i = 0;
        // 循环遍历输入字符串的每个字符
        while (i < input.length()) {
            char c = input.charAt(i);
            // 跳过空白字符
            if (Character.isWhitespace(c)) {
                i++;
                // 处理左括号
            } else if (c == '(') {
                tokens.add(new Token(TokenType.LPAREN, "("));
                i++;
                // 处理右括号
            } else if (c == ')') {
                tokens.add(new Token(TokenType.RPAREN, ")"));
                i++;
                // 处理逻辑与运算符
            } else if (i + 1 < input.length() && input.startsWith("&&", i)) {
                tokens.add(new Token(TokenType.AND, "&&"));
                i += 2;
                // 处理逻辑或运算符
            } else if (i + 1 < input.length() && input.startsWith("||", i)) {
                tokens.add(new Token(TokenType.OR, "||"));
                i += 2;
                // 处理原子表达式
            } else if (c == '{') {
                int j = input.indexOf('}', i);
                if (j == -1) {
                    LOGGER.error("expStr[{}],Missing }", input);
                    throw new RuntimeException("expStr:" + input + "Missing }");
                }
                String atom = input.substring(i + 1, j);
                tokens.add(new Token(TokenType.ATOMIC, atom));
                i = j + 1;
                // 处理非运算符
            } else if (c == '!') {
                tokens.add(new Token(TokenType.NOT, "!"));
                i++;
                // 遇到无效字符时抛出异常
            } else {
                LOGGER.error("expStr[{}],Invalid character at position[{}]", input, i);
                throw new RuntimeException("Invalid character at position " + i);
            }
        }
        // 返回解析后的Token列表
        return tokens;
    }
}
