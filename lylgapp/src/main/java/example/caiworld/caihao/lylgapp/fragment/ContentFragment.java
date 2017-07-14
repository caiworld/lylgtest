package example.caiworld.caihao.lylgapp.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import java.util.ArrayList;

import example.caiworld.caihao.lylgapp.R;
import example.caiworld.caihao.lylgapp.base.BasePager;
import example.caiworld.caihao.lylgapp.pager.FourPager;
import example.caiworld.caihao.lylgapp.pager.HomePager;
import example.caiworld.caihao.lylgapp.pager.SecondPager;
import example.caiworld.caihao.lylgapp.pager.ThirdPager2;

public class ContentFragment extends Fragment {

    private Activity mActivity;
    private RadioGroup rGroup;
    private ViewPager viewPager;
    public ArrayList<BasePager> pagerList;
    private static HomePager homePager;

    /**
     * fragment被创建
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();//获取到该fragment所依赖的activity
    }

    /**
     * 处理fragment的布局
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        return initView();
    }

    /**
     * 初始化页面布局
     *
     * @return
     */
    private View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_content, null);
        rGroup = (RadioGroup) view.findViewById(R.id.r_group);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        return view;
    }

    /**
     * 所依附的activity被创建成功时
     *
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {

        //默认选中首页
        rGroup.check(R.id.rb_home);
        pagerList = new ArrayList<>();
        homePager = new HomePager(mActivity);
        pagerList.add(homePager);
        pagerList.add(new SecondPager(mActivity));
//        pagerList.add(new ThirdPager(mActivity));
        pagerList.add(new ThirdPager2(mActivity));
        pagerList.add(new FourPager(mActivity));

        viewPager.setAdapter(new MyPagerAdapter());

        //监听radiogroup的选择事件
        rGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_home:
                        viewPager.setCurrentItem(0, false);//第二个参数，去掉去掉切换页面的动画
                        break;
                    case R.id.rb_news:
                        viewPager.setCurrentItem(1, false);//第二个参数，去掉去掉切换页面的动画
                        break;
                    case R.id.rb_smart:
                        viewPager.setCurrentItem(2, false);//第二个参数，去掉去掉切换页面的动画
                        break;
                    case R.id.rb_affair:
                        viewPager.setCurrentItem(3, false);//第二个参数，去掉去掉切换页面的动画
                        break;
//                    case R.id.rb_set:
//                        viewPager.setCurrentItem(4, false);//第二个参数，去掉去掉切换页面的动画
//                        break;
                }
            }
        });

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //页面被选中时调用
                if (position == 0) {
                    //TODO 因为当切换pager之后，地图变成了好多黑线（初步解决方案）
                    pagerList.remove(0);
                    pagerList.add(0, new HomePager(mActivity));
                }
                //调用各个页面的初始化数据的方法，这样可以防止viewpager本身的预加载
                pagerList.get(position).initData();
                Log.e("页面被选中：", position + "");
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //初始化首页数据，因为上面的onPageSelected()只有当页面被重新选中时才调用，所以需要先在此处调用首页初始化数据
        pagerList.get(0).initData();

    }

    class MyPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return pagerList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
//            return super.instantiateItem(container, position);
            container.addView(pagerList.get(position).view);
            return pagerList.get(position).view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
