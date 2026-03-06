package com.nantian.nbp.main.service.impl;


import com.nantian.nbp.main.mapper.TPbTranReplayMapper;
import com.nantian.nbp.main.po.TPbTranReplay;
import com.nantian.nbp.main.service.ITranReplayInfoService;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author : Liang Haizhen
 * @create 2024/2/28 14:18
 */
@Service
public class TranReplayImpl implements ITranReplayInfoService {

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;


    @Override
    public TPbTranReplay selectTranReplayInfo(int id) {
        TPbTranReplayMapper tPbTranReplayMapper = sqlSessionTemplate.getMapper(TPbTranReplayMapper.class);
        return tPbTranReplayMapper.selectTranReplayInfo(id);
    }

    @Override
    public int updateTranReplayInfo(TPbTranReplay tPbTranReplay) {
        TPbTranReplayMapper tPbTranReplayMapper = sqlSessionTemplate.getMapper(TPbTranReplayMapper.class);
        return tPbTranReplayMapper.updateTranReplayInfo(tPbTranReplay);
    }
}
