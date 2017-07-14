package example.caiworld.caihao.lylgapp;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

import java.util.LinkedHashSet;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import example.caiworld.caihao.lylgapp.fragment.ContentFragment;
import example.caiworld.caihao.lylgapp.jpush.ExampleUtil;
import example.caiworld.caihao.lylgapp.jpush.Logger;
import example.caiworld.caihao.lylgapp.pager.HomePager;

public class MainActivity extends AppCompatActivity {
//TODO 右上角加号图标设置
    private FrameLayout flMain;
    private static final String CONTENT_FRAGMENT = "fragment_content";
    private ContentFragment contentFragment;
    public static boolean isForeground = false;
    private static String tag;
    private TextView tvTitle;
    private DrawerLayout dl;
    private ImageView ibtAdd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        init();
        initFragment();
    }

    private void initView() {
        tvTitle = (TextView) findViewById(R.id.tv_title);
        ibtAdd = (ImageView) findViewById(R.id.ibt_add);
        dl = (DrawerLayout) findViewById(R.id.dl);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);//显示HomeAsUp这个按钮
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                switch (itemId) {
                    case R.id.nav_quit:
                        dl.closeDrawers();
                        break;
                }
                Toast.makeText(MainActivity.this, "点击了", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                dl.openDrawer(GravityCompat.START);
                break;
        }
        return true;
    }

    public TextView getTvTitle() {
        return tvTitle;
    }

    public ImageView getIbtAdd(){
        return ibtAdd;
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
        ((HomePager) contentFragment.pagerList.get(0)).myOnResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        isForeground = false;
        ((HomePager) contentFragment.pagerList.get(0)).myOnPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //主页面销毁时退出登录
        signOut();
        //停止LocationClient，即停止获取位置信息
        ((HomePager) contentFragment.pagerList.get(0)).stop();
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


    //    @Override
//    public void onBackPressed() {
//        //使用 Back 键返回桌面，但不关闭当前应用，而是使之进入后台，就像按下 Home 键一样
//        Intent launcherIntent = new Intent(Intent.ACTION_MAIN);
//        launcherIntent.addCategory(Intent.CATEGORY_HOME);
//        startActivity(launcherIntent);
//
//    }

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
        tag = getIntent().getStringExtra("username");

//        // 检查 tag 的有效性
//        if (TextUtils.isEmpty(tag)) {
//            Toast.makeText(MainActivity.this, "Tag不能为空", Toast.LENGTH_SHORT).show();
//            return;
//        }

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
