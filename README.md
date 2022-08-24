# LoveWeChatMsg
微信公众号测试号，定时推送早安消息

需要：
- 注册百度地图api

- 天行api

- 微信公众号测试号（有企业号的大佬当我没说）

配置信息见 application.yml

SendMsg.sendByTemplate()方法中xxx，是无法获取用户的位置信息，
写死的用户id，和位置信息，微信公众号测试号管理界面可以看到信息，填写其行政区域代码