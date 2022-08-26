# LoveWeChatMsg
微信公众号测试号，定时推送早安消息

需要：
- 注册百度地图api

- 天行api

- 微信公众号测试号（有企业号的大佬当我没说）

配置信息见 application.yml

SendMsg.sendByTemplate()方法中xxx，是无法获取用户的位置信息，
写死的用户id，和位置信息，微信公众号测试号管理界面可以看到信息，填写其行政区域代码


一个简单模板：

{{date.DATA}}  {{city.DATA}}

今天是我们恋爱的第：{{love.DATA}}天 
距离颜宝的生日还有：{{birthdayPrincess.DATA}}天
距离杰哥生日还有：{{birthdayMan.DATA}}天

此刻天气：{{now.DATA}} 
体感温度：{{feels_like.DATA}}°
当前风级：{{wind_class.DATA}}
当前风向：{{wind_dir.DATA}}

今日最低气温：{{low.DATA}}°
今日最高气温：{{high.DATA}}°
今日白天天气：{{text_day.DATA}}
今日白天风级：{{wc_day.DATA}}
今日夜间天气：{{text_night.DATA}}
今日夜间风级：{{wd_day.DATA}}

明日最低气温：{{low_tom.DATA}}°
明日最高气温：{{high_tom.DATA}}°
明日白天天气：{{text_day_tom.DATA}}
明日白天风级：{{wc_day_tom.DATA}}
明日夜间天气：{{text_night_tom.DATA}}
明日夜间风级：{{wc_night_tom.DATA}}

{{Rainbow.DATA}}
