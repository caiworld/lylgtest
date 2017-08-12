package example.caiworld.caihao.lylgapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;

import example.caiworld.caihao.lylgapp.MainActivity;
import example.caiworld.caihao.lylgapp.R;
import example.caiworld.caihao.lylgapp.base.BasePager;
import example.caiworld.caihao.lylgapp.pager.FourPager;
import example.caiworld.caihao.lylgapp.pager.HomePager;
import example.caiworld.caihao.lylgapp.pager.HomePager2;
import example.caiworld.caihao.lylgapp.pager.SFSPager;
import example.caiworld.caihao.lylgapp.pager.SecondPager2;
import example.caiworld.caihao.lylgapp.pager.ThirdPager;
import example.caiworld.caihao.lylgapp.pager.ThirdPager3;

public class ContentFragment extends Fragment {

    private MainActivity mActivity;
    private RadioGroup rGroup;
    private ViewPager viewPager;
    public ArrayList<BasePager> pagerList;
    private static HomePager homePager;
    private SecondPager2 secondPager2;
    private RadioButton rbSmart;//第三个单选
    private int id;
    private ThirdPager3 thirdPager3;
    private SFSPager sfsPager;
    private MyPagerAdapter adapter;

    /**
     * fragment被创建
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (MainActivity) getActivity();//获取到该fragment所依赖的activity
        id = mActivity.getId();
        Log.e("oncreate", "contentfragment");
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
        Log.e("oncreateview", "contentfragment");
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
        rbSmart = (RadioButton) view.findViewById(R.id.rb_smart);
        rbSmart.setText("顺风车");
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
        Log.e("onactivitycreated", "contentfragment");
        if (id == 1) {//收件人，需求方
            initData();
        } else if (id == 2) {//代购，帮买和找快递员
            initData();
        } else if (id == 3) {//快递员
            initData3();
        }
    }

    private void initData2() {
        //默认选中首页
        rGroup.check(R.id.rb_home);
        pagerList = new ArrayList<>();
        pagerList.add(new HomePager2(mActivity));//首页
        secondPager2 = new SecondPager2(mActivity);
        pagerList.add(secondPager2);//分享
        pagerList.add(new FourPager(mActivity));//通讯录

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
//                    case R.id.rb_smart:
//                        viewPager.setCurrentItem(2, false);//第二个参数，去掉去掉切换页面的动画
//                        break;
                    case R.id.rb_affair:
                        viewPager.setCurrentItem(2, false);//第二个参数，去掉去掉切换页面的动画
                        break;
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
                //TODO 因为当切换pager之后，地图变成了好多黑线（初步解决方案）
                if (position == 2) {
                    pagerList.remove(2);
                    pagerList.add(2, new ThirdPager3(mActivity));
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

    private void initData3() {
        //默认选中首页
        rGroup.check(R.id.rb_home);
        pagerList = new ArrayList<>();
        pagerList.add(new ThirdPager(mActivity));
        secondPager2 = new SecondPager2(mActivity);
        pagerList.add(secondPager2);
//        pagerList.add(new SecondPager(mActivity));
//        pagerList.add(new ThirdPager(mActivity));
        sfsPager = new SFSPager(mActivity);
        pagerList.add(sfsPager);
//        pagerList.add(new ThirdPager3(mActivity));
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
                //TODO 因为当切换pager之后，地图变成了好多黑线（初步解决方案）
                if (position == 2) {
                    pagerList.remove(2);
                    pagerList.add(2, new SFSPager(mActivity));
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

    public SecondPager2 getSecondPager2() {
        return secondPager2;
    }

    public ThirdPager3 getThirdPager3() {
        return thirdPager3;
    }

    public SFSPager getSfsPager() {
        return sfsPager;
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
        secondPager2 = new SecondPager2(mActivity);
        pagerList.add(secondPager2);
//        pagerList.add(new SecondPager(mActivity));
//        pagerList.add(new ThirdPager(mActivity));
        thirdPager3 = new ThirdPager3(mActivity);
        pagerList.add(thirdPager3);
        pagerList.add(new FourPager(mActivity));

        adapter = new MyPagerAdapter();

        viewPager.setAdapter(adapter);

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
                if (id == 1 || id == 2) {
                    if (position == 0) {
                        //TODO 因为当切换pager之后，地图变成了好多黑线（初步解决方案）
                        pagerList.remove(0);
                        pagerList.add(0, new HomePager(mActivity));
                    }
                }
                if (position == 2) {
                    pagerList.remove(2);
                    pagerList.add(2, new ThirdPager3(mActivity));
                    adapter.notifyDataSetChanged();
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
