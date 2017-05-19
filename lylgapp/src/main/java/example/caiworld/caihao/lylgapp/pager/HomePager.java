package example.caiworld.caihao.lylgapp.pager;

import android.app.Activity;
import android.view.View;

import example.caiworld.caihao.lylgapp.R;
import example.caiworld.caihao.lylgapp.base.BasePager;

/**
 * Created by caihao on 2017/5/18.
 */
public class HomePager extends BasePager {

    public HomePager(Activity activity) {
        super(activity);
    }

    @Override
    public View initView() {
        return View.inflate(mActivity, R.layout.pager_home, null);
    }

    /**
     * 初始化首页的数据,切换viewpager时调用
     */
    @Override
    public void initData() {
    }


}
