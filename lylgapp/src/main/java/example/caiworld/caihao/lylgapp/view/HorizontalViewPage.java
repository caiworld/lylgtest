package example.caiworld.caihao.lylgapp.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by caihao on 2017/7/15.
 */
public class HorizontalViewPage extends ViewPager {
    public HorizontalViewPage(Context context) {
        super(context);
    }

    public HorizontalViewPage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
            getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(ev);
    }
}
