package com.nantian.nbp.main.service;


import com.nantian.nbp.main.po.TPbTranReplay;

import java.util.Map;

/**
 * @Author : Liang Haizhen
 * @create 2024/2/28 14:18
 */
public interface ITranReplayInfoService {
    TPbTranReplay selectTranReplayInfo(int id);

    int updateTranReplayInfo(TPbTranReplay tPbTranReplay);

}
