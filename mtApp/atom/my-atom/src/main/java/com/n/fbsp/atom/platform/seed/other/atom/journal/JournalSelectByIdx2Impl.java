package com.n.fbsp.atom.platform.seed.other.atom.journal;

import com.google.common.collect.ImmutableSet;

import com.n.fbsp.atom.platform.seed.other.po.TEbJournalPo;
import com.n.fbsp.atom.platform.seed.other.service.IJournalService;
import com.nantian.nbp.flow.engine.service.api.Atom;
import com.nantian.nbp.flow.engine.service.api.ScopeValUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

import static com.n.fbsp.atom.platform.seed.other.po.TEbJournalPo.*;


/**
 * 流水查询实现类，通过reqChnl+acctNo+merCode+authCode索引用于在流程中查询流水信息。
 * 原子交易参数，流水对象名称
 * defJournal
 * <p>
 * 增强处理参数，四个参数都需要配置，不可缺少
 * reqChnl|XXXX
 * acctNo|XXXX
 * merCode|XXXX
 * authCode|XXXX
 *
 * @Author : 
 * @create 2025/12/12 14:18
 */
@Atom("fbsp.JournalSelectByIdx2")
public class JournalSelectByIdx2Impl extends AbstractJournalSelect {

    private static final Logger LOGGER = LoggerFactory.getLogger(JournalSelectByIdx2Impl.class);

    @Autowired
    private IJournalService journalService;

    @Override
    protected Set<String> getQueryParam() {
        return ImmutableSet.of(REQ_CHNL, ACCT_NO, MER_CODE, AUTH_CODE);
    }

    @Override
    protected TEbJournalPo selectJournalInfo(ScopeValUnit scopeValUnit) {
        // 查询流水表主表内容
        return journalService.selectJournalInfoByIdx2(scopeValUnit);
    }
}
