package com.pavilion.controller;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pavilion.common.BaseController;
import com.pavilion.common.ResponseData;
import com.pavilion.util.CommonUtil;
import com.pavilion.util.HttpClientUtil;
import com.pavilion.util.WeatherUtil;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Calendar;
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

    static int flowInNumA = 0;
    static int flowOutNumA = 0;
    static int holdValueA = 0;

    static int flowInNumB = 0;
    static int flowOutNumB = 0;
    static int holdValueB = 0;
    /**
     * 天气接口
     * @return
     */
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
    public JSONObject passengerFlowgroupsA(@PathVariable("type")String type){

        String result = null;
        int flowInRandNum =  RandomUtils.nextInt(0, 10)*3;
        int flowOutRandNum = RandomUtils.nextInt(0, 9)*3;
        if("1".equals(type)){
            flowInNumA  += flowInRandNum;
            flowOutNumA += flowOutRandNum;
            holdValueA = (flowInNumA - flowOutNumA);

            if(!(flowInNumA >= flowOutNumA && holdValueA >=0)){
                holdValueA = Math.abs(holdValueA)+flowInNumA-flowOutNumA;
                flowInNumA  -= flowInRandNum;
                flowOutNumA -= flowOutRandNum;
            }
            result = getResult(flowInNumA,flowOutNumA,holdValueA);
        }else if("2".equals(type)){
            flowInNumB  += flowInRandNum;
            flowOutNumB += flowOutRandNum;
            holdValueB = (flowInNumB - flowOutNumB);

            if(!(flowInNumB >= flowOutNumB && holdValueB >=0)){
                holdValueB = Math.abs(holdValueB)+flowInNumB-flowOutNumB;
                flowInNumB  -= flowInRandNum;
                flowOutNumB -= flowOutRandNum;
            }
             result = getResult(flowInNumB,flowOutNumB,holdValueB);

        }
    log.info("result", result);
    return JSONObject.parseObject(result);
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
      Calendar calendar = Calendar.getInstance();
      System.out.println();
      System.out.println(CommonUtil.arabicNumToChineseNum(LocalDateTime.now().getDayOfWeek().getValue()));
  }

	/**
	 * 初始化数据
	 */
	@GetMapping("/initData")
	public void initStada(String type,int inNum,int outNum,int holdeValu){
		if("1".equals(type)){
			flowInNumA = inNum;
			flowOutNumA= outNum;
			holdValueA = holdeValu;
		}else {
			flowInNumB = inNum;
			flowOutNumB= outNum;
			holdValueB = holdeValu;
		}

  }

  public String getResult(int inNUm,int outNum,int holValue){
	  String result =
			  "{\n"
					  + "\t\"code\": \"0\",\n"
					  + "\t\"data\": {\n"
					  + "\t\t\"flowInNum\": "+inNUm+",\n"
					  + "\t\t\"flowOutNum\": "+outNum+",\n"
					  + "\t\t\"groupId\": \"rrrr11111\",\n"
					  + "\t\t\"date\": \""+LocalDateTime.now().getYear()+"年"+LocalDateTime.now().getMonth().getValue()+"月"+LocalDateTime.now().getDayOfMonth()+"日"+"\",\n"
					  + "\t\t\"week\": \"星期"+CommonUtil.arabicNumToChineseNum(LocalDateTime.now().getDayOfWeek().getValue())+"\",\n"
					  + "\t\t\"holdValue\": "+holValue+"\n"
					  + "\t},\n"
					  + "\t\"msg\": \"success\"\n"
					  + "}";
	  return result;
  }
}
