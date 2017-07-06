package example.caiworld.caihao.lylgapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class HelpActivity extends AppCompatActivity {

    private LinearLayout llGoodsDetail;//物品详情
    private LinearLayout llReceiveTime;//收货时间
    private LinearLayout llReceive;//收货
    private LinearLayout llSend;//发货
    private TextView sendAddress;//发货地址
    private TextView receiveAddress;//收货地址
    private TextView sendHint;//发货提示
    private TextView sendDetail;//发货详情（名字和电话号码）
    private TextView receiveHint;//收货提示
    private TextView receiveDetail;//收货详情（名字和电话号码）

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        initView();
        initListener();
    }

    private void initView() {
        llSend = (LinearLayout) findViewById(R.id.ll_send);
        llReceive = (LinearLayout) findViewById(R.id.ll_receive);
        llReceiveTime = (LinearLayout) findViewById(R.id.ll_receive_time);
        llGoodsDetail = (LinearLayout) findViewById(R.id.ll_goods_detail);
        sendAddress = (TextView) findViewById(R.id.sendAddress);
        receiveAddress = (TextView) findViewById(R.id.receiveAddress);
        sendHint = (TextView) findViewById(R.id.sendHint);
        sendDetail = (TextView) findViewById(R.id.sendDetail);
        receiveHint = (TextView) findViewById(R.id.receiveHint);
        receiveDetail = (TextView) findViewById(R.id.receiveDetail);
    }

    private void initListener() {
        llSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HelpActivity.this, SendAddressActivity.class);
                startActivityForResult(intent, 10);
            }
        });

        llReceive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HelpActivity.this, ReceiveAddressActivity.class);
                startActivityForResult(intent, 20);
            }
        });

        llReceiveTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HelpActivity.this, "立即取货", Toast.LENGTH_SHORT).show();
            }
        });

        llGoodsDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HelpActivity.this, GoodsDetailActivity.class);
                startActivityForResult(intent, 30);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    }

    public void payment(View view) {
        Toast.makeText(this, "余额不足", Toast.LENGTH_SHORT).show();
    }
}
