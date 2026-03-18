package com.azizgraphics.clcltr.data.model

data class OrderItem(
    val name: String = "",
    val material: String? = null,
    val widthFt: Double = 0.0,
    val widthIn: Double = 0.0,
    val heightFt: Double = 0.0,
    val heightIn: Double = 0.0,
    val quantity: Int = 1,
    val pricePerSqFt: Double = 0.0
) {
    val widthTotalFt: Double get() = widthFt + (widthIn / 12.0)
    val heightTotalFt: Double get() = heightFt + (heightIn / 12.0)
    val areaSqFt: Double get() = widthTotalFt * heightTotalFt
    val totalArea: Double get() = areaSqFt * quantity
    val totalPrice: Double get() = totalArea * pricePerSqFt

    fun dimensionString(): String {
        val w = if (widthIn > 0) "${widthFt.toInt()}'${widthIn.toInt()}\"" else "${widthFt.toInt()}'"
        val h = if (heightIn > 0) "${heightFt.toInt()}'${heightIn.toInt()}\"" else "${heightFt.toInt()}'"
        return "$w x $h"
    }
}

enum class Currency(val symbol: String, val displayName: String) {
    PKR("PKR", "Pakistani Rupee"),
    USD("$", "US Dollar"),
    EUR("€", "Euro"),
    GBP("£", "British Pound"),
    AED("AED", "UAE Dirham"),
    SAR("SAR", "Saudi Riyal"),
    INR("₹", "Indian Rupee")
}
