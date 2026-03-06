package com.n.fbsp.atom.platform.seed.other.service.impl;



import com.n.fbsp.atom.platform.seed.other.mapper.ITebJournalMapper;
import com.n.fbsp.atom.platform.seed.other.po.TEbJournalPo;
import com.n.fbsp.atom.platform.seed.other.service.IJournalService;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Author : 
 * @create 2024/2/28 14:18
 */
@Service
public class JournalServiceImpl implements IJournalService {

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    @Override
    public int insertJournalInfo(TEbJournalPo tEbJournalPo) {
        ITebJournalMapper iTebJournalMapper = sqlSessionTemplate.getMapper(ITebJournalMapper.class);
        return iTebJournalMapper.insertJournalInfo(tEbJournalPo);
    }

    @Override
    public int updateJournalInfo(TEbJournalPo tEbJournalPo) {
        ITebJournalMapper iTebJournalMapper = sqlSessionTemplate.getMapper(ITebJournalMapper.class);
        return iTebJournalMapper.updateJournalInfo(tEbJournalPo);
    }

    @Override
    public TEbJournalPo selectJournalInfo(Map<String, Object> params) {
        ITebJournalMapper iTebJournalMapper = sqlSessionTemplate.getMapper(ITebJournalMapper.class);
        return iTebJournalMapper.selectJournalInfo(params);
    }

    @Override
    public int updateJournalState(TEbJournalPo tEbJournalPo) {
        ITebJournalMapper iTebJournalMapper = sqlSessionTemplate.getMapper(ITebJournalMapper.class);
        return iTebJournalMapper.updateJournalState(tEbJournalPo);
    }

    @Override
    public TEbJournalPo selectJournalInfoByPk(Map<String, Object> params) {
        ITebJournalMapper iTebJournalMapper = sqlSessionTemplate.getMapper(ITebJournalMapper.class);
        return iTebJournalMapper.selectJournalInfoByPk(params);
    }

    @Override
    public TEbJournalPo selectJournalInfoByIdx1(Map<String, Object> params) {
        ITebJournalMapper iTebJournalMapper = sqlSessionTemplate.getMapper(ITebJournalMapper.class);
        return iTebJournalMapper.selectJournalInfoByIdx1(params);
    }

    @Override
    public TEbJournalPo selectJournalInfoByIdx2(Map<String, Object> params) {
        ITebJournalMapper iTebJournalMapper = sqlSessionTemplate.getMapper(ITebJournalMapper.class);
        return iTebJournalMapper.selectJournalInfoByIdx2(params);
    }

    @Override
    public TEbJournalPo selectJournalInfoByIdx3(Map<String, Object> params) {
        ITebJournalMapper iTebJournalMapper = sqlSessionTemplate.getMapper(ITebJournalMapper.class);
        return iTebJournalMapper.selectJournalInfoByIdx3(params);
    }


}
