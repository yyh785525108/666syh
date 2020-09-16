package com.tchy.syh.listen.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

/**
 * 专辑播放进度
 */
@Entity(tableName = "tb_album_play_progress")
public class AlbumPlayProgress {
    @PrimaryKey()
    @NonNull
    private String albumId;
    private int progerss;

    @NonNull
    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(@NonNull String albumId) {
        this.albumId = albumId;
    }

    public int getProgerss() {
        return progerss;
    }

    public void setProgerss(int progerss) {
        this.progerss = progerss;
    }

    public AlbumPlayProgress(@NonNull String albumId, int progerss) {
        this.albumId = albumId;
        this.progerss = progerss;
    }
}
