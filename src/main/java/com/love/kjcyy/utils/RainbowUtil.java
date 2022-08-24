package com.love.kjcyy.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * @Author: kj
 * @Date: 2022/08/24/15:27
 * 彩虹屁
 */
@Slf4j
public class RainbowUtil {
    /**
     * @param key 控制台秘钥
     * @return
     */
    public static String getText(String key) {
        try {
            String text = HttpClientUtils.get("http://api.tianapi.com/caihongpi/index?key=" + key);
            Map map = JSON.parseObject(text, Map.class);
            JSONArray array = (JSONArray) map.get("newslist");
            Map content = (Map) array.get(0);
            return content.get("content").toString();
        } catch (Exception e) {
            log.error("error:{}", e.getMessage(), e);
            return null;
        }
    }
}
