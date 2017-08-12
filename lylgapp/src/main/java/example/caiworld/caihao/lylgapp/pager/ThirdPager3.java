package example.caiworld.caihao.lylgapp.pager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.hyphenate.chat.EMMessage;

import java.util.ArrayList;
import java.util.List;

import example.caiworld.caihao.lylgapp.MainActivity;
import example.caiworld.caihao.lylgapp.PeopleDetailActivity;
import example.caiworld.caihao.lylgapp.R;
import example.caiworld.caihao.lylgapp.ShareAddressActivity;
import example.caiworld.caihao.lylgapp.base.BasePager;
import example.caiworld.caihao.lylgapp.bean.SFS;
import example.caiworld.caihao.lylgapp.utils.CoordinateUtil;
import example.caiworld.caihao.lylgapp.utils.Gps;

/**
 * Created by caihao on 2017/7/20.
 */
public class ThirdPager3 extends BasePager {

    private View view;
    private MapView mapView;
    private ListView lvSfs;

    private boolean isFirstLocate = true;//用来判断是否是第一次移动位置，默认是true，移动一次之后置为false
    private BaiduMap baiduMap;
    private LocationClient mLocationClient;
    private BDLocation myLocation;
    private double lat, lon;
    private ImageView ibtAdd1;
    private ImageView ibtAdd2;
    private LinearLayout lls;
    private int id;
    private Button chooseAddres;
    private SFSAdapter adapter;

    public ThirdPager3(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        Log.e("ThirdPager3", "initData");
        ((MainActivity) mActivity).getTvTitle().setText("顺风送");
        ibtAdd1.setVisibility(View.GONE);
        ibtAdd2.setVisibility(View.GONE);
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_sfs, null);
        ibtAdd1 = ((MainActivity) mActivity).getIbtAdd1();
        ibtAdd2 = ((MainActivity) mActivity).getIbtAdd2();
        mapView = (MapView) view.findViewById(R.id.mapView);
        chooseAddres = (Button) view.findViewById(R.id.bt_chooseaddress);
        chooseAddres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, ShareAddressActivity.class);
                mActivity.startActivityForResult(intent, 11);
            }
        });
        lvSfs = (ListView) view.findViewById(R.id.lv_sfs);
        lls = (LinearLayout) view.findViewById(R.id.lls);
        SharedPreferences sp = mActivity.getSharedPreferences("id", Context.MODE_PRIVATE);
        id = sp.getInt("id", 1);
        if (id == 1) {
            moniShuju1();
            adapter = new SFSAdapter();
            lvSfs.setAdapter(adapter);
            Log.e("id", "111111111");
        } else if (id == 2) {
            moniShuju1();
            lvSfs.setAdapter(adapter);
            Log.e("id", "222222222");
        } else if (id == 3) {
            moniShuju3();
            lvSfs.setAdapter(new SFSAdapter3());
        }
        init();
        ((MainActivity) mActivity).getTvTitle().setText("顺风车");

        mLocationClient = new LocationClient(mActivity);
        mLocationClient.registerLocationListener(new MyBDLocationListener());
        requestLocation();
        Log.e("ThirdPager3", "initView");
        return view;
    }

    private List<String> list3;

    /**
     * 第三种身份时用
     */
    private void moniShuju3() {
        list3 = new ArrayList<>();
        list3.add("由洛阳理工学院王城校区出发途径河南科技大学到达洛阳理工学院开元校区");
        list3.add("由洛阳理工学院王城校区出发途径新区体育场到达洛阳理工学院开元校区");
    }

    class SFSAdapter3 extends BaseAdapter {

        @Override
        public int getCount() {
            return list3.size();
        }

        @Override
        public Object getItem(int position) {
            return list3.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(mActivity, R.layout.item_sfs3, null);
            }
            TextView tvRoute = (TextView) convertView.findViewById(R.id.tv_route);
            tvRoute.setText(list3.get(position));
            return convertView;
        }
    }

    private List<SFS> list;

    /**
     * 第一种身份时用
     */
    private void moniShuju1() {
        list = new ArrayList<>();
        list.add(new SFS(R.mipmap.icon, "white", "东方经典", "王城公园"));
        list.add(new SFS(R.mipmap.ic_launcher, "cai", "可不可奶茶店", "王城公园"));
        list.add(new SFS(R.mipmap.pinlove, "zzz", "东方经典", "沙厂西路"));
    }
SharedPreferences sp1 = mActivity.getSharedPreferences("user",Context.MODE_PRIVATE);
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
                convertView = View.inflate(mActivity, R.layout.item_sfs, null);
                holder = new SFSHolder();
                holder.ivHeader = (ImageView) convertView.findViewById(R.id.iv_header);
                holder.name = (TextView) convertView.findViewById(R.id.tv_name);
                holder.from = (TextView) convertView.findViewById(R.id.tv_from);
                holder.to = (TextView) convertView.findViewById(R.id.tv_to);
                convertView.setTag(holder);
            } else {
                holder = (SFSHolder) convertView.getTag();
            }
            final SFS sfs = list.get(position);
            holder.ivHeader.setImageDrawable(mActivity.getResources().getDrawable(sfs.getIvHeader()));
            holder.ivHeader.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mActivity, PeopleDetailActivity.class);
                    intent.putExtra("userId",sfs.getName() );
                    intent.putExtra("chatType", EMMessage.ChatType.Chat);
                    mActivity.startActivity(intent);
                }
            });
            holder.name.setText(sfs.getName());
            holder.from.setText(sfs.getFrom());
            holder.to.setText(sfs.getTo());
            return convertView;
        }
    }

    static class SFSHolder {
        ImageView ivHeader;
        TextView name,from,to;
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

    /**
     * 根据位置获取坐标信息，并标注在地图上
     *
     * @param arg0 0:from,1:to
     * @param arg1 百度地图地理位置
     */
    public void test(String arg0, String arg1) {
        GeoCoder search = GeoCoder.newInstance();
        search.geocode(new GeoCodeOption().city("洛阳市").address(arg1));


        search.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
            @Override
            public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
                if (geoCodeResult.getLocation() == null) {
                    return;
                }
                Log.e("geocoderesult", geoCodeResult.getLocation().latitude + ";" + geoCodeResult.getLocation().longitude);
//                    initLines();
            }

            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
            }
        });
        initLines();
        Log.e("ThirdPager3", lls.getVisibility() + "十六进制");
        lls.setVisibility(View.VISIBLE);
        lvSfs.setVisibility(View.VISIBLE);
    }

    public void test2() {
        initLines();
        Log.e("ThirdPager3", list.size() + "个");
//        lls.setVisibility(View.VISIBLE);
        lvSfs.setVisibility(View.VISIBLE);
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
//        baiduMap.addOverlay(options);
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
