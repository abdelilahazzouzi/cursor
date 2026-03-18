package com.azizgraphics.clcltr.data.database

import androidx.room.TypeConverter
import com.azizgraphics.clcltr.data.model.OrderItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromOrderItemList(items: List<OrderItem>): String {
        return gson.toJson(items)
    }

    @TypeConverter
    fun toOrderItemList(json: String): List<OrderItem> {
        val type = object : TypeToken<List<OrderItem>>() {}.type
        return gson.fromJson(json, type)
    }
}
