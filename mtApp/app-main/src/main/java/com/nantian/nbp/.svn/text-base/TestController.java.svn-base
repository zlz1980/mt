package com.nantian.nbp;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RequestMapping("tt")
@RestController
public class TestController {

    @RequestMapping("comm")
    public Map<String,Object> test(){
        Map<String,Object> resCode = new HashMap<>(1);
        resCode.put("resCode","0000");

        Map<String,Object> res = new HashMap<>(1);
        res.put("res",resCode);
        return res;
    }


}
