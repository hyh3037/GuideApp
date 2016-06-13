package com.jyyl.jinyou.ui.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClientOption;
import com.baidu.location.service.LocationService;
import com.baidu.location.service.Utils;
import com.baidu.location.service.WriteLog;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.bumptech.glide.Glide;
import com.jyyl.jinyou.MyApplication;
import com.jyyl.jinyou.R;
import com.jyyl.jinyou.abardeen.AbardeenMethod;
import com.jyyl.jinyou.constans.Sp;
import com.jyyl.jinyou.entity.MemberInfo;
import com.jyyl.jinyou.entity.MemberInfoResult;
import com.jyyl.jinyou.entity.TeamInfo;
import com.jyyl.jinyou.http.ApiException;
import com.jyyl.jinyou.http.BaseSubscriber;
import com.jyyl.jinyou.http.HttpMethods;
import com.jyyl.jinyou.http.HttpResult;
import com.jyyl.jinyou.http.ResultStatus;
import com.jyyl.jinyou.receive.AlarmReceiver;
import com.jyyl.jinyou.ui.base.BaseActivity;
import com.jyyl.jinyou.ui.dialog.MusterNowDialog;
import com.jyyl.jinyou.ui.dialog.MusterSingleDialog;
import com.jyyl.jinyou.ui.dialog.MusterTimeDialog;
import com.jyyl.jinyou.ui.fragment.NavLeftFragment;
import com.jyyl.jinyou.utils.LogUtils;
import com.jyyl.jinyou.utils.SPUtils;
import com.jyyl.jinyou.utils.T;
import com.jyyl.jinyou.voice.VoiceActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * @Fuction: 主页：地图定位，显示导游和腕表用户的实时位置，集合、定时、提醒等功能
 * @Author: Shang
 * @Date: 2016/4/13  14:55
 */
