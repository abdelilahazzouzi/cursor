package com.azizgraphics.clcltr.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
data class Order(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val date: Long = System.currentTimeMillis(),
    val itemsJson: String,
    val totalAmount: Double,
    val totalArea: Double,
    val totalQuantity: Int,
    val currency: String = "PKR"
)
