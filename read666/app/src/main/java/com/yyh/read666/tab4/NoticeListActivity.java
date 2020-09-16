package com.yyh.read666.tab4;

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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yyh.read666.Fragment2;
import com.yyh.read666.R;
import com.yyh.read666.okhttp.HttpImplement;
import com.yyh.read666.okhttp.OkHttpUtils;
import com.yyh.read666.tab1.BookFragment;
import com.yyh.read666.tab1.BooksActivity;
import com.yyh.read666.tab2.YinpingActivity;
import com.yyh.read666.utils.DateUtil;
import com.yyh.read666.utils.SharedPreferencesUtil;
import com.yyh.read666.view.IndicatorLineUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;

public class NoticeListActivity  extends AppCompatActivity implements View.OnClickListener  {
    private Button backBtn;
    private TextView statusTitleTv;

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

        setContentView(R.layout.activiti_notice_list);
        //方式一：这句代码必须写在setContentView()方法的后面
        getSupportActionBar().hide();
        initView();

    }

    public void initView(){
        statusTitleTv=findViewById(R.id.statusTitleTv);
        statusTitleTv.setText("通知列表");
        backBtn=findViewById(R.id.back);
        backBtn.setOnClickListener(this);
        listView=findViewById(R.id.listView);
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

        refreshData();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(NoticeListActivity.this, NoticeDetailActivity.class);
                JSONObject data=(JSONObject) adapterView.getItemAtPosition(i);
                try {
                    String id=data.getString("id");
                    intent.putExtra("id",id);
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });


    }


    /**
     * 初始化(活动)列表
     */
    private void refreshData(){
        httpImplement=new HttpImplement();
        httpImplement.notice_notice(SharedPreferencesUtil.getToken(this), page+"", new OkHttpUtils.MyNetCall() {
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
                                Toast.makeText(NoticeListActivity.this,info,Toast.LENGTH_SHORT).show();
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
                convertView = mInflater.inflate(R.layout.item_notice, null);
                viewHolder=new ViewHolder();
                viewHolder.title=convertView.findViewById(R.id.title);
                viewHolder.time=convertView.findViewById(R.id.time);
                viewHolder.description=convertView.findViewById(R.id.description);
                viewHolder.author=convertView.findViewById(R.id.author);



                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            try {
                JSONObject data=list.getJSONObject(position);
                String title=data.getString("title");
                String id=data.getString("id");
                String description=data.getString("description");
                String author=data.getString("author");
                String time=data.getString("time");
                int is_read=data.getInt("is_read");

                viewHolder.time.setText(DateUtil.timeStamp2Date(time,"mm:ss"));
                viewHolder.description.setText(description);
                viewHolder.author.setText(author);
                viewHolder.title.setText(title);



            } catch (JSONException e) {
                e.printStackTrace();
            }


//            viewHolder.name.setText(lxr.getName());
            return convertView;
        }

        class ViewHolder {
            public TextView title;
            public TextView description;
            public TextView time,author;
        }


    }

    @Override
    public void onClick(View view) {
        if (view==backBtn){
            finish();
        }
    }

}
