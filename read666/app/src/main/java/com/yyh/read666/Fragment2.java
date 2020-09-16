package com.yyh.read666;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yyh.read666.okhttp.HttpImplement;
import com.yyh.read666.okhttp.OkHttpUtils;
import com.yyh.read666.tab2.YinpingActivity;
import com.yyh.read666.view.MyListViewUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;

public class Fragment2 extends Fragment {
    private ListView listView;
    private HttpImplement httpImplement;

    private int page=0;
    private  RefreshLayout refreshLayout;
    private MyAdapter myAdapter;
    JSONArray allList;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_2,container,false);
        listView=view.findViewById(R.id.listView);
        allList=new JSONArray();
        myAdapter=new MyAdapter(getActivity(),allList);
        listView.setAdapter(myAdapter);

        refreshLayout = (RefreshLayout)view.findViewById(R.id.refreshLayout);
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
                Intent intent=new Intent(getActivity(), YinpingActivity.class);
                JSONObject data=(JSONObject) adapterView.getItemAtPosition(i);

                try {
                    String id=data.getInt("id")+"";
                    intent.putExtra("id",id);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                startActivity(intent);
            }
        });


        return view;
    }

    /**
     * 初始化音频数据
     */
    private void refreshData(){
        httpImplement=new HttpImplement();
        httpImplement.learn_items_audio_get("time", page+"", new OkHttpUtils.MyNetCall() {
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
                        Activity activity=getActivity();
                        if (activity!=null){
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // 更新数据
                                    myAdapter.notifyDataSetChanged();
                                }
                            });
                        }


                    }else {
                        String info=responseJson.getString("info");
                        Activity activity=getActivity();
                        if (activity!=null){

                        }
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity(),info,Toast.LENGTH_SHORT).show();
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
                convertView = mInflater.inflate(R.layout.item_yinping, null);
                viewHolder=new ViewHolder();
                viewHolder.title=convertView.findViewById(R.id.title);
                viewHolder.thumb=convertView.findViewById(R.id.thumb);
                viewHolder.description=convertView.findViewById(R.id.description);
                viewHolder.view_num=convertView.findViewById(R.id.view_num);
                viewHolder.album_num=convertView.findViewById(R.id.album_num);
                viewHolder.price=convertView.findViewById(R.id.price);
                viewHolder.vipprice=convertView.findViewById(R.id.vipprice);


                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            try {
                JSONObject data=list.getJSONObject(position);
                String title=data.getString("title");
                String thumb=data.getString("thumb");
                String description=data.getString("description");
                String view_num=data.getString("view_num");
                String album_num=data.getString("album_num");
                String price=data.getString("price");
                String vipprice=data.getString("vipprice");
                viewHolder.title.setText(title);
                viewHolder.description.setText(description);
                viewHolder.view_num.setText(album_num+"集");
                viewHolder.album_num.setText(view_num+"播放");
                Glide.with(getActivity()).load(thumb).into(viewHolder.thumb);

                int priceInt=(int)Float.parseFloat(price);
                int vippriceInt=(int)Float.parseFloat(vipprice);
                if (priceInt==0){
                    viewHolder.price.setText("价格:免费");
                }else {
                    viewHolder.price.setText("价格:"+price+"元");
                }
                if (vippriceInt==0){
                    viewHolder.vipprice.setText("VIP:免费");

                }else {
                    viewHolder.vipprice.setText("VIP:"+vipprice+"元");

                }

//                viewHolder.price.setText();

            } catch (JSONException e) {
                e.printStackTrace();
            }


//            viewHolder.name.setText(lxr.getName());
            return convertView;
        }

        class ViewHolder {
            public TextView title;
            public ImageView thumb;
            public TextView description;
            public TextView view_num,album_num,price,vipprice;
        }


    }
}
