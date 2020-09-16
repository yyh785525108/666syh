package com.tchy.syh.listen.play;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.lzx.musiclibrary.aidl.model.AudioInfo;
import com.lzx.musiclibrary.manager.MusicManager;
import com.tchy.syh.R;

import java.util.List;

public class AudioPlayingListAdapter extends RecyclerView.Adapter<AudioPlayingListAdapter.MyHolder> {
    Context mContext;
    List<AudioInfo> list;

    public AudioPlayingListAdapter(Context context, List<AudioInfo> list) {
        this.mContext = context;
        this.list = list;
    }

    public void update(List<AudioInfo> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_audio_playing, parent, false);
        MyHolder myHolder = new MyHolder(view);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {

        final AudioInfo audioInfo = list.get(position);
        holder.itemView.setOnClickListener(view -> {
            if (onItemClickListener!=null){
                onItemClickListener.onItemClick(audioInfo);
            }
        });
        String songId = audioInfo.getSongId();
        String cid = MusicManager.get().getCurrPlayingMusic().getSongId();
        if (cid.equals(songId)) {
            holder.textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            holder.textView.setTextColor(Color.parseColor("#000000"));
        } else {
            holder.textView.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            holder.textView.setTextColor(Color.parseColor("#666666"));
        }
        holder.textView.setText(audioInfo.getSongName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        public TextView textView;

        public MyHolder(View itemView) {
            super(itemView);
            this.textView = itemView.findViewById(R.id.tv_audio);
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private OnItemClickListener onItemClickListener;
    public interface OnItemClickListener{
        void onItemClick(AudioInfo audioInfo);
    }
}
