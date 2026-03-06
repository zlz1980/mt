/**
 * Copyright(c) 2017-2020 NANTIAN Electronic Information Co., Ltd.
 * Finance Business Service Platform (FBSP)
 *
 * @author:NANTIAN
 * @date:pengw at Jul 28, 2020 2:13:59 PM
 */
package com.nantian.nbp.flow.engine.service.core.parse;

import com.nantian.nbp.flow.engine.service.api.exception.ErrorCodeEnum;
import com.nantian.nbp.flow.engine.service.api.exception.FlowException;
import com.nantian.nbp.flowengine.model.ASTType;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

import static com.nantian.nbp.flowengine.model.ASTType.ASYNMODULE;
import static com.nantian.nbp.flowengine.model.ASTType.BEK;
import static com.nantian.nbp.flowengine.model.ASTType.COMM;
import static com.nantian.nbp.flowengine.model.ASTType.EXIT;
import static com.nantian.nbp.flowengine.model.ASTType.HANDLEMODULE;
import static com.nantian.nbp.flowengine.model.ASTType.IF;
import static com.nantian.nbp.flowengine.model.ASTType.PROC;
import static com.nantian.nbp.flowengine.model.ASTType.RETURN;
import static com.nantian.nbp.flowengine.model.ASTType.SWITCH;
import static com.nantian.nbp.flowengine.model.ASTType.TEMP;
import static com.nantian.nbp.flowengine.model.ASTType.TRY;
import static com.nantian.nbp.flowengine.model.ASTType.WHILE;

/**
 * 语法解析工厂类
 *
 * @author Administrator
 */
public class ParseStrategyFactory {

    private static final Map<ASTType, BaseParseStrategy> PARSE_STRATEGY_MAP =
            new EnumMap<ASTType, BaseParseStrategy>(ASTType.class) {
        {
            put(COMM, new CommBaseParse());
            put(HANDLEMODULE, new ModuleBaseParse());
            put(ASYNMODULE, new AsynBaseParse());
            put(IF, new IfBaseParse());
            put(WHILE, new WhileBaseParse());
            put(TRY, new TryBaseParse());
            put(SWITCH, new SwitchBaseParse());
            put(EXIT, new ExitBaseParse());
            put(RETURN, new ReturnBaseParse());
            put(TEMP, new TempValBaseParse());
            put(BEK, new BekBaseParse());
            put(PROC, new ProcBaseParse());
        }
    };

    private ParseStrategyFactory() {
    }

    public static BaseParseStrategy creator(ASTType type, String fTranCode) {
        BaseParseStrategy parseStrategy = PARSE_STRATEGY_MAP.get(type);
        if (Objects.isNull(parseStrategy)) {
            throw new FlowException(ErrorCodeEnum.T_C0003092.getCode(), fTranCode, String.format("Unknown ASTType [%s]",
                    type.getType()), null);
        }
        return parseStrategy;
    }

}
