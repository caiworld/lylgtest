package example.caiworld.caihao.lylgapp.fragment;


import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;

import java.util.ArrayList;
import java.util.List;

import example.caiworld.caihao.lylgapp.MainActivity;
import example.caiworld.caihao.lylgapp.R;
import example.caiworld.caihao.lylgapp.bean.SFS;
import example.caiworld.caihao.lylgapp.utils.CoordinateUtil;
import example.caiworld.caihao.lylgapp.utils.Gps;

/**
 * Created by caihao on 2017/7/13.
 */
public class SFSFragment extends Fragment {

    private View view;
    private MapView mapView;
    private ListView lvSfs;

    private boolean isFirstLocate = true;//用来判断是否是第一次移动位置，默认是true，移动一次之后置为false
    private BaiduMap baiduMap;
    private LocationClient mLocationClient;
    private BDLocation myLocation;
    private double lat, lon;
    private EditText etFrom;
    private EditText etTo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sfs, null);
        mapView = (MapView) view.findViewById(R.id.mapView);
        lvSfs = (ListView) view.findViewById(R.id.lv_sfs);
        moniShuju();
        lvSfs.setAdapter(new SFSAdapter());
        init();
        ((MainActivity) getActivity()).getTvTitle().setText("发");
//        ((MainActivity) getActivity()).getIbtAdd().setVisibility(View.INVISIBLE);
        return view;
    }

    private List<SFS> list;

    private void moniShuju() {
        list = new ArrayList<>();
        list.add(new SFS(R.mipmap.icon, "hao", "洛阳理工学院王城校区", "洛阳理工学院开元校区"));
        list.add(new SFS(R.mipmap.ic_launcher, "caihao", "河南科技大学", "洛阳理工学院开元校区"));
        list.add(new SFS(R.mipmap.pinlove, "zzz", "洛阳师范学院", "洛阳理工学院开元校区"));
    }

    class SFSAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            SFSHolder holder;
            if (convertView == null) {
                convertView = View.inflate(getActivity(), R.layout.item_sfs, null);
                holder = new SFSHolder();
                holder.ivHeader = (ImageView) convertView.findViewById(R.id.iv_header);
                holder.name = (TextView) convertView.findViewById(R.id.tv_name);
                holder.from = (TextView) convertView.findViewById(R.id.tv_from);
                holder.to = (TextView) convertView.findViewById(R.id.tv_to);
                convertView.setTag(holder);
            } else {
                holder = (SFSHolder) convertView.getTag();
            }
            SFS sfs = list.get(position);
            holder.ivHeader.setImageDrawable(getActivity().getResources().getDrawable(sfs.getIvHeader()));
            holder.name.setText(sfs.getName());
            holder.from.setText("from:" + sfs.getFrom());
            holder.to.setText("to:" + sfs.getTo());
            return convertView;
        }
    }

    static class SFSHolder {
        ImageView ivHeader;
        TextView name, from, to;
    }

    @Override
    public void onStart() {
        super.onStart();
        mLocationClient = new LocationClient(getActivity());
        mLocationClient.registerLocationListener(new MyBDLocationListener());
        requestLocation();
        etFrom.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.e("Editable1", s.toString() + ";" + etFrom.getText().toString());
                test(0, etFrom.getText().toString());
            }
        });
        etTo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.e("Editable2", s.toString() + ";" + etTo.getText().toString());
                test(0, etTo.getText().toString());
            }
        });
    }

    private void init() {
        // 设置地图级别（V2.X 3-19 V1.X 3-18）,现在最高级别为21
        // ① 修改了文件的格式 优化了空间的使用（北京 110M 15M）
        // ② 增加了级别 3D效果（18 19）
        baiduMap = mapView.getMap();
        baiduMap.setMyLocationEnabled(true);//允许我显示在地图上，即可以出现一个小圆圈代替我
        // BaiduMap: 管理具体的某一个MapView ： 旋转，移动，缩放，事件。。

        // 描述地图状态将要发生的变化 使用工厂类MapStatusUpdateFactory创建
        MapStatusUpdate mapstatusUpdate = MapStatusUpdateFactory.zoomTo(16);// 默认的级别12
        // 设置缩放级别
        baiduMap.setMapStatus(mapstatusUpdate);

        // LatLng latlng = new LatLng(arg0, arg1);// 坐标 经纬度 参数1 纬度 参数2 经度
//        MapStatusUpdate mapstatusUpdatePoint = MapStatusUpdateFactory
//                .newLatLng(hmPos);
//        // 设置中心点 默认是天安门
//        baiduMap.setMapStatus(mapstatusUpdatePoint);

        mapView.showZoomControls(false);// 默认是true 显示缩放按钮

        mapView.showScaleControl(false);// 默认是true 显示标尺
    }

    private void requestLocation() {
        initLocation();
        mLocationClient.start();
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setScanSpan(5000);//每5秒钟更新一下当前的位置信息
        option.setIsNeedAddress(true);//需要获取当前位置的详细的地址信息

        option.setIsNeedLocationDescribe(true);

        mLocationClient.setLocOption(option);
    }

    private LatLng fromLocate, toLocate;

    /**
     * 根据位置获取坐标信息，并标注在地图上
     *
     * @param arg0 0:from,1:to
     * @param arg1 百度地图地理位置
     */
    private void test(final int arg0, String arg1) {
        GeoCoder search = GeoCoder.newInstance();
        search.geocode(new GeoCodeOption().city("洛阳市").address(arg1));


        search.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
            @Override
            public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
                if (geoCodeResult.getLocation() == null) {
                    return;
                }
                Log.e("geocoderesult", geoCodeResult.getLocation().latitude + ";" + geoCodeResult.getLocation().longitude);
                if (arg0 == 0) {//from
                    fromLocate = new LatLng(geoCodeResult.getLocation().latitude, geoCodeResult.getLocation().longitude);
                } else if (arg0 == 1) {//to
                    toLocate = new LatLng(geoCodeResult.getLocation().latitude, geoCodeResult.getLocation().longitude);
                }
                if (etFrom.getText().toString() != "" && etTo.getText().toString() != "") {
                    initLines();
                }
            }

            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
            }
        });
    }

    private PolylineOptions options;
    private List<LatLng> points;
    private List<Integer> colors;

    /**
     * 地图上画线
     */
    private void initLines() {
        options = new PolylineOptions();
        options.color(Color.RED);//设置折线颜色
        points = new ArrayList<>();
        points.add(new LatLng(34.614124, 112.4236));
        points.add(new LatLng(34.618294, 112.422877));
        points.add(new LatLng(34.618547, 112.423703));
        points.add(new LatLng(34.618822, 112.42691));
        options.points(points);
        colors = new ArrayList<>();
        colors.add(Color.RED);
        options.colorsValues(colors);//设置折点颜色
        options.visible(true);
        baiduMap.addOverlay(options);
        Log.e("color", options.getColor() + "颜色");
    }


    class MyBDLocationListener implements BDLocationListener {
        /**
         * 定位请求回调函数
         *
         * @param bdLocation 回调结果
         */
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            myLocation = bdLocation;

            //将火星坐标转为百度坐标
            Gps bd = CoordinateUtil.bd_encrypt(bdLocation.getLatitude(), bdLocation.getLongitude());
            lat = bd.getWgLat();
            lon = bd.getWgLon();

            final StringBuilder currentPosition = new StringBuilder();
            currentPosition.append("纬度：").append(bdLocation.getLatitude()).append("\n");
            currentPosition.append("经线：").append(bdLocation.getLongitude()).append("\n");
            Log.e("SendAddress：", bdLocation.getLocationDescribe());
            if (isFirstLocate) {//第一次移动到“我”的位置
                MapStatusUpdate mapstatusUpdatePoint = MapStatusUpdateFactory
                        .newLatLng(new LatLng(lat, lon));
                // 设置中心点 默认是天安门
                baiduMap.setMapStatus(mapstatusUpdatePoint);
                if (TextUtils.isEmpty(bdLocation.getLocationDescribe())) {
                    Log.e("sendactivity", "我是假的");
                }
                isFirstLocate = false;
            }
            //让小圆圈出现在“我”的位置上
            MyLocationData.Builder locationBuilder = new MyLocationData.Builder();
            locationBuilder.latitude(lat);
            locationBuilder.longitude(lon);
            MyLocationData locationData = locationBuilder.build();
            baiduMap.setMyLocationData(locationData);


            currentPosition.append("国家：").append(bdLocation.getCountry()).append("\n");
            currentPosition.append("省：").append(bdLocation.getProvince()).append("\n");
            currentPosition.append("市：").append(bdLocation.getCity()).append("\n");
            currentPosition.append("区：").append(bdLocation.getDistrict()).append("\n");
            currentPosition.append("街道：").append(bdLocation.getStreet()).append("\n");

            currentPosition.append("定位方式：");

            if (bdLocation.getLocType() == BDLocation.TypeGpsLocation) {
                currentPosition.append("GPS");
            } else if (bdLocation.getLocType() == BDLocation.TypeNetWorkLocation) {
                currentPosition.append("网络");
            }
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    }


}
