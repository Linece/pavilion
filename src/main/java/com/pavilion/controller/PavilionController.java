package com.pavilion.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pavilion.common.BaseController;
import com.pavilion.common.ResponseData;
import com.pavilion.util.WeatherUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class PavilionController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(PavilionController.class);

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
}
