package com.n.fbsp.atom.platform.seed.db.seq;

import com.n.fbsp.atom.platform.seed.db.seq.mapper.SeqNumMapper;
import com.nantian.nbp.base.model.FlowUnit;
import com.nantian.nbp.flow.engine.service.api.Atom;
import com.nantian.nbp.flow.engine.service.api.AtomService;
import com.nantian.nbp.flow.engine.service.api.ScopeValUnit;
import com.nantian.nbp.flow.engine.service.api.TranContext;
import com.nantian.nbp.flow.engine.service.api.context.AtomResult;
import com.nantian.nbp.flow.engine.service.api.context.RetType;
import com.nantian.nbp.flow.engine.service.api.exception.FlowException;
import com.nantian.nbp.utils.StrUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.n.fbsp.atom.platform.seed.db.seq.mapper.SeqNumMapper.PUB_SEQ_TYPE;
import static com.nantian.nbp.flow.engine.service.api.Constants.PARAM_NUM_EIGHT;
import static com.nantian.nbp.flow.engine.service.api.Constants.PARAM_SPILT_FLAG;
import static com.nantian.nbp.flow.engine.service.api.exception.ErrorCodeEnum.T_C0003092;

/**
 * 数据库序号生成
 * 多参(paramList).存储名|序列组|序列键名|初始值|最大值|过期时间|重试次数|步长
 */
@Atom("base.SeqNum")
public class SeqNumImpl implements AtomService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SeqNumImpl.class);

    private static final String SEQ_TIMEOUT_ERR_CODE = "000001";

    private static final long WAIT_TIME = 10L;

    private final PlatformTransactionManager transactionManager;

    private final SeqNumMapper seqNumMapper;

    public SeqNumImpl(DataSourceTransactionManager transactionManager, SeqNumMapper seqNumMapper) {
        this.transactionManager = transactionManager;
        this.seqNumMapper = seqNumMapper;
    }

    @Override
    public AtomResult doService(TranContext tranContext, ScopeValUnit scopeValUnit, FlowUnit flowUnit) {
        AtomResult result = new AtomResult();
        List<String> paramList = flowUnit.getParamList();
        if(Objects.isNull(paramList) || paramList.isEmpty()){
            throw new FlowException(T_C0003092.getCode(), T_C0003092.getCodeMsg("原子交易多参不为空,请检查参数配置"));
        }
        for (String param:paramList){
            String atomTranParam = StrUtils.trim(param);
            // 存储名|序列组|序列键名|初始值|最大值|过期时间|重试次数|步长
            String[] atomTranParams = StringUtils.delimitedListToStringArray(atomTranParam, PARAM_SPILT_FLAG);
            if (atomTranParams.length != PARAM_NUM_EIGHT) {
                // 设置结果状态为失败
                result.setRetType(RetType.FAILED);
                // 抛出异常，指示原子交易参数数量错误
                throw new FlowException(T_C0003092.getCode(), T_C0003092.getCodeMsg("原子交易参数个数不为[{}],请检查参数配置[{}]",
                        PARAM_NUM_EIGHT, atomTranParam));
            }
            long seqNum = trySeqNum(atomTranParams[1],atomTranParams[2],Long.parseLong(atomTranParams[3]),Long.parseLong(atomTranParams[4]),
                    Integer.parseInt(atomTranParams[5]),Integer.parseInt(atomTranParams[6]),Integer.parseInt(atomTranParams[7]));
            scopeValUnit.put(atomTranParams[0],StrUtils.toStrDefBlank(seqNum));
        }
        result.setRetType(RetType.SUCCESS);
        return result;
    }

    public long trySeqNum(String seqGroup,String seqKey,long initVal,long maxVal, int expireSecond,int retryTimes){
        return trySeqNum(seqGroup,seqKey,initVal,maxVal,expireSecond,retryTimes,1);
    }

    /**
     * 基于DB获取序列号
     * @param seqGroup 序列组,用于类型分类
     * @param seqKey 序列键名
     * @param initVal 初始值
     * @param maxVal 最大值
     * @param expireSecond 过期时间
     * @param retryTimes 重试次数
     * @param seqIncrement 步长
     * @return 获取到的序列号
     */
    public long trySeqNum(String seqGroup,String seqKey,long initVal,long maxVal, int expireSecond,int retryTimes,int seqIncrement){
        Exception res = new Exception();
        int idx;
        for (int i = 0; i < retryTimes; i++){
            idx = i+1;
            try {
                return getSeqNum(seqGroup,seqKey,initVal,maxVal,expireSecond,seqIncrement);
            }catch (SeqNumException e){
                throw e;
            }catch (Exception e){
                // 并发情况会出现唯一约束冲突异常，需要重试解决
                res = e;
                LOGGER.warn("seqNum retry times[{}]", idx);
                try {
                    TimeUnit.MILLISECONDS.sleep(idx * WAIT_TIME);
                } catch (InterruptedException ex) {
                    throw new SeqNumException(ex);
                }
            }
        }
        throw new RuntimeException(res);
    }

    public long getSeqNum(String seqGroup,String seqKey,long initVal,long maxVal,int expireSecond,int seqIncrement){
        TransactionDefinition transactionDefinition = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus transactionStatus = transactionManager.getTransaction(transactionDefinition);
        try {
            SeqNum seqNum = seqNumMapper.findSeqNumVal(PUB_SEQ_TYPE,seqGroup,seqKey);
            // 初始创建
            if (!Objects.equals(0L, maxVal) && Objects.isNull(seqNum)) {
                if(maxVal < initVal){
                    throw new SeqNumException("maxVal must be greater than initVal");
                }
                seqNumMapper.insertSeqNum(PUB_SEQ_TYPE,seqGroup,seqKey,initVal,initVal,maxVal,expireSecond);
                seqNum = seqNumMapper.findSeqNumVal(PUB_SEQ_TYPE,seqGroup,seqKey);
            }
            long nxtVal;
            // 到期删除异常
            int expire = seqNum.getSeqExpire();
            if(!Objects.equals(0,expire) && expire < seqNum.getRemainingTime()){
                seqNumMapper.delSeqNumVal(PUB_SEQ_TYPE,seqGroup,seqKey);
                throw new SeqNumException(SEQ_TIMEOUT_ERR_CODE,String.format("seqNum[%s]-[%s] expired, remaining time[%s]",seqGroup,seqKey,expire));
            }
            nxtVal = seqNum.getSeqVal() + seqIncrement;
            // 超过maxVal
            long seqMaxVal = seqNum.getMaxVal();
            if(!Objects.equals(0L, seqMaxVal) && nxtVal > seqMaxVal){
                nxtVal = seqNum.getInitVal() + 1;
                seqNumMapper.updateRestSeqNumVal(PUB_SEQ_TYPE,seqGroup,seqKey,nxtVal);
            }else {
                seqNumMapper.updateSeqNumVal(PUB_SEQ_TYPE,seqGroup,seqKey, nxtVal);
            }
            transactionManager.commit(transactionStatus);
            return nxtVal;
        }catch (SeqNumException e){
            // 删除事物提交
            if(Objects.equals(SEQ_TIMEOUT_ERR_CODE,e.getCode())){
                transactionManager.commit(transactionStatus);
            }
            throw e;
        }catch (Exception e){
            transactionManager.rollback(transactionStatus);
            throw e;
        }
    }
}
