package example.caiworld.caihao.lylgapp.pager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import example.caiworld.caihao.lylgapp.MainActivity;
import example.caiworld.caihao.lylgapp.R;
import example.caiworld.caihao.lylgapp.base.BasePager;
import example.caiworld.caihao.lylgapp.bean.Dynamic;

/**
 * Created by caihao on 2017/5/18.
 */
public class FriendDynamicPager extends BasePager {

    private ListView lvShare;
    private ShareAdapter adapter;
    private List<Dynamic> dynamics;
    private ProgressDialog dialog;
    private RefreshLayout refreshLayout;

    public FriendDynamicPager(Activity activity) {
        super(activity);
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.pager_second, null);
        lvShare = (ListView) view.findViewById(R.id.lv_share);
        dynamics = new ArrayList<>();
        adapter = new ShareAdapter();
        lvShare.setAdapter(adapter);
        refreshLayout = (RefreshLayout) view.findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SystemClock.sleep(3000);
                        handler.sendEmptyMessage(2);
                    }
                }){}.start();
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadmore(2000);
            }
        });
        return view;
    }


    private void moniData() {
        Dynamic dynamic1 = new Dynamic(R.mipmap.icon, "cai", "今天天真热，待会去买水喝。哦，没带钱，还要回宿舍去拿钱，这么远，算了。", new int[]{R.mipmap.shou2}, "7-11 15:31", 3, null);
        Dynamic dynamic2 = new Dynamic(R.mipmap.icon, "zhang", "真饿，可以去吃饭了。", new int[]{ R.mipmap.shou2}, "7-11 15:28", 4, null);
        Dynamic dynamic3 = new Dynamic(R.mipmap.icon, "zhang", "今天天真热，吃不下饭。", new int[]{ R.mipmap.shou2}, "7-11 15:25", 2, null);
        Dynamic dynamic4 = new Dynamic(R.mipmap.icon, "cai", "今天天真热，待会去买水喝。哦，没带钱，还要回宿舍去拿钱，这么远，算了。", new int[]{R.mipmap.shou2}, "7-11 15:15", 5, null);
        Dynamic dynamic5 = new Dynamic(R.mipmap.icon, "zzz", "今天天真热，待会去买水喝。哦，没带钱，还要回宿舍去拿钱，这么远，算了。", new int[]{R.mipmap.fa2, R.mipmap.shou2}, "7-11 15:02", 1, null);
        dynamics.add(dynamic1);
        dynamics.add(dynamic2);
        dynamics.add(dynamic3);
        dynamics.add(dynamic4);
        dynamics.add(dynamic5);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==2){
                refreshLayout.finishRefresh(0);//下拉刷新
                return;
            }
            if (dialog.isShowing()) {
                if (msg.what == 1) {
                    dialog.dismiss();
                    adapter.notifyDataSetChanged();
                }
            } else {//用户取消了加载
                dynamics.clear();
                adapter.notifyDataSetChanged();
            }
        }
    };
    private boolean isFirst = true;

    @Override
    public void initData() {
//        ((MainActivity) mActivity).getTvTitle().setText("分享");
//        ((MainActivity) mActivity).getIbtAdd().setImageDrawable(mActivity.getResources().getDrawable(R.mipmap.publish));
//        ((MainActivity) mActivity).getIbtAdd().setVisibility(View.VISIBLE);
        if (isFirst) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Looper.prepare();
                    dialog = new ProgressDialog(mActivity);
                    dialog.setTitle("加载中...");
                    dialog.show();
                    moniData();
                    handler.sendEmptyMessage(1);
                    isFirst = false;
                    Looper.loop();
                }
            }).start();
        }
    }

    class ShareAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return dynamics.size();
        }

        @Override
        public Object getItem(int position) {
            return dynamics.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ShareHolder holder;
            if (convertView == null) {
                convertView = View.inflate(mActivity, R.layout.item_share, null);
                holder = new ShareHolder();
                holder.ivHeader = (ImageView) convertView.findViewById(R.id.iv_Header);
                holder.tvName = (TextView) convertView.findViewById(R.id.tv_Name);
                holder.tvContent = (TextView) convertView.findViewById(R.id.tv_content);
                holder.iv1 = (ImageView) convertView.findViewById(R.id.iv1);
                holder.iv2 = (ImageView) convertView.findViewById(R.id.iv2);
                holder.ivLike = (ImageView) convertView.findViewById(R.id.iv_like);
                holder.date = (TextView) convertView.findViewById(R.id.tv_date);
                holder.llPics = (LinearLayout) convertView.findViewById(R.id.ll_pics);
                convertView.setTag(holder);
            } else {
                holder = (ShareHolder) convertView.getTag();
            }
            holder.ivHeader.setImageDrawable(mActivity.getResources().getDrawable(dynamics.get(position).getPicHeader()));
            holder.tvName.setText(dynamics.get(position).getUsername());
            holder.tvContent.setText(dynamics.get(position).getContent());
            holder.date.setText(dynamics.get(position).getDate());
            holder.ivLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            holder.ivLike.setImageDrawable(mActivity.getResources().getDrawable(R.mipmap.liked));
                        }
                    });
                }
            });
//            if (position % 3 == 1) {
//                holder.iv1.setImageDrawable(mActivity.getResources().getDrawable(dynamics.get(position).getPics()[0]));
//                holder.iv2.setImageDrawable(mActivity.getResources().getDrawable(dynamics.get(position).getPics()[1]));
//                holder.iv1.setVisibility(View.VISIBLE);
//                holder.iv2.setVisibility(View.VISIBLE);
//            } else if (position % 3 == 0) {
//                holder.iv1.setImageDrawable(mActivity.getResources().getDrawable(dynamics.get(position).getPics()[0]));
//                holder.iv2.setVisibility(View.INVISIBLE);
//            } else {
//                holder.iv1.setImageDrawable(mActivity.getResources().getDrawable(dynamics.get(position).getPics()[1]));
//                holder.iv2.setVisibility(View.INVISIBLE);
//            }
            holder.llPics.setVisibility(View.VISIBLE);
            if (dynamics.get(position).getPics().length==1){
                holder.iv1.setImageDrawable(mActivity.getResources().getDrawable(dynamics.get(position).getPics()[0]));
                holder.iv1.setVisibility(View.VISIBLE);
                holder.iv2.setVisibility(View.GONE);
            }else if (dynamics.get(position).getPics().length==2){
                holder.iv1.setImageDrawable(mActivity.getResources().getDrawable(dynamics.get(position).getPics()[0]));
                holder.iv2.setImageDrawable(mActivity.getResources().getDrawable(dynamics.get(position).getPics()[1]));
                holder.iv1.setVisibility(View.VISIBLE);
                holder.iv2.setVisibility(View.VISIBLE);
            }
            return convertView;
        }
    }

    static class ShareHolder {
        LinearLayout llPics;
        ImageView ivHeader;
        TextView tvName;
        TextView tvContent;
        ImageView ivLike;
        ImageView iv1, iv2;
        TextView date;
        //        Button btComment, btZan, btShare;
    }
}
