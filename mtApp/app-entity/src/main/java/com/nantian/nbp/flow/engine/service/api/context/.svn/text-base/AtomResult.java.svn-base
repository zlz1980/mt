/**
 * Copyright(c) 2017-2020 NANTIAN Electronic Information Co., Ltd.
 * Finance Business Service Platform (FBSP)
 *
 * @author:NANTIAN
 * @date:pengw at Jul 28, 2020 2:13:59 PM
 */
package com.nantian.nbp.flow.engine.service.api.context;

import org.springframework.stereotype.Component;

@Component
public class AtomResult extends Result {

    public static final String ATOM_DATA_KEY = "_data";
    public static final String ATOM_RESULT_KEY = "_atomResult";
    public static final String ATOM_RET_TYPE_KEY = "_atomRetType";
    public static final String ATOM_MSG_KEY = "_message";
    public static final String ATOM_ERR_CODE_KEY = "_errCode";

    /**
     * 错误码
     */
    private String errCode = null;
    /**
     * 错误信息
     */
    private String errMsg = null;
    /**
     * 扩展属性
     */
    private PbScope<Object> props = new PbScope<>();

    public AtomResult() {
    }

    public AtomResult(RetType retType) {
        super(retType);
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }


    public PbScope<Object> getProps() {
        return props;
    }

    public void copyValue(AtomResult atomResult) {
        if (atomResult != null) {
            setRetType(atomResult.getRetType());
            setMsg(atomResult.getMsg());
            setErrCode(atomResult.getErrCode());
            this.props = atomResult.getProps();
        }
    }

    public PbScope<Object> resToPbScope() {
        PbScope<Object> res = new PbScope<>();
        res.put(ATOM_RET_TYPE_KEY, getRetType().getType());
        res.put(ATOM_MSG_KEY, getMsg());
        res.put(ATOM_ERR_CODE_KEY, getErrCode());

        return res;
    }
}
