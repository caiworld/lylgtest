package example.caiworld.caihao.lylgapp.pager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMMessage;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import example.caiworld.caihao.lylgapp.MainActivity;
import example.caiworld.caihao.lylgapp.PeopleDetailActivity;
import example.caiworld.caihao.lylgapp.R;
import example.caiworld.caihao.lylgapp.base.BasePager;
import example.caiworld.caihao.lylgapp.bean.LYLGBmobUser;
import example.caiworld.caihao.lylgapp.utils.ImageCompressUtil;

/**
 * 联系人页面
 * Created by caihao on 2017/5/18.
 */
public class FourPager extends BasePager {

    private ListView lvFriends;
    private List<LYLGBmobUser> friends;
    private FriendsAdapter adapter;
    private ProgressDialog dialog;
    private ImageView ibtAdd1;
    private ImageView ibtAdd2;

    public FourPager(Activity activity) {
        super(activity);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (dialog.isShowing()) {
                if (msg.what == 1) {
                    adapter.notifyDataSetChanged();
                } else if (msg.what == 2) {
                    Toast.makeText(mActivity, "访问服务器失败", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            } else {//用户取消了加载
                friends.clear();//清空之前的数据
                friends.add(new LYLGBmobUser("客服", "kefu", ""));//把客服重新添加进去
                adapter.notifyDataSetChanged();
            }
        }
    };

    @Override
    public View initView() {
        ibtAdd1 = ((MainActivity) mActivity).getIbtAdd1();
        ibtAdd2 = ((MainActivity) mActivity).getIbtAdd2();
        friends = new ArrayList<>();
        friends.add(new LYLGBmobUser("客服", "kefu", ""));//添加客服，以免一个好友也没有
        View view = View.inflate(mActivity, R.layout.pager_four, null);
        lvFriends = (ListView) view.findViewById(R.id.lv_friends);
//        requestServer();
        adapter = new FriendsAdapter();
        lvFriends.setAdapter(adapter);
        lvFriends.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mActivity, PeopleDetailActivity.class);
                // EaseUI封装的聊天界面需要这两个参数，聊天者的username，以及聊天类型，单聊还是群聊
                intent.putExtra("userId", friends.get(position).getUsername());
                intent.putExtra("chatType", EMMessage.ChatType.Chat);
                intent.putExtra("headerPic", bitmaps[position % 3]);
                mActivity.startActivity(intent);
            }
        });
        return view;
    }

    private boolean isFirst = true;

    @Override
    public void initData() {
        ((MainActivity) mActivity).getTvTitle().setText("通讯录");

        ibtAdd1.setVisibility(View.GONE);
        ibtAdd2.setVisibility(View.VISIBLE);
        //TODO 在这里写的话则requestServer()中获取不到friends.size();
        //每次进入这个页面的时候都去请求服务器获取好友数据
        if (isFirst) {
            dialog = new ProgressDialog(mActivity);
            dialog.setTitle("加载中");
            dialog.show();
            requestServer();
            isFirst = false;
        }
    }

    /**
     * 查询服务器获取数据
     */
    private void requestServer() {
        BmobQuery<LYLGBmobUser> query = new BmobQuery<>();
        query.addQueryKeys("username");//只查询username这一列的值
        query.findObjects(new FindListener<LYLGBmobUser>() {
            @Override
            public void done(List<LYLGBmobUser> list, BmobException e) {
                if (e == null) {//查询成功
                    friends.clear();//清空之前的数据
                    friends.add(new LYLGBmobUser("客服", "kefu", ""));//把客服重新添加进去
                    friends.addAll(list);
                    for (LYLGBmobUser user : list) {
                        if (user.getUsername().equals(MainActivity.getUserName())) {
                            friends.remove(user);//剔除自己
                            break;
                        }

                    }
                    handler.sendEmptyMessage(1);
                } else {//查询失败
                    handler.sendEmptyMessage(2);
                    Log.e("FourPager:", "requestServer失败" + e.getMessage());
                }
            }
        });
        if (friends != null) {
            Log.e("friends.size", friends.size() + "");
        }
    }


    private int[] bitmaps = {R.mipmap.icon, R.mipmap.pinlove, R.mipmap.ic_launcher};
    private int i;
    private Bitmap bitmap;

    class FriendsAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return friends.size();
        }

        @Override
        public Object getItem(int position) {
            return friends.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            FriendHolder holder;
            if (convertView == null) {
                convertView = View.inflate(mActivity, R.layout.item_friend, null);
                holder = new FriendHolder();
                holder.ivHeader = (ImageView) convertView.findViewById(R.id.iv_header);
                holder.tvFriend = (TextView) convertView.findViewById(R.id.tv_friend);
                convertView.setTag(holder);
            } else {
                holder = (FriendHolder) convertView.getTag();
            }
            i = position % 3;
            bitmap = BitmapFactory.decodeResource(mActivity.getResources(), bitmaps[i]);
            bitmap = ImageCompressUtil.compressBySize(bitmap, 30, 30);
            holder.ivHeader.setImageDrawable(new BitmapDrawable(mActivity.getResources(), bitmap));
            holder.tvFriend.setText(friends.get(position).getUsername());
            return convertView;
        }
    }

    static class FriendHolder {
        ImageView ivHeader;
        TextView tvFriend;
    }

}
