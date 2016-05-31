package com.jyyl.jinyou.http;


import com.jyyl.jinyou.entity.LoginResult;
import com.jyyl.jinyou.entity.VersionInfo;

import java.util.Map;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

/**
 * @Fuction: 请求数据服务，这里是所有网络请求的接口方法
 * @Author: Shang
 * @Date: 2016/4/11  14:21
 */
public interface ApiService {


    //获取验证码
    @GET(Api.SEURITYCODE_URL+"/{phone}&{type}&{industry}")
    Observable<HttpResultList<String>> getSecurityCode(@Path("phone") String phone,
                                                       @Path("type") String type,
                                                       @Path("industry") String industry);

    //注册
    @POST(Api.REGISTER_URL)
    Observable<HttpResultList<String>> registerAccount(@Body Map<String, String> params);

    //登录
    @GET(Api.LOGIN_URL + "/{loginTime}&{memberAccount}&{memberPassword}&{industry}&{enterpriseCode}")
    Observable<HttpResultList<LoginResult>> loginAccount(@Path("loginTime") String loginTime,
                                                         @Path("memberAccount") String memberAccount,
                                                         @Path("memberPassword") String memberPassword,
                                                         @Path("industry") String industry,
                                                         @Path("enterpriseCode") String enterpriseCode);


    //上传导游位置
    @POST(Api.UPLOAD_LOCATION_URL)
    Observable<HttpResultList<String>> uploadLocation(@Body Map<String, String> params);

    //上传导游信息
    @POST(Api.UPLOAD_GUIDEINFO_URL)
    Observable<HttpResultList<String>> uploadGuideInfo(@Body Map<String, String> params);

    //获取导游信息
    @POST(Api.GET_GUIDEINFO_URL)
    Observable<HttpResultList<String>> getGuideInfo(@Body Map<String, String> params);


    //获取导游所有设备
    @POST(Api.USER_DEVICES_URL)
    Observable<HttpResultList<String>> getUserDevices(@Body Map<String, String> params);



    //版本更新
    @GET(Api.UPDATE_URL+"/{versioncode}&{versiontype}&{industry}")
    Observable<HttpResultList<VersionInfo>> updateVersion(@Path("versioncode") String versioncode,
                                                          @Path("versiontype") String versiontype,
                                                          @Path("industry") String industry);

}
