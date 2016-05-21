package com.jyyl.guideapp.http;


import com.jyyl.guideapp.entity.DeviceInfo;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @Fuction: 请求数据服务，这里是所有网络请求的接口方法
 * @Author: Shang
 * @Date: 2016/4/11  14:21
 */
public interface ApiService {

    @GET("http://1y5a133877.iask.in:19425/tourism/messageauth/{phone}&{type}&{industry}")
    Observable<HttpResult<String>> sendCode(@Path("phone") String phone,
                                            @Path("type") String type,
                                            @Path("industry") String industry);

    @GET("platformenter/platformenter/registered")
    Observable<HttpResult<List<DeviceInfo>>> getDeviceInfo(@Query("phone") String phone);
}
