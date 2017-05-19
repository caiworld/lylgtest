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

public class MainActivity extends AppCompatActivity {

    private FrameLayout flMain;
    private static final String CONTENT_FRAGMENT = "fragment_content";

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
        transaction.replace(R.id.fl_main, new ContentFragment(), CONTENT_FRAGMENT);
        transaction.commit();//提交事物

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //主页面销毁时退出登录
        signOut();
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
}
