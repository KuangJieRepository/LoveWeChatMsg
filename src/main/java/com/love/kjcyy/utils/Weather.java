package com.love.kjcyy.utils;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: kj
 * @Date: 2022/08/23/18:08
 */
@Slf4j
public class Weather {

    public Map<String, String> getWeatherInfo(String districtId, String city, String ak) {
        // 城市名
        Map<String, String> info = new HashMap<>();
        try {
            // 获取天气信息
            String weatherRs = HttpClientUtils.get("https://api.map.baidu.com/weather/v1/?district_id=" + districtId + "&data_type=all&ak=" + ak);
            Map weather = (Map) JSON.parseObject(weatherRs, Map.class).get("result");
            Map now = (Map) weather.get("now");
            JSONArray forecastsArr = (JSONArray) weather.get("forecasts");
            Map forecastsToday = (Map) forecastsArr.get(0);
            Map forecastsTomorrow = (Map) forecastsArr.get(1);
            // 当前天气
            info.put("now", now.get("text").toString());
            // 体感温度
            info.put("feels_like", now.get("feels_like").toString());
            // 当前风级
            info.put("wind_class", now.get("wind_class").toString());
            // 当前风向
            info.put("wind_dir", now.get("wind_dir").toString());
            // 今日最低气温
            info.put("low", forecastsToday.get("low").toString());
            // 今日最高气温
            info.put("high", forecastsToday.get("high").toString());
            // 今日白天天气
            info.put("text_day", forecastsToday.get("text_day").toString());
            // 今日夜间天气
            info.put("text_night", forecastsToday.get("text_night").toString());
            // 今日白天风级
            info.put("wc_day", forecastsToday.get("wc_day").toString());
            // 今日白天风向
            info.put("wd_day", forecastsToday.get("wd_day").toString());
            // 今日夜间风级
            info.put("wc_night", forecastsToday.get("wc_night").toString());
            // 今日夜间风向
            info.put("wd_night", forecastsToday.get("wd_night").toString());

            // 明日最低气温
            info.put("low_tom", forecastsTomorrow.get("low").toString());
            // 明日最高气温
            info.put("high_tom", forecastsTomorrow.get("high").toString());
            // 明日白天天气
            info.put("text_day_tom", forecastsTomorrow.get("text_day").toString());
            // 明日夜间天气
            info.put("text_night_tom", forecastsTomorrow.get("text_night").toString());
            // 明日白天风级
            info.put("wc_day_tom", forecastsTomorrow.get("wc_day").toString());
            // 明日白天风向
            info.put("wd_day_tom", forecastsTomorrow.get("wd_day").toString());
            // 明日夜间风级
            info.put("wc_night_tom", forecastsTomorrow.get("wc_night").toString());
            // 明日夜间风向
            info.put("wd_night_tom", forecastsTomorrow.get("wd_night").toString());
            // 城市名
            info.put("city", city);
            return info;
        } catch (Exception e) {
            log.error("error:{}", e.getMessage(), e);
            return null;
        }
    }
}
