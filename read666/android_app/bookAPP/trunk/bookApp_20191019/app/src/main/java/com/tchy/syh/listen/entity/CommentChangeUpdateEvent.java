package com.tchy.syh.listen.entity;

import com.tchy.syh.comment.entity.CommentResp;

public class CommentChangeUpdateEvent {
    public int position;
    public CommentResp.DataBean.ListBean listBean;

    public CommentChangeUpdateEvent(int position, CommentResp.DataBean.ListBean listBean) {
        this.position = position;
        this.listBean = listBean;
    }
}
