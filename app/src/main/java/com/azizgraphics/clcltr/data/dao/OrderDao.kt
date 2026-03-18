package com.azizgraphics.clcltr.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.azizgraphics.clcltr.data.entity.Order
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao {
    @Query("SELECT * FROM orders ORDER BY date DESC")
    fun getAllOrders(): Flow<List<Order>>

    @Query("SELECT * FROM orders WHERE id = :id")
    suspend fun getOrderById(id: Long): Order?

    @Insert
    suspend fun insert(order: Order): Long

    @Delete
    suspend fun delete(order: Order)

    @Query("DELETE FROM orders")
    suspend fun deleteAll()
}
