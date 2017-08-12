package example.caiworld.caihao.lylgapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ShareAddressActivity extends AppCompatActivity {

    private SharedPreferences sp;
    private int id;
    private TextView tvFinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_address);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        sp = getSharedPreferences("id", MODE_PRIVATE);
        initView();
        initData();
    }

    private void initView() {
        TextView tvTitle = (TextView) findViewById(R.id.tv_title);
        tvFinish = (TextView) findViewById(R.id.tv_finish);
        tvFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (id == 1) {
//resultcode =11
                    Intent intent = new Intent();
                    ShareAddressActivity.this.setResult(11, intent);
                    finish();
                } else if (id == 2) {
                    finish();
                } else if (id == 3) {
                    Intent intent = new Intent();
                    ShareAddressActivity.this.setResult(12, intent);
                    finish();
                }
            }
        });
        id = sp.getInt("id", 1);
        if (id == 1) {
            tvTitle.setText("匹配快递员路线");
        } else if (id == 2) {
            tvTitle.setText("匹配快递员路线");
        } else if (id == 3) {
            tvTitle.setText("今日路线");
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initData() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
}
