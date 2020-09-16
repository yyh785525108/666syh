package com.yyh.read666.tab1;

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

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yyh.read666.Fragment2;
import com.yyh.read666.R;
import com.yyh.read666.details.DetailControlActivity;
import com.yyh.read666.okhttp.HttpImplement;
import com.yyh.read666.okhttp.OkHttpUtils;
import com.yyh.read666.utils.DateUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;

public class VideoActivity extends AppCompatActivity implements View.OnClickListener {

    private Button backBtn;
    private ListView listView;

    private HttpImplement httpImplement;


    private int page=0;
    private RefreshLayout refreshLayout;
    private MyAdapter myAdapter;
    JSONArray allList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//remove title bar  即隐藏标题栏

        setContentView(R.layout.activity_videos);
        //方式一：这句代码必须写在setContentView()方法的后面
        getSupportActionBar().hide();
        initView();
        refreshData();

    }

    public void initView(){
        listView=findViewById(R.id.listView);
        backBtn=findViewById(R.id.back);
        backBtn.setOnClickListener(this);
        allList=new JSONArray();
        myAdapter=new MyAdapter(this,allList);
        listView.setAdapter(myAdapter);

        refreshLayout = (RefreshLayout)findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                page=0;
                refreshData();
                refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                page=page+1;
                refreshData();
                refreshlayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long tid) {
                    JSONObject data=(JSONObject) parent.getItemAtPosition(position);
                String id= null;
                try {
                    id = data.getString("id");
                    Intent intent=new Intent(VideoActivity.this, DetailControlActivity.class);
                    intent.putExtra("id", id);
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        });

    }

    private void refreshData(){
        httpImplement=new HttpImplement();
        httpImplement.learn_items_video_get("","time",page+"", new OkHttpUtils.MyNetCall() {
            @Override
            public void success(Call call, String response) throws IOException {
                   try {
                    JSONObject responseJson=new JSONObject(response);
                    int status=responseJson.getInt("status");
                    if (status==1){
                        JSONObject data=responseJson.getJSONObject("data");
                        JSONArray list=data.getJSONArray("list");
                        if (page==0){//下拉刷新
                            //清空数据再重新加载
                            for (int i = 0; i < allList.length(); i++) {
                                allList.remove(i);
                                i--;
                            }
                            for (int i=0;i<list.length();i++){
                                allList.put(list.getJSONObject(i));
                            }
                            refreshLayout.finishRefresh();

                        }else {//上拉加载刷新
                            for (int i=0;i<list.length();i++){
                                allList.put(list.getJSONObject(i));
                            }
                            refreshLayout.finishLoadMore();
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // 更新数据
                                myAdapter.notifyDataSetChanged();
                            }
                        });

                    }else {
                        String info=responseJson.getString("info");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(VideoActivity.this,info,Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(Call call, IOException e) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view==backBtn){
            finish();
        }
    }



    public class MyAdapter extends BaseAdapter{

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
                viewHolder=new ViewHolder();
                viewHolder.title=convertView.findViewById(R.id.title);
                viewHolder.thumb=convertView.findViewById(R.id.thumb);
                viewHolder.description=convertView.findViewById(R.id.description);
                viewHolder.view_num=convertView.findViewById(R.id.view_num);
                viewHolder.add_time=convertView.findViewById(R.id.add_time);



                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            try {
                JSONObject data=list.getJSONObject(position);
                String id=data.getString("id");

                String title=data.getString("title");
                String thumb=data.getString("thumb");
                String description=data.getString("description");
                String view_num=data.getString("view_num");
                String add_time=data.getString("add_time");

                viewHolder.title.setText(title);
                viewHolder.description.setText(description);
                viewHolder.view_num.setText(view_num);
                viewHolder.add_time.setText(DateUtil.timeStamp2Date(add_time,"yyyy-MM-dd")+"上架");

                Glide.with(VideoActivity.this).load(thumb).into(viewHolder.thumb);
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
            public TextView view_num,add_time;
        }


    }
}

