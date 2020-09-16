package com.yyh.read666.details;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yyh.read666.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JiehuoReplyAdapter extends BaseAdapter {

    private JSONArray list;
    private Context ctx;
    private LayoutInflater mInflater;

    public JiehuoReplyAdapter(Context context, JSONArray list) {
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
            convertView = mInflater.inflate(R.layout.item_jiehuo_reply, null);
            viewHolder = new JiehuoReplyAdapter.ViewHolder();
            viewHolder.headImg = convertView.findViewById(R.id.headImg);
            viewHolder.nameTv = convertView.findViewById(R.id.nameTv);
            viewHolder.contentTv = convertView.findViewById(R.id.contentTv);
            viewHolder.timeTv = convertView.findViewById(R.id.timeTv);

            viewHolder.vipImg = convertView.findViewById(R.id.vipImg);



            convertView.setTag(viewHolder);
        } else {
            viewHolder = (JiehuoReplyAdapter.ViewHolder) convertView.getTag();
        }
        try {
            JSONObject data = list.getJSONObject(position);
            String id = data.getString("id");
            String uid = data.getString("uid");

            String content = data.getString("content");
            String avatar = data.getString("avatar");
            String nickname = data.getString("nickname");
            int level=Integer.parseInt(data.getString("level"));
            String time = data.getString("time");

            viewHolder.nameTv.setText(nickname);
            viewHolder.contentTv.setText(content);
            viewHolder.timeTv.setText(time);

            Glide.with(ctx).load(avatar).into(viewHolder.headImg);
            if (level>0){
                viewHolder.vipImg.setVisibility(View.VISIBLE);
            }else {
                viewHolder.vipImg.setVisibility(View.GONE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return convertView;
    }

    class ViewHolder {
        public ImageView headImg;
        public TextView nameTv;
        public TextView  contentTv,timeTv;
        public ImageView vipImg;



    }


}

