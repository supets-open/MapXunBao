package com.supets.map.marker;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.supets.map.R;

import java.util.ArrayList;
import java.util.List;

/**
 * MapXunBao
 *
 * @user lihongjiang
 * @description
 * @date 2017/8/25
 * @updatetime 2017/8/25
 */

public class MYCustomOverlay extends OverlayManager implements BaiduMap.OnMarkerClickListener {

    public MYCustomOverlay(MapView mapView) {
        super(mapView);
        mapView.getMap().setOnMarkerClickListener(this);
        mapView.getMap().setOnMapLoadedCallback(new BaiduMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                zoomToSpan();
            }
        });
    }


    @Override
    public List<OverlayOptions> getOverlayOptions() {

        View drawable = Util.getDrawableOnMap(getContext(), "张力红", 5);
        //定义Maker坐标点
        LatLng point = new LatLng(39.973175, 116.400244);
        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromView(drawable);

        Bundle info = new Bundle();
        info.putString("key", "哇哈哈");
        MarkerOptions option = new MarkerOptions()
                .extraInfo(info)
                .position(point)
                .icon(bitmap)
                .zIndex(9)  //设置marker所在层级
                .draggable(true);  //设置手势拖拽

        //2
        //定义Maker坐标点
        LatLng point2 = new LatLng(39.963175, 116.400244);
        //构建Marker图标
        BitmapDescriptor bitmap2 = BitmapDescriptorFactory.fromResource(R.mipmap.icon_marka);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option2 = new MarkerOptions()
                .position(point2)
                .icon(bitmap2)
                .zIndex(9)  //设置marker所在层级
                .draggable(true);  //设置手势拖拽

        ArrayList<OverlayOptions> list = new ArrayList<>();
        list.add(option);
        list.add(option2);
        return list;
    }


    public OverlayOptions  buildMarker(LatLng  point,int id){
        return  new MarkerOptions()
                .position(point)
                .icon(BitmapDescriptorFactory.fromResource(id))
                .zIndex(9)  //设置marker所在层级
                .draggable(true);  //设置手势拖拽
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        String name = "test";
        if (marker.getExtraInfo() != null) {
            name = marker.getExtraInfo().getString("key");
        }
        Toast.makeText(getContext(), name, Toast.LENGTH_SHORT).show();
        return false;
    }


}
