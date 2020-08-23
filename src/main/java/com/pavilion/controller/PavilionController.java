package com.pavilion.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pavilion.common.BaseController;
import com.pavilion.common.ResponseData;
import com.pavilion.hclib.HcOpenApi;
import com.pavilion.util.HttpClientUtil;
import com.pavilion.util.WeatherUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class PavilionController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(PavilionController.class);

//    @Autowired
//    private MemberFlowUploadService memberFlowUploadService;

    @GetMapping("/getWeather")
    public ResponseData getWeather(){
        Map map = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            map = mapper.readValue(WeatherUtil.getWeather(), Map.class);
        }catch (Exception e){
            log.error(e.getMessage());
        }
        return success(map);
    }

    @GetMapping("/flow")
    public String getHcFlow(){
//        String url = "https://156.142.99.132/api/visitor/v2/appointment";
//        String jsonStr = "{}";
//        String httpOrgCreateTestRtn = HttpClientUtil.doPost(url, jsonStr, "utf-8");
//        System.out.println("result:"+httpOrgCreateTestRtn);
//        return httpOrgCreateTestRtn;
        return HcOpenApi.result();
    }

    @GetMapping("/flo")
    public String getHc(){
        String url = "https://156.142.99.132/api/visitor/v2/appointment";
        String jsonStr = "{}";
        String httpOrgCreateTestRtn = HttpClientUtil.doPost(url, jsonStr, "utf-8");
        System.out.println("result:"+httpOrgCreateTestRtn);
        return httpOrgCreateTestRtn;
    }
}
