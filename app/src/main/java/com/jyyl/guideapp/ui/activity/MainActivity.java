package com.jyyl.guideapp.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.jyyl.guideapp.MyApplication;
import com.jyyl.guideapp.R;
import com.jyyl.guideapp.ui.base.BaseActivity;
import com.jyyl.guideapp.ui.view.NowMusterDialog;
import com.jyyl.guideapp.utils.T;

import java.util.LinkedList;


/**
 * @Fuction: 主页：地图定位，显示导游和腕表用户的实时位置，集合、定时、提醒等功能
 * @Author: Shang
 * @Date: 2016/4/13  14:55
 */
public class MainActivity extends BaseActivity implements NowMusterDialog.SendMusterMsgListener{

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
    private LinkedList<LocationEntity> locationList = new LinkedList<>();
    // 存放历史定位结果的链表，最大存放当前结果的前5次定位结果

    private Marker mMarkerSelf = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setSwipeBackEnable(false);  //禁用SwipeBackLayout
        mContext = this;

        initBaiduMap();
        initNavigationLeft();
    }

    @Override
    protected void initViews() {
        super.initViews();
        mLocNormalButton = (ImageButton) findViewById(R.id.ib_loc_normal);
        mFab = (FloatingActionButton) findViewById(R.id.fab_remind);
        mNowMuster = (Button) findViewById(R.id.btn_now_muster);
        mTimeMuster = (Button) findViewById(R.id.btn_time_muster);
    }

    @Override
    protected void initListener() {
        super.initListener();
        mLocNormalButton.setOnClickListener(this);
        mFab.setOnClickListener(this);
        mNowMuster.setOnClickListener(this);
        mTimeMuster.setOnClickListener(this);
    }

    @Override
    protected void onViewClick(View v) {
        super.onViewClick(v);
        switch (v.getId()) {
            case R.id.ib_loc_normal: //重新定位
                locService.start();
                break;
            case R.id.fab_remind:
                Log.d("==================", String.valueOf(isMusterShow));
                if (!isMusterShow) {
                    setMusterVisibility(true);
                }else {
                    setMusterVisibility(false);
                }
                break;
            case R.id.btn_now_muster:
                NowMusterDialog nowMusterDialog = new NowMusterDialog();
                nowMusterDialog.show(getFragmentManager(), "NowMuster");
                setMusterVisibility(false);
                break;
            case R.id.btn_time_muster:
                T.showShortToast(mContext, "定时集合");
                setMusterVisibility(false);
                break;
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
            isMusterShow = true;
        } else {
            mNowMuster.setVisibility(View.INVISIBLE);
            mTimeMuster.setVisibility(View.INVISIBLE);
            isMusterShow = false;
        }

    }


    private void initBaiduMap() {
        mMapView = (MapView) findViewById(R.id.mapview);
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        //设置地图类型 MAP_TYPE_NORMAL 普通图； MAP_TYPE_SATELLITE 卫星图；MAP_TYPE_NONE 空白地图
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(15)); //改变地图状态
        mMapView.showZoomControls(false); //隐藏缩放按钮
        mMapView.removeViewAt(1); //隐藏百度logo

        locService = ((MyApplication) getApplication()).locationService;
        LocationClientOption mOption = locService.getDefaultLocationClientOption();
        mOption.setLocationMode(LocationClientOption.LocationMode.Battery_Saving);
        locService.setLocationOption(mOption);
        locService.registerListener(mListener);
        locService.start();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();

        //关闭抽屉菜单
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mDrawerLayout.closeDrawers();
            }
        }, 350);
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
            locData.putInt("iscalculate", 0);
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
                locData.putInt("iscalculate", 1);
            } else {
                locData.putInt("iscalculate", 0);
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
                locService.stop();
                BDLocation location = msg.getData().getParcelable("loc");
                int iscal = msg.getData().getInt("iscalculate");
                if (location != null) {
                    LatLng point = new LatLng(location.getLatitude(), location.getLongitude());
                    // 构建Marker图标
                    BitmapDescriptor bitmap;
                    if (iscal == 0) {
                        bitmap = BitmapDescriptorFactory.fromResource(R.drawable.huaji); // 非推算结果
                    } else {
                        bitmap = BitmapDescriptorFactory.fromResource(R.drawable
                                .icon_openmap_focuse_mark); // 推算结果
                    }

                    // 构建MarkerOption，用于在地图上添加Marker
                    OverlayOptions option = new MarkerOptions().position(point).icon(bitmap);
                    // 在地图上添加Marker，并显示
                    if (mMarkerSelf == null) {
                        mMarkerSelf = (Marker) mBaiduMap.addOverlay(option);
                    } else {
                        mMarkerSelf.setPosition(point);
                    }
                    mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(point));
                }
            } catch (Exception e) {
                // TODO: handle exception
            }
        }

    };

    /**
     * 初始化抽屉菜单
     */
    private void initNavigationLeft() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.id_drawerLayout);
        //侧边导航不能滑动，只能通过点击按钮打开
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED,
                Gravity.LEFT);
        mDrawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerStateChanged(int newState) {
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
            }

            @Override
            public void onDrawerOpened(View drawerView) {
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                mDrawerLayout.setDrawerLockMode(
                        DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.LEFT);
            }
        });
    }

    //打开导航菜单的按钮 点击事件
    public void OpenLeftMenu(View view) {
        mDrawerLayout.openDrawer(Gravity.LEFT);
        //侧边导航可以滑动，可以通过滑动关闭
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED,
                Gravity.LEFT);
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
                finish();
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

    @Override
    public void sendMsg(String msg) {
        T.showShortToast(this, "集合信息已发送");
    }

    /**
     * 封装定位结果和时间的实体类
     * @author baidu
     */
    class LocationEntity {
        BDLocation location;
        long time;
    }
}