public class MainActivity extends BaseActivity
        implements MusterNowDialog.MusterNowListener, MusterTimeDialog.MusterTimeListener,
                   MusterSingleDialog.MusterSingleListener, BaiduMap.OnMarkerClickListener,
                   NavLeftFragment.NavCallback {

    private static String TAG = "MainActivity";

    private Context mContext;
    private DrawerLayout mDrawerLayout;
    private MapView mMapView;
    private ImageButton mLocNormalButton;
    private FloatingActionButton mFab;
    private boolean isMusterShow = false;
    private Button mNowMuster;
    private Button mTimeMuster;

    private LocationService locService;
    private BaiduMap mBaiduMap;
    private BDLocation location; //当前位置
    private LinkedList<LocationEntity> locationList = new LinkedList<>();
    // 存放历史定位结果的链表，最大存放当前结果的前5次定位结果

    private Marker mMarkerSelf = null;  //导游位置marker
    private BitmapDescriptor bitmap; //导游位置marker图标
    private BitmapDescriptor markerBitmap;
    private ArrayList<MemberInfo> mMemberList = new ArrayList<>();//游客信息集合
    private ArrayList<String> imeiList = new ArrayList<>();
    private LinearLayout infowindow;
    private InfoWindow mInfoWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_main);
        setSwipeBackEnable(false);  //禁用SwipeBackLayout

        initBaiduMap();
        initTeamInfo();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void initViews() {
        super.initViews();
        mLocNormalButton = (ImageButton) findViewById(R.id.ib_loc_normal);
        mFab = (FloatingActionButton) findViewById(R.id.fab_remind);
        mNowMuster = (Button) findViewById(R.id.btn_now_muster);
        mTimeMuster = (Button) findViewById(R.id.btn_time_muster);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.id_drawerLayout);
    }

    @Override
    protected void initListener() {
        super.initListener();
        mLocNormalButton.setOnClickListener(this);
        mFab.setOnClickListener(this);
        mNowMuster.setOnClickListener(this);
        mTimeMuster.setOnClickListener(this);
    }

    /**
     * @param v
     *         被点击的view
     */
    @Override
    protected void onViewClick(View v) {
        super.onViewClick(v);
        switch (v.getId()) {
            case R.id.ib_loc_normal: //手动更新定位
                resetMarker();
                locService.request();//重新请求定位
                break;
            case R.id.fab_remind:
                imeiList.clear();
                for (MemberInfo memberInfo : mMemberList) {
                    imeiList.add(memberInfo.getMemberInfoResult().getDeviceIMEI());
                }
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("imeiList", imeiList);
                openActivity(mContext, VoiceActivity.class, bundle);
                //                if (!isMusterShow) {
                //                    setMusterVisibility(true);
                //                } else {
                //                    setMusterVisibility(false);
                //                }
                break;
            case R.id.btn_now_muster:
                MusterNowDialog musterNowDialog = new MusterNowDialog();
                musterNowDialog.show(getFragmentManager(), "NowMuster");
                setMusterVisibility(false);
                break;
            case R.id.btn_time_muster:
                MusterTimeDialog musterTimeDialog = new MusterTimeDialog();
                musterTimeDialog.show(getFragmentManager(), "TimeMuster");
                setMusterVisibility(false);
                break;
        }
    }

    /**
     * 初始化旅游团队信息
     */
    private void initTeamInfo() {
        HttpMethods.getInstance().getTeamInfo()
                .subscribe(new BaseSubscriber<List<TeamInfo>>() {
                    @Override
                    public void onNext(List<TeamInfo> teamInfos) {

                        if (teamInfos.size() > 0) {
                            TeamInfo teamInfo = teamInfos.get(0);
                            String mTeamId = teamInfo.getTeamId();
                            initMembers(mTeamId);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof ApiException) {
                            return;
                        }
                        super.onError(e);
                    }
                });
    }

    private void initMembers(String mTeamId) {
        HttpMethods.getInstance().getMemberInfo(mTeamId)
                .subscribe(new BaseSubscriber<List<MemberInfoResult>>() {
                    @Override
                    public void onNext(List<MemberInfoResult> memberInfoResults) {
                        mMemberList.clear();
                        for (MemberInfoResult memberInfoResultInfo : memberInfoResults) {
                            mMemberList.add(new MemberInfo(memberInfoResultInfo));
                            LogUtils.d(memberInfoResultInfo.toString());
                        }
                    }
                });
    }


    /**
     * 重置marker
     */
    private void resetMarker() {
        mBaiduMap.clear();
        mMarkerSelf = null;
        if (location != null) {
            addMarkers();
        }
    }

    /**
     * 是否显示集合按钮
     * @param isVisibility
     *         true：显示
     */
    private void setMusterVisibility(boolean isVisibility) {

        if (isVisibility) {
            mNowMuster.setVisibility(View.VISIBLE);
            mTimeMuster.setVisibility(View.VISIBLE);
            mTimeMuster.setText((String) SPUtils.get(this, Sp.SP_KEY_MUSTER_TIME, "定时集合"));
            isMusterShow = true;
        } else {
            mNowMuster.setVisibility(View.INVISIBLE);
            mTimeMuster.setVisibility(View.INVISIBLE);
            isMusterShow = false;
        }

    }

    /**
     * ==================================地图START================================================
     */
    private void initBaiduMap() {
        mMapView = (MapView) findViewById(R.id.mapview);
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        //设置地图类型 MAP_TYPE_NORMAL 普通图； MAP_TYPE_SATELLITE 卫星图；MAP_TYPE_NONE 空白地图
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(13)); //改变地图状态
        mBaiduMap.getUiSettings().setRotateGesturesEnabled(false);//禁止旋转手势
        mBaiduMap.getUiSettings().setOverlookingGesturesEnabled(false); //禁止俯视手势
        mBaiduMap.setOnMarkerClickListener(this);
        mMapView.showZoomControls(false); //隐藏缩放按钮
        mMapView.removeViewAt(1); //隐藏百度logo

        markerBitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_openmap_mark);
        infowindow = (LinearLayout) getLayoutInflater().inflate(R.layout.item_info_window, null);

        locService = ((MyApplication) getApplication()).locationService;
        LocationClientOption mOption = locService.getDefaultLocationClientOption();
        mOption.setLocationMode(LocationClientOption.LocationMode.Battery_Saving);//低功耗
        locService.setLocationOption(mOption);

        locService.registerListener(mListener);
        locService.start();
    }

    /*****
     * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
     */
    private BDLocationListener mListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {

            if (location != null && (location.getLocType() == 161 || location.getLocType() == 66)) {
                Message locMsg = locHander.obtainMessage();
                Bundle locData;
                locData = Algorithm(location);
                if (locData != null) {
                    locData.putParcelable("loc", location);
                    locMsg.setData(locData);
                    locHander.sendMessage(locMsg);
                }
            }
        }

    };

    /***
     * 平滑策略代码实现方法，主要通过对新定位和历史定位结果进行速度评分， 来判断新定位结果的抖动幅度，如果超过经验值，则判定为过大抖动，进行平滑处理,若速度过快，
     * 则推测有可能是由于运动速度本身造成的，则不进行低速平滑处理 ╭(●｀∀´●)╯
     * @param location
     *
     * @return Bundle
     */
    private Bundle Algorithm(BDLocation location) {
        Bundle locData = new Bundle();
        double curSpeed = 0;
        if (locationList.isEmpty() || locationList.size() < 2) {
            LocationEntity temp = new LocationEntity();
            temp.location = location;
            temp.time = System.currentTimeMillis();
            locationList.add(temp);
        } else {
            if (locationList.size() > 5)
                locationList.removeFirst();
            double score = 0;
            for (int i = 0; i < locationList.size(); ++i) {
                LatLng lastPoint = new LatLng(locationList.get(i).location.getLatitude(),
                        locationList.get(i).location.getLongitude());
                LatLng curPoint = new LatLng(location.getLatitude(), location.getLongitude());
                double distance = DistanceUtil.getDistance(lastPoint, curPoint);
                curSpeed = distance / (System.currentTimeMillis() - locationList.get(i).time) /
                        1000;
                score += curSpeed * Utils.EARTH_WEIGHT[i];
            }
            if (score > 0.00000999 && score < 0.00005) { // 经验值,开发者可根据业务自行调整，也可以不使用这种算法
                location.setLongitude(
                        (locationList.get(locationList.size() - 1).location.getLongitude() +
                                location.getLongitude())
                                / 2);
                location.setLatitude(
                        (locationList.get(locationList.size() - 1).location.getLatitude() +
                                location.getLatitude())
                                / 2);
            }
            LocationEntity newLocation = new LocationEntity();
            newLocation.location = location;
            newLocation.time = System.currentTimeMillis();
            locationList.add(newLocation);

        }
        return locData;
    }

    /***
     * 接收定位结果消息，并显示在地图上
     */
    private Handler locHander = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                location = msg.getData().getParcelable("loc");
                if (location != null) {
                    addMarkers();

                    //上传导游位置
                    HttpMethods.getInstance().uploadLocation(String.valueOf(location.getLongitude
                                    ()),
                            String.valueOf(location.getLatitude()), location.getTime())
                            .subscribe(new BaseSubscriber<HttpResult>() {
                                @Override
                                public void onStart() {
                                    LogUtils.d(TAG, "导游位置上传......");
                                }

                                @Override
                                public void onNext(HttpResult result) {
                                    if (ResultStatus.HTTP_SUCCESS.equals(result.getStatus())) {
                                        LogUtils.d(TAG, "导游位置上传成功");
                                    }
                                }
                            });
                }
            } catch (Exception e) {
                LogUtils.d(TAG, "定位失败");
                e.printStackTrace();
            }
        }

    };


    private void addMarkers() {
        // 构建Marker图标
        bitmap = BitmapDescriptorFactory.fromResource(R.drawable
                .main_icon_compass);
        LatLng point = new LatLng(location.getLatitude(), location.getLongitude());
        // 构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions().position(point).icon(bitmap);
        // 在地图上添加Marker，并显示
        if (mMarkerSelf == null) {
            mMarkerSelf = (Marker) mBaiduMap.addOverlay(option);
            mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLngZoom(point, 13));
            refreshMembersLct();
        } else {
            mMarkerSelf.setPosition(point);
        }
    }

    /**
     * 获取启用的腕表当前位置
     */
    private void refreshMembersLct() {
        Observable.create(new Observable.OnSubscribe<ArrayList<MemberInfo>>() {
            @Override
            public void call(Subscriber<? super ArrayList<MemberInfo>> subscriber) {
                for (MemberInfo memberInfo : mMemberList) {
                    String deviceImei = memberInfo.getMemberInfoResult().getDeviceIMEI();
                    LogUtils.d("查询位置 imei == >>" + deviceImei);
                    JSONArray jsonArray = AbardeenMethod.getInstance()
                            .getDeviceDatas(deviceImei);
                    try {
                        if (jsonArray.length() > 0) {
                            JSONObject jsonObject = (JSONObject) jsonArray.get(0);
                            JSONObject lct = (JSONObject) jsonObject.get("lct");
                            double lat = lct.getDouble("u");
                            double lng = lct.getDouble("o");
                            memberInfo.setLatLng(new LatLng(lat, lng));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                subscriber.onNext(mMemberList);
                subscriber.onCompleted();
            }
        })
                .subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                .subscribe(new BaseSubscriber<ArrayList<MemberInfo>>() {
                    @Override
                    public void onNext(ArrayList<MemberInfo> memberInfos) {
                        addMarkerMember(memberInfos);
                    }
                });
    }

    /**
     * 添加游客marker
     */
    private void addMarkerMember(ArrayList<MemberInfo> memberList) {
        LogUtils.d(TAG, "添加游客的marker");
        for (int i = 0; i < memberList.size(); i++) {
            MemberInfo memberInfo = memberList.get(i);
            LatLng point = memberInfo.getLatLng();
            if (point != null) {
                // 构建MarkerOption，用于在地图上添加Marker
                OverlayOptions option = new MarkerOptions().position(point).icon(markerBitmap);
                // 在地图上添加Marker，并显示
                Marker marker = (Marker) mBaiduMap.addOverlay(option);
                Bundle bundle = new Bundle();
                bundle.putSerializable("marker", memberInfo);
                marker.setExtraInfo(bundle);
            }
        }

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        //获得marker中的数据
        MemberInfo memberInfo = (MemberInfo) marker.getExtraInfo().get("marker");
        createInfoWindow(infowindow, memberInfo);
        mInfoWindow = new InfoWindow(infowindow, marker.getPosition(), -70);
        mBaiduMap.showInfoWindow(mInfoWindow);
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mBaiduMap.hideInfoWindow();
        return super.onTouchEvent(event);
    }

    /**
     * 创建 弹出窗口
     */
    private void createInfoWindow(LinearLayout infowindow, final MemberInfo memberInfo) {

        InfoWindowHolder holder = null;
        if (infowindow.getTag() == null) {
            holder = new InfoWindowHolder();

            holder.mPhotoIv = (ImageView) infowindow.findViewById(R.id.iv_info_photo);
            holder.mNameTv = (TextView) infowindow.findViewById(R.id.tv_info_name);
            holder.mNoticeBtn = (Button) infowindow.findViewById(R.id.btn_info_notice);
            infowindow.setTag(holder);
        }

        holder = (InfoWindowHolder) infowindow.getTag();

        Glide.with(mContext).load(memberInfo.getMemberInfoResult().getHeadAddress())
                .error(R.drawable.default_photo)
                .into(holder.mPhotoIv);

        holder.mNameTv.setText(memberInfo.getMemberInfoResult().getTouristName());

        holder.mNoticeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //                MusterSingleDialog musterSingleDialog = new MusterSingleDialog();
                //                musterSingleDialog.show(getFragmentManager(), "MusterSingle");
                imeiList.clear();
                imeiList.add(memberInfo.getMemberInfoResult().getDeviceIMEI());
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("imeiList", imeiList);
                openActivity(mContext, VoiceActivity.class, bundle);
                //隐藏InfoWindow
                mBaiduMap.hideInfoWindow();
            }
        });
    }

    /**
     * ==================================地图END================================================
     */

    @Override
    public void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
        JPushInterface.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
        JPushInterface.onPause(this);
        //        new Handler().postDelayed(new Runnable() {
        //            @Override
        //            public void run() {
        //                mDrawerLayout.closeDrawers();
        //            }
        //        }, 500);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        WriteLog.getInstance().close();
        locService.unregisterListener(mListener);
        locService.stop();
        mMapView.onDestroy();
    }

    //打开导航菜单的按钮 点击事件
    public void OpenLeftMenu(View view) {
        mDrawerLayout.openDrawer(Gravity.LEFT);
    }

    //关闭抽屉菜单
    @Override
    public void closeLeftMenu() {
        mDrawerLayout.closeDrawers();
    }


    /**
     * ========================= 退出程序 ===========================
     */
    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            // 判断是否在两秒之内连续点击返回键，是则退出，否则不退出
            if (System.currentTimeMillis() - exitTime > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序",
                        Toast.LENGTH_SHORT).show();
                // 将系统当前的时间赋值给exitTime
                exitTime = System.currentTimeMillis();
            } else {
                MyApplication.getInstance().exitApp();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.push_app_exit);
    }


    /**
     * =====================================Dialog响应================================
     */
    @Override
    public void sendSingleMsg(String msg) {
        T.showShortToast(this, "集合信息已发送");
    }

    @Override
    public void sendNowMsg(String msg) {
        T.showShortToast(this, "集合信息已发送");
    }

    @Override
    public void saveMessage(String msg, int hour, int minute) {
        T.showShortToast(this, "定时集合设置成功");

        String musterTime = (hour < 10 ? "0" + hour : hour) + ":"
                + (minute < 10 ? "0" + minute : minute) + "集合";
        SPUtils.put(this, Sp.SP_KEY_MUSTER_TIME, musterTime);
        /* Retrieve a PendingIntent that will perform a broadcast */
        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);

        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }


    /**
     * 封装定位结果和时间的实体类
     * @author baidu
     */
    class LocationEntity {
        BDLocation location;
        long time;
    }

    /**
     * 复用弹出面板Marker的控件
     */
    public class InfoWindowHolder {
        public ImageView mPhotoIv;
        public TextView mNameTv;
        public Button mNoticeBtn;
    }
}
