package com.pavilion.controller;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pavilion.common.BaseController;
import com.pavilion.common.ResponseData;
import com.pavilion.hclib.HcOpenApi;
import com.pavilion.util.HttpClientUtil;
import com.pavilion.util.WeatherUtil;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Map;

@RestController
public class PavilionController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(PavilionController.class);

    private static  String pattern = "yyyy-MM-dd'T'HH:mm:ss:SSSZZ";

    @Value("${openApi.host}")
    private String host;
    @Value("${openApi.appKey}")
    private String appKey;
    @Value("${openApi.appSecret}")
    private String appSecret;
    @Value("${openApi.groupsUrl}")
    private String groupsUrl;
    @Value("${openApi.allGroupUrl}")
    private String allGroupUrl;
    @Value("${openApi.countGroupUrl}")
    private String countGroupUrl;

    @Value("${openApi.cameraIndexCode}")
    private String cameraIndexCode;

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

    /**
     * 2.2.1根据时间及时间维度查询双目客流统计数据
     * @return
     */
    @GetMapping("/passengerFlowAll")
    public ResponseData passengerFlowAll(){
    String aa =
        "{\n"
            + "  \"code\": \"0\",\n"
            + "  \"data\": {\n"
            + "    \"list\": [\n"
            + "      {\n"
            + "        \"createTime\": 1520111111,\n"
            + "        \"flowInNum\": 10,\n"
            + "        \"flowOutNum\": 10,\n"
            + "        \"groupId\": \"rrrr1111\",\n"
            + "        \"groupName\": \"test\",\n"
            + "        \"holdValue\": 5.0,\n"
            + "        \"statTime\": \"2019-01-01T00:00:00+08:00\",\n"
            + "        \"updateTime\": 1520111111\n"
            + "      }\n"
            + "    ],\n"
            + "    \"statTime\": \"2019-02-01T00:00:00+08:00\",\n"
            + "    \"total\": 10\n"
            + "  },\n"
            + "  \"msg\": \"success\"\n"
            + "}";
        //return success(HcOpenApi.byStartDate(host,appKey,appSecret,allGroupUrl));
        return success(aa);
    }

    /**
     *2.2.2查询时间范围内的多个统计组的客流统计数据
     * @return
     */
    @GetMapping("/passengerFlowgroups/{type}")
    public ResponseData passengerFlowgroups(@PathVariable("type") String type){
    // return success(HcOpenApi.byStartAndEnd(host,appKey,appSecret,groupsUrl,countGroupUrl));

    StringBuilder builder =
        new StringBuilder("{\n" + "  \"code\": \"0\",\n" + "  \"data\": {\n" + "    \"list\": [");
        for (int i = 0;i<10;i++){
            int flowInNum = RandomUtils.nextInt(1, 100);
            int flowOutNum = RandomUtils.nextInt(1, 100);
            if(i==9){
                builder.append(
                        "{\n"
                                + "        \"createTime\": 1520111111,\n"
                                + "        \"flowInNum\": "+flowInNum+",\n"
                                + "        \"flowOutNum\": "+flowOutNum+",\n"
                                + "        \"groupId\": \"rrrr1111\",\n"
                                + "        \"groupName\": \"test\",\n"
                                + "        \"holdValue\": 5.0,\n"
                                + "        \"statTime\": \"2019-01-01T00:00:00+08:00\",\n"
                                + "        \"updateTime\": 1520111111\n"
                                + "      }");
            }else {
                builder.append("{\n"
                        + "        \"createTime\": 1520111111,\n"
                        + "        \"flowInNum\": "+flowInNum+",\n"
                        + "        \"flowOutNum\": "+flowOutNum+",\n"
                        + "        \"groupId\": \"rrrr1111\",\n"
                        + "        \"groupName\": \"test\",\n"
                        + "        \"holdValue\": 5.0,\n"
                        + "        \"statTime\": \"2019-01-01T00:00:00+08:00\",\n"
                        + "        \"updateTime\": 1520111111\n"
                        + "      },");
            }

        }
        builder.append("],\n" +
                "    \"total\": 10\n" +
                "  },\n" +
                "  \"msg\": \"success\"\n" +
                "}");

    return success(JSONObject.parseObject(builder.toString()));
    }



    @GetMapping("/flo")
    public String getHc(){
        String url = "https://156.142.99.132/api/visitor/v2/appointment";
        String jsonStr = "{}";
        String httpOrgCreateTestRtn = HttpClientUtil.doPost(url, jsonStr, "utf-8");
        System.out.println("result:"+httpOrgCreateTestRtn);
        return httpOrgCreateTestRtn;
    }

  public static void main(String[] args) {
    System.out.println(RandomUtils.nextInt(1, 100));
  }
}
