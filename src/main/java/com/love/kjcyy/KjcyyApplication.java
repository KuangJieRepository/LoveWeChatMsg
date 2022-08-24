package com.love.kjcyy;

import com.love.kjcyy.utils.Weather;
import com.love.kjcyy.wx.SendMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
@EnableScheduling
public class KjcyyApplication {

    @Autowired
    private SendMsg sendMsg;

    public static void main(String[] args) {
        SpringApplication.run(KjcyyApplication.class, args);
    }

    //    @Scheduled(cron = "0 0 8 * * ?")
    @Scheduled(cron = "0/10 * 17 * * ? ")
    public void sendMsg() {
        sendMsg.sendByTemplate();
    }
}
