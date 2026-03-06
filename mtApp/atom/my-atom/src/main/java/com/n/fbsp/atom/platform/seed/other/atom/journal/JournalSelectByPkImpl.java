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
 * 流水查询实现类，使用busiKey+reqdate+reqchnl用于在流程中查询流水信息。
 * 原子交易参数，流水对象名称
 * defJournal
 * <p>
 * 增强处理参数，三个参数都需要配置，不可缺少
 * busiKey|XXXX
 * reqDate|XXXX
 * reqChnl|XXXX
 *
 * @Author : 
 * @create 2024/2/28 14:18
 */
@Atom("fbsp.JournalSelectByPk")
public class JournalSelectByPkImpl extends AbstractJournalSelect {

    private static final Logger LOGGER = LoggerFactory.getLogger(JournalSelectByPkImpl.class);

    @Autowired
    private IJournalService journalService;


    @Override
    protected Set<String> getQueryParam() {
        return ImmutableSet.of(REQ_CHNL, REQ_DATE, BUSI_KEY);
    }

    @Override
    protected TEbJournalPo selectJournalInfo(ScopeValUnit scopeValUnit) {
        // 查询流水表主表内容
        return journalService.selectJournalInfoByPk(scopeValUnit);
    }
}
