package com.nantian.nbp.flow.engine.service.api.saga;

import com.nantian.nbp.flow.engine.service.api.context.PbScope;
import com.nantian.nbp.flow.engine.service.api.context.RetType;
import com.nantian.nbp.rule.RuleService;
import com.nantian.nbp.rule.entity.Rule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

import static com.nantian.nbp.flow.engine.service.api.Constants.PARAM_NUM_ONE;
import static com.nantian.nbp.flow.engine.service.api.FeConstants.OUT_SCOPE;

public class DefSagaTransitionResConfirmationImpl implements SagaTransitionResConfirmation {

    private static final String RULE_SET_TYPE = "SagaTransitionResultJudg";
    @Autowired
    private RuleService ruleService;

    /**
     * 应用层扩展事务结果确认机制，根据流程返回结果信息，自定义逻辑判断是否确认事务执行成功
     * @param out 流程返回信息，以return的返回结果作为输入
     * @return boolean 事务是否执行成功
     */
    @Override
    public SagaTransitionResult isResConfirmed(PbScope<Object> out) {

        SagaTransitionResult result = new SagaTransitionResult();
        Map<String, Object> map = new HashMap<>(PARAM_NUM_ONE);
        map.put(OUT_SCOPE, out);
        try {
            Rule ruleResult = ruleService.findRule(RULE_SET_TYPE, map);
            if (StringUtils.hasText(ruleResult.getResult())) {
                result.setRetType(RetType.SUCCESS);
                result.setMsg("冲正事务执行成功");
            } else {
                result.setRetType(RetType.FAILED);
                result.setMsg("冲正事务执行失败");
            }
        } catch (Exception e) {
            result.setRetType(RetType.FAILED);
            result.setMsg(e.getMessage());
        }
        return result;
    }
}
