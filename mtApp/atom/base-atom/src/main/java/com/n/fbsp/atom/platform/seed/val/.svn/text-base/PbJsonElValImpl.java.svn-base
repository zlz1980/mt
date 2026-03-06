package com.n.fbsp.atom.platform.seed.val;

import com.nantian.nbp.base.model.FlowUnit;
import com.nantian.nbp.flow.engine.service.api.Atom;
import com.nantian.nbp.flow.engine.service.api.AtomService;
import com.nantian.nbp.flow.engine.service.api.ScopeValUnit;
import com.nantian.nbp.flow.engine.service.api.TranContext;
import com.nantian.nbp.flow.engine.service.api.context.AtomResult;
import com.nantian.nbp.flow.engine.service.api.context.RetType;
import com.nantian.nbp.flow.engine.service.api.exception.FlowException;
import com.nantian.nbp.utils.JsonUtil;
import com.nantian.nbp.utils.StrUtils;

import java.util.List;
import java.util.Map;

import static com.nantian.nbp.flow.engine.service.api.Constants.VAR_END_FLAG;
import static com.nantian.nbp.flow.engine.service.api.Constants.VAR_FLAG;
import static com.nantian.nbp.flow.engine.service.api.Constants.VAR_START_FLAG;
import static com.nantian.nbp.flow.engine.service.api.context.AtomResult.ATOM_DATA_KEY;

/**
 * @author JiangTaiSheng
 */
@Atom("base.PbJsonElVal")
public class PbJsonElValImpl implements AtomService {

    /**
     * Json模板变量赋值操作
     *
     * @param tranContext
     * @param scopeValUnit 流程上下文对象
     * @param flowUnit     当前单元
     * @return Result
     * @throws FlowException 流程异常
     */
    @Override
    public AtomResult doService(TranContext tranContext, ScopeValUnit scopeValUnit, FlowUnit flowUnit) {
        AtomResult atomResult = new AtomResult();
        String jsonTmp = flowUnit.getAtomTranParam();

        try {
            Map<String,Object> root = JsonUtil.strToMap(jsonTmp);
            paresMap(root,tranContext);
            scopeValUnit.put(ATOM_DATA_KEY,root);
        } catch (Exception e) {
            throw new FlowException(FlowException.EFLW001,tranContext.getFeTranCode(),"JsonProcessingException",e);
        }
        atomResult.setRetType(RetType.SUCCESS);
        return atomResult;
    }

    private void paresMap(Map<String,Object> pNode,TranContext tranContext){
        for (Map.Entry<String,Object> entry : pNode.entrySet()) {
            Object val = entry.getValue();
            if(val instanceof String){
                String valStr = StrUtils.toStrDefBlank(val);
                if(valStr.startsWith(VAR_FLAG) ||
                        (valStr.startsWith(VAR_START_FLAG) && valStr.endsWith(VAR_END_FLAG))){
                    entry.setValue(tranContext.getCtxVal(valStr).getVal());
                }
            }else if(val instanceof Map){
                paresMap((Map)val,tranContext);
            }else if(val instanceof List){
                paresList((List)val,tranContext);
            }
        }
    }

    private void paresList(List<?> pNode,TranContext tranContext){
        for (Object val : pNode){
        	if(val instanceof Map){
        		paresMap((Map) val,tranContext);
        	}
        }
    }
}
