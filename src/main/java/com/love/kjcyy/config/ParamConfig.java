package com.love.kjcyy.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author: kj
 * @Date: 2022/08/23/18:12
 */
@Component
@ConfigurationProperties(prefix = "wx")
@Data
public class ParamConfig {
    // 百度ak
    public String ak;
    // 天行 api key
    public String txKey;
    // 微信公众号APPID
    public String appId;
    // 公众号 秘钥
    public String secret;
    // 微信公众号模板id
    public String templateId;
    // 恋爱日
    public String love;
    // 是否使用公历
    public boolean isGregorian;
    // 女朋友生日（农历）
    public String birthdayPrincess;
    // 男朋友生日（农历）
    public String birthdayMan;

}
