package com.nantian.nbp.main.service.impl;

import com.nantian.nbp.main.mapper.VersionMapper;
import com.nantian.nbp.main.service.VersionService;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VersionServiceImpl implements VersionService {

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;
    @Override
    public String findVersion() {
        VersionMapper mapper = sqlSessionTemplate.getMapper(VersionMapper.class);
        return mapper.findVersion();
    }
}
