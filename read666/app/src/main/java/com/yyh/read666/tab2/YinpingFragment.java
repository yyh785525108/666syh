package com.yyh.read666.tab2;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yyh.read666.R;
import com.yyh.read666.music.MediaUtil;
import com.yyh.read666.utils.DateUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class YinpingFragment extends Fragment {
    public RecyclerView recyclerView;
    public JSONObject data;//该页面获取的详情数据

    LinearLayout duoshaojiLay;
    ImageView duoshaojiImg1;
    TextView duoshaojiTv;
    ImageView duoshaojiImg2;
    boolean zhengfuFlag=true;//是否正序

    private boolean isPlaying;//是否正在播放，切换下一首需要用到
    private MediaPlayer mediaPlayer;
    JSONArray album;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_yinping,container,false);
        recyclerView=view.findViewById(R.id.recyclerView);
        duoshaojiLay=view.findViewById(R.id.duoshaojiLay);
        duoshaojiImg1=view.findViewById(R.id.duoshaojiImg1);
        duoshaojiTv=view.findViewById(R.id.duoshaojiTv);
        duoshaojiImg2=view.findViewById(R.id.duoshaojiImg2);
        initQbbfLay(view);

        return view;
    }


    public void initDuoshaojiLay(  JSONArray tAlbum){
        this.album=tAlbum;

        duoshaojiTv.setText("共"+album.length()+"集");

        duoshaojiLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zhengfuFlag=!zhengfuFlag;
                duoshaojiImg1.setSelected(zhengfuFlag);
                duoshaojiImg2.setSelected(zhengfuFlag);

                JSONArray album2=new JSONArray();
                for (int i=album.length()-1;i>=0;i--){
                    try {
                        album2.put(album.getJSONObject(i));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                 album=album2;
                refreshListView(album);
            }
        });
    }
    private void initQbbfLay(View view){
        LinearLayout qbbfLay=view.findViewById(R.id.qbbfLay);
        ImageView qbbfImg=view.findViewById(R.id.qbbfImg);
        TextView qbbfTv=view.findViewById(R.id.qbbfTv);

        qbbfLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),YinpingDetailActivity.class);
                 intent.putExtra("position",0);//当前音频位置
                intent.putExtra("data",YinpingFragment.this.data.toString());
                intent.putExtra("album",album.toString());

                startActivity(intent);

