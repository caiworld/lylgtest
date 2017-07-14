package example.caiworld.caihao.lylgapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.baidu.mapapi.map.MapView;

public class MoreHelpActivity extends AppCompatActivity {

    private ListView lvFuhe;
    private MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_help);
        initView();
    }

    private void initView() {
        lvFuhe = (ListView) findViewById(R.id.lv_fuhe);
        lvFuhe.setAdapter(new MyAdapter());
        mapView = (MapView) findViewById(R.id.mapView);
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return null;
        }
    }
}
