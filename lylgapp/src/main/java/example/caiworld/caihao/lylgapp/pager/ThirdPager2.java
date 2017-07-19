package example.caiworld.caihao.lylgapp.pager;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import example.caiworld.caihao.lylgapp.MainActivity;
import example.caiworld.caihao.lylgapp.R;
import example.caiworld.caihao.lylgapp.base.BasePager;
import example.caiworld.caihao.lylgapp.fragment.SFSFragment;
import example.caiworld.caihao.lylgapp.fragment.SSSFragment;

/**
 * Created by caihao on 2017/7/13.
 */
public class ThirdPager2 extends BasePager implements View.OnClickListener {
//    private FrameLayout fl_content;
    private TextView tv_sss;
    private TextView tv_sfs;
    private FragmentManager fragmentManager;
    private SSSFragment sssFragment;//随手送Fragment
    private SFSFragment sfsFragment;//顺风送Fragment
    private FragmentTransaction mTransaction;
    private ImageView ibtAdd2;
    private ImageView ibtAdd1;

    public ThirdPager2(Activity activity) {
        super(activity);
    }

    @Override
    public View initView() {
        ibtAdd1 = ((MainActivity) mActivity).getIbtAdd1();
        ibtAdd2 = ((MainActivity) mActivity).getIbtAdd2();
        View view = View.inflate(mActivity, R.layout.pager_third2, null);
//        fl_content = (FrameLayout) view.findViewById(R.id.fl_content);

        tv_sss = (TextView) view.findViewById(R.id.tv_sss);

        tv_sfs = (TextView) view.findViewById(R.id.tv_sfs);

        tv_sss.setOnClickListener(this);
        tv_sfs.setOnClickListener(this);
//        //获取到fragment的管理者
//
//        fragmentManager = mActivity.getFragmentManager();
//        //开启事务
//        mTransaction = fragmentManager.beginTransaction();

        sssFragment = new SSSFragment();
        sfsFragment = new SFSFragment();

        return view;
    }

    @Override
    public void initData() {
        ((MainActivity) mActivity).getTvTitle().setText("收");

        ibtAdd1.setVisibility(View.GONE);
        ibtAdd2.setVisibility(View.GONE);
        //获取到fragment的管理者

        fragmentManager = mActivity.getFragmentManager();
        //开启事务
        mTransaction = fragmentManager.beginTransaction();
        /**
         * 替换界面
         * 1 需要替换的界面的id
         * 2具体指某一个fragment的对象
         */
        mTransaction.replace(R.id.fl_content, sssFragment,"sss").commit();
    }

    @Override
    public void onClick(View v) {
        //获取到fragment的管理者
        fragmentManager = mActivity.getFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        switch (v.getId()) {
            case R.id.tv_sss:
                //随手送
//                tv_sss.setBackgroundResource(R.drawable.tab_left_pressed);
//                tv_sfs.setBackgroundResource(R.drawable.tab_right_default);
                tv_sss.setBackgroundResource(R.color.colorPrimaryDark);
                tv_sfs.setBackgroundResource(R.color.colorPrimary);

                ft.replace(R.id.fl_content, sssFragment);
                System.out.println("切换到SSSFragment");
                break;

            case R.id.tv_sfs:
                //顺风送
//                tv_sss.setBackgroundResource(R.drawable.tab_left_default);
//                tv_sfs.setBackgroundResource(R.drawable.tab_right_pressed);
                tv_sss.setBackgroundResource(R.color.colorPrimary);
                tv_sfs.setBackgroundResource(R.color.colorPrimaryDark);
                ft.replace(R.id.fl_content, sfsFragment);
                System.out.println("切换到SFSFragment");
                break;
        }
        ft.commit();
    }
}
