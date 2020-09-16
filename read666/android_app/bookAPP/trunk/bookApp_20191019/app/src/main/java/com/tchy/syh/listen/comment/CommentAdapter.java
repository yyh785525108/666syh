package com.tchy.syh.listen.comment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tchy.syh.R;
import com.tchy.syh.comment.entity.CommentResp;
import com.tchy.syh.utils.NumberFormatUtil;
import com.tchy.syh.utils.TimeFormatUtil;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentHolder> {

    private Context context;
    private List<CommentResp.DataBean.ListBean> datas;

    public CommentAdapter(Context context, List<CommentResp.DataBean.ListBean> datas) {
        this.context = context;
        this.datas = datas;
    }

    @NonNull
    @Override
    public CommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.audio_comment_list_item, parent, false);
        return new CommentHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentHolder holder, int position) {
        CommentResp.DataBean.ListBean listBean = datas.get(position);
        holder.comment_root.setOnClickListener(view -> {
            onCommentClickListener.onItemClick(position);
        });
        Glide.with(context).load(listBean.getAvatar()).into(holder.avatar);
        holder.nickname.setText(listBean.getNickname()+"("+listBean.getLearn_rank()+")");
        if ("shang".equals(listBean.getType())) {
            holder.content.setTextColor(Color.parseColor("#ff0000"));
        } else {
            holder.content.setTextColor(Color.parseColor("#000000"));
        }
        holder.content.setText(Html.fromHtml(listBean.getContent()));
//        holder.content.setText(listBean.getContent());
        holder.zan.setText(NumberFormatUtil.format(listBean.getLovenum(), 10000, "万"));

        Drawable right = listBean.getIs_love() == 1 ?
                context.getResources().getDrawable(R.mipmap.up_selected) :
                context.getResources().getDrawable(R.mipmap.up);
        holder.zan.setCompoundDrawablesWithIntrinsicBounds(null, null, right, null);
        holder.zan.setOnClickListener(view -> {
            onCommentClickListener.onZanClick(position);
        });
        holder.no.setText("#" + (position + 1));
        holder.add_time.setText(TimeFormatUtil.formatLatest(listBean.getAdd_time()));
        holder.reply_num.setText(listBean.getReply_num() + "");
        if (listBean.getComment_list() != null && listBean.getComment_list().size() > 0) {
            holder.subComment.setVisibility(View.VISIBLE);
            CommentResp.DataBean.ListBean lb = listBean.getComment_list().get(0);
            holder.sub_1.setText(lb.getNickname() + ":" + lb.getContent());
            if (listBean.getComment_list().size() > 1) {
                CommentResp.DataBean.ListBean lb2 = listBean.getComment_list().get(1);
                holder.sub_2.setText(lb2.getNickname() + ":" + lb2.getContent());
            }
            if (listBean.getComment_list().size() > 2) {
                holder.seeAll.setVisibility(View.VISIBLE);
            } else {
                holder.seeAll.setVisibility(View.GONE);
            }
        } else {
            holder.subComment.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class CommentHolder extends RecyclerView.ViewHolder {

        public final ImageView avatar;
        public final TextView nickname;
        public final TextView content;
        public final TextView zan;
        public final TextView no;
        public final TextView add_time;
        public final TextView reply_num;
        public final LinearLayout subComment;
        public final TextView sub_1;
        public final TextView sub_2;
        public final TextView seeAll;
        public final View comment_root;


        public CommentHolder(View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.imageView2);
            nickname = itemView.findViewById(R.id.textView4);
            content = itemView.findViewById(R.id.textView6);
            zan = itemView.findViewById(R.id.textView7);
            no = itemView.findViewById(R.id.no);
            add_time = itemView.findViewById(R.id.textView9);
            reply_num = itemView.findViewById(R.id.textView10);
            subComment = itemView.findViewById(R.id.subComment);
            sub_1 = itemView.findViewById(R.id.tv1);
            sub_2 = itemView.findViewById(R.id.tv2);
            seeAll = itemView.findViewById(R.id.seeAll);
            comment_root = itemView.findViewById(R.id.comment_root);

        }
    }

    public void setOnCommentClickListener(OnCommentClickListener onCommentClickListener) {
        this.onCommentClickListener = onCommentClickListener;
    }

    private OnCommentClickListener onCommentClickListener;

    public interface OnCommentClickListener {

        void onZanClick(int position);

        void onItemClick(int position);
    }


}