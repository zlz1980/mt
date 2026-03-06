package com.n.fbsp.atom.platform.seed.other.mapper;


import com.n.fbsp.atom.platform.seed.other.po.JournalExt;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

/**
 * @author Administrator
 */
@Mapper
public interface InitJournalExtMapper {

    JournalExt selectJournalExt(Map<String, Object> params);

    int insertJournalExt(JournalExt journalExt);

    int updateJournalExt(JournalExt journalExt);
}
