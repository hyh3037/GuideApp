package com.jyyl.guideapp.http;


import com.jyyl.guideapp.bean.WeatherInfo;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * @Fuction: 请求数据服务，这里是所有网络请求的接口方法
 * @Author: Shang
 * @Date: 2016/4/11  14:21
 */
public interface ApiService {

    @GET("http://www.weather.com.cn/adat/sk/101010100.html")
    Call<ResponseBody> getString();

    @GET("http://www.weather.com.cn/adat/sk/101010100.html")
    Call<WeatherInfo> getWeatherInfo();
}
