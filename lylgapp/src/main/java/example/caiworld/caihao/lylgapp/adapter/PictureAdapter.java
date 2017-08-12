package example.caiworld.caihao.lylgapp.adapter;

import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.util.List;

import example.caiworld.caihao.lylgapp.PunishedDynamic;
import example.caiworld.caihao.lylgapp.R;

/**
 * Created by caihao on 2017/7/29.
 */
public class PictureAdapter extends RecyclerView.Adapter<PictureAdapter.ViewHolder> {

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_rvpic, null);
        final ViewHolder holder = new ViewHolder(view);
        holder.ivPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                if (position == mPicLists.size() - 1) {//点击的添加图片的那张图片
                    //进入到知乎图片选择器页面
                    Matisse.from(mActivity)
                            .choose(MimeType.allOf())
                            .countable(true)
                            .capture(true)
                            .captureStrategy(
                                    new CaptureStrategy(true, "example.caiworld.mkw.fileprovider"))
                            .maxSelectable(9)
                            .gridExpectedSize(
                                    mActivity.getResources().getDimensionPixelSize(R.dimen.photo1))
                            .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                            .thumbnailScale(0.85f)
                            .imageEngine(new GlideEngine())
                            .forResult(10);
                }
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.ivPic.setImageURI(mPicLists.get(position));
    }

    @Override
    public int getItemCount() {
        return mPicLists.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivPic;

        public ViewHolder(View itemView) {
            super(itemView);
            ivPic = (ImageView) itemView.findViewById(R.id.iv_Pic);
        }
    }

    private List<Uri> mPicLists;
    private PunishedDynamic mActivity;

    public PictureAdapter(List<Uri> picLists, PunishedDynamic activity) {
        mPicLists = picLists;
        mActivity = activity;
    }

}
