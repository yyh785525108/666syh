package com.yyh.read666.details;

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

public class ShuyouquanAdapter extends  RecyclerView.Adapter<ShuyouquanAdapter.ViewHolder>{
    private JSONArray list;
    private Context ctx;
    private LayoutInflater mInflater;
    private HttpInterface httpInterface;
    public interface ForecastAdapterOnClickHandler {
        void onClick(String weatherForDay);
    }

    public ShuyouquanAdapter(Context context, JSONArray list) {
        ctx = context;
        this.list = list;
        mInflater = LayoutInflater.from(context);
        httpInterface=new HttpImplement();
    }
    //
        private ShuyouquanAdapter.OnMyItemClickListener listener;

    public   interface OnMyItemClickListener{
        public void onItemClick(View v, int pos);
    }
    private ShuyouquanAdapter.OnMyItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(ShuyouquanAdapter.OnMyItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }


    @NonNull
    @Override
    public ShuyouquanAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_yinping_pinglun,parent,false);
        ShuyouquanAdapter.ViewHolder viewHolder = new ShuyouquanAdapter.ViewHolder(convertView);

//            convertView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    shuyouReplayLay.setVisibility(View.VISIBLE);
//                    sreplyContentEt.setText("");
//
//
//                    JSONObject data= (JSONObject) parent.getItemAtPosition(position);
//                    try {
//                        String id = data.getString("id");
//                        String content = data.getString("content");
//                        String uid = data.getString("uid");
//                        shuyouReplayLay.setTag(id);
//                        System.out.println("pid211111111111:"+uid);
//                        sreplyWentiTv.setText(content);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ShuyouquanAdapter.ViewHolder viewHolder, int position) {
        if(mOnItemClickListener != null){
            //为ItemView设置监听器
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = viewHolder.getLayoutPosition(); // 1
                    mOnItemClickListener.onItemClick(viewHolder.itemView,position); // 2
                }
            });
        }

//
//                // set LongClick
//                viewHolder.yinpingLay.setOnLongClickListener(new View.OnLongClickListener() {
//                    @Override
//                    public boolean onLongClick(View v) {
//                        listener.mLongClick(v,position);
//                        return true;
//                    }
//                });
//            }
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
            viewHolder.nameTv.setText(nickname);
            viewHolder.dengjiTv.setText(learn_rank);
            viewHolder.contentTv.setText(content);
            viewHolder.dianZanTv.setText(lovenum+"");
            viewHolder.dianZanImg.setSelected(is_love==0?false:true);
            viewHolder.dianZanTv.setTextColor(is_love==0?0xFF838383:0xFFff6868);
            viewHolder.timeTv.setText(time);
            Glide.with(ctx).load(avatar).into(viewHolder.headImg);

            ShuyouquanAdapter.ViewHolder finalViewHolder = viewHolder;
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
                                            finalViewHolder.dianZanTv.setText(num+"");
                                            finalViewHolder.dianZanImg.setSelected(is_add==1?true:false);
                                            finalViewHolder.dianZanTv.setTextColor(is_add==0?0xFF838383:0xFFff6868);
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
    public class ViewHolder extends RecyclerView.ViewHolder {
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
