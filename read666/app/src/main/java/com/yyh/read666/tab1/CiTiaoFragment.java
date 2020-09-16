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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.yyh.read666.R;
import com.yyh.read666.details.DetailControlActivity;
import com.yyh.read666.okhttp.HttpImplement;
import com.yyh.read666.okhttp.HttpInterface;
import com.yyh.read666.okhttp.OkHttpUtils;
import com.yyh.read666.utils.SharedPreferencesUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;

public class CiTiaoFragment extends Fragment implements View.OnClickListener {

    private ExpandableListView exListView1;
    private ListView listView2;
    private ImageView headImg;
    private TextView tipsTv;
    private  TextView descriptionTv;

    public int type;
    public String id;

    private MyAdapter1 myAdapter1;

    private int selectGroupPosition,selectChildPosiiton;
    private JSONArray citiaoData;
    private JSONObject citiaoItemData;

    private ExlistViewOnChildClickListener exlistViewOnChildClickListener;

    private RelativeLayout citiaoLay;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_citiao,container,false);
        headImg=view.findViewById(R.id.headimg);
        tipsTv=view.findViewById(R.id.tipsTv);
        descriptionTv=view.findViewById(R.id.descriptionTv);
        citiaoLay=view.findViewById(R.id.citiaoLay);
        citiaoLay.setOnClickListener(this);

        exListView1=view.findViewById(R.id.exListView1);
        listView2=view.findViewById(R.id.listView2);
        exListView1.setDivider(null);
        initExListView1();
