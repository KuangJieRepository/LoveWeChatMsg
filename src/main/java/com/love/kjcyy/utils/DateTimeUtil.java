package com.love.kjcyy.utils;

import cn.hutool.core.date.ChineseDate;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @Author: kj
 * @Date: 2022/08/24/13:19
 */
@Slf4j
public class DateTimeUtil {

    public static String getNowDate() {
        LocalDateTime now = LocalDateTime.now();
        String date = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) + "  " + changeWeek(now.getDayOfWeek().getValue());
        return date;
    }

    /**
     * 星期几转化
     *
     * @param num
     * @return
     */
    public static String changeWeek(int num) {
        switch (num) {
            case 1:
                return "星期一";
            case 2:
                return "星期二";
            case 3:
                return "星期三";
            case 4:
                return "星期四";
            case 5:
                return "星期五";
            case 6:
                return "星期六（今天就是最后一天啦）";
            case 7:
                return "星期日（宝宝终于放假啦~）";
            default:
                return "";
        }
    }

    /**
     * 计算恋爱了多少天
     * 使用 hutool 工具类
     *
     * @param loveDay
     * @return
     */
    public static long getLoveDay(String loveDay) {
        LocalDateTime start = LocalDateTimeUtil.parse(loveDay, "yyyy-MM-dd");
        LocalDateTime end = LocalDateTimeUtil.now();
        Duration between = LocalDateTimeUtil.between(start, end);
        return between.toDays() + 1;
    }


    /**
     * 计算生日距离多少天
     * 使用 hutool 工具类
     *
     * @param birthday    生日
     * @param isGregorian 是否是公历
     * @return
     */
    public static long getBirthdayDay(String birthday, boolean isGregorian) {
        try {
            if (isGregorian) {
                // 公历
                LocalDateTime birthdayDate = LocalDateTimeUtil.parse(birthday, "yyyy-MM-dd");
                LocalDateTime now = LocalDateTimeUtil.now();
                // 当天
                if (birthdayDate.getMonthValue() == now.getMonthValue() && birthdayDate.getDayOfMonth() == now.getDayOfMonth()) {
                    return 0;
                }
                LocalDateTime birthdayDateNew = null;
                if (birthdayDate.getMonthValue() <= now.getMonthValue() && birthdayDate.getDayOfMonth() <= now.getDayOfMonth()) {
                    // 已经过了
                    birthdayDateNew = LocalDateTime.of(now.getYear() + 1, birthdayDate.getMonthValue(), birthdayDate.getDayOfMonth(), 0, 0);
                } else {
                    // 还没过
                    birthdayDateNew = LocalDateTime.of(now.getYear(), birthdayDate.getMonthValue(), birthdayDate.getDayOfMonth(), 0, 0);
                }
                return LocalDateTimeUtil.between(now, birthdayDateNew).toDays() + 1;
            } else {
                // 农历
                String[] split = birthday.split("-");
                ChineseDate now = new ChineseDate(LocalDateTimeUtil.now().toLocalDate());
                int birthMonth = Integer.parseInt(split[1]);
                int birthDay = Integer.parseInt(split[2]);
                if (birthMonth == now.getMonth() && birthDay == now.getDay()) {
                    return 0;
                }
                ChineseDate birthChineseDate = null;
                if (birthMonth <= now.getMonth() && birthDay <= now.getDay()) {
                    // 已经过了
                    birthChineseDate = new ChineseDate(now.getChineseYear() + 1, birthMonth, birthDay);
                } else {
                    // 还没过
                    birthChineseDate = new ChineseDate(now.getChineseYear(), birthMonth, birthDay);
                }
                return DateUtil.between(now.getGregorianDate(), birthChineseDate.getGregorianDate(), DateUnit.DAY);
            }
        } catch (Exception e) {
            log.error("日期格式填写错误:{}", e.getMessage(), e);
            return 0;
        }
    }
}
