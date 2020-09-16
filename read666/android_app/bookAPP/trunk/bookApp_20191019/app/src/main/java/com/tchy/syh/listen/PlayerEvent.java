package com.tchy.syh.listen;

import com.lzx.musiclibrary.aidl.model.AudioInfo;

public class PlayerEvent {
    public int EventType = -1;
    public AudioInfo audioInfo;
    public boolean isFinishLoading;

    public PlayerEvent(int eventType) {
        EventType = eventType;
    }

    public PlayerEvent(int eventType, AudioInfo audioInfo) {
        EventType = eventType;
        this.audioInfo = audioInfo;
    }

    public PlayerEvent(int eventType, boolean isFinishLoading) {
        EventType = eventType;
        this.isFinishLoading = isFinishLoading;
    }
}
