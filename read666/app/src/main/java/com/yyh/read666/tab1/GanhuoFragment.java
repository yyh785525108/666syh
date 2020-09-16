package com.yyh.read666.tab1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yyh.read666.R;
import com.yyh.read666.WebActivity;
import com.yyh.read666.details.DetailControlActivity;
import com.yyh.read666.okhttp.HttpImplement;
import com.yyh.read666.okhttp.HttpInterface;
import com.yyh.read666.okhttp.OkHttpUtils;
import com.yyh.read666.okhttp.UrlConstant;
import com.yyh.read666.utils.SharedPreferencesUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;

/**
 * 必读干货
 */
public class GanhuoFragment extends Fragment {
    private ListView listView;
    private HttpImplement httpImplement;
    private Activity mActivity;
    public String id;

    private int page = 0;
    private RefreshLayout refreshLayout;
    private MyAdapter myAdapter;
    JSONArray allList;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listview, container, false);
        listView = view.findViewById(R.id.listView);
        allList = new JSONArray();
        myAdapter = new MyAdapter(getActivity(), allList);
        listView.setAdapter(myAdapter);
        refreshData(id);
        refreshLayout = (RefreshLayout) view.findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                page = 0;
                refreshData(id);
                refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                page = page + 1;
                refreshData(id);
                refreshlayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                JSONObject data= (JSONObject) adapterView.getItemAtPosition(i);
                try {
                    String id=data.getString("id");
                    Intent intent=new Intent(getActivity(), WebActivity.class);
                    intent.putExtra("url", UrlConstant.GANHUO_DETAIL_URL+"?id="+id);
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
        System.out.println("----onAttach----");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivity = null;
        System.out.println("----onDetach----");

    }

    public void refreshData(String id) {
        this.id = id;
        httpImplement = new HttpImplement();
        String m="news";
        //底下listview
        httpImplement.citiao_citiao(SharedPreferencesUtil.getToken(getActivity()),m,id+"", page+"",new OkHttpUtils.MyNetCall() {            @Override
            public void success(Call call, String response) throws IOException {
                try {
                    JSONObject responseJson = new JSONObject(response);
                    int status = responseJson.getInt("status");
                    if (status == 1) {

                        JSONArray list = responseJson.getJSONArray("data");
                        if (mActivity!=null){
                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        if (page == 0) { //下拉刷新
                                            //清空数据再重新加载
                                            for (int i = 0; i < allList.length(); i++) {
                                                allList.remove(i);
                                                i--;
                                            }
                                            for (int i = 0; i < list.length(); i++) {
                                                allList.put(list.getJSONObject(i));
                                            }
                                        } else {//上拉加载刷新
                                            for (int i = 0; i < list.length(); i++) {
                                                allList.put(list.getJSONObject(i));
                                            }
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    // 更新数据
                                    myAdapter.notifyDataSetChanged();
                                    refreshLayout.finishRefresh();
                                    refreshLayout.finishLoadMore();

                                }
                            });

                        }

                    } else {
                        String info = responseJson.getString("info");
                        if (mActivity!=null){
                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getActivity(), info, Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
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
                convertView = mInflater.inflate(R.layout.item_ganhuo, null);
                viewHolder = new ViewHolder();
                viewHolder.nameTv = convertView.findViewById(R.id.nameTv);
                viewHolder.thumb = convertView.findViewById(R.id.thumb);
                viewHolder.desTv = convertView.findViewById(R.id.desTv);


                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            try {
                JSONObject data = list.getJSONObject(position);
                String title = data.getString("title");
                String thumb = data.getString("thumb");
                String description = data.getString("description");
                String play_num = data.getString("play_num");
                String collect_num = data.getString("collect_num");

                viewHolder.nameTv.setText(title);
                viewHolder.desTv.setText(play_num+"人阅读  "+collect_num+"人收藏");
                Glide.with(getActivity()).load(thumb).into(viewHolder.thumb);
//                viewHolder.price.setText();

            } catch (JSONException e) {
                e.printStackTrace();
            }


            return convertView;
        }

        class ViewHolder {
            public TextView nameTv;
            public ImageView thumb;
            public TextView desTv;
        }


    }
}
