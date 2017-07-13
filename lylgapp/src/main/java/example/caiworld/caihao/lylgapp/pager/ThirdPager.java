package example.caiworld.caihao.lylgapp.pager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DialogTitle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import example.caiworld.caihao.lylgapp.MainActivity;
import example.caiworld.caihao.lylgapp.PackageDetailActivity;
import example.caiworld.caihao.lylgapp.R;
import example.caiworld.caihao.lylgapp.base.BasePager;
import example.caiworld.caihao.lylgapp.bean.SenderDetail;

/**
 * Created by caihao on 2017/5/18.
 */
public class ThirdPager extends BasePager {

    private ListView lvSendPackage;
    private List<SenderDetail> packages;
    private MyAdapter adapter;
    private MyHandler handler;
    private ProgressDialog dialog;

    public ThirdPager(Activity activity) {
        super(activity);
    }

    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (dialog.isShowing()) {
                if (msg.what == 1) {
                    dialog.dismiss();
                    adapter.notifyDataSetChanged();
                }
            } else {
                packages.clear();//用户取消了加载，则将数据清空，防止加载多遍
            }
        }
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.pager_third, null);
        lvSendPackage = (ListView) view.findViewById(R.id.lv_sendPackage);
        handler = new MyHandler();
        packages = new ArrayList<>();
        adapter = new MyAdapter();
        lvSendPackage.setAdapter(adapter);
        lvSendPackage.setOnItemClickListener(new MyItemClickListener());
        return view;
    }

    private boolean isFirst = true;

    @Override
    public void initData() {
        ((MainActivity) mActivity).getTvTitle().setText("随手送");
        ((MainActivity) mActivity).getIbtAdd().setVisibility(View.INVISIBLE);
        if (isFirst) {
            dialog = new ProgressDialog(mActivity);
            dialog.setTitle("加载中");
            dialog.show();
            packages.clear();

            BmobQuery<SenderDetail> bmobQuery = new BmobQuery<>();
            bmobQuery.findObjects(new FindListener<SenderDetail>() {
                @Override
                public void done(List<SenderDetail> list, BmobException e) {
                    if (e == null) {
                        for (SenderDetail p : list) {
                            Log.e("详情1", p.toString());
                        }
                        packages.addAll(list);
                        handler.sendEmptyMessage(1);
                        isFirst = false;
                    } else {
                        Log.e("RequestPackage", "查询失败");
                    }
                }
            });
        }

    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return packages.size();
        }

        @Override
        public Object getItem(int position) {
            return packages.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            PackageHolder holder;
            if (convertView == null) {
                convertView = View.inflate(mActivity, R.layout.item_sendpackage, null);
                holder = new PackageHolder();
                holder.ivHeader = (ImageView) convertView.findViewById(R.id.iv_header);
                holder.username = (TextView) convertView.findViewById(R.id.tv_username);
                holder.sendAddress = (TextView) convertView.findViewById(R.id.tv_sendAddress);
                holder.receiveAddress = (TextView) convertView.findViewById(R.id.tv_receiveAddress);
                holder.convinient = (TextView) convertView.findViewById(R.id.tv_convinient);
                convertView.setTag(holder);
            } else {
                holder = (PackageHolder) convertView.getTag();
            }
            if (packages.size() > 0) {
                holder.username.setText(packages.get(position).getUsername());
                holder.sendAddress.setText(packages.get(position).getSendAddress());
                holder.receiveAddress.setText(packages.get(position).getReceiveAddress());
                if (position == 0) {
                    holder.convinient.setVisibility(View.VISIBLE);
                } else {
                    holder.convinient.setVisibility(View.INVISIBLE);
                }
            }
            return convertView;
        }
    }

    static class PackageHolder {
        ImageView ivHeader;
        TextView username;
        TextView sendAddress;
        TextView receiveAddress;
        TextView convinient;
    }

    /**
     * <p/>
     * private String username;//用户名
     * <p/>
     * private String sendAddress;//发货人地址
     * private double lat;//发货人纬度
     * private double lon;//发货人经度
     * private String sendName;//发货人姓名
     * private String sendNumber;//发货人号码
     * <p/>
     * private String receiveAddress;//收货人地址
     * private String receiveName;//收货人名字
     * private String receiveNumber;//收货人号码
     * <p/>
     * private String pickUpTime;//取货时间
     * private String goodsDetail;//物品信息
     * private String comment;//备注
     */
    class MyItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(mActivity, PackageDetailActivity.class);
            SenderDetail senderDetail = packages.get(position);
            intent.putExtra("username", senderDetail.getUsername());
            intent.putExtra("sendAddress", senderDetail.getSendAddress());
            intent.putExtra("lat", senderDetail.getLat());
            intent.putExtra("lon", senderDetail.getLon());
            intent.putExtra("sendName", senderDetail.getSendName());
            intent.putExtra("sendNumber", senderDetail.getSendNumber());
            intent.putExtra("receiveAddress", senderDetail.getReceiveAddress());
            intent.putExtra("receiveName", senderDetail.getReceiveName());
            intent.putExtra("receiveNumber", senderDetail.getReceiveNumber());
            intent.putExtra("pickUpTime", senderDetail.getPickUpTime());
            intent.putExtra("goodsDetail", senderDetail.getGoodsDetail());
            intent.putExtra("comment", senderDetail.getComment());
            mActivity.startActivity(intent);
        }
    }
}
