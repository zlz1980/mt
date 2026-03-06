package org.jts.mock;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/webapi/obps/execTran")
public class TestController {

    @RequestMapping("/v1/{tranCode}")
    public String v1(){
        return "v1";
    }

    @RequestMapping("/v2/{tranCode}")
    public String v2(){
        return "v2";
    }
}
