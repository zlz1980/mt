package com.n.fbsp.atom.platform.seed.other.service;




import com.n.fbsp.atom.platform.seed.other.po.TEbJournalPo;

import java.util.Map;

/**
 * @Author :
 * @create 2024/2/28 14:18
 */
public interface IJournalService {

    int insertJournalInfo(TEbJournalPo tEbJournalPo);

    int updateJournalInfo(TEbJournalPo tEbJournalPo);

    TEbJournalPo selectJournalInfo(Map<String, Object> params);

    int updateJournalState(TEbJournalPo tEbJournalPo);

    /**
     * 根据主键查询流水信息
     *
     * @param params 包含查询条件的映射，通常包括主键和对应的值
     * @return 返回一个TEbJournalPo对象，包含查询到的流水信息
     */
    TEbJournalPo selectJournalInfoByPk(Map<String, Object> params);


    /**
     * 根据tranDate+innerBusiKey索引查询流水信息
     *
     * @param params 包含查询条件的参数映射，其中应包括用于定位日志记录的索引信息
     * @return 返回一个TEbJournalPo对象，包含查询到的流水信息
     */
    TEbJournalPo selectJournalInfoByIdx1(Map<String, Object> params);

    /**
     * 根据reqChnl+acctNo+merCode+authCode索引查询流水信息
     *
     * @param params 包含查询条件的参数映射，其中应包括用于定位日志记录的索引信息
     * @return 返回一个TEbJournalPo对象，包含查询到的流水信息
     */
    TEbJournalPo selectJournalInfoByIdx2(Map<String, Object> params);

    /**
     * 根据rltReqDate+rltBusiKey索引查询流水信息
     *
     * @param params 包含查询条件的参数映射，其中应包括用于定位日志记录的索引信息
     * @return 返回一个TEbJournalPo对象，包含查询到的流水信息
     */
    TEbJournalPo selectJournalInfoByIdx3(Map<String, Object> params);

}
