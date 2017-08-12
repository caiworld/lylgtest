package example.caiworld.caihao.lylgapp.adapter;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

import example.caiworld.caihao.lylgapp.MainActivity;
import example.caiworld.caihao.lylgapp.R;

/**
 * Created by caihao on 2017/7/29.
 */
public class PictureShareAdapter extends BaseAdapter {
    private List<String> mList;
    private MainActivity activity;

    public PictureShareAdapter(List<String> picsList, MainActivity mainActivity) {
        mList = picsList;
        activity = mainActivity;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

//        ImageView imageView;
//        if (convertView == null) {
//            imageView = new ImageView(activity);
//            imageView.setLayoutParams(new GridView.LayoutParams(75, 75));//设置ImageView对象布局 
//            imageView.setAdjustViewBounds(false);//设置边界对齐 
//            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);//设置刻度的类型 
//            imageView.setPadding(8, 8, 8, 8);//设置间距 
//        } else {
//            imageView = (ImageView) convertView;
//        }
//        imageView.setImageResource(R.mipmap.h2);
////        Glide.with(activity).load(mList.get(position)).into(imageView); //为ImageView设置图片资源 
//        return imageView;

//        ImageView imageView;
//        if (convertView == null) {
//            imageView = new ImageView(activity);
//            imageView.setLayoutParams(new GridView.LayoutParams(75, 75));//设置ImageView对象布局 
//            imageView.setAdjustViewBounds(false);//设置边界对齐 
//            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);//设置刻度的类型 
//            imageView.setPadding(8, 8, 8, 8);//设置间距 
//        } else {
//            imageView = (ImageView) convertView;
//        }
////        imageView.setImageResource(mList.get(position));//为ImageView设置图片资源 
//        imageView.setImageResource(list.get(position));
//        Log.e("为什么","why");
//        return imageView;

        convertView = View.inflate(activity, R.layout.item_rvpic, null);
        ImageView ivPic = (ImageView) convertView.findViewById(R.id.iv_Pic);
        ivPic.setTag(R.id.tv_convinient,mList.get(position));
        if (ivPic.getTag(R.id.tv_convinient) != null && ivPic.getTag(R.id.tv_convinient) == mList.get(position)) {
            Log.e("pictureAdater内",mList.size()+"长度，"+mList.get(position));
            Glide.with(activity).load(mList.get(position)).placeholder(R.mipmap.empty_photo).into(ivPic);
        }
        return convertView;
    }
}