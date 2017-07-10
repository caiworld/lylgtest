package example.caiworld.caihao.lylgapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;

import java.util.List;

import example.caiworld.caihao.lylgapp.utils.CoordinateUtil;
import example.caiworld.caihao.lylgapp.utils.Gps;

public class SendAddressActivity extends AppCompatActivity {

    private EditText abstractAddress;
    private EditText detailAddress;
    private EditText name;
    private EditText number;
    private MapView mapView;
    private ImageView moveToMe;
    private boolean isFirstLocate = true;//用来判断是否是第一次移动位置，默认是true，移动一次之后置为false
    private BaiduMap baiduMap;
    private LocationClient mLocationClient;
    private BDLocation myLocation;
    private double lat, lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_address);
        initView();
        init();

        mLocationClient = new LocationClient(this);
        mLocationClient.registerLocationListener(new MyBDLocationListener());
        requestLocation();

        initListener();
    }

    private void initView() {
        abstractAddress = (EditText) findViewById(R.id.et_abstract_address);
        detailAddress = (EditText) findViewById(R.id.et_detail_address);
        name = (EditText) findViewById(R.id.et_name);
        number = (EditText) findViewById(R.id.et_number);
        mapView = (MapView) findViewById(R.id.mapView);
        moveToMe = (ImageView) findViewById(R.id.iv_moveTOMe);

    }

    private void init() {
        // 设置地图级别（V2.X 3-19 V1.X 3-18）,现在最高级别为21
        // ① 修改了文件的格式 优化了空间的使用（北京 110M 15M）
        // ② 增加了级别 3D效果（18 19）
        baiduMap = mapView.getMap();
        baiduMap.setMyLocationEnabled(true);//允许我显示在地图上，即可以出现一个小圆圈代替我
        // BaiduMap: 管理具体的某一个MapView ： 旋转，移动，缩放，事件。。

        // 描述地图状态将要发生的变化 使用工厂类MapStatusUpdateFactory创建
        MapStatusUpdate mapstatusUpdate = MapStatusUpdateFactory.zoomTo(18);// 默认的级别12
        // 设置缩放级别
        baiduMap.setMapStatus(mapstatusUpdate);

        // LatLng latlng = new LatLng(arg0, arg1);// 坐标 经纬度 参数1 纬度 参数2 经度
//        MapStatusUpdate mapstatusUpdatePoint = MapStatusUpdateFactory
//                .newLatLng(hmPos);
//        // 设置中心点 默认是天安门
//        baiduMap.setMapStatus(mapstatusUpdatePoint);

//        mapView.showZoomControls(false);// 默认是true 显示缩放按钮

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

    private void initListener() {
        //baidumap拖拽
        baiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus) {
                //开始拖拽
            }

            @Override
            public void onMapStatusChange(MapStatus mapStatus) {
            }

            @Override
            public void onMapStatusChangeFinish(MapStatus mapStatus) {
                //拖拽完成
                //target地图操作的中心点。
                LatLng target = mapStatus.target;
                //根据坐标获取位置信息
                test(target);
            }
        });
        //移动到 我 的位置上
        moveToMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //将火星坐标转为百度坐标
                Gps bd = CoordinateUtil.bd_encrypt(myLocation.getLatitude(), myLocation.getLongitude());
                lat = bd.getWgLat();
                lon = bd.getWgLon();
                MapStatusUpdate mapstatusUpdatePoint = MapStatusUpdateFactory
                        .newLatLng(new LatLng(lat, lon));
                // 设置中心点 默认是天安门
                baiduMap.setMapStatus(mapstatusUpdatePoint);
                isFirstLocate = false;

                //让小圆圈出现在“我”的位置上
                MyLocationData.Builder locationBuilder = new MyLocationData.Builder();
                locationBuilder.latitude(lat);
                locationBuilder.longitude(lon);
                MyLocationData locationData = locationBuilder.build();
                baiduMap.setMyLocationData(locationData);

                abstractAddress.setText(myLocation.getLocationDescribe());

                // 描述地图状态将要发生的变化 使用工厂类MapStatusUpdateFactory创建
                MapStatusUpdate mapstatusUpdate = MapStatusUpdateFactory.zoomTo(18);// 默认的级别12
                // 设置缩放级别
                baiduMap.setMapStatus(mapstatusUpdate);
            }
        });
    }

    /**
     * 根据坐标获取位置信息
     *
     * @param arg0 百度地图坐标
     */
    private void test(LatLng arg0) {
        GeoCoder search = GeoCoder.newInstance();

        search.reverseGeoCode(new ReverseGeoCodeOption().location(arg0));


        search.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
            @Override
            public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

            }

            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
                //TODO 同ReceiveAddressActivity需要弄一个周边的位置显示出来
                //反Geo搜索
                String description;
                address = result.getAddress();
                Log.e("address我",address);
                List<PoiInfo> poiList = result.getPoiList();
                if (poiList != null && poiList.size() > 0) {
                    if (poiList.size() == 1) {
                        description = poiList.get(0).name;
                        abstractAddress.setText(description);
                    } else {
                        description = poiList.get(1).name;
                        abstractAddress.setText(description);
                    }
                } else {
                    description = result.getSematicDescription();
                    abstractAddress.setText(description);
                }
            }
        });
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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        abstractAddress.setText(myLocation.getLocationDescribe());
                    }
                });
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

    @Override
    public void finish() {
        haveFinished();
        super.finish();
    }

    private String address;//省市区街

    private void haveFinished() {
        Intent intent = new Intent();
        intent.putExtra("saddress", address);
        intent.putExtra("sabsAddress", abstractAddress.getText().toString().trim());
        intent.putExtra("sdetailAddress", detailAddress.getText().toString().trim());
        intent.putExtra("lat",lat);
        intent.putExtra("lon",lon);
        intent.putExtra("sname", name.getText().toString().trim());
        intent.putExtra("snumber", number.getText().toString().trim());
        this.setResult(10, intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.stop();
        mapView.onDestroy();
        baiduMap.setMyLocationEnabled(false);
    }
}
