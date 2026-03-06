package com.n.fbsp.atom.platform.seed.other.utils;

import com.alibaba.fastjson.JSONObject;
import com.n.fbsp.atom.platform.seed.other.po.JournalExt;
import com.n.fbsp.atom.platform.seed.other.po.TEbJournalPo;
import com.nantian.nbp.flow.engine.service.api.ScopeValUnit;
import com.nantian.nbp.flow.engine.service.api.TranContext;
import com.nantian.nbp.flow.engine.service.api.context.PbScope;
import com.nantian.nbp.flow.engine.service.api.exception.FlowException;
import com.nantian.nbp.utils.BeanMapUtil;
import com.nantian.nbp.utils.JsonUtil;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.*;


import static com.n.fbsp.atom.platform.seed.other.po.TEbJournalPo.*;
import static com.n.fbsp.atom.platform.seed.other.po.TEbJournalPo.F_TRAN_CODE;
import static com.n.fbsp.atom.platform.seed.other.utils.Constants.OPERATE_TYPE;
import static com.nantian.nbp.flow.engine.service.api.FeConstants.*;
import static com.nantian.nbp.flow.engine.service.api.FeConstants.TRAN_DATE;
import static com.nantian.nbp.flow.engine.service.api.exception.ErrorCodeEnum.T_C0003092;


/**
 * 流水相关操作
 *
 * @author Administrator
 */
public class JournalUtils {

    /**
     * 请求参数转换为流水表Bo
     *
     * @param inScope  inScope
     * @param sysScope sysScope
     */
    public static TEbJournalPo buildJournalPo(PbScope<Object> inScope, PbScope<Object> sysScope) {
        TEbJournalPo tEbJournalPo = BeanMapUtil.mapToBean(inScope, new TEbJournalPo());
        if (ObjectUtils.isEmpty(tEbJournalPo.getReqChnl())) {
            tEbJournalPo.setReqChnl((String) sysScope.get(CHNL_NO));
        }
        if (ObjectUtils.isEmpty(tEbJournalPo.getTranDate())) {
            tEbJournalPo.setTranDate((String) sysScope.get(TRAN_DATE));
        }
        if (ObjectUtils.isEmpty(tEbJournalPo.getTranTime())) {
            tEbJournalPo.setTranTime((String) sysScope.get(TRAN_TIME));
        }
        if (ObjectUtils.isEmpty(tEbJournalPo.getJourNo())) {
            tEbJournalPo.setJourNo((String) sysScope.get(BIZ_ID));
        }
        if (ObjectUtils.isEmpty(tEbJournalPo.getfTranCode())) {
            tEbJournalPo.setfTranCode((String) sysScope.get(F_TRAN_CODE));
        }
        if (ObjectUtils.isEmpty(tEbJournalPo.getTranAtt())) {
            tEbJournalPo.setTranAtt((String) sysScope.get(TRAN_TYPE));
        }
        if (!ObjectUtils.isEmpty(inScope.get(TAG_SET))) {
            tEbJournalPo.setUpdateTagSet((Set<String>) inScope.get(TAG_SET));
        }
        if (!ObjectUtils.isEmpty(inScope.get(EXT_INFO))) {
            tEbJournalPo.setExtInfo(JsonUtil.objToString(inScope.get(EXT_INFO)));
        }
        return tEbJournalPo;
    }

    /**
     * journalBo转换为流水表po
     */
    public static PbScope<Object> buildJournalScope(TEbJournalPo tEbJournalPo, PbScope<Object> pbScope,
            String operateType) {
        Map<String, Object> map = BeanMapUtil.beanToMap(tEbJournalPo);
        pbScope.putAll(map);
        if (!ObjectUtils.isEmpty(tEbJournalPo.getTagSet())) {
            String[] tagList = StringUtils.commaDelimitedListToStringArray(tEbJournalPo.getTagSet());
            Set<String> hashSet = new HashSet<>(Arrays.asList(tagList));
            pbScope.put(TAG_SET, hashSet);
        } else {
            pbScope.put(TAG_SET, new HashSet<>());
        }
        if (!ObjectUtils.isEmpty(tEbJournalPo.getExtInfo())) {
            JSONObject json = JSONObject.parseObject(tEbJournalPo.getExtInfo());
            pbScope.put(EXT_INFO, new LinkedHashMap<>(json));
        } else {
            pbScope.put(EXT_INFO, new LinkedHashMap<>());
        }
        pbScope.put(OPERATE_TYPE, operateType);
        return pbScope;
    }

