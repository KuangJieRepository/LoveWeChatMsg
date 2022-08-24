package com.love.kjcyy.wx;

import com.love.kjcyy.config.ParamConfig;
import com.love.kjcyy.utils.DateTimeUtil;
import com.love.kjcyy.utils.RainbowUtil;
import com.love.kjcyy.utils.Weather;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.result.WxMediaUploadResult;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.bean.WxMpMassNews;
import me.chanjar.weixin.mp.bean.WxMpMassOpenIdsMessage;
import me.chanjar.weixin.mp.bean.material.WxMediaImgUploadResult;
import me.chanjar.weixin.mp.bean.material.WxMpNewsArticle;
import me.chanjar.weixin.mp.bean.result.WxMpMassSendResult;
import me.chanjar.weixin.mp.bean.result.WxMpMassUploadResult;
import me.chanjar.weixin.mp.bean.result.WxMpUserList;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @Author: kj
 * @Date: 2022/08/22/14:52
 */
@Slf4j
@RequestMapping("/wxConfig")
@RestController
public class SendMsg {

    private WxMpDefaultConfigImpl wxMpDefaultConfig;
    private WxMpService wxMpService;

    @Autowired
    private ParamConfig paramConfig;


    @PostConstruct
    public void init() {
        wxMpDefaultConfig = new WxMpDefaultConfigImpl();
        wxMpDefaultConfig.setAppId(paramConfig.getAppId());
        wxMpDefaultConfig.setSecret(paramConfig.getSecret());
        wxMpService = new WxMpServiceImpl();
        wxMpService.setWxMpConfigStorage(wxMpDefaultConfig);
    }

    /**
     * 微信公众号消息验证
     *
     * @param signature
     * @param timestamp
     * @param nonce
     * @param echostr
     * @return
     */
    @RequestMapping("/auth")
    public String auth(String signature, String timestamp, String nonce, String echostr) {
        ArrayList<String> temp = new ArrayList<>();
        String token = "kjcyy";
        temp.add(timestamp);
        temp.add(nonce);
        temp.add(token);
        Collections.sort(temp);
        String sha1Hex = DigestUtils.sha1Hex(temp.get(0) + temp.get(1) + temp.get(2));
        if (sha1Hex.equals(signature)) {
            return echostr;
        } else {
            log.error("验证失败");
            return null;
        }
    }

    /**
     * 获取token
     *
     * @return
     */
    public String getToken() {
        try {
            return wxMpService.getAccessToken();
        } catch (WxErrorException e) {
            log.error("error:{}", e.getMessage(), e);
            return "";
        }
    }


