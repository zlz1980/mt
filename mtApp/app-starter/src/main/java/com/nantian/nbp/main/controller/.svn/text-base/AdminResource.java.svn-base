package com.nantian.nbp.main.controller;

import com.nantian.nbp.main.filter.AppInfo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 应用管理层接口
 * @author Administrator
 */
@RequestMapping("admin")
@RestController
public class AdminResource {

    private final AppInfo appInfo;

    public AdminResource(AppInfo appInfo) {
        this.appInfo = appInfo;
    }

    @GetMapping("info")
    public AppInfo info() {
        return appInfo;
    }

}
