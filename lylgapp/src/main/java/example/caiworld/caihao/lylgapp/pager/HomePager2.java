package example.caiworld.caihao.lylgapp.pager;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import example.caiworld.caihao.lylgapp.R;
import example.caiworld.caihao.lylgapp.base.BasePager;

/**
 * Created by caihao on 2017/7/21.
 */
public class HomePager2 extends BasePager {

    private String[] tabNames;
    private List<BasePager> list;
    private ViewPager mViewPager;
    private PagerTabStrip mPagerTabStrip;

    public HomePager2(Activity activity) {
        super(activity);
    }

    @Override
    public View initView() {
        View view= View.inflate(mActivity, R.layout.pager_home2,null);
        tabNames = new String[]{"帮人购物", "一条龙", "点点送","顺风送"};
        list = new ArrayList<>();
        list.add(new ShoppingPager(mActivity));
        list.add(new ThirdPager(mActivity));
        list.add(new HomePager(mActivity));
        list.add(new ThirdPager3(mActivity));
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        mPagerTabStrip = (PagerTabStrip) view.findViewById(R.id.pager_tab_strip);
        mPagerTabStrip.setTabIndicatorColor(mActivity.getResources().getColor(R.color.colorPrimary));
        mViewPager.setAdapter(new MyFragmentPagerAdapter());
        mViewPager.setCurrentItem(1);//设置先展示全部动态
        list.get(1).initData();//初始化全部动态
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                list.get(position).initData();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        return view;
    }

    @Override
    public void initData() {
    }

    class MyFragmentPagerAdapter extends PagerAdapter {

        @Override
        public CharSequence getPageTitle(int position) {
            return tabNames[position];
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = list.get(position).view;
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
