package com.jyyl.jinyou.abading;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @Fuction: 腕表接口相关调用方法
 * @Author: Shang
 * @Date: 2016/6/3  13:35
 */
public class ABaDingMethod {

    private static String TAG = "ABaDingMethod";
    private static volatile ABaDingMethod instance = null;

    private ABaDingMethod() {
    }

    /**
     * 获取单例
     * @return 实例
     */
    public static ABaDingMethod getInstance() {

        // if already inited, no need to get lock everytime
        if (instance == null) {
            synchronized (ABaDingMethod.class) {
                if (instance == null) {
                    instance = new ABaDingMethod();
                }
            }
        }

        return instance;
    }


    /**
     * 连接腕表服务器 参数 {"cmd":"REQ","id":"1463126051218","params":{"clientCode":"C005","clientInfo":"iOS
     * 7.1.1|6|kt01w","deviceId":"sssssssssss","key":"cZTpQ4ddo6PahMhf","locale":"zh",
     * "mapType":"gaode","password":"123","username":"demo","ver":"1.0.4"}}
     *
     * @return
     * {"cmd":"ARE","code":"0","id":"1463126051218",
     * "params":{"sessionId":"1DefaultSession2","userId":774672119496704,"ver":"1.0.4",
     * "wearerStatus":[{"bVer":"1002","btMac":"100000000000","fVer":"v02",
     * "fnStatus":{"fns":[{"mobile":"13533334444","name":"","no":1,"picId":0},
     * {"mobile":"13533335555","name":"","no":2,"picId":0},{"mobile":"13533335555","name":"",
     * "no":3,"picId":0},{"mobile":"13533335555","name":"","no":4,"picId":0},
     * {"mobile":"13533335555","name":"","no":5,"picId":0},{"mobile":"13533335555","name":"",
     * "no":6,"picId":0},{"mobile":"13533335555","name":"","no":7,"picId":0},
     * {"mobile":"13533335555","name":"","no":8,"picId":0},{"mobile":"13533335555","name":"",
     * "no":9,"picId":0}],"imei":"860860000030000","lut":"20150710122140",
     * "userId":774672119496704,"userName":""},"functionStatus":{"all":true},
     * "imei":"860860000030000","mrStatus":{"imei":"860860000030000","lut":"20160507165744",
     * "mrs":[],"userId":123,"userName":""},"online":false,"productId":"KT01W",
     * "sVer":"KT04_abr_60_hv01_sv90_cn_st20150424","wifiMac":"200000000000",
     * "wmStatus":{"lut":"20150106151838"}}]}}
     */
    public void connectServer(String username, String password) {

        JSONObject params = new JSONObject();
        JSONObject jsonObject = new JSONObject();

        try {
            params.put("username", username);
            params.put("password", password);
            params.put("clientInfo", "Android 5.0.1|6|kt01w");
            params.put("locale", "zh");
            params.put("clientCode", "C005");
            params.put("mapType", "baidu");
            params.put("ver", "1.0.4");
            params.put("key", "cZTpQ4ddo6PahMhf");

            jsonObject.put("cmd", "REQ");
            jsonObject.put("id", SocketOpenHelper.nextCommandId());
            jsonObject.put("params", params);

            SocketOpenHelper.getInstance().connectServer(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * 绑定腕表 参数 {"cmd":"C_WEARER","id":"123","params":{"bindingValidationCode":"uX7H8UBO"}}
     * @param bindingValidationCode 扫描成功后的字符串后面的查询字段
     *
     * @return {"cmd":"R_C_WEARER","code":"0","id":"123",
     * "params":{"areas":[{"id":254699763348692992,"lut":"160525150108","type":0}],"avatarFn":"",
     * "createdTime":"160531060444","gender":2,"height":0,"id":"269288159689732096",
     * "imageServer":"http://cwtcn-dl.6655.la:7190/","imei":"867327020000856","isAdmin":0,
     * "lastUpdatedTime":"160531060444","markPicId":0,"mobile":"15813852565","name":"宝贝",
     * "productId":"KT04","relationship":8,"relationshipName":"其它","relationshipPic":8,
     * "source":1,"weight":0}}
     */
    public JSONObject bindingDevice(String bindingValidationCode) {

        JSONObject params = new JSONObject();
        JSONObject jsonObject = new JSONObject();

        try {
            params.put("bindingValidationCode", bindingValidationCode);

            jsonObject.put("cmd", "C_WEARER");
            jsonObject.put("id", SocketOpenHelper.nextCommandId());
            jsonObject.put("params", params);

            JSONObject paramsJson = SocketOpenHelper.getInstance().getResultDatas(jsonObject);
            Log.d(TAG, paramsJson.toString());
            return paramsJson;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解除绑定 参数： {"cmd":"D_WEARER","id":"123","params":{"id":"269287238872231936"}}
     * @param id 佩戴对象id
     * @return {"cmd":"R_D_WEARER","code":"0","id":"123"}
     */
    public void removeDevice(String id) {

        JSONObject params = new JSONObject();
        JSONObject jsonObject = new JSONObject();

        try {
            params.put("id", id);

            jsonObject.put("cmd", "D_WEARER");
            jsonObject.put("id", SocketOpenHelper.nextCommandId());
            jsonObject.put("params", params);

            SocketOpenHelper.getInstance().getResultDatas(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * 设置SOS号码（亲情号码） 参数： {"cmd":"S_FN","id":"1","params":{"imei":"860860000030000",
     * "fns":[{"no":1,"mobile":"13533334444","familyMobile":"123"},{"no":7,
     * "mobile":"13533335555","familyMobile":"123"}]}}
     * @return {"cmd":"R_S_FN","code":"0","id":"1","params":{"lut":"20160518134020"}}
     * <p/>
     * lut: 亲情号码最后更新时间
     */
    public void setSosNumber(String imei, String mobile) {

        JSONObject params = new JSONObject();
        JSONArray fns = new JSONArray();
        JSONObject jsonObject = new JSONObject();

        try {
            JSONObject fn = new JSONObject();
            fn.put("no", "1");
            fn.put("mobile", mobile);
            fn.put("familyMobile", "familyMobile");

            fns.put(fn);

            params.put("imei", imei);
            params.put("fns", fns);

            jsonObject.put("cmd", "S_FN");
            jsonObject.put("id", SocketOpenHelper.nextCommandId());
            jsonObject.put("params", params);

            SocketOpenHelper.getInstance().getResultDatas(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取腕表最新数据 参数：{"cmd":"Q_TLD","id":"20140825121212","params":{"imei":"860860000030001"}}
     *
     * @return {"cmd":"R_Q_TLD","code":"0","id":"20140825121212",
     * "params":[{"act":{"i":"150610000140","p":64,"s":176,"ss":0,"ut":"150707150105"},
     * "auto_time":"0","auto_type":"0","connect_mode":"0","enable_fn_wl":0,
     * "hit":"141024103233-0","hourTime24":1,"imei":"860860000030001","lct":{"i":"150402071533",
     * "id":"38487486913462272","isStatic":false,"mt":"4","o":113.945810300000000,"p":62,"t":"1",
     * "ts":"150618143820","u":22.547382600000000,"wt":0},"p":58,"ring_mode":"0",
     * "sms_location":"0","st":"1506100001390581291111001
     * ","ts":"1463627533168"}]}
     */
    public JSONObject getDeviceDatas(String imei) {

        JSONObject params = new JSONObject();
        JSONObject jsonObject = new JSONObject();

        try {
            params.put("imei", imei);

            jsonObject.put("cmd", "Q_TLD");
            jsonObject.put("id", SocketOpenHelper.nextCommandId());
            jsonObject.put("params", params);

            JSONObject paramsJson = SocketOpenHelper.getInstance().getResultDatas(jsonObject);
            Log.d(TAG, paramsJson.toString());
            return paramsJson;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
