package com.nantian.nbp.main.controller;

import com.nantian.nbp.cache.server.api.CacheClientApi;
import com.nantian.nbp.main.service.VersionService;
import com.nantian.nbp.main.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import static com.nantian.nbp.main.utils.Constants.*;

/**
 * 查询版本号
 */
@RestController
public class VersionController {
    @Autowired
    private CacheClientApi cacheClientApi;
    @Autowired
    private VersionService versionService;

    @RequestMapping(value = "/version", method = { RequestMethod.GET })
    public Map<String, String> getVersion() {
        Map<String, String> map = new HashMap<>(4);
        String dbVersion = VERSION_PREFIX + versionService.findVersion();
        String cacheVersion = VERSION_PREFIX + cacheClientApi.getSysCfg(Constants.CURRENT_CACHE_VERSION);
        map.put(DB_VERSION_KEY, dbVersion);
        map.put(CACHE_VERSION_KEY, cacheVersion);
        return map;
    }
}