    /**
     * 获取用户列表
     *
     * @return
     */
    public List<String> getUserList() {
        try {
            WxMpUserList wxMpUserList = wxMpService.getUserService().userList(null);
            return wxMpUserList.getOpenids();
        } catch (WxErrorException e) {
            log.error("error:{}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * 推送消息
     */
    @RequestMapping("/send")
    public void send() {
        List<String> userList = this.getUserList();
        File file = new File("D:\\kj\\BaiduNetdiskWorkspace\\图片\\可达鸭.jpg");
        try {
            FileInputStream inputStream = new FileInputStream(file);
            // 上传图文消息的封面图片
            WxMediaUploadResult uploadMediaRes = wxMpService.getMaterialService().mediaUpload(WxConsts.MediaFileType.IMAGE, "jpg", inputStream);

            // 上传图文消息的正文图片(返回的url拼在正文的<img>标签中)
            WxMediaImgUploadResult imagedMediaRes = wxMpService.getMaterialService().mediaImgUpload(file);
            String url = imagedMediaRes.getUrl();

            WxMpMassNews news = new WxMpMassNews();
            WxMpNewsArticle article1 = new WxMpNewsArticle();
            article1.setTitle("标题1");
            article1.setContent("内容1内容1内容1内容1内容1内容1内容" +
                    "1内容1内容1内容1内容1内容1内容1内容1内容1内容1内容1内容1" +
                    "内容1内容1内容1内容1内容1内容1内容1内容1内容1内容1内容1内容1");
            article1.setThumbMediaId(uploadMediaRes.getMediaId());
            news.addArticle(article1);

            WxMpMassUploadResult massUploadResult = wxMpService.getMassMessageService().massNewsUpload(news);
            WxMpMassOpenIdsMessage massMessage = new WxMpMassOpenIdsMessage();
            massMessage.setMsgType(WxConsts.MassMsgType.MPNEWS);
            massMessage.setMediaId(massUploadResult.getMediaId());
            userList.forEach((id) -> massMessage.getToUsers().add(id));
            WxMpMassSendResult massResult = wxMpService.getMassMessageService().massOpenIdsMessageSend(massMessage);
            log.info("群发结果：{}", massResult);
        } catch (Exception e) {
            log.error("error:{}", e.getMessage(), e);
        }

    }

    /**
     * 发送模板消息
     */
//    @RequestMapping("/sendByTemplate")
    public void sendByTemplate() {
        Weather weather = new Weather();

        // 获取天气信息
        List<String> userList = this.getUserList();
        for (String uid : userList) {
            Map<String, String> weatherInfo = null;
            /*
             *  此处，测试公众号，没能力获取用户的省市区信息，所以此处写死，，等以后优化
             */
            if ("xxx".equals(uid)) {
                weatherInfo = weather.getWeatherInfo("410103", "河南省郑州市二七区", paramConfig.ak);
            }
            if ("xxx".equals(uid)) {
                weatherInfo = weather.getWeatherInfo("110114", "北京市北京市昌平区", paramConfig.ak);
            }
            WxMpTemplateMessage templateMessage = WxMpTemplateMessage.builder()
                    .toUser(uid)
                    .templateId(paramConfig.templateId)
                    .url("https://github.com/KuangJieRepository/LoveWeChatMsg")
                    .build();
            //日期
            templateMessage.addData(new WxMpTemplateData("date", DateTimeUtil.getNowDate(), "blue"));
            templateMessage.addData(new WxMpTemplateData("city", weatherInfo.get("city"), "blue"));
            templateMessage.addData(new WxMpTemplateData("love", DateTimeUtil.getLoveDay(paramConfig.getLove()) + "", "#fd79a8"));
            templateMessage.addData(new WxMpTemplateData("birthdayPrincess", DateTimeUtil.getBirthdayDay(paramConfig.getBirthdayPrincess(), paramConfig.isGregorian()) + "", "#fd79a8"));
            templateMessage.addData(new WxMpTemplateData("birthdayMan", DateTimeUtil.getBirthdayDay(paramConfig.getBirthdayMan(), paramConfig.isGregorian()) + "", "#fd79a8"));
            templateMessage.addData(new WxMpTemplateData("now", weatherInfo.get("now"), "#7bed9f"));
            templateMessage.addData(new WxMpTemplateData("feels_like", weatherInfo.get("feels_like"), "#7bed9f"));
            templateMessage.addData(new WxMpTemplateData("wind_class", weatherInfo.get("wind_class"), "blue"));
            templateMessage.addData(new WxMpTemplateData("wind_dir", weatherInfo.get("wind_dir"), "blue"));
            templateMessage.addData(new WxMpTemplateData("low", weatherInfo.get("low"), "#ffa502"));
            templateMessage.addData(new WxMpTemplateData("high", weatherInfo.get("high"), "#ffa502"));
            templateMessage.addData(new WxMpTemplateData("text_day", weatherInfo.get("text_day"), "blue"));
            templateMessage.addData(new WxMpTemplateData("wc_day", weatherInfo.get("wc_day"), "blue"));
//            templateMessage.addData(new WxMpTemplateData("wd_day", weatherInfo.get("wd_day"), "blue"));
            templateMessage.addData(new WxMpTemplateData("text_night", weatherInfo.get("text_night"), "blue"));
            templateMessage.addData(new WxMpTemplateData("wd_day", weatherInfo.get("wd_day"), "blue"));
//            templateMessage.addData(new WxMpTemplateData("wd_night", weatherInfo.get("wd_night"), "blue"));
            templateMessage.addData(new WxMpTemplateData("low_tom", weatherInfo.get("low_tom"), "#f1c40f"));
            templateMessage.addData(new WxMpTemplateData("high_tom", weatherInfo.get("high_tom"), "#f1c40f"));
            templateMessage.addData(new WxMpTemplateData("text_day_tom", weatherInfo.get("text_day_tom"), "blue"));
            templateMessage.addData(new WxMpTemplateData("wc_day_tom", weatherInfo.get("wc_day_tom"), "blue"));
//            templateMessage.addData(new WxMpTemplateData("wd_day_tom", weatherInfo.get("wd_day_tom"), "blue"));
            templateMessage.addData(new WxMpTemplateData("text_night_tom", weatherInfo.get("text_night_tom"), "blue"));
            templateMessage.addData(new WxMpTemplateData("wc_night_tom", weatherInfo.get("wc_night_tom"), "blue"));
//            templateMessage.addData(new WxMpTemplateData("wd_night_tom", weatherInfo.get("wd_night_tom"), "blue"));
            templateMessage.addData(new WxMpTemplateData("Rainbow", RainbowUtil.getText(paramConfig.getTxKey()), "#ffa502"));

            try {
                wxMpService.getTemplateMsgService().sendTemplateMsg(templateMessage);
            } catch (WxErrorException e) {
                log.error("error:{}", e.getMessage(), e);
            }
        }
    }
}