    public static void validateFields(TEbJournalPo tEbJournalPo, String fTranCode) {
        if (ObjectUtils.isEmpty(tEbJournalPo.getReqChnl())) {
            throw new FlowException(T_C0003092.getCode(), fTranCode, T_C0003092.getCodeMsg(REQ_CHNL + "未上送"), null);
        }
        if (ObjectUtils.isEmpty(tEbJournalPo.getReqDate())) {
            throw new FlowException(T_C0003092.getCode(), fTranCode, T_C0003092.getCodeMsg(REQ_DATE + "未上送"), null);
        }
        if (ObjectUtils.isEmpty(tEbJournalPo.getReqTime())) {
            throw new FlowException(T_C0003092.getCode(), fTranCode, T_C0003092.getCodeMsg(REQ_TIME + "未上送"), null);
        }
        if (ObjectUtils.isEmpty(tEbJournalPo.getTranDate())) {
            throw new FlowException(T_C0003092.getCode(), fTranCode, T_C0003092.getCodeMsg(TRAN_DATE + "未上送"), null);
        }
        if (ObjectUtils.isEmpty(tEbJournalPo.getTranTime())) {
            throw new FlowException(T_C0003092.getCode(), fTranCode, T_C0003092.getCodeMsg(TRAN_TIME + "未上送"), null);
        }
        if (ObjectUtils.isEmpty(tEbJournalPo.getJourNo())) {
            throw new FlowException(T_C0003092.getCode(), fTranCode, T_C0003092.getCodeMsg(JOUR_NO + "未上送"), null);
        }
        if (ObjectUtils.isEmpty(tEbJournalPo.getfTranCode())) {
            throw new FlowException(T_C0003092.getCode(), fTranCode, T_C0003092.getCodeMsg(F_TRAN_CODE + "未上送"), null);
        }
        if (ObjectUtils.isEmpty(tEbJournalPo.getBusiKey())) {
            throw new FlowException(T_C0003092.getCode(), fTranCode, T_C0003092.getCodeMsg(BUSI_KEY + "未上送"), null);
        }
        if (ObjectUtils.isEmpty(tEbJournalPo.getTranState())) {
            throw new FlowException(T_C0003092.getCode(), fTranCode, T_C0003092.getCodeMsg(TRAN_STATE + "未上送"), null);
        }
        if (ObjectUtils.isEmpty(tEbJournalPo.getCheckFlag())) {
            throw new FlowException(T_C0003092.getCode(), fTranCode, T_C0003092.getCodeMsg(CHECK_FLAG + "未上送"), null);
        }
    }

