package example.caiworld.caihao.lylgapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;

public class SplashActivity extends AppCompatActivity {

    private String username;
    private String password;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {//登录成功
                // 加载所有会话到内存
                EMClient.getInstance().chatManager().loadAllConversations();
                // 加载所有群组到内存，如果使用了群组的话
                // EMClient.getInstance().groupManager().loadAllGroups();
                // 登录成功跳转至选择身份界面
                Intent intent = new Intent(SplashActivity.this, ChooseIdentity.class);
//                        intent.putExtra("username", username);
                startActivity(intent);
                finish();
            } else if (msg.what == 2) {//登录失败，跳转至登录界面
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initTouMing();
        initView();
    }

    private void initTouMing() {
        //透明状态栏
        if (Build.VERSION.SDK_INT >= 21) {//版本号大于等于5.0
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
            //设置系统UI元素的可见性
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            getWindow().setNavigationBarColor(Color.TRANSPARENT);
        }
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.hide();
    }

    private void initView() {
        SharedPreferences sp = getSharedPreferences("user", MODE_PRIVATE);
        username = sp.getString("username", "");
        password = sp.getString("password", "");
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {//用户名和密码其一为空
                    SystemClock.sleep(2500);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                } else {//两者都不为空，帮助用户登录
                    login();
                }
            }
        }).start();
    }

    private void login() {
        final long start = System.currentTimeMillis();
        EMClient.getInstance().login(username, password, new EMCallBack() {
            @Override
            public void onSuccess() {
                //登录成功的回调
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        // 加载所有会话到内存
//                        EMClient.getInstance().chatManager().loadAllConversations();
//                        // 加载所有群组到内存，如果使用了群组的话
//                        // EMClient.getInstance().groupManager().loadAllGroups();
//                        //TODO 做本地化存储，将username和password保存到本地，下次直接进入主页面不用重新登录
//                        // 登录成功跳转至选择身份界面
//                        Intent intent = new Intent(SplashActivity.this, ChooseIdentity.class);
////                        intent.putExtra("username", username);
//                        startActivity(intent);
//                        finish();
//                    }
//                });
                long end = System.currentTimeMillis();
                long l = end - start;
                if (2500 - l >= 0) {
                    handler.sendEmptyMessageDelayed(1, 2500 - l);
                } else {
                    handler.sendEmptyMessage(1);
                }
            }

            @Override
            public void onError(final int i, final String s) {
//                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
//                startActivity(intent);
//                finish();
                long end = System.currentTimeMillis();
                long l = end - start;
                if (2500 - l >= 0) {
                    handler.sendEmptyMessageDelayed(2, 2500 - l);
                } else {
                    handler.sendEmptyMessage(2);
                }
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }
    //真正的沉浸式模式
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            );
        }

    }
}
