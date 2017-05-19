package example.caiworld.caihao.lylgapp.base;

import android.app.Activity;
import android.view.View;

/**
 * Created by caihao on 2017/5/18.
 */
public abstract class BasePager {
    public Activity mActivity;
    public View view;

    public BasePager(Activity activity) {
        mActivity = activity;
        view = initView();
    }

    /**
     * 由于每个页面的布局都不一样，所以需要其子类自己去实现
     *
     * @return 页面布局的view
     */
    public abstract View initView();

    /**
     * 初始化首页的数据,切换viewpager时调用
     */
    public void initData() {
    }
}
