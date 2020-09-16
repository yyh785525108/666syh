package com.tchy.syh.listen.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity(tableName = "tb_audio_play_history")
public class AudioHistory {
    @PrimaryKey()
    @NonNull
    private String audioId;
    private String AlbumId;
    private String AlbumName;
    private String audioTitle;
    private String audioUrl;
    private String audioCover;
    private String audioPlayNum;
    private String audioUpdateTime;
    private String audioAddTime;
    private String audioCommentNum;
    private String duration;
    private String isFree;
    private int sort;

    private String collectNum;
    private String commentNum;
    private int isCollect;
    private int isZan;
    private int zanNum;

    @NonNull
    public String getAudioId() {
        return audioId;
    }

    public void setAudioId(@NonNull String audioId) {
        this.audioId = audioId;
    }

    public String getAlbumId() {
        return AlbumId;
    }

    public void setAlbumId(String albumId) {
        AlbumId = albumId;
    }

    public String getAudioTitle() {
        return audioTitle;
    }

    public void setAudioTitle(String audioTitle) {
        this.audioTitle = audioTitle;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    public String getAudioCover() {
        return audioCover;
    }

    public void setAudioCover(String audioCover) {
        this.audioCover = audioCover;
    }

    public String getAudioPlayNum() {
        return audioPlayNum;
    }

    public void setAudioPlayNum(String audioPlayNum) {
        this.audioPlayNum = audioPlayNum;
    }

    public String getAudioUpdateTime() {
        return audioUpdateTime;
    }

    public void setAudioUpdateTime(String audioUpdateTime) {
        this.audioUpdateTime = audioUpdateTime;
    }

    public String getAudioAddTime() {
        return audioAddTime;
    }

    public void setAudioAddTime(String audioAddTime) {
        this.audioAddTime = audioAddTime;
    }

    public String getAudioCommentNum() {
        return audioCommentNum;
    }

    public void setAudioCommentNum(String audioCommentNum) {
        this.audioCommentNum = audioCommentNum;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getIsFree() {
        return isFree;
    }

    public void setIsFree(String isFree) {
        this.isFree = isFree;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getAlbumName() {
        return AlbumName;
    }

    public void setAlbumName(String albumName) {
        AlbumName = albumName;
    }

    public String getCollectNum() {
        return collectNum;
    }

    public void setCollectNum(String collectNum) {
        this.collectNum = collectNum;
    }

    public String getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(String commentNum) {
        this.commentNum = commentNum;
    }

    public int getIsCollect() {
        return isCollect;
    }

    public void setIsCollect(int isCollect) {
        this.isCollect = isCollect;
    }

    public int getIsZan() {
        return isZan;
    }

    public void setIsZan(int isZan) {
        this.isZan = isZan;
    }

    public int getZanNum() {
        return zanNum;
    }

    public void setZanNum(int zanNum) {
        this.zanNum = zanNum;
    }
}
