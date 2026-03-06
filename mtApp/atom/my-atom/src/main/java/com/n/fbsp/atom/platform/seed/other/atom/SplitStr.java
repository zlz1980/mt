package com.n.fbsp.atom.platform.seed.other.atom;

import com.nantian.nbp.base.model.FlowUnit;
import com.nantian.nbp.flow.engine.service.api.Atom;
import com.nantian.nbp.flow.engine.service.api.AtomService;
import com.nantian.nbp.flow.engine.service.api.ScopeValUnit;
import com.nantian.nbp.flow.engine.service.api.TranContext;
import com.nantian.nbp.flow.engine.service.api.context.AtomResult;
import com.nantian.nbp.flow.engine.service.api.context.RetType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 09030 按照分隔符对字符串拆分
 *     第一行 结构号|字段号|维数 需分割的原始字符串
 *     第二行 分隔符
 *     第三行 结构号|字段号 分割后存储值的变量
 */
@Atom("fbsp.SplitStr")
public class SplitStr implements AtomService {

    private static final Logger logger = LoggerFactory.getLogger(SplitStr.class);

    @Override
    public AtomResult doService(TranContext tranContext, ScopeValUnit scopeValUnit, FlowUnit flowUnit) {
        AtomResult atomResult = new AtomResult();

        List<String> flowparaList = flowUnit.getParamList();

        String oldStr = (String) tranContext.getCtxVal(flowparaList.get(0)).getVal();

        //分隔符
        String splitStr = flowparaList.get(1);
        //结构号|字段号
        String newStrPool = flowparaList.get(2);

        logger.info("oldStr={}", oldStr);
        String[] strArr = StringUtils.delimitedListToStringArray(oldStr, splitStr);
        scopeValUnit.put(newStrPool, strArr);
        atomResult.setRetType(RetType.SUCCESS);

        return atomResult;
    }

}
