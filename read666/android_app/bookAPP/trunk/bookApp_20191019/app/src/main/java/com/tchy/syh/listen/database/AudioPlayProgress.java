package com.tchy.syh.listen.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity(tableName = "tb_audio_play_progress")
public class AudioPlayProgress {
    @PrimaryKey()
    @NonNull
    private String audioId;
    private int progerss;

    @NonNull
    public String getAudioId() {
        return audioId;
    }



    public void setAudioId(@NonNull String audioId) {
        this.audioId = audioId;
    }

    public int getProgerss() {
        return progerss;
    }

    public void setProgerss(int progerss) {
        this.progerss = progerss;
    }

    public AudioPlayProgress(@NonNull String audioId, int progerss) {
        this.audioId = audioId;
        this.progerss = progerss;
    }
}
