package com.nantian.nbp.flow.engine.service.api;

import com.nantian.nbp.base.model.PbLog;
import com.nantian.nbp.flow.engine.service.api.context.FeContext;

/**
 * 扩展PBlog发送消息队列接口
 *
 * @author JiangTaiSheng
 */
public interface PbLogHandler {

    /**
     * 对异常对象处理返回信息
     */
    void handle (PbLog pbLog, FeContext feContext);
}
