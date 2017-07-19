package example.caiworld.caihao.lylgapp.pager;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import example.caiworld.caihao.lylgapp.MainActivity;
import example.caiworld.caihao.lylgapp.R;
import example.caiworld.caihao.lylgapp.base.BasePager;

/**
 * Created by caihao on 2017/7/15.
 */
public class SecondPager2 extends BasePager {
    private ViewPager mViewPager;
    private PagerTabStrip mPagerTabStrip;//viewpager的标题
    private String[] tabNames;
    private ImageView ibtAdd2;
    private ImageView ibtAdd1;
    private FriendDynamicPager friendDynamic;
    private AllDynamicPager allDynamic;
    private MyDynamicPager myDynamic;


    public SecondPager2(Activity activity) {
        super(activity);
    }

    public AllDynamicPager getAllDynamic(){
        return allDynamic;
    }

    @Override
    public View initView() {
        ibtAdd1 = ((MainActivity) mActivity).getIbtAdd1();
        ibtAdd2 = ((MainActivity) mActivity).getIbtAdd2();
        View view = View.inflate(mActivity, R.layout.pager_second2, null);
        tabNames = new String[]{"好友动态", "全部动态", "我的动态"};
        list = new ArrayList<>();
        friendDynamic = new FriendDynamicPager(mActivity);
        list.add(friendDynamic);
        allDynamic = new AllDynamicPager(mActivity);
        list.add(allDynamic);
        myDynamic = new MyDynamicPager(mActivity);
        list.add(myDynamic);
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
        ((MainActivity) mActivity).getTvTitle().setText("分享");
        ibtAdd1.setVisibility(View.VISIBLE);
        ibtAdd2.setVisibility(View.GONE);
    }

    private List<BasePager> list;

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
