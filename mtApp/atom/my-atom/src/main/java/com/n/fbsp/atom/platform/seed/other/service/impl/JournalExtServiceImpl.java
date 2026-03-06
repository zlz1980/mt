package com.n.fbsp.atom.platform.seed.other.service.impl;

import com.n.fbsp.atom.platform.seed.other.po.JournalExt;
import com.n.fbsp.atom.platform.seed.other.service.IJournalExtService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Author : 
 * @create 2024/2/28 14:18
 */
@Service
public class JournalExtServiceImpl implements IJournalExtService {

//    @Autowired
//    private SqlSessionTemplate sqlSessionTemplate;

    @Override
    public JournalExt selectJournalExt(Map<String, Object> params) {
//        InitJournalExtMapper initJournalExtMapper = sqlSessionTemplate.getMapper(InitJournalExtMapper.class);
//        return initJournalExtMapper.selectJournalExt(params);
        return null;
    }

    @Override
    public int insertJournalExt(JournalExt journalExt) {
//        InitJournalExtMapper initJournalExtMapper = sqlSessionTemplate.getMapper(InitJournalExtMapper.class);
//        return initJournalExtMapper.insertJournalExt(journalExt);
        return 0;
    }

    @Override
    public int updateJournalExt(JournalExt journalExt) {
//        InitJournalExtMapper initJournalExtMapper = sqlSessionTemplate.getMapper(InitJournalExtMapper.class);
//        return initJournalExtMapper.updateJournalExt(journalExt);
        return 0;
    }
}
