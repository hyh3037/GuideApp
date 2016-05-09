package com.jyyl.guideapp.widget.pickerview;

import java.util.Calendar;

public class DateObject extends Object{
    private int year ;
    private int month;
    private int day;
    private int week;
    private int hour;
    private int minute;
    private String listItem;
    
    /**
     * 日期对象的4个参数构造器，用于设置日期
     * @param year
     * @param month
     * @param day
     * @author sxzhang
     */
    public DateObject(int year, int month, int day,int week) {
        super();
        this.year = year;
        int maxDayOfMonth = Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH);
        if(day > maxDayOfMonth){
            this.month = month + 1;
            this.day = day % maxDayOfMonth;
        }else{
            this.month = month;
            this.day = day;
        }
        this.week = week % 7 == 0 ? 7 : week % 7;
        
        if(day == Calendar.getInstance().get(Calendar.DAY_OF_MONTH)){
            this.listItem = String.format("%02d", this.month) +"月" + String.format("%02d", this.day)  + 
                    "日      "+ "  今天  ";
        }else{
            this.listItem = String.format("%02d", this.month) +"月" + String.format("%02d", this.day)  + 
                    "日      "+ getDayOfWeekCN(week);
        }
        
    }
    
    /**
     * 日期对象的2个参数构造器，用于设置时间
     * @param hour
     * @param minute
     * @param isHourType true:传入的是hour; false: 传入的是minute
     * @author sxzhang
     */
    public DateObject(int hour,int minute,boolean isHourType) {
        super();
        if(isHourType && hour != -1){        //设置小时
            if(hour >= 24){
                this.hour = hour % 24;
            }else
                this.hour = hour;

            if (this.hour<10){
                this.listItem = "0"+this.hour;
            }else {
                this.listItem = String.valueOf(this.hour);
            }

        }else if(!isHourType && minute != -1){    //设置分钟
            if(minute >= 60)
                this.minute = minute % 60;
            else
                this.minute = minute;

            if (this.minute<10){
                this.listItem = "0"+this.minute;
            }else {
                this.listItem = String.valueOf(this.minute);
            }
        }
    }
    
    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public int getYear() {
        return year;
    }
    public void setYear(int year) {
        this.year = year;
    }
    public int getMonth() {
        return month;
    }
    public void setMonth(int month) {
        this.month = month;
    }
    public int getDay() {
        return day;
    }
    public void setDay(int day) {
        this.day = day;
    }
    public String getListItem() {
        return listItem;
    }
    public void setListItem(String listItem) {
        this.listItem = listItem;
    }
    
    /**
     * 根据day_of_week得到汉字星期
     * @return
     */
    public static String getDayOfWeekCN(int day_of_week){
        String result = null;
        switch(day_of_week){
        case 1:
            result = "星期日";
            break;
        case 2:
            result = "星期一";
            break;
        case 3:
            result = "星期二";
            break;
        case 4:
            result = "星期三";
            break;
        case 5:
            result = "星期四";
            break;
        case 6:
            result = "星期五";
            break;
        case 7:
            result = "星期六";
            break;    
        default:
            break;
        }
        return result;
    }
}
