package com.example.binlist

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface BinDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBinInfo(binInfo: BinInfoEntity)

    @Query("SELECT * FROM bin_history ORDER BY id DESC")
    suspend fun getAllBinHistory(): List<BinInfoEntity>
}
