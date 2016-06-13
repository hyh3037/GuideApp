package com.jyyl.jinyou.http;


import com.jyyl.jinyou.entity.DeviceInfoResult;
import com.jyyl.jinyou.entity.HeadImage;
import com.jyyl.jinyou.entity.GuideInfoResult;
import com.jyyl.jinyou.entity.MemberInfoResult;
import com.jyyl.jinyou.entity.TeamInfo;
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
    @GET(Api.SEURITYCODE_URL + "/{phone}&{type}&{industry}")
    Observable<HttpResult> getSecurityCode(@Path("phone") String phone,
                                           @Path("type") String type,
                                           @Path("industry") String industry);

    //注册
    @POST(Api.REGISTER_URL)
    Observable<HttpResult> registerAccount(@Body Map<String, String> params);

    //重置密码
    @POST(Api.RESET_URL)
    Observable<HttpResult> resetAccount(@Body Map<String, String> params);

    //登录
    @GET(Api.LOGIN_URL + "/{loginTime}&{memberAccount}&{memberPassword}&{industry}")
    Observable<HttpResult<GuideInfoResult>> loginAccount(@Path("loginTime") String loginTime,
                                                         @Path("memberAccount") String memberAccount,
                                                         @Path("memberPassword") String memberPassword,
                                                         @Path("industry") String industry);

    //上传头像
    @POST(Api.UPLOAD_IMAGE_URL)
    Observable<HttpResult<HeadImage>> uploadeImage(@Body Map<String, String> params);


    //上传导游位置
    @POST(Api.UPLOAD_LOCATION_URL)
    Observable<HttpResult> uploadLocation(@Body Map<String, String> params);

    //上传导游信息
    @POST(Api.UPLOAD_GUIDEINFO_URL)
    Observable<HttpResult<GuideInfoResult>> uploadGuideInfo(@Body Map<String, String> params);

    //获取导游信息
    @POST(Api.GET_GUIDEINFO_URL)
    Observable<HttpResult<GuideInfoResult>> getGuideInfo(@Body Map<String, String> params);


    //获取导游设备信息
    @POST(Api.USER_DEVICES_URL)
    Observable<HttpResult<DeviceInfoResult>> getUserDevices(@Body Map<String, String> params);

    //修改设备信息
    @POST(Api.DEVICE_INFO_EDIT_URL)
    Observable<HttpResult<DeviceInfoResult>> updateDevice(@Body Map<String, String> params);

    //解除设备绑定
    @GET(Api.DEVICE_DELETE_URL + "/{deviceIEMI}&{industry}&{token}")
    Observable<HttpResult> deleteDevice(@Path("deviceIEMI") String deviceIEMI,
                                        @Path("industry") String industry,
                                        @Path("token") String token);

    //创建旅游团队
    @POST(Api.TEAM_CREATE_URL)
    Observable<HttpResult> createTeam(@Body Map<String, String> params);

    //查询团队信息
    @POST(Api.TEAM_INFO_URL)
    Observable<HttpResult<TeamInfo>> getTeamInfo(@Body Map<String, String> params);

    //解散旅游团队
    @GET(Api.TEAM_DELETE_URL + "/{tourTeamId}&{memberId}&{industry}&{token}")
    Observable<HttpResult> deleteTeam(@Path("tourTeamId") String tourTeamId,
                                      @Path("memberId") String memberId,
                                      @Path("industry") String industry,
                                      @Path("token") String token);

    //添加游客
    @POST(Api.MEMBER_ADD_URL)
    Observable<HttpResult> addMember(@Body Map<String, String> params);

    //获取游客信息
    @POST(Api.MEMBER_INFO_URL)
    Observable<HttpResult<MemberInfoResult>> getMemberInfo(@Body Map<String, String> params);


    //版本更新
    @GET(Api.UPDATE_URL + "/{versioncode}&{versiontype}&{industry}")
    Observable<HttpResult<VersionInfo>> updateVersion(@Path("versioncode") String versioncode,
                                                      @Path("versiontype") String versiontype,
                                                      @Path("industry") String industry);

}
