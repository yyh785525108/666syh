package com.tchy.syh.listen.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface AudioHistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(AudioHistory audioHistory);

    @Query("Select * from tb_audio_play_history")
    List<AudioHistory> listAudioHistory();

    @Query("Delete from tb_audio_play_history")
    void clearAll();

    @Query("Update tb_audio_play_history set isCollect=:va")
    void updateCollect(int va);

    @Query("Update tb_audio_play_history set isZan=:va,zanNum=:num")
    void updateZan(int va,int num);
}
