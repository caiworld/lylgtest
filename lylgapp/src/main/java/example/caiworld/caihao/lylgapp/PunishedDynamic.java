package example.caiworld.caihao.lylgapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PunishedDynamic extends AppCompatActivity {

    private TextView tvPublished;
    private TextView etContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_punished_dynamic);
        init();
    }

    private void init() {
        tvPublished = (TextView) findViewById(R.id.tv_published);
        etContent = (TextView) findViewById(R.id.et_content);
        tvPublished.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                haveFinished();
                finish();
            }
        });
    }

    private void haveFinished() {
        String content = etContent.getText().toString();
        SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm");
        String date = format.format(new Date());
        Intent intent = new Intent();
        intent.putExtra("content", content);
        intent.putExtra("date", date);
        this.setResult(10, intent);
    }
}
