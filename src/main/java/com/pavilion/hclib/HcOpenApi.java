package com.pavilion.hclib;

import com.hikvision.artemis.sdk.ArtemisHttpUtil;
import com.hikvision.artemis.sdk.config.ArtemisConfig;

import java.util.HashMap;
import java.util.Map;

public class HcOpenApi {

   public static  String result(){
       ArtemisConfig.host="156.142.99.132";
       ArtemisConfig.appKey="29013874";
       ArtemisConfig.appSecret="t5wa1nrQ2OJ6xDIb2zde";
       final String getSecurityApi = "/api/visitor/v2/appointment"; // 接口路径
       Map<String, String> path = new HashMap<String, String>(2) {
           {
               put("https://", getSecurityApi);
           }
       };
       Map<String,String> querys = new HashMap<String,String>();//get 请求的查询参数
       querys.put("start", "0");
       querys.put("size", "20");
       String result = ArtemisHttpUtil.doGetArtemis(path, querys, null, null);
       System.out.println(result);
       return result;
   }
}
