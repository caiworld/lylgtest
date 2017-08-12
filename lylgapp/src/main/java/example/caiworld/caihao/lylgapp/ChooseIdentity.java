package example.caiworld.caihao.lylgapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

public class ChooseIdentity extends AppCompatActivity {

    private Button btTrue;
    private String username;
    private RadioButton rb2;
    private RadioButton rb1;
    private TextView tv1;
    private TextView tvId;
    private TextView tvNow;
    private RadioButton rb3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_identity);
//        username = getIntent().getStringExtra("username");
        initView();
        initDate();
    }

    private void initView() {
        btTrue = (Button) findViewById(R.id.bt_true);
        rb1 = (RadioButton) findViewById(R.id.rb1);
        rb2 = (RadioButton) findViewById(R.id.rb2);
        rb3 = (RadioButton) findViewById(R.id.rb3);
        tvId = (TextView) findViewById(R.id.tv_id);
        tv1 = (TextView) findViewById(R.id.tv1);
        tvNow = (TextView) findViewById(R.id.tv_now);
        rb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvId.setText("宅神自传");
                tv1.setText("我是宅神，蜗居家中，轻松实现找人代购、传递物品，还有分享动态聊天等功能");
                tvNow.setText("当前选择身份为：宅神");
                rb2.setChecked(false);
                rb3.setChecked(false);
            }
        });
        rb2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvId.setText("shopping达人自述");
                tv1.setText("我是shopping达人，乐于助人，帮助他人代购物品，只愿ta能得到最好的");
                tvNow.setText("当前选择身份为：shopping达人");
                rb1.setChecked(false);
                rb3.setChecked(false);
            }
        });
        rb3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvId.setText("快递员内心os");
                tv1.setText("我是快递员，我的里程愿与你分享，传递物品的同时也带去一些快乐，还能挣外快交好友，何乐而不为");
                tvNow.setText("当前选择身份为：快递员");
                rb1.setChecked(false);
                rb2.setChecked(false);
            }
        });

        btTrue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = getSharedPreferences("id", MODE_PRIVATE);
                SharedPreferences.Editor edit = sp.edit();
                //跳转至主界面
                Intent intent = new Intent(ChooseIdentity.this, MainActivity.class);
//                intent.putExtra("username", username);
                if (rb1.isChecked()) {
                    intent.putExtra("id", 1);
                    edit.putInt("id", 1).commit();
                } else if (rb2.isChecked()) {
                    intent.putExtra("id", 2);
                    edit.putInt("id", 2).commit();
                } else if (rb3.isChecked()) {
                    intent.putExtra("id", 3);
                    edit.putInt("id", 3).commit();
                }
                startActivity(intent);
                finish();
            }
        });
    }

    private void initDate() {

    }

}
