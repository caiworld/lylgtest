package example.caiworld.caihao.lylgapp;

import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

public class SplashActivity extends AppCompatActivity {

    private ImageView ivSplash;
    private AnimationSet as;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initView();
        startLogoAnimation();
    }

    private void initView() {
        ivSplash = (ImageView) findViewById(R.id.iv_splash);
    }

    private void startLogoAnimation() {
        as = new AnimationSet(this, null);
        //旋转动画
        RotateAnimation rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(1500);
        rotateAnimation.setFillAfter(true);//旋转完成后保持位置不要动，默认是false
        as.addAnimation(rotateAnimation);
        //缩放动画
        ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(1500);
        scaleAnimation.setFillAfter(true);
        as.addAnimation(scaleAnimation);
        //透明动画
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(1500);
        alphaAnimation.setFillAfter(true);
        as.addAnimation(alphaAnimation);

        initListener();

        ivSplash.startAnimation(as);
    }

    /**
     * 动画监听器
     */
    private void initListener() {
        as.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //当动画结束时跳到登录页面
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }
}