//                isPlaying=!isPlaying;
//                qbbfImg.setSelected(isPlaying);
//                qbbfTv.setText(isPlaying?"暂停播放":"全部播放");
//                if (isPlaying){
//                    mediaPlayer.start();
//                }else {
//                    mediaPlayer.pause();
//                }

            }
        });
    }


    /**
     * 初始化音频播放器
     * @param audioUrl 音频地址
     */
    public void initMedia(String audioUrl){

        mediaPlayer= MediaUtil.getMediaPlayer();
        if (MediaUtil.MEDIA_IS_INIT){//播放过
            mediaPlayer.reset();
        }
        try {
            mediaPlayer.setDataSource(audioUrl);
            mediaPlayer.prepare();
            int duration = mediaPlayer.getDuration();
            if (0 != duration) {
                //更新 seekbar 长度
                int s = duration / 1000;
                //设置文件时长，单位 "分:秒" 格式
                String total = s / 60 + ":" + s % 60;
                Message msg=new Message();
                msg.what=100;
                msg.arg1=duration;

                UIhandle.sendMessage(msg);

            }
            if (isPlaying){
                //初始化时，判断下是否需要播放
                mediaPlayer.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public  Handler UIhandle = new Handler(){

        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            if(msg.what==100) {
                int position = mediaPlayer.getCurrentPosition();
//                System.out.println("position:"+position+"   all:"+msg.arg1);


                if (position>=msg.arg1){
                    //尽头了，播下一集
//                    onClick(nextBtn);
                }else {
                    Message msg1=new Message();
                    msg1.what=100;
                    msg1.arg1=msg.arg1;

                    UIhandle.sendMessage(msg1);
                }

            }
        }
    };








    public void refreshListView(JSONArray list){
        System.out.println("----------refreshListView-----------");
        MyRecyclerAdapter adapter=new MyRecyclerAdapter(getActivity(),list);
        //必须先设置LayoutManager
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        adapter.setOnMyItemClickListener(new MyRecyclerAdapter.OnMyItemClickListener() {
            @Override
            public void myClick(View v, int position) {
                Intent intent=new Intent(getActivity(),YinpingDetailActivity.class);

//                    if (zhengfuFlag){
//                        intent.putExtra("position",position);//当前音频位置
//                    }else {
//                        intent.putExtra("position",album.length()-1-position);
//                    }
//                    intent.putExtra("data",YinpingFragment.this.data.toString());
//                    startActivity(intent);

                intent.putExtra("position",position);//当前音频位置
                intent.putExtra("data",YinpingFragment.this.data.toString());
                intent.putExtra("album",album.toString());

                startActivity(intent);

            }

            @Override
            public void mLongClick(View v, int pos) {

            }
        });



    }


    public static class MyRecyclerAdapter extends  RecyclerView.Adapter<MyRecyclerAdapter.ViewHolder>{
        private JSONArray list;
        private Context ctx;
        private LayoutInflater mInflater;
        public MyRecyclerAdapter(Context context, JSONArray list) {
            ctx = context;
            this.list = list;
            mInflater = LayoutInflater.from(context);
        }

        private OnMyItemClickListener listener;
        public void setOnMyItemClickListener(OnMyItemClickListener listener){
            this.listener = listener;

        }

        public   interface OnMyItemClickListener{
            void myClick(View v,int pos);
            void mLongClick(View v,int pos);
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_yinping2,parent,false);
            ViewHolder viewHolder = new ViewHolder(convertView);

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
            if (listener!=null) {
                viewHolder.yinpingLay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.myClick(v,position);
                    }
                });


                // set LongClick
                viewHolder.yinpingLay.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        listener.mLongClick(v,position);
                        return true;
                    }
                });
            }
            try {
                JSONObject data=list.getJSONObject(position);
                int id=data.getInt("id");
                String title=data.getString("title");
                String duration=data.getString("duration");
                String play_num=data.getString("play_num");
                String time=data.getString("time");
                JSONArray tags=data.getJSONArray("tags");



                viewHolder.nameTv.setText(title);
                viewHolder.countTv.setText(play_num);
                viewHolder.timeTv.setText(duration);
                viewHolder.dateTv.setText(DateUtil.timeStamp2Date(time,"yyyy/MM/dd"));
                if (tags!=null&&tags.length()!=0){
                    viewHolder.tagTv.setText(tags.getString(0));
                    viewHolder.tagTv.setVisibility(View.VISIBLE);
                }else {
                    viewHolder.tagTv.setVisibility(View.GONE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


        @Override
        public int getItemCount() {
            return list.length();
        }
        class ViewHolder extends RecyclerView.ViewHolder {
            public TextView nameTv;
            public TextView countTv;
            public TextView dateTv;
            public TextView timeTv;
            public LinearLayout yinpingLay;
            private TextView tagTv;
            public ViewHolder (View view)
            {
                super(view);
                nameTv = view.findViewById(R.id.nameTv);
                countTv = view.findViewById(R.id.countTv);
                dateTv = view.findViewById(R.id.dateTv);
                timeTv = view.findViewById(R.id.timeTv);
                yinpingLay = view.findViewById(R.id.yinpingLay);
                tagTv=view.findViewById(R.id.tagTv);
            }
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

//        public void addItem(final JSONObject item) {
//            list.put(item);
//            notifyDataSetChanged();
//        }

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
                convertView = mInflater.inflate(R.layout.item_yinping2, null);
                viewHolder=new ViewHolder();
                viewHolder.nameTv=convertView.findViewById(R.id.nameTv);
                viewHolder.countTv=convertView.findViewById(R.id.countTv);
                viewHolder.dateTv=convertView.findViewById(R.id.dateTv);
                viewHolder.timeTv=convertView.findViewById(R.id.timeTv);

System.out.println("AAAAAAA:"+position);
                convertView.setTag(viewHolder);
            } else {
                System.out.println("BBBBB:"+position);

                viewHolder = (ViewHolder) convertView.getTag();


            }
            try {
                JSONObject data=list.getJSONObject(position);
                int id=data.getInt("id");
                String title=data.getString("title");
                String duration=data.getString("duration");
                String play_num=data.getString("play_num");

                viewHolder.nameTv.setText(title);
                viewHolder.countTv.setText(play_num);
                viewHolder.timeTv.setText(duration);
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return convertView;
        }

        class ViewHolder {
            public TextView nameTv;
            public TextView countTv;
            public TextView dateTv;
            public TextView timeTv;
        }


    }
}
