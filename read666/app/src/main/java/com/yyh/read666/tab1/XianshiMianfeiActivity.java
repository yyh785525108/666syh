package com.yyh.read666.tab1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yyh.read666.R;
import com.yyh.read666.details.DetailControlActivity;
import com.yyh.read666.okhttp.HttpImplement;
import com.yyh.read666.okhttp.OkHttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;

public class XianshiMianfeiActivity extends AppCompatActivity implements View.OnClickListener {
    private ListView listView;
    private Button backBtn;

    private MyAdapter myAdapter;
    JSONArray allList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//remove title bar  即隐藏标题栏

        setContentView(R.layout.activity_xianshimianfei);
        //方式一：这句代码必须写在setContentView()方法的后面
        getSupportActionBar().hide();
        backBtn=findViewById(R.id.back);
        listView = findViewById(R.id.listView);



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                JSONObject data= (JSONObject) adapterView.getItemAtPosition(i);
                try {
                    String id=data.getString("id");
                    Intent intent=new Intent(XianshiMianfeiActivity.this, DetailControlActivity.class);
                    intent.putExtra("id",id);
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        backBtn.setOnClickListener(this);
        initData();
    }


    public void initData( ) {
        String data=getIntent().getStringExtra("data");
        try {
            allList = new JSONArray(data);
            myAdapter = new MyAdapter(XianshiMianfeiActivity.this, allList);
            listView.setAdapter(myAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View v) {
        if (backBtn==v){
            finish();
        }
    }

    public class MyAdapter extends BaseAdapter {

        private JSONArray list;
        private Context ctx;
        private LayoutInflater mInflater;

        public MyAdapter(Context context, JSONArray list) {
            ctx = context;
            this.list = list;
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return list.length();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            try {
                return list.getJSONObject(position);
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item_book, null);
                viewHolder = new ViewHolder();
                viewHolder.title = convertView.findViewById(R.id.title);
                viewHolder.thumb = convertView.findViewById(R.id.thumb);
                viewHolder.description = convertView.findViewById(R.id.description);
                viewHolder.view_num = convertView.findViewById(R.id.view_num);
                viewHolder.add_time = convertView.findViewById(R.id.add_time);


                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            try {
                JSONObject data = list.getJSONObject(position);
                String title = data.getString("title");
                String thumb = data.getString("thumb");
                String description = data.getString("description");
                String view_num = data.getString("view_num");
                String add_time = data.getString("add_time");

                viewHolder.title.setText(title);
                viewHolder.description.setText(description);
                viewHolder.view_num.setText(view_num);
                viewHolder.add_time.setText(add_time + "上架");
                Glide.with(XianshiMianfeiActivity.this).load(thumb).into(viewHolder.thumb);
//                viewHolder.price.setText();

            } catch (JSONException e) {
                e.printStackTrace();
            }


            return convertView;
        }

        class ViewHolder {
            public TextView title;
            public ImageView thumb;
            public TextView description;
            public TextView view_num, add_time;
        }


    }
}
