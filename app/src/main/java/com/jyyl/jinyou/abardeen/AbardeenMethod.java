package com.jyyl.jinyou.abardeen;

import android.content.Context;
import android.util.Log;

import com.jyyl.jinyou.MyApplication;
import com.jyyl.jinyou.constans.Sp;
import com.jyyl.jinyou.utils.LogUtils;
import com.jyyl.jinyou.utils.SPUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * @Fuction: 腕表接口相关调用方法
 * @Author: Shang
 * @Date: 2016/6/3  13:35
 */
public class AbardeenMethod {

    private static String TAG = "AbardeenMethod";
    private static volatile AbardeenMethod instance = null;
    private Context appContext;

    private AbardeenMethod() {
        appContext = MyApplication.getInstance().getApplicationContext();
    }

    /**
     * 获取单例
     * @return 实例
     */
    public static AbardeenMethod getInstance() {

        // if already inited, no need to get lock everytime
        if (instance == null) {
            synchronized (AbardeenMethod.class) {
                if (instance == null) {
                    instance = new AbardeenMethod();
                }
            }
        }

        return instance;
    }


    /**
     * 连接腕表服务器 参数 {"cmd":"REQ","id":"1463126051218","params":{"clientCode":"C005","clientInfo":"iOS
     * 7.1.1|6|kt01w","deviceId":"sssssssssss","key":"cZTpQ4ddo6PahMhf","locale":"zh",
     * "mapType":"gaode","password":"123","username":"demo","ver":"1.0.4"}}
     * @return
     */
    public boolean connectServer() {

        JSONObject params = new JSONObject();
        JSONObject jsonObject = new JSONObject();

        try {
            String abdAccount = (String) SPUtils.get(appContext, Sp.SP_KEY_ABARDEEN_ACCOUNT,
                    "account");
            String abdPassword = (String) SPUtils.get(appContext, Sp.SP_KEY_ABARDEEN_PASSWORD,
                    "password");
            String clientInfo = "Android " + android.os.Build.VERSION.RELEASE + "|1|kt04w";
            params.put("username", abdAccount);
            params.put("password", abdPassword);
            params.put("clientInfo", clientInfo);
            params.put("locale", "zh");
            params.put("clientCode", "C005");
            params.put("mapType", "baidu");
            params.put("ver", "1.0.4");
            params.put("key", "cZTpQ4ddo6PahMhf");

            jsonObject.put("cmd", "REQ");
            jsonObject.put("id", SocketOpenHelper.nextCommandId());
            jsonObject.put("params", params);

            boolean isConnect = SocketOpenHelper.getInstance().connectServer(jsonObject);
            Log.d(TAG, "isConnect" + isConnect);
            return isConnect;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 绑定腕表 参数 {"cmd":"C_WEARER","id":"123","params":{"bindingValidationCode":"uX7H8UBO"}}
     * @param bindingValidationCode
     *         扫描成功后的字符串后面的查询字段
     *
     * @return {"cmd":"R_C_WEARER","code":"0","id":"123", "params":{"areas":[
     * {"id":254699763348692992, "lut":"160525150108", "type":0}], "avatarFn":"",
     * "createdTime":"160531060444", "gender":2, "height":0, "id":"269288159689732096",
     * "imageServer":"http://cwtcn-dl.6655.la:7190/", "imei":"867327020000856", "isAdmin":0,
     * "lastUpdatedTime":"160531060444", "markPicId":0, "mobile":"15813852565", "name":"宝贝",
     * "productId":"KT04", "relationship":8, "relationshipName":"其它", "relationshipPic":8,
     * "source":1, "weight":0}}
     */
    public JSONObject bindingDeviceByScan(String bindingValidationCode) {

        JSONObject params = new JSONObject();
        JSONObject jsonObject = new JSONObject();

        try {
            params.put("bindingValidationCode", bindingValidationCode);

            jsonObject.put("cmd", "C_WEARER");
            jsonObject.put("id", SocketOpenHelper.nextCommandId());
            jsonObject.put("params", params);

            JSONObject resultJson = SocketOpenHelper.getInstance().getResultDatas(jsonObject, "R_C_WEARER");
            return resultJson;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解除绑定 参数： {"cmd":"D_WEARER","id":"123","params":{"id":"269287238872231936"}}
     * @param id
     *         佩戴对象id
     *
     * @return {"cmd":"R_D_WEARER","code":"0","id":"123"}
     */
    public boolean deleteDevice(String id) {

        JSONObject params = new JSONObject();
        JSONObject jsonObject = new JSONObject();

        try {
            params.put("id", id);
            LogUtils.d(TAG, "删除设备id==>>" + id);

            jsonObject.put("cmd", "D_WEARER");
            jsonObject.put("id", SocketOpenHelper.nextCommandId());
            jsonObject.put("params", params);

            JSONObject resultJson = SocketOpenHelper.getInstance().getResultDatas(jsonObject,"R_D_WEARER");
            if (resultJson.toString().contains("code")){
                String code = (String) resultJson.get("code");
                if (code != null && "0".equals(code)) {
                    return true;
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 设置SOS号码（亲情号码 KT04）
     * 参数： {"cmd":"S_FN_EX","id":"1","params":
     *      {"imei":"867327020000856",
     *      "fns":[
     *          {"familyMobile":"","mobile":"112233","name":"GMA","no":1,"picId":5},
     *          {"familyMobile":"","mobile":"15898127566","name":"爸爸","no":2,"picId":0},
     *          {"familyMobile":"","mobile":"25555","name":"QWW","no":3,"picId":8},
     *          {"familyMobile":"","mobile":"1555","name":"QWWr","no":4,"picId":8},
     *          {"familyMobile":"","mobile":"122233","name":"GFS","no":5,"picId":4},
     *          {"familyMobile":"","mobile":"15840523536","name":"爷爷","no":6,"picId":2},
     *          {"familyMobile":"","mobile":"13700086848","name":"妈妈","no":7,"picId":1}]}}
     * <p/>
     *
     * 成功:
     {"cmd":"R_S_FN_EX","code":"0","id":"1","params":"20160607092431"}
     */
    public JSONObject setSosNumber(String imei, String mobile) {

        JSONObject params = new JSONObject();
        JSONArray fns = new JSONArray();
        JSONObject jsonObject = new JSONObject();

        try {
            JSONObject fn1 = new JSONObject();
            fn1.put("no", "1");
            fn1.put("mobile", mobile);
            fn1.put("familyMobile", "familyMobile");

            JSONObject fn2 = new JSONObject();
            fn2.put("no", "2");
            fn2.put("mobile", mobile);
            fn2.put("familyMobile", "familyMobile");

            JSONObject fn3 = new JSONObject();
            fn3.put("no", "3");
            fn3.put("mobile", mobile);
            fn3.put("familyMobile", "familyMobile");

            fns.put(fn1);
            fns.put(fn2);
            fns.put(fn3);

            params.put("imei", imei);
            params.put("fns", fns);

            jsonObject.put("cmd", "S_FN_EX");
            jsonObject.put("id", SocketOpenHelper.nextCommandId());
            jsonObject.put("params", params);

            JSONObject resultJson = SocketOpenHelper.getInstance().getResultDatas(jsonObject, "R_S_FN_EX");
            LogUtils.d(TAG, resultJson.toString());

            return resultJson;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 编辑手表信息
     * <p/>
     * 请求： {"cmd":"U_WEARER","id":"123", "params":{"areas":[], "createdTime":"150612073550",
     * "dob":"20081223","gender":2,"height":0.0, "id":"141025780779335680",
     * "imei":"860860000030000", "lastUpdatedTime":"150612073550", "markPicId":0,
     * "mobile":"13500000000", "name":"宝贝","weight":0.0}}
     * @param imei
     *         响应 {"cmd":"R_U_WEARER","code":"0","id":"123", "params":{"areas":[], "avatarFn":"",
     *         "createdTime":"741010124113", "dob":"20081223","gender":2,"height":0.0,
     *         "id":"141025780779335680","imageServer":"","imei":"860860000030000",
     *         "lastUpdatedTime":"741010124113","markPicId":0,"name":"宝贝","productId":"KT04",
     *         "relationship":8,"relationshipName":"其他", "relationshipPic":8,"source":1,
     *         "weight":0.0}}
     */
    public void setDeviceInfo(String id, String imei, String name, String mobile, String
            userMobile) {

        JSONObject params = new JSONObject();
        JSONObject jsonObject = new JSONObject();

        try {
            params.put("id", id);       //佩戴对象id
            params.put("imei", imei);
            params.put("name", name);           //佩戴对象名字，最大长度为32个英文字符。
            params.put("mobile", mobile);       //手表的SIM号码
            params.put("userMobile", userMobile);   //用户手机号码，回拨监听使用
            params.put("markPicId", 8);         //标记头像的索引值
            params.put("relationship", 8);      //对象与用户关系
            params.put("relationshipPic", 8);   //对象的关系头像索引
            params.put("relationshipName", "其他");   //对象的关系名称
            params.put("markPicId", 8);

            jsonObject.put("cmd", "U_WEARER");
            jsonObject.put("id", SocketOpenHelper.nextCommandId());
            jsonObject.put("params", params);

            SocketOpenHelper.getInstance().getResultDatas(jsonObject,"R_U_WEARER");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取腕表基本信息 参数： {"cmd":"Q_WEARER","id":"1462970483875","params" : {"imei" : "86086000003000"}}
     * @param imei
     *         执行成功 {"cmd":"R_Q_WEARER","code":"0","id":"1462970483875", "params":[ {"areas":[],
     *         "avatarFn":"", "createdTime":"150612073550", "dob":"20081223", "gender":2,
     *         "height":0.0, "id":"141025780779335680",  //腕表绑定id，删除用 "imageServer":"",
     *         "imei":"860860000030000", "isAdmin":0, "lastUpdatedTime":"150612073550",
     *         "markPicId":0, "mobile":"13500000000", "name":"Tom", "relationship":0,
     *         "relationshipName":"其他", "relationshipPic":1, "source":0, "userMobile":"",
     *         "weight":0.0}]} 执行失败： {"cmd":"R_Q_WEARER","code":"E500","id":"1462970483875",
     *         "message":"服务器内部错误"}
     */
    public void getDeviceInfo(String imei) {

        JSONObject params = new JSONObject();
        JSONObject jsonObject = new JSONObject();

        try {
            params.put("imei", imei);

            jsonObject.put("cmd", "Q_WEARER");
            jsonObject.put("id", SocketOpenHelper.nextCommandId());
            jsonObject.put("params", params);

            SocketOpenHelper.getInstance().getResultDatas(jsonObject,"R_Q_WEARER");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取腕表最新数据 参数：{"cmd":"Q_TLD","id":"20140825121212","params":{"imei":"860860000030001"}}
     * @return {"cmd":"R_Q_TLD","code":"0","id":"20140825121212", "params":[
     * {"act":{"i":"150610000140", "p":64, "s":176, "ss":0, "ut":"150707150105"}, "auto_time":"0",
     * "auto_type":"0", "connect_mode":"0", "enable_fn_wl":0, "hit":"141024103233-0",
     * "hourTime24":1, "imei":"860860000030001", "lct":{"i":"150402071533",
     * "id":"38487486913462272", "isStatic":false, "mt":"4", "o":113.945810300000000, "p":62,
     * "t":"1", "ts":"150618143820", "u":22.547382600000000, "wt":0}, "p":58, "ring_mode":"0",
     * "sms_location":"0", "st":"1506100001390581291111001", "ts":"1463627533168"}]}
     */
    public JSONArray getDeviceDatas(String imei) {

        JSONObject params = new JSONObject();
        JSONObject jsonObject = new JSONObject();

        try {
            params.put("imei", imei);

            jsonObject.put("cmd", "Q_TLD");
            jsonObject.put("id", SocketOpenHelper.nextCommandId());
            jsonObject.put("params", params);

            JSONObject resultJson = SocketOpenHelper.getInstance().getResultDatas(jsonObject,"R_Q_TLD");
            if (resultJson != null) {
                JSONArray paramsArray = resultJson.getJSONArray("params");
                return paramsArray;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }



    /**
     * 请求传输数据
     *
     * 参数:{"cmd":"RAU","id":"1312","params":{
     *      "dataId":"1",
     *      "targets":["T867293023112009"],
     *      "length":2048,
     *      "parts":2,
     *      "playLength":2,
     *      "type":"DF"}}
     * @return
     *
     * {"cmd":"ARA","code":"0","id":"12",
     *      "params":{"dataId":"2","offlines":["T867293023112009"]}}
     *
     * {"cmd":"ARA","code":"E500","id":"18","message":"服务器内部错误!"}
     */
    public JSONObject requestTransferVoice(String dataId, JSONArray targets, int length,
                                           int parts, int playLength, String type) {

        JSONObject params = new JSONObject();
        JSONObject jsonObject = new JSONObject();

        try {
            params.put("dataId", dataId);
            params.put("length", length);
            params.put("parts", parts);
            params.put("playLength", playLength);
            params.put("targets", targets);
            params.put("type", type);

            jsonObject.put("cmd", "RAU");
            jsonObject.put("id", SocketOpenHelper.nextCommandId());
            jsonObject.put("params", params);

            JSONObject resultJson = SocketOpenHelper.getInstance().getResultDatas(jsonObject,"ARA");
            return resultJson;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 客户端传输语音(SAU)
     *
     * {"attachments":[{"from":0,"to":2405}],
     * "cmd":"SAU",
     * "id":"22",
     * "params":{"dataId":"4","part":0}}二进制字节流
     * @return
     */
    public void startTransferVoice(String dataId, byte[] annex) {

        JSONObject params = new JSONObject();
        JSONObject attachment = new JSONObject();
        JSONArray  attachments = new JSONArray();
        JSONObject jsonObject = new JSONObject();

        try {
            params.put("dataId", dataId);
            params.put("part", 0);

            int parts = annex.length/(1024*20) + 1;
            int i = 1;
            boolean b = true;
            while (b){
                if (i == parts){
                    b = false;
                }
            }
            attachment.put("from", 0);
            attachment.put("to", annex.length - 1);
            attachments.put(attachment);

            jsonObject.put("cmd", "SAU");
            jsonObject.put("id", SocketOpenHelper.nextCommandId());
            jsonObject.put("params", params);
            jsonObject.put("attachments", attachments);

            SocketOpenHelper.getInstance().outputWrite(jsonObject, annex);

        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 客户端结束传输语音(SAE)
     *
     * {"cmd":"SAE","id":"23","params":{"dataId":"4"}}
     * @return
     *
     * {"cmd":"ASA","code":"0","id":"23","params":{"dataId":"4"}}
     * {"cmd":"ASA","code":"0","id":"11","params":{"dataId":"1"},"missing":[2,4,7]}
     */
    public boolean endTransferVoice(String dataId) {

        JSONObject params = new JSONObject();
        JSONObject jsonObject = new JSONObject();

        try {
            params.put("dataId", dataId);

            jsonObject.put("cmd", "SAE");
            jsonObject.put("id", SocketOpenHelper.nextCommandId());
            jsonObject.put("params", params);

            JSONObject resultJson = SocketOpenHelper.getInstance().getResultDatas(jsonObject,"ASA");
            if ("0".equals(resultJson.get("code"))){
                return true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

}
