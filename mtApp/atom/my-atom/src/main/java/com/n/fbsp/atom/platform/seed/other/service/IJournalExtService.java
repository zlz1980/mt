package com.n.fbsp.atom.platform.seed.other.service;




import com.n.fbsp.atom.platform.seed.other.po.JournalExt;

import java.util.Map;

/**
 * @Author :
 * @create 2024/2/28 14:18
 */
public interface IJournalExtService {

    JournalExt selectJournalExt(Map<String, Object> params);

    int insertJournalExt(JournalExt journalExt);

    int updateJournalExt(JournalExt journalExt);

}
