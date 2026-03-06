package com.nantian.nbp.ev;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.SpelEvaluationException;
import org.springframework.expression.spel.SpelMessage;
import org.springframework.expression.spel.SpelParserConfiguration;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.util.StringUtils;

import java.util.Objects;

/**
 * @author Administrator
 */
public class PbElExpressionParser {
    private static final Logger LOGGER = LoggerFactory.getLogger(PbElExpressionParser.class);
    private static final SpelParserConfiguration CONFIGURATION = new SpelParserConfiguration();

    private static final ExpressionParser PARSER = new SpelExpressionParser(CONFIGURATION);

    private PbElExpressionParser(){
    }

    public static Expression parseExpression(String expressionString){
        if (!StringUtils.hasText(expressionString)) {
            throw new RuntimeException("PbElExpressionParser: expressionString is null or empty");
        }
        return PARSER.parseExpression(expressionString);
    }

    public static PbVal getVal(String expressionString, PbEvContext evCtx){
        PbVal pbVal = new PbVal();
        Expression expression = parseExpression(expressionString);
        try{
            Object val = expression.getValue(evCtx);
            pbVal.setVal(val);
            pbVal.setExists(Boolean.TRUE);
        }catch (SpelEvaluationException e){
            if(Objects.equals(SpelMessage.METHOD_CALL_ON_NULL_OBJECT_NOT_ALLOWED,e.getMessageCode())){
                LOGGER.warn("未获取到属性[{}]", expressionString);
                pbVal.setExists(Boolean.FALSE);
                pbVal.setMsg(e.getMessage());
            }else {
                throw e;
            }
        }
        return pbVal;
    }

    public static Boolean getBoolVal(String expressionString, PbEvContext evCtx){
        Expression expression = parseExpression(expressionString);
        return expression.getValue(evCtx,Boolean.class);
    }

    public static Boolean getRuleRes(String expressionString){
        Expression expression = parseExpression(expressionString);
        return expression.getValue(Boolean.class);

    }
}
