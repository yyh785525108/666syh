package com.yyh.read666.details;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.yyh.read666.R;
import com.yyh.read666.okhttp.HttpImplement;
import com.yyh.read666.okhttp.OkHttpUtils;
import com.yyh.read666.utils.SharedPreferencesUtil;
import com.yyh.read666.vo.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;

public class GuankanzhongFragment extends Fragment {
    private ListView listView;
    private HttpImplement httpImplement;
    private Activity mActivity;
    private String cat_id;

    private int page = 0;
    private MyAdapter myAdapter;
    JSONArray allList;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listview, container, false);
        listView = view.findViewById(R.id.listView);
        allList = new JSONArray();




        return view;
    }
    public void refreshListView(JSONArray data){
        myAdapter = new MyAdapter(getActivity(), data);
        listView.setAdapter(myAdapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivity = null;

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
                convertView = mInflater.inflate(R.layout.item_online, null);
                viewHolder = new ViewHolder();
                viewHolder.userName = convertView.findViewById(R.id.userName);
                viewHolder.userHead = convertView.findViewById(R.id.userHead);
                viewHolder.description = convertView.findViewById(R.id.description);
                viewHolder.dianzanImg = convertView.findViewById(R.id.dianzanImg);
                viewHolder.dianzanTv = convertView.findViewById(R.id.dianzanTv);


                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            try {
                JSONObject data = list.getJSONObject(position);
                int uid=data.getInt("uid");
                String nickname = data.getString("nickname");
                String avatar = data.getString("avatar");
                int times = data.getInt("times");
                int zan = data.getInt("zan");
                int is_zan = data.getInt("is_zan");
                String time ="";

                try {
                     time = data.getString("time");
                     time=time+"开始看书";
                }catch (JSONException e){

                }

                viewHolder.userName.setText(nickname);
                viewHolder.description.setText(time+"已观看"+times+"次");
                viewHolder.dianzanImg.setSelected(is_zan==1?true:false);
                viewHolder.dianzanTv.setText(zan + "");
                Glide.with(getActivity()).load(avatar).into(viewHolder.userHead);

                ViewHolder finalViewHolder = viewHolder;
                viewHolder.dianzanImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        User user= SharedPreferencesUtil.getLoginUser(getActivity());
                        String access_token="";
                        if (user!=null){
                            access_token = user.getAccess_token();
                        }
                        httpImplement=new HttpImplement();
                        httpImplement.learn(access_token, uid + "", "member_zan", new OkHttpUtils.MyNetCall() {
                            @Override
                            public void success(Call call, String response) throws IOException {
                                try {
                                    JSONObject responseJson=new JSONObject(response);
                                    int status=responseJson.getInt("status");
                                    String info=responseJson.getString("info");
                                    if (status==1){
                                        JSONObject data=responseJson.getJSONObject("data");
                                        int num=data.getInt("num");
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                finalViewHolder.dianzanImg.setSelected(num==1?true:false);
                                                finalViewHolder.dianzanTv.setText((Integer.parseInt( finalViewHolder.dianzanTv.getText().toString())+num)+"");
                                               Toast.makeText(getActivity(),info,Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                    }else {
                                        getActivity().runOnUiThread(new Runnable() {
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
                });

            } catch (JSONException e) {
                e.printStackTrace();
            }


            return convertView;
        }

        class ViewHolder {
            public TextView userName;
            public ImageView userHead,dianzanImg;
            public TextView description;
            public TextView dianzanTv;

        }


    }
}
