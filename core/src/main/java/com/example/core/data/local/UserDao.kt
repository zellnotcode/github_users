package com.example.core.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Query("SELECT * FROM favorite ORDER BY login ASC")
    fun getAllFavorite(): Flow<List<UserEntity>>

    @Query("SELECT EXISTS(SELECT * FROM favorite WHERE login = :login)")
    fun isFavoriteExist(login: String) : Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(userEntity: UserEntity)

    @Delete
    suspend fun deleteFavorite(userEntity: UserEntity)
}