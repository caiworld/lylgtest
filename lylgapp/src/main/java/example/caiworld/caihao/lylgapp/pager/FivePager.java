package example.caiworld.caihao.lylgapp.pager;

import android.app.Activity;
import android.view.View;

import example.caiworld.caihao.lylgapp.R;
import example.caiworld.caihao.lylgapp.base.BasePager;

/**
 * Created by caihao on 2017/5/18.
 */
public class FivePager extends BasePager {
    public FivePager(Activity activity) {
        super(activity);
    }

    @Override
    public View initView() {
        return View.inflate(mActivity, R.layout.pager_five, null);
    }

    @Override
    public void initData() {

    }
}
