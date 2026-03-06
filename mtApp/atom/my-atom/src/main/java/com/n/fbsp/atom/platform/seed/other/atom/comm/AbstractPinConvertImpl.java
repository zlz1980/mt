package com.n.fbsp.atom.platform.seed.other.atom.comm;

import com.n.fbsp.atom.platform.seed.other.eccs.EsscAssistant;
import com.nantian.nbp.base.model.FlowUnit;
import com.nantian.nbp.cache.server.api.CacheClientApi;
import com.nantian.nbp.flow.engine.service.api.AtomService;
import com.nantian.nbp.flow.engine.service.api.ScopeValUnit;
import com.nantian.nbp.flow.engine.service.api.TranContext;
import com.nantian.nbp.flow.engine.service.api.context.AtomResult;
import com.nantian.nbp.flow.engine.service.api.context.RetType;
import com.nantian.nbp.flow.engine.service.api.exception.FlowException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import static com.nantian.nbp.flow.engine.service.api.Constants.APP_ATOM_RUN_ERR_KEY;
import static com.nantian.nbp.flow.engine.service.api.exception.ErrorCodeEnum.T_C0003092;

/**
 * @创建人 fbsp
 * @创建时间 2025/6/11 18:01
 * @描述 PIN转换抽象类
 *
 * 入参出参做到明确、清晰
 * 配置的值放到配置表中
 */
public abstract class AbstractPinConvertImpl implements AtomService {
    protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractPinConvertImpl.class);

    @Autowired
    protected CacheClientApi cacheClientApi;
    @Autowired
    protected EsscAssistant esscAssistant;

    @Override
    public AtomResult doService(TranContext tranContext, ScopeValUnit scopeValUnit, FlowUnit flowUnit) {
        AtomResult atomResult = new AtomResult();
        try {
            //参数校验
            if (!StringUtils.hasText(flowUnit.getAtomTranParam())) {
                LOGGER.error(APP_ATOM_RUN_ERR_KEY + "PIN转换原子交易参数未配置,请检查参数配置[{}]", flowUnit.getAtomTranParam());
                throw new FlowException(T_C0003092.getCode(), T_C0003092.getCodeMsg("PIN转换原子交易参数未配置,请检查参数配置[{}]", flowUnit.getAtomTranParam()));
            }

            //PIN转换
            doConvertPin(flowUnit.getAtomTranParam().trim(),scopeValUnit,tranContext);

            atomResult.setRetType(RetType.SUCCESS);
            return atomResult;
        }catch (Exception e){
            LOGGER.error("PIN转换原子交易，参数[{}]执行失败", flowUnit.getAtomTranParam()+"",e);
            atomResult.setRetType(RetType.FAILED);
            atomResult.setErrMsg("PIN转换原子交易执行失败");
            return atomResult;
        }
    }

    protected abstract void doConvertPin(String atomTranParam,ScopeValUnit scopeValUnit,TranContext tranContext) throws Exception;


    /*protected EsscAPI getEsscAPI() throws Exception {
        return esscAssistant.getEsscAPI();
    }*/


}
