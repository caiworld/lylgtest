package example.caiworld.caihao.lylgapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import cn.bmob.v3.datatype.BmobFile;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import de.hdodenhof.circleimageview.CircleImageView;
import example.caiworld.caihao.lylgapp.bean.Dynamic;
import example.caiworld.caihao.lylgapp.fragment.ContentFragment;
import example.caiworld.caihao.lylgapp.jpush.ExampleUtil;
import example.caiworld.caihao.lylgapp.jpush.Logger;
import example.caiworld.caihao.lylgapp.pager.AllDynamicPager;
import example.caiworld.caihao.lylgapp.pager.HomePager;

public class MainActivity extends AppCompatActivity {
    private FrameLayout flMain;
    private static final String CONTENT_FRAGMENT = "fragment_content";
    private ContentFragment contentFragment;
    public static boolean isForeground = false;
    private static String tag;
    private TextView tvTitle;
    private DrawerLayout dl;
    private ImageView ibtAdd1, ibtAdd2;
    private CircleImageView menuHeader;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Thread() {
            @Override
            public void run() {
                super.run();
                saveMyPic();
            }
        }.start();
        id = getIntent().getIntExtra("id", 1);
        initView();

        init();
        initFragment();
    }

    /**
     * 本地存储我的头像
     */
    private void saveMyPic() {
        File file = new File(getFilesDir(), "mypic.png");
        if (file.exists()) {
            return;
        }
        try {
            //通过相关方法生成一个Bitmap类型的对象
//            Bitmap qrcodeBitmap = EncodingHandler.createQRCode(content, 400);
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.h2);
            FileOutputStream fileOutStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutStream); // 把位图输出到指定的文件中
            fileOutStream.flush();
            fileOutStream.close();
            String path = file.getPath();
            long length = file.length();
            Log.e("path", path + ";;length" + length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //从网上获取我的头像
        requestFromServer();
    }

    private void requestFromServer() {

    }

    public int getId() {
        return id;
    }

    private void initView() {
        tvTitle = (TextView) findViewById(R.id.tv_title);
        ibtAdd1 = (ImageView) findViewById(R.id.ibt_add1);
        ibtAdd2 = (ImageView) findViewById(R.id.ibt_add2);
        dl = (DrawerLayout) findViewById(R.id.dl);
        menuHeader = (CircleImageView) findViewById(R.id.menu_header);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
//        if (actionBar != null) {
//            actionBar.setDisplayHomeAsUpEnabled(true);//显示HomeAsUp这个按钮
//
//            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
//        }
        menuHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dl.openDrawer(GravityCompat.START);
            }
        });
        Menu menu = navView.getMenu();
        MenuItem change = menu.getItem(1);
        if (id == 1) {
            change.setTitle("切换身份（宅神）");
        } else if (id == 2) {
            change.setTitle("切换身份（shopping达人）");
        } else if (id == 3) {
            change.setTitle("切换身份（快递员）");
        }

        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                switch (itemId) {
                    case R.id.nav_quit:
                        dl.closeDrawers();
                        quit();
                        break;
                    case R.id.nav_change://切换身份
                        Intent intent1 = new Intent(MainActivity.this, ChooseIdentity.class);
                        startActivity(intent1);
                        finish();
                        break;
                    case R.id.nav_certificate:
                        //去认证界面
                        Intent intent2 = new Intent(MainActivity.this, CertifacateActivity.class);
                        startActivity(intent2);
                        dl.closeDrawers();
                        break;
                    case R.id.nav_order:
                        //去订单页
                        Intent intent3 = new Intent(MainActivity.this, OrderActivity.class);
                        startActivity(intent3);
                        break;
                }
                return true;
            }
        });
        ibtAdd1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PunishedDynamic.class);
                startActivityForResult(intent, 10);
            }
        });
    }

    private void quit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("退出");
        builder.setMessage("确认退出当前账号");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 调用sdk的退出登录方法，第一个参数表示是否解绑推送的token，没有使用推送或者被踢都要传false
                EMClient.getInstance().logout(false, new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        Log.i("lzan13", "logout success");
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        // 调用退出成功，结束app
                        finish();
                    }

                    @Override
                    public void onError(int i, String s) {
                        Log.i("lzan13", "logout error " + i + " - " + s);
                    }

                    @Override
                    public void onProgress(int i, String s) {

                    }
                });
            }
        });
        builder.setNegativeButton("取消", null);
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 10 && resultCode == 10) {
            String content = data.getStringExtra("content");
            String date = data.getStringExtra("date");
            ArrayList<String> picList = data.getStringArrayListExtra("picList");
            AllDynamicPager allDynamic = contentFragment.getSecondPager2().getAllDynamic();
            List<BmobFile> pics = new ArrayList<>();
            int i = 0;
            Log.e("Mainac,piclist.size",picList.size()+"tiao");
                for (String bfUrl : picList) {
                BmobFile bf = new BmobFile();
                bf.setUrl(bfUrl);
                pics.add(bf);
                Log.e("Mainacticity图片长度", "wo" + (i++)+";;bfUrl:"+bfUrl);
            }
            Dynamic dynamic = new Dynamic(null, tag, content, pics, date, null, null);
            allDynamic.getDynamicList().add(0, dynamic);
            allDynamic.getHandler().sendEmptyMessage(3);
        } else if (requestCode == 11) {
//            data.getStringExtra("")TODO 获取到输入的要匹配的快递员的路线
            Log.e("MainActivity", "finish了");
            contentFragment.getThirdPager3().test2();
        } else if (requestCode == 12 && resultCode == 12) {
            contentFragment.getSfsPager().setLLVisibility();
        }
    }

    public TextView getTvTitle() {
        return tvTitle;
    }

    public ImageView getIbtAdd1() {
        return ibtAdd1;
    }

    public ImageView getIbtAdd2() {
        return ibtAdd2;
    }

    public Fragment getFragment() {
        return contentFragment;
    }

    private void init() {
        setTag();
    }

    /**
     * 初始化fragment，把fragment数据填充给布局文件
     */
    private void initFragment() {
        flMain = (FrameLayout) findViewById(R.id.fl_main);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();//开启一个事物
        //第三个参数给fragment添加标签tag，便于查找
        //fm.findFragmentByTag(CONTENT_FRAGMENT);
        contentFragment = new ContentFragment();
        transaction.replace(R.id.fl_main, contentFragment, CONTENT_FRAGMENT);
        transaction.commit();//提交事物

    }

    @Override
    protected void onResume() {
        super.onResume();
        isForeground = true;
        if (id == 1) {
            ((HomePager) contentFragment.pagerList.get(0)).myOnResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isForeground = false;
        if (id == 1) {
            ((HomePager) contentFragment.pagerList.get(0)).myOnPause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //主页面销毁时退出登录
        signOut();
        //停止LocationClient，即停止获取位置信息
        if (id == 1) {
            ((HomePager) contentFragment.pagerList.get(0)).stop();
        }
        Log.e("MainActivity", "主页面销毁");
    }

    /**
     * 退出登录
     */
    private void signOut() {
        // 调用sdk的退出登录方法，第一个参数表示是否解绑推送的token，没有使用推送或者被踢都要传false
        EMClient.getInstance().logout(false, new EMCallBack() {
            @Override
            public void onSuccess() {
                Log.i("lzan13", "logout success");
                // 调用退出成功，结束app
                finish();
            }

            @Override
            public void onError(int i, String s) {
                Log.i("lzan13", "logout error " + i + " - " + s);
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }


    private int mBackKeyPressedTimes = 0;

    @Override
    public void onBackPressed() {
        if (mBackKeyPressedTimes == 0) {
            Toast.makeText(this, "再按一次退出程序 ", Toast.LENGTH_SHORT).show();
            mBackKeyPressedTimes = 1;
            new Thread() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        mBackKeyPressedTimes = 0;
                    }
                }
            }.start();
            return;
        } else {
            this.finish();
        }
        super.onBackPressed();
    }

    //start-->JPush
    //for receive customer msg from jpush server
    public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";

    private static final String TAG = "JPush";
    private static final int MSG_SET_TAGS = 1002;
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_TAGS:
                    Logger.d(TAG, "Set tags in handler.");
                    JPushInterface.setAliasAndTags(getApplicationContext(), null, (Set<String>) msg.obj, mTagsCallback);
                    break;

                default:
                    Logger.i(TAG, "Unhandled msg - " + msg.what);
            }
        }
    };
    private final TagAliasCallback mTagsCallback = new TagAliasCallback() {

        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    Logger.i(TAG, logs);
                    break;

                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    Logger.i(TAG, logs);
                    if (ExampleUtil.isConnected(getApplicationContext())) {
                        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_TAGS, tags), 1000 * 60);
                    } else {
                        Logger.i(TAG, "No network");
                    }
                    break;

                default:
                    logs = "Failed with errorCode = " + code;
                    Logger.e(TAG, logs);
            }

//            ExampleUtil.showToast(logs, getApplicationContext());
        }

    };

    /**
     * 设置标签，使用用户名作为标签
     */
    private void setTag() {
//        EditText tagEdit = (EditText) findViewById(R.id.et_tag);
//        tag = getIntent().getStringExtra("username");
        SharedPreferences sp = getSharedPreferences("user", MODE_PRIVATE);

        tag = sp.getString("username", "");

        // ","隔开的多个 转换成 Set
        String[] sArray = tag.split(",");
        Set<String> tagSet = new LinkedHashSet<String>();
        for (String sTagItme : sArray) {
            if (!ExampleUtil.isValidTagAndAlias(sTagItme)) {
                Toast.makeText(MainActivity.this, "格式不对", Toast.LENGTH_SHORT).show();
                return;
            }
            tagSet.add(sTagItme);
        }

        //调用JPush API设置Tag
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_TAGS, tagSet));

    }
//end-->JPush

    /**
     * 获取用户名
     *
     * @return
     */
    public static String getUserName() {
        return tag;
    }
}
