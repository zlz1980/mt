package org.jts.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.SpelParserConfiguration;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.StringUtils;

/**
 * @author Administrator
 */
public class PbElExpressionParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(PbElExpressionParser.class);

    private static final SpelParserConfiguration CONFIGURATION = new SpelParserConfiguration();

    private static final ExpressionParser PARSER = new SpelExpressionParser(CONFIGURATION);

    static final String TRUE = "TRUE";

    static final String FALSE = "FALSE";

    private PbElExpressionParser(){
    }

    public static Expression parseExpression(String expressionString){
        if (!StringUtils.hasText(expressionString)) {
            throw new RuntimeException("PbElExpressionParser: expressionString is null or empty");
        }
        return PARSER.parseExpression(expressionString);
    }

    public static Boolean getBoolVal(String expressionString, StandardEvaluationContext evCtx){
        Expression expression = parseExpression(expressionString);
        return expression.getValue(evCtx,Boolean.class);
    }

}
