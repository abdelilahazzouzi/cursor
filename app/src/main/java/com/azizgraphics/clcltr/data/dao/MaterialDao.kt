package com.azizgraphics.clcltr.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.azizgraphics.clcltr.data.entity.Material
import kotlinx.coroutines.flow.Flow

@Dao
interface MaterialDao {
    @Query("SELECT * FROM materials ORDER BY name ASC")
    fun getAllMaterials(): Flow<List<Material>>

    @Insert
    suspend fun insert(material: Material): Long

    @Update
    suspend fun update(material: Material)

    @Delete
    suspend fun delete(material: Material)
}
