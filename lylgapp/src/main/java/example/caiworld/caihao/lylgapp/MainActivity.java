package example.caiworld.caihao.lylgapp;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

import example.caiworld.caihao.lylgapp.fragment.ContentFragment;
import example.caiworld.caihao.lylgapp.pager.HomePager;

public class MainActivity extends AppCompatActivity {

    private FrameLayout flMain;
    private static final String CONTENT_FRAGMENT = "fragment_content";
    private ContentFragment contentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initFragment();
    }

    private void initView() {

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
        ((HomePager)contentFragment.pagerList.get(0)).myOnResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ((HomePager)contentFragment.pagerList.get(0)).myOnPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //主页面销毁时退出登录
        signOut();
        //停止LocationClient，即停止获取位置信息
        ((HomePager)contentFragment.pagerList.get(0)).stop();
        Log.e("MainActivity","主页面销毁");
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

//    @Override
//    public void onBackPressed() {
//        //使用 Back 键返回桌面，但不关闭当前应用，而是使之进入后台，就像按下 Home 键一样
//        Intent launcherIntent = new Intent(Intent.ACTION_MAIN);
//        launcherIntent.addCategory(Intent.CATEGORY_HOME);
//        startActivity(launcherIntent);
//
//    }
}
