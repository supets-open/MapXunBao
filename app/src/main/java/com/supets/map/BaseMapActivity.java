package com.supets.map;

import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.supets.map.marker.MYCustomOverlay;
import com.supets.map.marker.MyOrientationListener;

public class BaseMapActivity extends BaseLocationActivity {

    private MapView mMapView = null;
    Button startLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basemap);
        //获取地图控件引用
        mMapView = findViewById(R.id.bmapView);

        startLocation = findViewById(R.id.addfence);
        startLocation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (startLocation.getText().toString().equals("开始定位")) {
                    locationService.start();// 定位SDK
                    myOrientationListener.start();
                    // start之后会默认发起一次定位请求，开发者无须判断isstart并主动调用request
                    startLocation.setText("停止定位");
                } else {
                    locationService.stop();
                    startLocation.setText("开始定位");
                }
            }
        });


        initMapEvent();
        initOritationListener();

        mMapView.getMap().setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus) {

            }

            @Override
            public void onMapStatusChange(MapStatus mapStatus) {

            }

            //需求是这样的，手动滑动地图停止后，直接获取到当前地图中心坐标的经纬度
            public void onMapStatusChangeFinish(MapStatus status) {
                LatLng _latLng = status.target;
                System.out.println(_latLng.latitude + "," + _latLng.longitude);

                markerManager.removeFromMap();

                OverlayOptions options = markerManager.buildMarker(_latLng, R.mipmap.icon_markb);
                markerManager.addData(options);
            }
        });


        mMapView.getMap().setOnMapTouchListener(new BaiduMap.OnMapTouchListener() {
            @Override
            public void onTouch(MotionEvent motionEvent) {

                int action = motionEvent.getAction() & MotionEvent.ACTION_MASK;

                switch (action) {
                    case MotionEvent.ACTION_UP: {
                        /** 获得屏幕点击的位置 */
                        int x = (int) motionEvent.getX();
                        int y = (int) motionEvent.getY();
                        // 像素坐标转换为地址坐标
                        LatLng _latLng = mMapView.getMap().getProjection().fromScreenLocation(new Point(x, y));
                        Log.v("dd", x + ":" + y + ":" + _latLng.latitude + _latLng.longitude);
                        OverlayOptions options = markerManager.buildMarker(_latLng, R.mipmap.icon_markc);
                        markerManager.addData(options);
                    }
                    break;
                }
            }
        });

    }

    private void initMapEvent() {
        mMapView.showScaleControl(false);//设置是否显示比例尺控件
        mMapView.showZoomControls(false);//设置是否显示缩放控件

        mMapView.getMap().setTrafficEnabled(false);//设置是否打开交通图层
        mMapView.getMap().setIndoorEnable(false);//默认室内图不显示
        mMapView.getMap().setBaiduHeatMapEnabled(false);//设置是否打开百度热力图图层
        mMapView.getMap().setBuildingsEnabled(false);//设置是否允许楼块效果
        mMapView.getMap().setMapType(BaiduMap.MAP_TYPE_NORMAL);//设置地图类型
        mMapView.getMap().setMaxAndMinZoomLevel(21, 3);//设置地图最大以及最小缩放级别
        mMapView.getMap().showMapIndoorPoi(false);//设置是否显示室内图标注, 默认显示
        mMapView.getMap().showMapPoi(true);//控制是否显示底图默认标注, 默认显示

        mMapView.getMap().getUiSettings().setCompassEnabled(false);//设置指南针是否显示
        mMapView.getMap().getUiSettings().setOverlookingGesturesEnabled(false);//设置是否允许俯视手势
        mMapView.getMap().getUiSettings().setRotateGesturesEnabled(true);//设置是否允许旋转手势
        mMapView.getMap().getUiSettings().setZoomGesturesEnabled(true);//设置是否允许缩放手势
        mMapView.getMap().getUiSettings().setScrollGesturesEnabled(true);//设置是否允许拖拽手势

        markerManager = new MYCustomOverlay(mMapView);

    }


    MYCustomOverlay markerManager;


    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
        myOrientationListener.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
        myOrientationListener.stop();
    }

    boolean isFirstLoc = true;

    BDLocation locData;

    @Override
    public void locSuccess(BDLocation location) {
        //停止服务
        locationService.stop();
        startLocation.setText("开始定位");
        locData = location;
        addCustomOverLay();
        updateLoc(locData);
    }


    private void updateLoc(BDLocation location) {
        // 开启定位图层
        mMapView.getMap().setMyLocationEnabled(true);
        // 构造定位数据
        MyLocationData locData = new MyLocationData.Builder()
                .accuracy(location.getRadius())
                // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(location.getDirection() < 0 ? mXDirection : location.getDirection()).latitude(location.getLatitude())
                .longitude(location.getLongitude()).build();
        // 设置定位数据
        mMapView.getMap().setMyLocationData(locData);

        // 设置定位图层的配置（定位模式，是否允许方向信息，用户自定义定位图标）
        BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory
                .fromResource(R.mipmap.icon_st);
        MyLocationConfiguration config = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, true, mCurrentMarker);
        mMapView.getMap().setMyLocationConfiguration(config);

        LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
        //第一次定位需要更新下地图显示状态
        if (isFirstLoc) {
            isFirstLoc = false;
            centerXY(ll);
        }
    }

    private void centerXY(LatLng ll) {
        MapStatus.Builder builder = new MapStatus.Builder()
                .target(ll)//地图缩放中心点
                .zoom(18f);//缩放倍数 百度地图支持缩放21级 部分特殊图层为20级 //改变地图状态
        mMapView.getMap().setMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
    }


    /**
     * 初始化方向传感器
     */

    MyOrientationListener myOrientationListener;
    private int mXDirection = 0;

    private void initOritationListener() {
        myOrientationListener = new MyOrientationListener(
                this);
        myOrientationListener
                .setOnOrientationListener(new MyOrientationListener.OnOrientationListener() {
                    @Override
                    public void onOrientationChanged(float x) {
                        mXDirection = (int) x;
                        Log.v("xdirection", mXDirection + "==");
                        if (locData != null)
                            updateLoc(locData);
                    }
                });
    }


    @Override
    public void logMsg(String s) {
        Log.v("tag", s);
    }


    public void addCustomOverLay() {
        markerManager.addToMap();
    }


}

