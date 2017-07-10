package example.caiworld.caihao.lylgapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import stfalcon.universalpickerdialog.UniversalPickerDialog;

public class GoodsDetailActivity extends AppCompatActivity implements UniversalPickerDialog.OnPickListener {

    private EditText goodsPrice;
    private LinearLayout goodsWeight;
    private EditText goodsFind;
    private TextView weight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_detail);
        initView();
        init();
        initListener();
        initData();
    }

    private String[] weights = new String[100];

    private void init() {
        for (int i = 0; i < 100; i++) {
            weights[i] = (i + 1) + "kg";
        }
    }


    private void initView() {
        goodsFind = (EditText) findViewById(R.id.et_goodsFind);
        goodsWeight = (LinearLayout) findViewById(R.id.ll_goodsWeight);
        goodsPrice = (EditText) findViewById(R.id.et_goodsPrice);
        weight = (TextView) findViewById(R.id.tv_weight);
    }

    private void initListener() {
        goodsWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setGoodsWeight();
            }
        });
    }

    private void setGoodsWeight() {
        showDefaultPicker(
                R.string.set_goods_weight,
                1,
                new UniversalPickerDialog.Input(4,weights));//默认选中5kg
    }

    private void showDefaultPicker(@StringRes int title, int key, UniversalPickerDialog.Input... inputs) {
        new UniversalPickerDialog.Builder(this)
                .setTitle(title)
                .setListener(this)
                .setInputs(inputs)
                .setKey(key)
                .show();
    }


    @Override
    public void onPick(int[] selectedValues, int key) {
        weight.setText(weights[selectedValues[0]]);
    }

    private void initData() {

    }

    @Override
    public void finish() {
        haveFinished();
        super.finish();
    }

    private void haveFinished() {
        Intent intent = new Intent();
        intent.putExtra("goodsFind",goodsFind.getText().toString().trim());
        intent.putExtra("goodsWeight",weight.getText().toString().trim());
        intent.putExtra("goodsPrice",goodsPrice.getText().toString().trim());
        this.setResult(30,intent);
    }
}
