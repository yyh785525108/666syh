package com.tchy.syh.listen.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface AudioPlayProgressDao {


    @Query("Select * from tb_audio_play_progress where audioId =:id Limit 1")
    AudioPlayProgress getAudioPlayProgressById(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveProgress(AudioPlayProgress audioPlayProgress);
}
