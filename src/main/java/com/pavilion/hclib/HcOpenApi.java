package com.pavilion.hclib;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hikvision.artemis.sdk.ArtemisHttpUtil;
import com.hikvision.artemis.sdk.config.ArtemisConfig;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
}