//        initListView2(id+"");
        exlistViewOnChildClickListener=new ExlistViewOnChildClickListener();
        exListView1.setOnChildClickListener(exlistViewOnChildClickListener);
        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long tId) {
                JSONObject value=(JSONObject) parent.getItemAtPosition(position);
                String id= null;
                try {
                    id = value.getString("id");
                    Intent intent=new Intent(getActivity(), DetailControlActivity.class);
                    intent.putExtra("id", id);
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


        return view;
    }

    @Override
    public void onClick(View v) {
        if (v==citiaoLay){
            if (citiaoItemData!=null){
                Intent intent=new Intent(getActivity(),CiTiaoDetailActivity.class);
                intent.putExtra("citiaoItemData",citiaoItemData.toString());
                startActivity(intent);
            }

        }
    }

    private class ExlistViewOnChildClickListener implements ExpandableListView.OnChildClickListener{

        @Override
        public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long tid) {
            JSONObject valueJson = (JSONObject) myAdapter1.getChild(groupPosition,childPosition);
            selectGroupPosition=groupPosition;
            selectChildPosiiton=childPosition;
            myAdapter1.notifyDataSetInvalidated();

            try {
                String id = valueJson.getString("id");
                String thumb = valueJson.getString("thumb");
                Activity activity=getActivity();
                if (activity!=null){
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Glide.with(getActivity()).load(thumb).into(headImg);
                            initListView2(id);
                        }
                    });
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

            return false;
        }
    }

    private void initExListView1(){
        HttpInterface httpInterface=new HttpImplement();
        httpInterface.citiao_cate(SharedPreferencesUtil.getToken(getActivity()),type+"", new OkHttpUtils.MyNetCall() {
            @Override
            public void success(Call call, String response) throws IOException {
                try {
                    JSONObject responseJson = new JSONObject(response);
                    int status = responseJson.getInt("status");
                    if (status == 1) {
                         citiaoData=responseJson.getJSONArray("data");
                         myAdapter1=new MyAdapter1(getActivity(),citiaoData);
                        Activity activity=getActivity();
                        if (activity!=null){
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    exListView1.setAdapter(myAdapter1);
                                    //默认展开
                                    for (int i=0; i<citiaoData.length(); i++) {
                                        exListView1.expandGroup(i);
                                    }
                                }
                            });
                            //初始化右边

                            for (int i=0;i<citiaoData.length();i++){
                                JSONObject value=citiaoData.getJSONObject(i);
                                String id=value.getString("id");
                                if (CiTiaoFragment.this.id==null||CiTiaoFragment.this.id.equals("")){
                                        JSONArray list=value.getJSONArray("list");
                                        if (list.length()!=0){
                                            exlistViewOnChildClickListener.onChildClick(exListView1,null,i,0,0);
                                            break;
                                        }

                                }else {
                                    if (CiTiaoFragment.this.id.equals(id)){
                                        JSONArray list=value.getJSONArray("list");
                                        if (list.length()!=0){
                                            exlistViewOnChildClickListener.onChildClick(exListView1,null,i,0,0);
                                            break;
                                        }
                                    }
                                }

                            }
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
    private void initListView2(String id){
        HttpInterface httpInterface=new HttpImplement();
        httpInterface.citiao_citiao(SharedPreferencesUtil.getToken(getActivity()),id+"", new OkHttpUtils.MyNetCall() {
            @Override
            public void success(Call call, String response) throws IOException {
                try {
                    JSONObject responseJson = new JSONObject(response);
                    int status = responseJson.getInt("status");
                    if (status == 1) {
                        citiaoItemData=responseJson.getJSONObject("data");
                try {
                    String id = citiaoItemData.getString("id");
                    String title = citiaoItemData.getString("title");
                    String description = citiaoItemData.getString("description");
                    String sub_num = citiaoItemData.getString("sub_num");
                    String num = citiaoItemData.getString("num");
                    String is_sub = citiaoItemData.getString("is_sub");
                    Activity activity=getActivity();
                    if (activity!=null){
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tipsTv.setText(title);
                                descriptionTv.setText(sub_num+"人关注  "+num+"条内容" );
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
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
        String m="book";

        //底下listview
        httpInterface.citiao_citiao(SharedPreferencesUtil.getToken(getActivity()),m,id+"", "0",new OkHttpUtils.MyNetCall() {
            @Override
            public void success(Call call, String response) throws IOException {
                try {
                    JSONObject responseJson = new JSONObject(response);
                    int status = responseJson.getInt("status");
                    if (status == 1) {
                        JSONArray data=responseJson.getJSONArray("data");

                            Activity activity=getActivity();
                            if (activity!=null){
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        MyAdapter2 myAdapter2=new MyAdapter2(getActivity(),data);
                                        listView2.setAdapter(myAdapter2);
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


    public class MyAdapter1 extends BaseExpandableListAdapter {
        private Context mContext;
        private JSONArray data;
        private final LayoutInflater mInflater;
        public MyAdapter1(Context context, JSONArray data){
            this.mContext = context;
            this.data = data;
            mInflater = LayoutInflater.from(context);
        }
        //父项的个数
        @Override
        public int getGroupCount() {

            return data.length();
        }
        //某个父项的子项的个数
        @Override
        public int getChildrenCount(int groupPosition) {
            JSONArray list=new JSONArray();
            try {
                JSONObject dataJson = data.getJSONObject(groupPosition);
                list = dataJson.getJSONArray("list");

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return list.length();
        }
        //获得某个父项
        @Override
        public Object getGroup(int groupPosition) {
            JSONObject dataJson=new JSONObject();
            try {
                dataJson=data.getJSONObject(groupPosition);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return dataJson;

        }
        //获得某个子项
        @Override
        public Object getChild(int groupPosition, int childPosition) {
            JSONArray list=new JSONArray();
            JSONObject listData=new JSONObject();
            try {
                JSONObject dataJson = data.getJSONObject(groupPosition);
                list = dataJson.getJSONArray("list");
                listData=list.getJSONObject(childPosition);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return listData;
        }
        //父项的Id
        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }
        //子项的id
        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }
        @Override
        public boolean hasStableIds() {
            return false;
        }
        //获取父项的view
        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            if (convertView == null){
                convertView = mInflater.inflate(R.layout.item_ex_list_1,parent,false);
            }
            try {
                JSONObject dataJson = data.getJSONObject(groupPosition);
                String icon=dataJson.getString("icon");
                String name=dataJson.getString("name");

                ImageView img = (ImageView) convertView.findViewById(R.id.img);
                TextView nameTv = (TextView) convertView.findViewById(R.id.nameTv);
                Glide.with(getActivity()).load(icon).into(img);
                nameTv.setText(name);

            } catch (JSONException e) {
                e.printStackTrace();
            }



            return convertView;
        }
        //获取子项的view
        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
//            final String child = mItemList.get(groupPosition).get(childPosition);
            ViewHolder viewHolder = null;
            System.out.println("-------------------------------------------getChildView-----------------------------------------------");
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.item_ex_list_2, null);
                viewHolder.nameTv = convertView.findViewById(R.id.nameTv);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            System.out.println("selectGroupPosition："+selectGroupPosition+"  selectChildPosiiton:"+selectChildPosiiton );
            System.out.println("groupPosition："+groupPosition+"  childPosition:"+childPosition );

            if (selectGroupPosition==groupPosition&&selectChildPosiiton==childPosition){
                viewHolder.nameTv.setTextColor(0xff009DFD);
            }else {
                viewHolder.nameTv.setTextColor(0xff000000);

            }


            try {
                JSONObject valueJson = (JSONObject) getChild(groupPosition,childPosition);
                String title = valueJson.getString("title");
                viewHolder.nameTv.setText(title);


            } catch (JSONException e) {
                e.printStackTrace();
            }


            return convertView;
        }

        @Override
        public void onGroupExpanded(int groupPosition) {
//            super.onGroupExpanded(groupPosition);
//            if (groupPosition==0){//第一项控制全部
//                for (int i=0; i<coupon_packages.length(); i++) {
//                    exListView.expandGroup(i);
//                };
//            }
        }

        @Override
        public void onGroupCollapsed(int groupPosition) {
//            super.onGroupCollapsed(groupPosition);
//            if (groupPosition==0){ //第一项控制全部
//                for (int i=0; i<coupon_packages.length(); i++) {
//                    exListView.collapseGroup(i);
//                };
//            }
        }

        class ViewHolder {
            public TextView nameTv;



        }
        //子项是否可选中,如果要设置子项的点击事件,需要返回true
        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }


    public class MyAdapter2 extends BaseAdapter {

        private JSONArray list;
        private Context ctx;
        private LayoutInflater mInflater;

        public MyAdapter2(Context context, JSONArray list) {
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
                convertView = mInflater.inflate(R.layout.item_ex_list_4, null);
                viewHolder = new ViewHolder();
                viewHolder.title = convertView.findViewById(R.id.nameTv);
                viewHolder.thumb = convertView.findViewById(R.id.thumb);
                viewHolder.description = convertView.findViewById(R.id.desTv);
                viewHolder.add_time = convertView.findViewById(R.id.timeTv);


                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            try {
                JSONObject data = list.getJSONObject(position);
                String id = data.getString("id");
                String title = data.getString("title");
                String thumb = data.getString("thumb");
                String description = data.getString("description");
                String play_num = data.getString("play_num");
                String add_time = data.getString("add_time");

                viewHolder.title.setText(title);
                viewHolder.description.setText(description);
                viewHolder.add_time.setText(add_time + "上架");
                Glide.with(getActivity()).load(thumb).into(viewHolder.thumb);
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
