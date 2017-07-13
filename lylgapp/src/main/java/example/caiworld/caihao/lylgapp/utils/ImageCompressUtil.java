package example.caiworld.caihao.lylgapp.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

/**
 * 压缩图片工具类
 * Created by caihao on 2017/7/11.
 */
public class ImageCompressUtil {
    /**
     * 通过压缩图片的尺寸来压缩图片大小 
     *
     * @param bitmap       要压缩图片 
     * @param targetWidth  缩放的目标宽度 
     * @param targetHeight 缩放的目标高度 
     * @return 缩放后的图片 
     */
    public static Bitmap compressBySize(Bitmap bitmap, int targetWidth,
                                        int targetHeight) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        bitmap = BitmapFactory.decodeByteArray(baos.toByteArray(), 0, baos.toByteArray().length, opts);
        // 得到图片的宽度、高度；  
        int imgWidth = opts.outWidth;
        int imgHeight = opts.outHeight;
        // 分别计算图片宽度、高度与目标宽度、高度的比例；取大于该比例的最小整数；  
        int widthRatio = (int) Math.ceil(imgWidth / (float) targetWidth);
        int heightRatio = (int) Math.ceil(imgHeight / (float) targetHeight);
        if (widthRatio > 1 && widthRatio > 1) {
            if (widthRatio > heightRatio) {
                opts.inSampleSize = widthRatio;
            } else {
                opts.inSampleSize = heightRatio;
            }
        }
        // 设置好缩放比例后，加载图片进内存；  
        opts.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeByteArray(baos.toByteArray(), 0, baos.toByteArray().length, opts);
        return bitmap;
    }
}
