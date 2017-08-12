package example.caiworld.caihao.lylgapp.pager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import example.caiworld.caihao.lylgapp.ImagePagerActivity;
import example.caiworld.caihao.lylgapp.MainActivity;
import example.caiworld.caihao.lylgapp.R;
import example.caiworld.caihao.lylgapp.adapter.PictureShareAdapter;
import example.caiworld.caihao.lylgapp.base.BasePager;
import example.caiworld.caihao.lylgapp.bean.Dynamic;

/**
 * Created by caihao on 2017/5/18.
 */
public class AllDynamicPager extends BasePager {
    private ListView lvShare;
    private ShareAdapter adapter;
    private ProgressDialog dialog;
    private RefreshLayout refreshLayout;
    private HashMap<Integer, List<String>> map;

    public AllDynamicPager(Activity activity) {
        super(activity);
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.pager_second, null);

        lvShare = (ListView) view.findViewById(R.id.lv_share);
        dynamicList = new ArrayList<>();
        adapter = new ShareAdapter();
        lvShare.setAdapter(adapter);
        refreshLayout = (RefreshLayout) view.findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
//                        dynamics.add(0, new Dynamic(R.mipmap.icon, "张二", "李四真傻。", new int[]{R.mipmap.fa2, R.mipmap.shou2}, "7-17 12:30", 3, null));
                        SystemClock.sleep(2000);
                        handler.sendEmptyMessage(2);
                    }
                }) {
                }.start();
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadmore(2000);
                handler.sendEmptyMessage(4);
            }
        });
        return view;
    }

    public List<Dynamic> getDynamicList() {
        return dynamicList;
    }

    public Handler getHandler() {
        return handler;
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 3) {//自己发动态了
                adapter.notifyDataSetChanged();
                return;
            }
            if (msg.what == 4) {//上拉加载
                Toast.makeText(mActivity, "没有更多数据了", Toast.LENGTH_SHORT).show();
                return;
            }

            if (msg.what == 2) {
                refreshLayout.finishRefresh(0);//下拉刷新
                adapter.notifyDataSetChanged();
                return;
            }
            if (dialog.isShowing()) {
                if (msg.what == 1) {
                    dialog.dismiss();
                    adapter.notifyDataSetChanged();
                }
            } else {//用户取消了加载
//                dynamics.clear();
                adapter.notifyDataSetChanged();
            }
        }
    };
    private boolean isFirst = true;

    @Override
    public void initData() {
        map = new HashMap<>();
        if (isFirst) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Looper.prepare();
                    dialog = new ProgressDialog(mActivity);
                    dialog.setTitle("加载中...");
                    dialog.show();
                    requestData();
//                    handler.sendEmptyMessage(1);
                    isFirst = false;
                    Looper.loop();
                }
            }).start();
        }
    }

    private void requestData() {
        select();
    }

    private List<Dynamic> dynamicList;//所有的dynamic

    private void select() {
        BmobQuery<Dynamic> query = new BmobQuery<>();
        query.setLimit(10).order("-createdAt").findObjects(new FindListener<Dynamic>() {
            @Override
            public void done(List<Dynamic> list, BmobException e) {
                if (e == null) {
                    if (list != null) {
                        Log.e("list.size", list.size() + "条");
                        for (Dynamic d : list) {
                            Log.e("AllDynamicPager", d.getDate());
                        }
                        dynamicList.addAll(list);
                        handler.sendEmptyMessage(1);
                    } else {
                        Log.e("list为空", "空");
                    }
                    Log.e("e==null", "空2");
                } else {
                    Log.e("e!=null", e.getMessage() + e.getErrorCode());
                }
            }
        });
    }

    class ShareAdapter extends BaseAdapter {

        private List<String> picsList;
        ShareHolder holder;

        @Override
        public int getCount() {
            return dynamicList.size();
        }

        @Override
        public Object getItem(int position) {
            return dynamicList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = View.inflate(mActivity, R.layout.item_share, null);
                holder = new ShareHolder();
                holder.ivHeader = (ImageView) convertView.findViewById(R.id.iv_Header);
                holder.tvName = (TextView) convertView.findViewById(R.id.tv_Name);
                holder.tvContent = (TextView) convertView.findViewById(R.id.tv_content);
                holder.ivLike = (ImageView) convertView.findViewById(R.id.iv_like);
                holder.date = (TextView) convertView.findViewById(R.id.tv_date);
                holder.gridView = (GridView) convertView.findViewById(R.id.gv);
                convertView.setTag(holder);
            } else {
                holder = (ShareHolder) convertView.getTag();
            }
            //设置dynamic的头像
            Dynamic dynamic = dynamicList.get(position);
            if (dynamic.getPicHeader() != null) {
                Glide.with(mActivity).load(dynamic.getPicHeader().getFileUrl()).into(holder.ivHeader);
            } else {
                holder.ivHeader.setImageResource(R.mipmap.h2);
            }
            holder.tvName.setText(dynamic.getUsername());
            holder.tvContent.setText(dynamic.getContent());
            holder.date.setText(dynamic.getDate());
            holder.ivLike.setTag(position);
            holder.ivLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("position", position + "个");
//                    mActivity.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            int tag = (int) holder.ivLike.getTag();
//                            if (tag == position) {
//                                holder.ivLike.setImageDrawable(mActivity.getResources().getDrawable(R.mipmap.liked));
//                            }else{
//
//                            }
//                        }
//                    });
                    int tag = (int) holder.ivLike.getTag();
                    if (tag == position) {
                        holder.ivLike.setImageDrawable(mActivity.getResources().getDrawable(R.mipmap.liked));
                    } else {

                    }
                }
            });
            List<BmobFile> pics = dynamic.getPics();
            Log.e("AllDynamicPager", "pics.size" + pics.size());
            picsList = new ArrayList<>();
            for (BmobFile bf : pics) {
//                Log.e("url+fileurl", bf.getFileUrl() + "url:" + bf.getUrl());
                picsList.add(bf.getUrl());
            }
            if (pics != null || pics.size() > 0) {//有图片
                map.put(position,picsList);
                holder.gridView.setVisibility(View.VISIBLE);
                holder.gridView.setAdapter(new PictureShareAdapter(picsList, (MainActivity) mActivity));
                holder.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                        Log.e("第几个条目", position + "ge" + picsList.size());
                        for (String s : picsList) {
                            Log.e("点击piclist", s);
                        }
                        Intent intent = new Intent(mActivity, ImagePagerActivity.class);
                        Log.e("position273",position+"");
                        for (String st : map.get(position)) {
                            Log.e("数据",st);
                        }
                        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, (Serializable) map.get(position));
                        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, i);
                        mActivity.startActivity(intent);
                    }
                });
            } else {
                holder.gridView.setVisibility(View.GONE);
            }
            return convertView;
        }
    }


    static class ShareHolder {
        ImageView ivHeader;
        ImageView ivLike;
        TextView tvName;
        TextView tvContent;
        TextView date;
        GridView gridView;
        //        Button btComment, btZan, btShare;
    }
}
