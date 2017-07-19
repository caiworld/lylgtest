package example.caiworld.caihao.lylgapp.pager;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MapViewLayoutParams;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.hyphenate.chat.EMMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import example.caiworld.caihao.lylgapp.ChatActivity;
import example.caiworld.caihao.lylgapp.HelpActivity;
import example.caiworld.caihao.lylgapp.MainActivity;
import example.caiworld.caihao.lylgapp.R;
import example.caiworld.caihao.lylgapp.base.BasePager;
import example.caiworld.caihao.lylgapp.utils.CoordinateUtil;
import example.caiworld.caihao.lylgapp.utils.Gps;

/**
 * Created by caihao on 2017/5/18.
 */
public class HomePager extends BasePager {

    private View view;
    private Button helpMe;
    private MapView mMapView;
    private LocationClient mLocationClient;
    private BaiduMap baiduMap;
    private boolean isFirstLocate = true;//用来判断是否是第一次移动位置，默认是true，移动一次之后置为false
    private ImageView moveToMe;
    private BDLocation myLocation;
    private double lat;
    private double lon;
    private TextView title;
    private View pop;
    private ImageView ibtAdd2;
    private ImageView ibtAdd1;

    public HomePager(Activity activity) {
        super(activity);
    }

    @Override
    public View initView() {
        ibtAdd1 = ((MainActivity) mActivity).getIbtAdd1();
        ibtAdd2 = ((MainActivity) mActivity).getIbtAdd2();
        view = View.inflate(mActivity, R.layout.pager_home, null);
        helpMe = (Button) view.findViewById(R.id.bt_helpMe);//求帮助
        helpMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, HelpActivity.class);
                mActivity.startActivity(intent);
            }
        });
        moveToMe = (ImageView) view.findViewById(R.id.iv_moveTOMe);
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

                //让指针到我的位置上
                test(new LatLng(lat, lon));
                ViewGroup.LayoutParams params = new MapViewLayoutParams.Builder()
                        .layoutMode(MapViewLayoutParams.ELayoutMode.mapMode)// 按照经纬度设置位置
                        .position(new LatLng(lat, lon))// 不能传null
                        .width(MapViewLayoutParams.WRAP_CONTENT)
                        .height(MapViewLayoutParams.WRAP_CONTENT)
                        .yOffset(-5)// 距离position的像素 向下是正值 向上是负值
                        .build();
                mMapView.updateViewLayout(pop, params);
                pop.setVisibility(View.VISIBLE);

                // 描述地图状态将要发生的变化 使用工厂类MapStatusUpdateFactory创建
                MapStatusUpdate mapstatusUpdate = MapStatusUpdateFactory.zoomTo(19);// 默认的级别12
                // 设置缩放级别
                baiduMap.setMapStatus(mapstatusUpdate);
            }
        });
        mMapView = (MapView) view.findViewById(R.id.mapView);
        init();
        mLocationClient = new LocationClient(mActivity);
        mLocationClient.registerLocationListener(new MyLocationListener());
        requestLocation();
        return view;
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

    /**
     * 初始化首页的数据,切换viewpager时调用
     */
    @Override
    public void initData() {
        ((MainActivity)mActivity).getTvTitle().setText("首页");

        ibtAdd1.setVisibility(View.GONE);
        ibtAdd2.setVisibility(View.GONE);
//        drawZBLD();
    }

    private void drawZBLD() {
//        BitmapDescriptor bitmapDes = BitmapDescriptorFactory
//                .fromResource(R.mipmap.pinlove);

        if (lat <= 0 || lon <= 0) {//还未获取当前位置
            Log.e("homepager", "还未获取当前位置");
            return;
        }

        Log.e("homepager", "获取到当前位置" + lat + ";" + lon);

//        MarkerOptions markerOptions = new MarkerOptions();
//        markerOptions.position(new LatLng(lat, lon))// 设置位置
//                .icon(me)// 设置图标
//                .draggable(true)// 设置是否可以拖拽 默认是否
//                .title("myself");// 设置标题
//        baiduMap.addOverlay(markerOptions);

        MarkerOptions markerOptions;
        BitmapDescriptor bitmapDes = BitmapDescriptorFactory
                .fromResource(R.mipmap.z1);
        markerOptions = new MarkerOptions().title("向北")
                .position(new LatLng(lat + 0.001, lon))
                .icon(bitmapDes)
                .title("hao");
        baiduMap.addOverlay(markerOptions);
        bitmapDes = BitmapDescriptorFactory
                .fromResource(R.mipmap.z2);
        markerOptions = new MarkerOptions().title("向北")
                .position(new LatLng(lat - 0.001, lon))
                .icon(bitmapDes).title("caihao");
        baiduMap.addOverlay(markerOptions);

//        ArrayList<BitmapDescriptor> bitmaps = new ArrayList<BitmapDescriptor>();
//        bitmaps.add(bitmapDes);
//        bitmaps.add(BitmapDescriptorFactory.fromResource(R.drawable.icon_geo));
//        markerOptions = new MarkerOptions().title("向东")
//                .position(new LatLng(latitude, longitude + 0.001))
//                .icons(bitmaps)// 显示多个图片来回切换 帧动画
//                .period(10);// 设置多少帧刷新一次图片资源，Marker动画的间隔时间，值越小动画越快
//        baiduMap.addOverlay(markerOptions);
        bitmapDes = BitmapDescriptorFactory
                .fromResource(R.mipmap.z3);
        markerOptions = new MarkerOptions().title("向东")
                .position(new LatLng(lat, lon + 0.001))
                .icon(bitmapDes).title("zzz");
        baiduMap.addOverlay(markerOptions);
        bitmapDes = BitmapDescriptorFactory
                .fromResource(R.mipmap.z4);
        markerOptions = new MarkerOptions().title("向西南")
                .position(new LatLng(lat - 0.001, lon - 0.001))
                .icon(bitmapDes).title("bao");
        baiduMap.addOverlay(markerOptions);
        baiduMap.setOnMarkerClickListener(new MyClickListener());
        baiduMap.setOnMapStatusChangeListener(new MyStatusListener());
//        baiduMap.setOnMarkerDragListener(new MyDragListener());
    }

    /**
     * 初始化小弹窗，用来显示当前位置
     *
     * @param hmPos
     */
    private void initPop(LatLng hmPos) {
        // 加载pop 添加到mapview 设置为隐藏
        Log.e("initpop", hmPos.latitude + ";" + hmPos.longitude);
        pop = View.inflate(mActivity, R.layout.pop, null);
        final ViewGroup.LayoutParams params = new MapViewLayoutParams.Builder()
                .layoutMode(MapViewLayoutParams.ELayoutMode.mapMode)// 按照经纬度设置位置
                .position(hmPos)// 不能传null 设置为mapMode时 必须设置position
                .width(MapViewLayoutParams.WRAP_CONTENT)
                .height(MapViewLayoutParams.WRAP_CONTENT)
//                .yOffset(5)
                .build();
        if (pop == null) {
            Log.e("pop", "pop为空");
        }
        if (mMapView == null) {
            Log.e("pop", "mMapView为空");
        }
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                mMapView.addView(pop, params);
                pop.setVisibility(View.VISIBLE);
            }
        });
        title = (TextView) pop.findViewById(R.id.title);

        title.setText(myLocation.getLocationDescribe());
    }

    /**
     * mark点击
     */
    class MyClickListener implements BaiduMap.OnMarkerClickListener {

        @Override
        public boolean onMarkerClick(Marker marker) {
            String name = marker.getTitle();//获取假的昵称
            Intent intent = new Intent(mActivity, ChatActivity.class);
            // EaseUI封装的聊天界面需要这两个参数，聊天者的username，以及聊天类型，单聊还是群聊
            intent.putExtra("userId", name);
            intent.putExtra("chatType", EMMessage.ChatType.Chat);
            mActivity.startActivity(intent);
            return true;
        }
    }

    /**
     * 拖拽地图
     */
    class MyStatusListener implements BaiduMap.OnMapStatusChangeListener {

        @Override
        public void onMapStatusChangeStart(MapStatus mapStatus) {
            // 手势操作地图，设置地图状态等操作导致地图状态开始改变。
            pop.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onMapStatusChange(MapStatus mapStatus) {
        }

        @Override
        public void onMapStatusChangeFinish(MapStatus mapStatus) {
            // 地图状态改变结束  
            //target地图操作的中心点。
            LatLng target = mapStatus.target;
            //根据坐标获取位置信息
            test(target);
            ViewGroup.LayoutParams params = new MapViewLayoutParams.Builder()
                    .layoutMode(MapViewLayoutParams.ELayoutMode.mapMode)// 按照经纬度设置位置
                    .position(target)// 不能传null
                    .width(MapViewLayoutParams.WRAP_CONTENT)
                    .height(MapViewLayoutParams.WRAP_CONTENT)
                    .yOffset(-5)// 距离position的像素 向下是正值 向上是负值
                    .build();
            mMapView.updateViewLayout(pop, params);
//            pop.setVisibility(View.VISIBLE);

        }
    }

    /**
     * mark拖拽
     */
    class MyDragListener implements BaiduMap.OnMarkerDragListener {
        @Override
        public void onMarkerDragEnd(Marker marker) {
            //TODO 给头上添加一个信息显示我在哪
            //获取每次拖拽后的坐标
            LatLng latLng = marker.getPosition();
            //将火星坐标转为百度坐标
            Gps bd = CoordinateUtil.bd_encrypt(latLng.latitude, latLng.longitude);
            LatLng position = new LatLng(bd.getWgLat(), bd.getWgLon());

            ViewGroup.LayoutParams params = new MapViewLayoutParams.Builder()
                    .layoutMode(MapViewLayoutParams.ELayoutMode.mapMode)// 按照经纬度设置位置
                    .position(position)// 不能传null
                    .width(MapViewLayoutParams.WRAP_CONTENT)
                    .height(MapViewLayoutParams.WRAP_CONTENT)
                    .yOffset(-5)// 距离position的像素 向下是正值 向上是负值
                    .build();
            mMapView.updateViewLayout(pop, params);
            pop.setVisibility(View.VISIBLE);
//            myLocation.setLatitude(position.latitude);
//            myLocation.setLongitude(position.longitude);
        }

        @Override
        public void onMarkerDrag(Marker marker) {
        }

        @Override
        public void onMarkerDragStart(Marker marker) {
//            pop.setVisibility(View.INVISIBLE);
        }
    }


    public void stop() {
        if (mLocationClient != null) {
            mLocationClient.stop();
            Log.e("HomePager", "mLocationClient停掉了");
        }
        mMapView.onDestroy();
        baiduMap.setMyLocationEnabled(false);
    }

    public void myOnResume() {
        mMapView.onResume();
    }

    public void myOnPause() {
        mMapView.onPause();
    }


    private void init() {
        // 设置地图级别（V2.X 3-19 V1.X 3-18）,现在最高级别为21
        // ① 修改了文件的格式 优化了空间的使用（北京 110M 15M）
        // ② 增加了级别 3D效果（18 19）
        baiduMap = mMapView.getMap();
        baiduMap.setMyLocationEnabled(true);//允许我显示在地图上，即可以出现一个小圆圈代替我
        // BaiduMap: 管理具体的某一个MapView ： 旋转，移动，缩放，事件。。

        // 描述地图状态将要发生的变化 使用工厂类MapStatusUpdateFactory创建
        MapStatusUpdate mapstatusUpdate = MapStatusUpdateFactory.zoomTo(19);// 默认的级别12
        // 设置缩放级别
        baiduMap.setMapStatus(mapstatusUpdate);

        // LatLng latlng = new LatLng(arg0, arg1);// 坐标 经纬度 参数1 纬度 参数2 经度
//        MapStatusUpdate mapstatusUpdatePoint = MapStatusUpdateFactory
//                .newLatLng(hmPos);
//        // 设置中心点 默认是天安门
//        baiduMap.setMapStatus(mapstatusUpdatePoint);

        // mapview.showZoomControls(false);// 默认是true 显示缩放按钮
        //
        // mapview.showScaleControl(false);// 默认是true 显示标尺
    }

    class MyLocationListener implements BDLocationListener {
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
            Log.e("getDescribe：", bdLocation.getLocationDescribe());

            if (isFirstLocate) {//第一次移动到“我”的位置
                MapStatusUpdate mapstatusUpdatePoint = MapStatusUpdateFactory
                        .newLatLng(new LatLng(lat, lon));
                // 设置中心点 默认是天安门
                baiduMap.setMapStatus(mapstatusUpdatePoint);
                drawZBLD();
                initPop(new LatLng(lat, lon));
                isFirstLocate = false;
            }
//            else {
//                ViewGroup.LayoutParams params = new MapViewLayoutParams.Builder()
//                        .layoutMode(MapViewLayoutParams.ELayoutMode.mapMode)// 按照经纬度设置位置
//                        .position(new LatLng(lat,lon))// 不能传null
//                        .width(MapViewLayoutParams.WRAP_CONTENT)
//                        .height(MapViewLayoutParams.WRAP_CONTENT)
//                        .yOffset(-5)// 距离position的像素 向下是正值 向上是负值
//                        .build();
//                mMapView.updateViewLayout(pop, params);
//                pop.setVisibility(View.VISIBLE);
//            }

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
//            Log.e("MyLocationListener",currentPosition.toString());
        }

        /**
         * 回调连接wifi是否是移动热点
         *
         * @param s connectWifiMac - 非连接wifi或者异常时返回null,返回的mac已经去除了『冒号』
         * @param i hotSpotState - LocationClient.CONNECT_HOT_SPOT_TRUE,LocationClient.CONNECT_HOT_SPOT_FALSE,LocationClient. CONNECT_HOT_SPOT_UNKNOWN
         */
        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
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
                //反Geo搜索
                String description = result.getSematicDescription();
                title.setText(description);
                pop.setVisibility(View.VISIBLE);
            }
        });
    }

    //    位移-->start
    private List<Integer> colors;
    private List<LatLng> points;
    private PolylineOptions options;
    private int i = 1;
    private KDLocation kd;
    private void startMove() {
        new Thread() {
            @Override
            public void run() {
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        kd = getLocation();
                        Message msg = new Message();
                        Bundle bundle = new Bundle();
                        bundle.putDoubleArray("location", new double[]{kd.getLat(), kd.getLon()});
                        msg.setData(bundle);
                        msg.what = 1;
                        myHandler.sendMessage(msg);
                    }
                }, 3000, 3000);
            }
        }.start();
    }

    class KDLocation {
        private double lat;
        private double lon;

        public KDLocation(double lat, double lon) {
            this.lat = lat;
            this.lon = lon;
        }

        public double getLat() {
            return lat;
        }

        public double getLon() {
            return lon;
        }
    }

    public KDLocation getLocation() {
        KDLocation location;
        location = new KDLocation(34.618822 + 0.0001 * i, 112.42691 - 0.0001 * i);
        i++;
        return location;
    }

    private List<LatLng> newList = new ArrayList<>();
    Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                Bundle data = msg.getData();
                newList.clear();
                newList.add(points.get(points.size() - 1));
                newList.add(new LatLng(data.getDoubleArray("location")[0], data.getDoubleArray("location")[1]));
                Log.e("上一个", points.get(points.size() - 1).latitude + ";" + points.get(points.size() - 1).longitude);
                Log.e("下一个", data.getDoubleArray("location")[0] + ";" + data.getDoubleArray("location")[1]);
                points.add(new LatLng(data.getDoubleArray("location")[0], data.getDoubleArray("location")[1]));
                options.points(newList);
                baiduMap.addOverlay(options);
            }
        }
    };

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
        colors.add(Color.BLUE);
        colors.add(Color.GREEN);
        options.colorsValues(colors);//设置折点颜色
        options.width(10);
        options.visible(true);
        baiduMap.addOverlay(options);
        Log.e("color", options.getColor() + "颜色");
    }
//    位移-->end

}