    public static JournalExt buildJournalExt(TranContext tranContext, ScopeValUnit scopeValUnit,
                                             PbScope<Object> jouScope) {
        JournalExt journalExt = new JournalExt();
        journalExt.setDefaultValue();
        PbScope<Object> sysScope = tranContext.getSysScope();
        if (!ObjectUtils.isEmpty(jouScope.get(REQ_CHNL))) {
            journalExt.setReqChnl((String) jouScope.get(REQ_CHNL));
        }
        if (!ObjectUtils.isEmpty(jouScope.get(REQ_DATE))) {
            journalExt.setReqDate((String) jouScope.get(REQ_DATE));
        }
        if (!ObjectUtils.isEmpty(jouScope.get(BUSI_KEY))) {
            journalExt.setBusiKey((String) jouScope.get(BUSI_KEY));
        }
        if (!ObjectUtils.isEmpty(jouScope.get(REF_CODE))) {
            String refCode = (String) jouScope.get(REF_CODE);
            journalExt.setSysSeqNum(refCode.substring(refCode.length() - 6));
        }
        if (!ObjectUtils.isEmpty(jouScope.get(TRAN_STATE))) {
            String tranState = (String) jouScope.get(TRAN_STATE);
            if ("U".equals(tranState)) {
                journalExt.setTranState("8");
            }
            if ("R".equals(tranState)) {
                journalExt.setTranState("9");
            }
            if ("S".equals(tranState)) {
                journalExt.setTranState("0");
            }
        }
        if (!ObjectUtils.isEmpty(sysScope.get(TRAN_CODE))) {
            journalExt.setIntTxnCode((String) sysScope.get(TRAN_CODE));
        }
        if (!ObjectUtils.isEmpty(sysScope.get(BIZ_TYPE))) {
            journalExt.setBusiType((String) sysScope.get(BIZ_TYPE));
        }
        journalExt = BeanMapUtil.mapToBean(scopeValUnit, journalExt);
        //替换track2,track3，reserve6，reserve7中的@符号为|
        if (!ObjectUtils.isEmpty(journalExt.getTrack2())) {
            journalExt.setTrack2(journalExt.getTrack2().replaceAll("@", "|"));
        }
        if (!ObjectUtils.isEmpty(journalExt.getTrack3())) {
            journalExt.setTrack3(journalExt.getTrack3().replaceAll("@", "|"));
        }
        if (!ObjectUtils.isEmpty(journalExt.getReserve6())) {
            journalExt.setReserve6(journalExt.getReserve6().replaceAll("@", "|"));
        }
        if (!ObjectUtils.isEmpty(journalExt.getReserve7())) {
            journalExt.setReserve7(journalExt.getReserve7().replaceAll("@", "|"));
        }
        return journalExt;
    }

    /**
     * 处理标签集合
     * 从ScopeValUnit对象中提取标签集合，并将其添加到HashSet中返回
     * 此方法主要用于处理和转换标签数据结构，确保返回的HashSet中不包含空字符串
     *
     * @param scopeValUnit 包含标签集合的ScopeValUnit对象，不应为null
     * @return 包含处理后标签集合的HashSet
     */
    public static Set<String> processTagSet(ScopeValUnit scopeValUnit) {
        Set<String> hashSet = new HashSet<>();
        // 检查参数列表是否为空
        if (!ObjectUtils.isEmpty(scopeValUnit.get(TAG_SET))) {
            String tagSet = scopeValUnit.get(TAG_SET).toString();
            if (!ObjectUtils.isEmpty(tagSet)) {
                hashSet.add(tagSet);
            }
        }
        return hashSet;
    }

    /**
     * 处理扩展信息
     * 该方法旨在从ScopeValUnit对象中提取并处理扩展信息，如果扩展信息存在且为Map类型，则将其转换为Map<String, Object>类型并返回
     * 同时，从原始ScopeValUnit对象中移除已处理的扩展信息
     *
     * @param scopeValUnit 包含可能的扩展信息的ScopeValUnit对象
     * @return 返回一个Map<String, Object>，包含处理后的扩展信息如果不存在扩展信息或其类型不是Map，则返回一个空的Map
     */
    public static Map<String, Object> processExtInfo(ScopeValUnit scopeValUnit) {
        // 初始化一个空的Map，用于存储扩展信息
        Map<String, Object> extInfoMap = new HashMap<>();
        // 检查ScopeValUnit对象中是否存在扩展信息
        if (!ObjectUtils.isEmpty(scopeValUnit.get(EXT_INFO))) {
            // 获取扩展信息
            Object scopeExtInfo = scopeValUnit.get(EXT_INFO);
            // 判断扩展信息是否为Map类型
            if (scopeExtInfo instanceof Map) {
                // 将扩展信息转换为Map<String, Object>类型
                extInfoMap = (Map<String, Object>) scopeExtInfo;
            }
        }
        // 返回处理后的扩展信息Map
        return extInfoMap;
    }
}