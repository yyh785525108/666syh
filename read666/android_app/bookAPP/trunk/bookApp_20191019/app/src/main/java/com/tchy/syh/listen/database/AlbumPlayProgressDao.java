package com.tchy.syh.listen.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface AlbumPlayProgressDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveProgress(AlbumPlayProgress albumPlayProgress);

    @Query("Select * from tb_album_play_progress where albumId =:id Limit 1")
    AlbumPlayProgress getAlbumPlayProgressById(String id);
}
