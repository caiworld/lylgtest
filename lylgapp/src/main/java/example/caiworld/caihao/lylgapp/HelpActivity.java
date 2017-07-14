package example.caiworld.caihao.lylgapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import example.caiworld.caihao.lylgapp.bean.SenderDetail;
import stfalcon.universalpickerdialog.UniversalPickerDialog;

public class HelpActivity extends AppCompatActivity implements UniversalPickerDialog.OnPickListener {

    private LinearLayout llGoodsDetail;//物品详情
    private LinearLayout llReceiveTime;//收货时间
    private LinearLayout llReceive;//收货
    private LinearLayout llSend;//发货
    private TextView sendAddress;//发货地址
    private TextView receiveAddress;//收货地址
    private TextView sendDetail;//发货详情（名字和电话号码）
    private TextView receiveDetail;//收货详情（名字和电话号码）
    private TextView receiveTime;
    private TextView goods;
    private TextView tvTip;
    private String sAddress;
    private String sAbsAddress;
    private String sDetailAddress;
    private String sName;
    private String sNumber;
    private double lat;
    private double lon;
    private String rAddress;
    private String rAbsAddress;
    private String rDetailAddress;
    private String rName;
    private String rNumber;
    private String goodsFind;
    private String goodsPrice;
    private String goodsWeight;
    private EditText etComment;
    private Button moreHelp;


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
        sendDetail = (TextView) findViewById(R.id.sendDetail);
        receiveDetail = (TextView) findViewById(R.id.receiveDetail);
        receiveTime = (TextView) findViewById(R.id.tv_receiveTime);
        goods = (TextView) findViewById(R.id.tv_goods);
        tvTip = (TextView) findViewById(R.id.tv_tip);
        etComment = (EditText) findViewById(R.id.et_comment);
        moreHelp = (Button) findViewById(R.id.bt_moreHelp);
    }

    private void initListener() {
        moreHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HelpActivity.this, MoreHelpActivity.class);
                startActivity(intent);
            }
        });
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
                setReceiveTime();
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

    private Integer[] days = {0, 1, 2};//天
    private Integer[] hours = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23};
    private Integer[] minutes = {0, 15, 30, 45};

    /**
     * 设置取货时间
     */
    private void setReceiveTime() {
        showDefaultPicker(
                R.string.set_receive_time,
                2,
                getFormattedCitiesInput(new UniversalPickerDialog.Input(0, days), days, 0),
                getFormattedCitiesInput(new UniversalPickerDialog.Input(1, hours), hours, 1),
                getFormattedCitiesInput(new UniversalPickerDialog.Input(0, minutes), minutes, 2)
        );
    }

    public void showDefaultPicker(@StringRes int title, int key, UniversalPickerDialog.Input... inputs) {
        new UniversalPickerDialog.Builder(this)
                .setTitle(title)
                .setListener(this)
                .setInputs(inputs)
                .setKey(key)
                .show();
    }

    private UniversalPickerDialog.Input getFormattedCitiesInput(UniversalPickerDialog.Input in, final Integer[] datas, final int type) {
        UniversalPickerDialog.Input input = in;
        input.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                if (type == 0) {
                    return datas[value] + "天";
                } else if (type == 1) {
                    return datas[value] + "小时";
                } else {
                    return datas[value] + "分钟后";
                }
            }
        });
        return input;
    }

    @Override
    public void onPick(int[] selectedValues, int key) {
        long now = System.currentTimeMillis();
        //多久后取货（毫秒）
        long free = (days[selectedValues[0]] * 24 * 60 * 60 + hours[selectedValues[1]] * 60 * 60 + minutes[selectedValues[2]] * 60) * 1000;
        SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm");
        long total = now + free;
        String date = format.format(total);
        receiveTime.setText(date);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 10 && resultCode == 10) {//SendAddressActivity
            setSendData(data);
            setTipVisible();
        } else if (requestCode == 20 && resultCode == 20) {//ReceiveAddressActivity
            setReceiveData(data);
            setTipVisible();
        } else if (requestCode == 30 && resultCode == 30) {
            setGoodsData(data);
        }

    }

    private void setTipVisible() {
        if (!TextUtils.isEmpty(sendAddress.getText().toString().trim()) &&
                !TextUtils.isEmpty(receiveAddress.getText().toString().trim())) {
            //发货地址和收获地址都不为空，显示“小费”
            tvTip.setVisibility(View.VISIBLE);
        } else {
            tvTip.setVisibility(View.GONE);
        }
    }

    private void setGoodsData(Intent data) {
        goodsFind = data.getStringExtra("goodsFind");
        goodsPrice = data.getStringExtra("goodsPrice");
        goodsWeight = data.getStringExtra("goodsWeight");
        String find = "其他";//默认的物品种类
        if (!TextUtils.isEmpty(goodsFind)) {
            find = goodsFind;
        }
        String price = 100 + "元";
        if (!TextUtils.isEmpty(goodsPrice)) {
            price = goodsPrice + "元";
        }
        goods.setText("(" + find + "/" + price + "/" + goodsWeight + ")");
    }

    /**
     * 设置收货人数据
     *
     * @param data
     */
    private void setReceiveData(Intent data) {
        rAddress = data.getStringExtra("raddress");
        rAbsAddress = data.getStringExtra("rabsAddress");
        rDetailAddress = data.getStringExtra("rdetailAddress");
        rName = data.getStringExtra("rname");
        rNumber = data.getStringExtra("rnumber");
        if (TextUtils.isEmpty(rAbsAddress)) {//收货地址为空
            receiveAddress.setHint("填写收货信息");
            receiveDetail.setVisibility(View.GONE);
        } else {
            receiveAddress.setText(rAbsAddress);
            receiveDetail.setVisibility(View.VISIBLE);
            if (TextUtils.isEmpty(rName) || TextUtils.isEmpty(rNumber)) {//姓名或电话为空
                receiveDetail.setHint("填写发货人信息");
            } else {
                receiveDetail.setText(rName + "    " + rNumber);
            }
        }
    }

    /**
     * 设置发货人数据
     */
    private void setSendData(Intent data) {
        sAddress = data.getStringExtra("saddress");
        sAbsAddress = data.getStringExtra("sabsAddress");
        sDetailAddress = data.getStringExtra("sdetailAddress");
        lat = data.getDoubleExtra("lat", 36.565413);
        lon = data.getDoubleExtra("lon",129.354125);
        sName = data.getStringExtra("sname");
        sNumber = data.getStringExtra("snumber");
        if (TextUtils.isEmpty(sAbsAddress)) {//发货地址为空
            sendAddress.setHint("填写发货信息");
            sendDetail.setVisibility(View.GONE);
        } else {
            sendAddress.setText(sAbsAddress);
            sendDetail.setVisibility(View.VISIBLE);
            if (TextUtils.isEmpty(sName) || TextUtils.isEmpty(sNumber)) {//姓名或电话为空
                sendDetail.setHint("填写发货人信息");
            } else {
                sendDetail.setText(sName + "    " + sNumber);
            }
        }
    }

    public void payment(View view) {
        //TODO 数据校验，初赛不做
        uploadData();

    }

    /**
     * <p>
     * private String username;//用户名
     * <p>
     * private String sendAddress;//发货人地址
     * private double lat;//发货人纬度
     * private double lon;//发货人经度
     * private String sendName;//发货人姓名
     * private String sendNumber;//发货人号码
     * <p>
     * private String receiveAddress;//收货人地址
     * private String receiveName;//收货人名字
     * private String receiveNumber;//收货人号码
     * <p>
     * private String pickUpTime;//取货时间
     * private String goodsDetail;//物品信息
     * private String comment;//备注
     */
    private void uploadData() {
        SenderDetail senderDetail = new SenderDetail();
        senderDetail.setUsername(MainActivity.getUserName());
        senderDetail.setSendAddress(sAddress+"_"+sAbsAddress+"_"+sDetailAddress);
        senderDetail.setLat(lat);
        senderDetail.setLon(lon);
        senderDetail.setSendName(sName);
        senderDetail.setSendNumber(sNumber);
        senderDetail.setReceiveAddress(rAddress+"_"+rAbsAddress+"_"+rDetailAddress);
        senderDetail.setReceiveName(rName);
        senderDetail.setReceiveNumber(rNumber);
        senderDetail.setPickUpTime("7-7 15:00");
        senderDetail.setGoodsDetail(goodsFind+"/"+goodsPrice+"/"+goodsWeight);
        senderDetail.setComment(etComment.getText().toString().trim());
        senderDetail.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    Toast.makeText(HelpActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(HelpActivity.this, "支付失败，余额不足", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
