package com.jyyl.guideapp.http;


import com.jyyl.guideapp.entity.WeatherInfo;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @Fuction: 请求数据服务，这里是所有网络请求的接口方法
 * @Author: Shang
 * @Date: 2016/4/11  14:21
 */
public interface ApiService {

    @GET("platformenter/platformenter/registered")
    Call<ResponseBody> getString();

    @GET("platformenter/platformenter/registered")
    Call<WeatherInfo> login(@Query("phone") String phone,@Query("password") String password);
}
