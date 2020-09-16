package com.yyh.read666.tab2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.yyh.read666.R;
import com.yyh.read666.WebActivity;
import com.yyh.read666.details.JiehuoReplyAdapter;
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

public    class PinglunAdapter extends  RecyclerView.Adapter<PinglunAdapter.ViewHolder>{
    private JSONArray list;
    private Context ctx;
    private LayoutInflater mInflater;
    private HttpInterface httpInterface;

    public PinglunAdapter(Context context, JSONArray list) {
        ctx = context;
        this.list = list;
        mInflater = LayoutInflater.from(context);
        httpInterface=new HttpImplement();
    }

    private PinglunAdapter.OnMyItemClickListener listener;
        public void setOnMyItemClickListener(PinglunAdapter.OnMyItemClickListener listener){
            this.listener = listener;

        }

        public   interface OnMyItemClickListener{
            void onItemClick(View v,int pos);
            void mLongClick(View v,int pos);
        }

    @NonNull
    @Override
    public PinglunAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_yinping_pinglun,parent,false);
        PinglunAdapter.ViewHolder viewHolder = new PinglunAdapter.ViewHolder(convertView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PinglunAdapter.ViewHolder viewHolder, int position) {
        if(listener != null){
            //为ItemView设置监听器
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = viewHolder.getLayoutPosition(); // 1
                    listener.onItemClick(viewHolder.itemView,position); // 2
                }
            });
        }


        try {
            JSONObject data = list.getJSONObject(position);
            String id = data.getString("id");
            String content = data.getString("content");
            String uid = data.getString("uid");
            int reply_num = data.getInt("reply_num");
            int lovenum = data.getInt("lovenum");
            String type = data.getString("type");
            String nickname = data.getString("nickname");
            String avatar = data.getString("avatar");
            String time = data.getString("time");
            String learn_rank = data.getString("learn_rank");
            int is_love = data.getInt("is_love");
            int level=Integer.parseInt(data.getString("level"));



            viewHolder.nameTv.setText(nickname);
            viewHolder.dengjiTv.setText(learn_rank);
            viewHolder.contentTv.setText(content);
            viewHolder.timeTv.setText(time);
            viewHolder.dianZanTv.setText(lovenum+"");
            viewHolder.dianZanImg.setSelected(is_love==0?false:true);
            Glide.with(ctx).load(avatar).into(viewHolder.headImg);
            JiehuoReplyAdapter myAdapter;
            JSONArray reply;
            if (reply_num!=0){
                reply=data.getJSONArray("reply");
                myAdapter=new JiehuoReplyAdapter(ctx,reply);
                viewHolder.replyListView.setAdapter(myAdapter);
            }else {
                reply=new JSONArray();
                myAdapter=new JiehuoReplyAdapter(ctx,reply);
                viewHolder.replyListView.setAdapter(myAdapter);
            }

            if (level>0){
                viewHolder.vipImg.setVisibility(View.VISIBLE);
            }else {
                viewHolder.vipImg.setVisibility(View.GONE);
            }
            if (type!=null&&type.equals("shang")){
                viewHolder.contentTv.setTextColor(Color.RED);
            }else {
                viewHolder.contentTv.setTextColor(0xff444444);
            }
            viewHolder.headImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(ctx, WebActivity.class);
                    intent.putExtra("url", UrlConstant.ZHUYE_URL+"?uid="+uid);
                    ctx.startActivity(intent);
                }
            });
            viewHolder.dianZanImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    httpInterface.learn_comment(SharedPreferencesUtil.getToken(ctx), "love", id, new OkHttpUtils.MyNetCall() {
                        @Override
                        public void success(Call call, String response) throws IOException {
                            try {
                                JSONObject responseJson = new JSONObject(response);
                                int status = responseJson.getInt("status");
                                String info=responseJson.getString("info");
                                if (status == 1) {
                                    JSONObject data = responseJson.getJSONObject("data");
                                    int is_add=data.getInt("is_add");
                                    int num=data.getInt("num");
                                    Activity activity=(Activity)ctx;
                                    activity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            viewHolder.dianZanTv.setText(num+"");
                                            viewHolder.dianZanImg.setSelected(is_add==1?true:false);
                                            viewHolder.dianZanTv.setTextColor(is_add==0?0xFF838383:0xFFff6868);
                                            Toast.makeText(ctx,info,Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }catch (JSONException e){

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

    }


    @Override
    public int getItemCount() {
        return list.length();
    }
    class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView headImg;
        public TextView nameTv;
        public TextView dengjiTv;
        public TextView contentTv, timeTv;
        private ImageView dianZanImg;
        private TextView dianZanTv;
        private ImageView vipImg;
        private ListView replyListView;
        public ViewHolder (View view)
        {
            super(view);
            headImg = view.findViewById(R.id.headImg);
            nameTv = view.findViewById(R.id.nameTv);
            dengjiTv = view.findViewById(R.id.dengjiTv);
            contentTv = view.findViewById(R.id.contentTv);
            timeTv = view.findViewById(R.id.timeTv);
            dianZanImg=view.findViewById(R.id.dianZanImg);
            dianZanTv=view.findViewById(R.id.dianzanTv);
            vipImg=view.findViewById(R.id.vipImg);
            replyListView=view.findViewById(R.id.replyListView);
        }
    }

}

