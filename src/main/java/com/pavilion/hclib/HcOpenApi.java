package com.pavilion.hclib;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hikvision.artemis.sdk.ArtemisHttpUtil;
import com.hikvision.artemis.sdk.config.ArtemisConfig;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class HcOpenApi {

    private static final Logger log = LoggerFactory.getLogger(HcOpenApi.class);
    private static  String pattern = "yyyy-MM-dd'T'HH:mm:ss:SSSZZ";
    private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    private static DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

//2.2.1根据时间及时间维度查询双目客流统计数据
   public static  String byStartDate(String host,String appKey,String appSecret,String securityApi){
       String result = null;
       try{
           ArtemisConfig.host=host;
           ArtemisConfig.appKey=appKey;
           ArtemisConfig.appSecret=appSecret;
           final String getSecurityApi =  securityApi; // 接口路径
           Map<String, String> path = new HashMap<String, String>(2) {
               {
                   put("https://", getSecurityApi);
               }
           };
           JSONObject jsonBody = new JSONObject();
           jsonBody.put("granularity","hourly");
           jsonBody.put("statTime",DateFormatUtils.format(new Date(), pattern));
           String body = jsonBody.toJSONString();
           result = ArtemisHttpUtil.doPostStringArtemis(path, body, null,
                   null,"application/json");
       }catch (Exception e){
           log.error("根据时间及时间维度查询双目客流统计数据",e.getMessage());
       }

       return result;
   }

    //查询时间范围内的多个统计组的客流统计数据
    public static  String byStartAndEnd(String host,String appKey,String appSecret,String groupsUrl,String countGroupUrl){
        String result = null;

       try{
           String startTime = DateFormatUtils.format(df2.parse(df.format(new Date())+" 00:00:00"), pattern);
           String endTime = DateFormatUtils.format(df2.parse(df.format(new Date())+" 23:59:59"), pattern);
           ArtemisConfig.host=host;
           ArtemisConfig.appKey=appKey;
           ArtemisConfig.appSecret=appSecret;
           final String getSecurityApi =  groupsUrl; // 接口路径
           Map<String, String> path = new HashMap<String, String>(2) {
               {
                   put("https://", getSecurityApi);
               }
           };
           JSONObject jsonBody = new JSONObject();
           jsonBody.put("ids",groupId(host,appKey,appSecret,countGroupUrl));
           jsonBody.put("startTime",startTime);
           jsonBody.put("startTime",DateFormatUtils.format(new Date(), pattern));
           jsonBody.put("granularity","hourly");
           String body = jsonBody.toJSONString();
           result = ArtemisHttpUtil.doPostStringArtemis(path, body, null,
                   null,"application/json");

       }catch (Exception e){
           log.error("查询时间范围内的多个统计组的客流统计数据",e.getMessage());
       }

        return result;
    }


    //2.2.3查询客流排行
    public static  String ranking(String host,String appKey,String appSecret,String rankingUrl){
        String result = null;

        try{
            String startTime = DateFormatUtils.format(df2.parse(df.format(new Date())+" 00:00:00"), pattern);
            String endTime = DateFormatUtils.format(df2.parse(df.format(new Date())+" 23:59:59"), pattern);
            ArtemisConfig.host=host;
            ArtemisConfig.appKey=appKey;
            ArtemisConfig.appSecret=appSecret;
            final String getSecurityApi =  rankingUrl; // 接口路径
            Map<String, String> path = new HashMap<String, String>(2) {
                {
                    put("https://", getSecurityApi);
                }
            };
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("topNumber","100");
            jsonBody.put("startTime",startTime);
            jsonBody.put("startTime",endTime);
            jsonBody.put("granularity","hourly");
            String body = jsonBody.toJSONString();
            result = ArtemisHttpUtil.doPostStringArtemis(path, body, null,
                    null,"application/json");

        }catch (Exception e){
            log.error("查询时间范围内的多个统计组的客流统计数据",e.getMessage());
        }

        return result;
    }


    /**
     * 获取统计组ID数据
     * @return
     */
    public static String groupId(String host, String appKey, String appSecret, String securityApi){
        StringBuilder builder = new StringBuilder();
        try{
            ArtemisConfig.host=host;
            ArtemisConfig.appKey=appKey;
            ArtemisConfig.appSecret=appSecret;
            final String getSecurityApi =  securityApi; // 接口路径
            Map<String, String> path = new HashMap<String, String>(2) {
                {
                    put("https://", getSecurityApi);
                }
            };
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("groupType","0");
            jsonBody.put("isCascade","0");
            jsonBody.put("pageNo","1");
            jsonBody.put("pageSize","500");
            //jsonBody.put("regionId","1");
            jsonBody.put("statType","0");
            String body = jsonBody.toJSONString();
            String result = ArtemisHttpUtil.doPostStringArtemis(path, body, null,
                    null,"application/json");
            JSONObject reuslt = JSONObject.parseObject(result);
            JSONArray array = JSONArray.parseArray(reuslt.get("list").toString());
            int k;
            if(array.size() > 9){
                k = 9;
            }else {
                k = array.size();
            }
            for (int i=0;i<k;i++){
                builder.append(array.getJSONObject(i).get("groupId").toString()+",");
            }
        }catch (Exception e){
            log.error("获取统计组ID数据",e.getMessage());
        }

        return builder.substring(0, builder.lastIndexOf(",")).toString();
    }

  public static void main(String[] args) {
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
        int flowInNum = 0;
        int flowOutNum = 0;
        int holdValue = 0;
	    StringBuilder  groupId = new StringBuilder();
	  Object jsonObj = JSONObject.parseObject(JSONObject.parseObject(builder.toString()).get("data").toString()).get("list");
	  if(!ObjectUtils.isEmpty(jsonObj)){
		  JSONArray array = JSONArray.parseArray(jsonObj.toString());
		  for(int k=0;k<array.size();k++){
			  flowInNum += Integer.valueOf(JSONObject.parseObject(array.get(k).toString()).get("flowInNum").toString());
			  flowOutNum += Integer.valueOf(JSONObject.parseObject(array.get(k).toString()).get("flowOutNum").toString());
			  holdValue += Double.valueOf(JSONObject.parseObject(array.get(k).toString()).get("holdValue").toString());
			  groupId.append(JSONObject.parseObject(array.get(k).toString()).get("groupId").toString()+",");
		  }
	  }
	  String result =
			  "{\n"
					  + "\t\"code\": \"0\",\n"
					  + "\t\"data\": {\n"
					  + "\t\t\"flowInNum\": "+flowInNum+",\n"
					  + "\t\t\"flowOutNum\": "+flowOutNum+",\n"
					  + "\t\t\"groupId\": \""+groupId.toString()+"\",\n"
					  + "\t\t\"holdValue\": "+holdValue+"\n"
					  + "\t},\n"
					  + "\t\"msg\": \"success\"\n"
					  + "}";
System.out.println(result);

  }
}
