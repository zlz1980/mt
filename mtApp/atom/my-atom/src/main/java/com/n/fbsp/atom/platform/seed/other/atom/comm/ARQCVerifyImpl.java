package com.n.fbsp.atom.platform.seed.other.atom.comm;

import com.n.fbsp.atom.platform.seed.other.eccs.EsscAssistant;
import com.nantian.nbp.base.model.FlowUnit;
import com.nantian.nbp.flow.engine.service.api.Atom;
import com.nantian.nbp.flow.engine.service.api.AtomService;
import com.nantian.nbp.flow.engine.service.api.ScopeValUnit;
import com.nantian.nbp.flow.engine.service.api.TranContext;
import com.nantian.nbp.flow.engine.service.api.context.AtomResult;
import com.nantian.nbp.flow.engine.service.api.context.RetType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * In:
 * 第一行:卡号  acctNo|
 * 第二行:卡序列号 cardSeqNum|
 * 第三行: 应用上送的arqc数据 icArqcData|
 * 第四行:应用交易记数器9F36   processGene|
 * 第五行:待验证的ARQC(应用密文9F26)  arqcData|
 * 第六行:分散秘钥索引9F10第3到4位作为“分散密钥索引” keyIndex|
 * 第七行:算法标识9F10第15到16位作为算法标识   algFlag|
 * 第八行:密文版本号  01 -接触式   17 -非接触式   cryptoVersion|
 *
 * @Author :
 * @create 2025/2/10 14:18
 */
@Atom("fbsp.ARQCVerify")
public class ARQCVerifyImpl implements AtomService {


    private static final Logger LOGGER = LoggerFactory.getLogger(ARQCVerifyImpl.class);
    @Autowired
    protected EsscAssistant esscAssistant;
//    @Autowired
//    private BusiApi busiApi;
//    @Autowired
//    private ARQCARPCApi arqcarpcApi;

    @Override
    public AtomResult doService(TranContext tranContext, ScopeValUnit scopeValUnit, FlowUnit flowUnit) {
        AtomResult atomResult = new AtomResult();
        //获取增强处理多参
        /*List<String> paramList = flowUnit.getParamList();
        if (paramList.size() != 8) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + " 增强处理参数个数不为8,请检查参数配置[{}]", paramList);
            throw new FlowException(T_C0003092.getCode(), T_C0003092.getCodeMsg("增强处理参数个数不为8,请检查参数配置[{}]", paramList));
        }
        //日志输出增强处理参数信息
        LOGGER.debug("增强处理参数 [{}]", JsonUtil.objToString(paramList));
        ScopeValUnit reqScopeValUnit = new ScopeValUnit();
        FlowParaUtils.setFlowParaValues(tranContext, reqScopeValUnit, flowUnit);
        //卡号
        String acctNo = (String) reqScopeValUnit.get(ACCT_NO);
        Map<String, Object> cardBinResult = busiApi.getCardBin(acctNo);
        // 如果查询操作成功
        String cardBin;
        if (!ObjectUtils.isEmpty(cardBinResult)) {
            cardBin = (String) cardBinResult.get(BANK_ID);
            LOGGER.debug("卡号:[{}],卡Bin[{}]", acctNo, cardBin);
        } else {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "基于卡号在缓存中未找到对应卡BIN [{}]", acctNo);
            throw new FlowException(T_C0003092.getCode(), T_C0003092.getCodeMsg("基于卡号在缓存中未找到对应卡BIN [{}]", acctNo));
        }

        String cardSeqNum = (String) reqScopeValUnit.get(CARD_SEQ_NUM);
        if (cardSeqNum.length() > 2) {
            cardSeqNum = cardSeqNum.substring(cardSeqNum.length() - 2);
        } else {
            cardSeqNum = StrUtils.padLeftWithZero(2, cardSeqNum);
        }
        LOGGER.debug("卡序列号后两位cardSeqNum:[{}]", cardSeqNum);
        String panAndCardSeqNum = acctNo + cardSeqNum;
         *//* 应用主账号（PAN）和应用主账号序列号用来组成一个16位长的数据，用来生成分散的唯一DEA密钥A。如果应用主账号序列号不存在，用一个字节
       00代替。如果应用主账号和应用主账号序列号的长度不等于16个数字：如果长度小于16个数字，右对齐，前面补0；如果长度大于16个数字，取最右边16个数字。*//*
        if (panAndCardSeqNum.length() > 16) {
            panAndCardSeqNum = panAndCardSeqNum.substring(panAndCardSeqNum.length() - 16);
        } else {
            panAndCardSeqNum = StrUtils.padLeftWithZero(16, cardSeqNum);
        }
        LOGGER.debug("卡号+序列号panAndCardSeqNum:[{}]", panAndCardSeqNum);
        //取得应用交易计数器
        String processGene = (String) reqScopeValUnit.get(PROCESS_GENE);
        //取得计算ARQC使用数据
        Object icArqcData = reqScopeValUnit.get(IC_ARQC_DATA);
        String cryptoVersion = (String) reqScopeValUnit.get(CRYPTO_VERSION);
        String icArqcDataStr = arqcarpcApi.getIcArqcData(icArqcData, cryptoVersion);
        //取得ARQC
        String arqcData = (String) reqScopeValUnit.get(ARQC_DATA);
        //分散密钥索引
        String keyIndex = (String) reqScopeValUnit.get(KEY_INDEX);
        //获取算法标识
        String algFlag = (String) reqScopeValUnit.get(ALG_FLAG);
        //据算法标志拼sBinEx
        String sBinEx;
        //国密算法
        if (algFlag.equals("04")) {
            sBinEx = String.format("%s-sm", cardBin);
        } else if (algFlag.equals("01")) {
            //国际算法
            sBinEx = cardBin;
        } else {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "算法标识非01或04,AlgFlag:[{}]", algFlag);
            throw new FlowException(T_C0003092.getCode(), T_C0003092.getCodeMsg("算法标识非01或04,AlgFlag:[{}]", algFlag));
        }
        try {
            //根据分散密钥索引取得交易主密钥的全名和交易主密钥版本号
            Map<String, Object> keyInfo = arqcarpcApi.getKeyInfo(keyIndex, sBinEx);
            String keyName = (String) keyInfo.get(KEY_NAME);
            int keyVersion = Integer.parseInt((String) keyInfo.get(KEY_VERSION));
            EsscAPI esscApi = esscAssistant.getEsscAPI();
            //VerifyARQC
            boolean verifyResult = esscApi.CebVerifyARQCUsingDerivedKey(keyName, keyVersion, panAndCardSeqNum, processGene, icArqcDataStr, arqcData, 0);
            LOGGER.info("ARQC验证成功,验证结果:[{}]", verifyResult);
            MapUtils.setVal(scopeValUnit, "ARQCResult", verifyResult);
        } catch (Exception e) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "验证ARQC异常,异常信息[{}]", e.getMessage(), e);
            throw new FlowException(T_C0003092.getCode(), tranContext.getFeTranCode(), T_C0003092.getCodeMsg("验证ARQC异常,异常信息[{}]", e.getMessage()), e);
        }
        LOGGER.debug("当前作用域信息 [{}]", scopeValUnit);*/
        atomResult.setRetType(RetType.SUCCESS);
        return atomResult;
    }

}
