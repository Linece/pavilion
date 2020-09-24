package com.pavilion.hclib;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hikvision.artemis.sdk.ArtemisHttpUtil;
import com.hikvision.artemis.sdk.config.ArtemisConfig;
import com.pavilion.util.DateUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class HcOpenApi {

    private static final Logger log = LoggerFactory.getLogger(HcOpenApi.class);
    private static  String pattern = "yyyy-MM-dd'T'HH:mm:ssZZ";
	private static  String formate1 = "yyyy-MM-dd";
	private static  String formate2 = "yyyy-MM-dd HH:mm:ss";

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
    public static  String byStartAndEnd(String host,String appKey,String appSecret,String groupsUrl,String countGroupUrl,String type){
        String result = null;

       try{
	       String startTime = DateFormatUtils.format(DateUtils.strToDate(DateUtils.DateToStr(new Date(),formate1)+" 00:00:00",formate2), pattern);
           ArtemisConfig.host=host;
           ArtemisConfig.appKey=appKey;
           ArtemisConfig.appSecret=appSecret;
           final String getSecurityApi =  groupsUrl; // 接口路径
           Map<String, String> path = new HashMap<String, String>(2) {
               {
                   put("https://", getSecurityApi);
               }
           };
            String ids = groupId(host,appKey,appSecret,countGroupUrl,type);
           if(ids != null && !"".equals(ids)){
               JSONObject jsonBody = new JSONObject();
               jsonBody.put("ids",groupId(host,appKey,appSecret,countGroupUrl,type));
               jsonBody.put("startTime",startTime);
               jsonBody.put("endTime",DateFormatUtils.format(new Date(), pattern));
               jsonBody.put("granularity","hourly");
               String body = jsonBody.toJSONString();
               result = ArtemisHttpUtil.doPostStringArtemis(path, body, null,
                       null,"application/json");
           }


       }catch (Exception e){
           log.error(type+":查询时间范围内的多个统计组的客流统计数据："+e.getMessage());
           e.printStackTrace();
       }
        return result;
    }


    //2.2.3查询客流排行
    public static  String ranking(String host,String appKey,String appSecret,String rankingUrl){
        String result = null;

        try{
            String startTime = null;
            String endTime = null;
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
            jsonBody.put("endTime",endTime);
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
    public static String groupId(String host, String appKey, String appSecret, String securityApi,String type){
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
            jsonBody.put("pageNo","1");
            jsonBody.put("pageSize","50");
            String body = jsonBody.toJSONString();
            String result = ArtemisHttpUtil.doPostStringArtemis(path, body, null,
                    null,"application/json");

            JSONObject reuslt = JSONObject.parseObject(result);
            JSONArray array = JSONArray.parseArray(((JSONObject)reuslt.get("data")).get("list").toString());
            if(array.size() > 0){
                for(int i=0;i<array.size();i++){
                    if("1".equals(type) && "A".equals(array.getJSONObject(i).get("name").toString())){
                        builder.append(array.getJSONObject(i).get("id").toString()+",");
                    }else if("2".equals(type) && "B".equals(array.getJSONObject(i).get("name").toString())){
                        builder.append(array.getJSONObject(i).get("id").toString()+",");
                    }

                }
            }
        }catch (Exception e){
            log.error("获取统计组ID数据",e.getMessage());
        }
        if(builder != null && !"".equals(builder.toString())){
            return builder.substring(0, builder.lastIndexOf(",")).toString();
        }
        return null;
    }

  public static void main(String[] args) throws ParseException {
		System.out.println(DateFormatUtils.format(DateUtils.strToDate(DateUtils.DateToStr(new Date(),formate1)+" 00:00:00",formate2), pattern));

  }

    public static String getPath(String path){
        try {
            return URLDecoder.decode(path, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
