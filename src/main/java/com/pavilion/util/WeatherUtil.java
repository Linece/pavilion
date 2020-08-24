package com.pavilion.util;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;

public class WeatherUtil {

   public static String getWeather(){
       BufferedReader in = null;

       String content = null;
       try {
           //拼地址
           String apiUrl = "http://www.weather.com.cn/data/sk/101100906.html";
           // 定义HttpClient
           HttpClient client = new DefaultHttpClient();
           // 实例化HTTP方法
           HttpGet request = new HttpGet();
           request.setURI(new URI(apiUrl));
           HttpResponse response = client.execute(request);
           in = new BufferedReader(new InputStreamReader(response.getEntity()
                   .getContent()));
           StringBuffer sb = new StringBuffer("");
           String line = "";
           String NL = System.getProperty("line.separator");
           while ((line = in.readLine()) != null) {
               sb.append(line + NL);
           }
           in.close();
           content = sb.toString();

       }catch (Exception e){

       }finally {
           if (in != null) {
               try {
                   in.close();// 最后要关闭BufferedReader
               } catch (Exception e) {
                   e.printStackTrace();
               }
           }

       }
        return content;
   }


}
