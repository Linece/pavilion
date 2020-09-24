package com.pavilion.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pavilion.common.BaseController;
import com.pavilion.common.ResponseData;
import com.pavilion.hclib.HcOpenApi;
import com.pavilion.hclib.HcOpenApiBak;
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

    @Value("${openApi.host}")
    private String host;
    @Value("${openApi.appKey}")
    private String appKey;
    @Value("${openApi.appSecret}")
    private String appSecret;
    @Value("${openApi.groupsUrl}")
    private String groupsUrl;

	@Value("${openApi.groupsUrlBak}")
	private String groupsUrlBak;


    @Value("${openApi.allGroupUrl}")
    private String allGroupUrl;
    @Value("${openApi.countGroupUrl}")
    private String countGroupUrl;

    @Value("${openApi.cameraIndexCode}")
    private String cameraIndexCode;
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
     *2.2.2查询时间范围内的多个统计组的客流统计数据
     * @return
     */
    @GetMapping("/passengerFlowgroups/{type}")
    public JSONObject passengerFlowgroupsA(@PathVariable("type")String type){
	     int inNUm = 0;
	     int outNum = 0;
	     int holValue = 0;
	     String result = HcOpenApi.byStartAndEnd(host, appKey, appSecret, groupsUrl,countGroupUrl,type);
		    JSONObject reuslt = JSONObject.parseObject(result);
		    if(null != reuslt && reuslt.get("data") != null && !"".equals(reuslt.get("data"))){
			    JSONArray array = JSONArray.parseArray(((JSONObject)reuslt.get("data")).get("list").toString());
			    if(array.size() > 0){
				    for(int i=0;i<array.size();i++){
					    inNUm += Integer.valueOf(array.getJSONObject(i).get("flowInNum").toString());
					    outNum += Integer.valueOf(array.getJSONObject(i).get("flowOutNum").toString());

				    }
			    }
		    }
		  holValue = inNUm -outNum;
    return JSONObject.parseObject( getResult(inNUm,outNum,holValue));
    }

  public static String getResult(int inNUm,int outNum,int holValue){
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
