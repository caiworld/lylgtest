package example.caiworld.caihao.lylgapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhihu.matisse.Matisse;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadBatchListener;
import example.caiworld.caihao.lylgapp.adapter.PictureAdapter;
import example.caiworld.caihao.lylgapp.bean.Comments;
import example.caiworld.caihao.lylgapp.bean.Dynamic;
import example.caiworld.caihao.lylgapp.utils.ContentUriUtil;

/**
 * 发表动态
 */
public class PunishedDynamic extends AppCompatActivity {

    private TextView tvPublished;
    private TextView etContent;
    private ImageView ivChoosePic;
    private RecyclerView rvPic;
    private PictureAdapter adapter;
    private String content;
    private String[] paths;
    private Dynamic dynamic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_punished_dynamic);
        picLists = new ArrayList<>();
        pics = new ArrayList<>();
        picLists.add(ContentUriUtil.resourceIdToUri(this, R.mipmap.choose));
        init();
    }

    private List<Uri> picLists;

    private void init() {
        tvPublished = (TextView) findViewById(R.id.tv_published);
        etContent = (TextView) findViewById(R.id.et_content);
        rvPic = (RecyclerView) findViewById(R.id.rv_pic);
        GridLayoutManager glm = new GridLayoutManager(this, 3);
        rvPic.setLayoutManager(glm);

        adapter = new PictureAdapter(picLists, this);
        rvPic.setAdapter(adapter);

        tvPublished.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                haveFinished();
                finish();
            }
        });
    }

    private ArrayList<String> pics;

    private void haveFinished() {
        content = etContent.getText().toString();
        SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm");
        String date = format.format(new Date());
        Intent intent = new Intent();
        intent.putExtra("content", content);
        intent.putExtra("date", date);
        intent.putStringArrayListExtra("picList", pics);
        this.setResult(10, intent);
        saveData();
    }

    private void saveData() {
//        String string = ContentUriUtil.resourceIdToUri(this,).toString();
//        Log.e("string",string);
//        String path = ContentUriUtil.getPath(this, Uri.parse(string));
//        Log.e("这path",path);
//        File file1 = new File(ContentUriUtil.getPath(this,ContentUriUtil.resourceIdToUri(this, R.mipmap.h2)));
        File file1 = new File(getFilesDir(),"mypic.png");
        String absolutePath = file1.getAbsolutePath();
        String path1 = file1.getPath();
        Log.e("path1", path1);
        Log.e("absolutepath", absolutePath);
        Log.e("file", file1.length() + "字节");
        BmobFile bmobFile = new BmobFile(file1);
        dynamic = new Dynamic();
        SharedPreferences sp = getSharedPreferences("user", MODE_PRIVATE);
        String username = sp.getString("username", "");
        dynamic.setUsername(username);//存储姓名
        dynamic.setPicHeader(bmobFile);//存储头像
        dynamic.setContent(content);//存储内容
        paths = new String[pics.size() + 1];
        paths[0] = path1;//头像路径
        for (int i = 0; i < pics.size(); i++) {
            Log.e("Uri",picLists.get(i).toString()+";;"+pics.get(i));
//            File file2 = new File(ContentUriUtil.getPath(this, picLists.get(i)));
            File file2 = new File(ContentUriUtil.getPath(this, Uri.parse(pics.get(i))));
            String path2 = file2.getPath();
            paths[i + 1] = path2;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd HH:mm");
        String format = sdf.format(new Date());
        dynamic.setDate(format);//存储时间
        dynamic.setZan(null);//存储赞的人
        dynamic.setComments(null);//存储评论

        new Thread(new Runnable() {
            @Override
            public void run() {
                BmobFile.uploadBatch(paths, new UploadBatchListener() {
                    @Override
                    public void onSuccess(List<BmobFile> list, List<String> urls) {
                        if (urls.size() == paths.length) {
                            Log.e("上传成功", "success"+urls.size() );
                            List<BmobFile> picss = new ArrayList<>();
                            for (BmobFile bf : list) {
                                picss.add(bf);
                            }
                            dynamic.setPicHeader(list.get(0));
                            picss.remove(0);
                            dynamic.setPics(picss);
                            dynamic.save(new SaveListener<String>() {
                                @Override
                                public void done(String s, BmobException e) {
                                    if (e == null) {
                                        Log.e("添加成功", "success");
                                        Toast.makeText(PunishedDynamic.this, "添加数据成功", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Log.e("添加失败", "fail" + e.getMessage() + ";" + e.getErrorCode());
                                        Toast.makeText(PunishedDynamic.this, "添加数据失败", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }

                    @Override
                    public void onProgress(int curIndex, int curPercent, int total,
                                           int totalPercent) {
                        Log.e("上传中", "curIndex:" + curIndex + ";curPercent:" + curPercent + ";total" + total + ";totalPercent:" + totalPercent);
                    }

                    @Override
                    public void onError(int i, String s) {
                        Log.e("文件上传失败", "fail" + s);
                    }
                });
            }
        }) {
        }.start();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 10 && resultCode == RESULT_OK) {
            List<Uri> mSelected = Matisse.obtainResult(data);
            Log.e("看到图片", mSelected + "");
            for (Uri picUri : mSelected) {
                if (picLists.size() > 9) {//最多只能有9张图
                    break;
                }
                picLists.add(0, picUri);
                pics.add(picUri.toString());
                String path = picUri.getPath();
                Log.e("pd,pics.size",pics.size()+"tiao");
                Log.e("path", path + ";;;" + picUri.toString());
            }
            adapter.notifyDataSetChanged();
//            Target<GlideDrawable> into = Glide.with(this)
//                    .load(mSelected.get(0))
////                    .load("/data/data/example.caiworld.mkw/image_manager_disk_cache/223600")
//                    .into();
//            File file = new File("")
        }
    }
}
