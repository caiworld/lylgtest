package example.caiworld.caihao.lylgapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMMessage;

import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import example.caiworld.caihao.lylgapp.utils.ImageCompressUtil;

public class PeopleDetailActivity extends AppCompatActivity {

    private Button sendMessage;
    private String userId;
    private TextView tvUserName;
    private CircleImageView ivHeader;
    private TextView tvIntroduce;
    private Button btJieDan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people_detail);
        initView();
        initData();
    }

    private void initView() {
        introduces = new String[]{"我掉钱了。。。", "我捡钱了，高兴", "热死了", "今天天气好热，吃不下饭，吃的是饺子。放的辣有点多，吃的时候热死。"};
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        tvIntroduce = (TextView) findViewById(R.id.tv_introduce);
        ivHeader = (CircleImageView) findViewById(R.id.iv_header);
        tvUserName = (TextView) findViewById(R.id.tv_username);
        btJieDan = (Button) findViewById(R.id.bt_jiedan);
        btJieDan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PeopleDetailActivity.this, HelpActivity.class);
//                intent.putExtra("xiadan", 2);//2代表只推送给某个人接单、1表示推送给所有人接单
                intent.putExtra("userId", userId);//推送给某个人
                startActivity(intent);
            }
        });
        sendMessage = (Button) findViewById(R.id.bt_sendMessage);
        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PeopleDetailActivity.this, ChatActivity.class);
                // EaseUI封装的聊天界面需要这两个参数，聊天者的username，以及聊天类型，单聊还是群聊
                intent.putExtra("userId", userId);
                intent.putExtra("chatType", EMMessage.ChatType.Chat);
                PeopleDetailActivity.this.startActivity(intent);
            }
        });
    }

    private Bitmap bitmap;
    private String[] introduces;

    private void initData() {
        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");
        int headerPic = intent.getIntExtra("headerPic", R.mipmap.icon);
        tvUserName.setText(userId);
        Random random = new Random();
        int i = random.nextInt(4);
        Log.e("随机个人简介", i + "");
        tvIntroduce.setText(introduces[i]);
        bitmap = BitmapFactory.decodeResource(getResources(), headerPic);
        bitmap = ImageCompressUtil.compressBySize(bitmap, 80, 80);
        ivHeader.setImageDrawable(new BitmapDrawable(getResources(), bitmap));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tb_people_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.jubao:
                Toast.makeText(PeopleDetailActivity.this, "已举报该用户", Toast.LENGTH_SHORT).show();
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        Log.e("PeopleDetailActivity", "onDestroy销毁了页面");
        super.onDestroy();
    }
}
