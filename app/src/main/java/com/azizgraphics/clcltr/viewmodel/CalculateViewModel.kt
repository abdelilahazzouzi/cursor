package com.azizgraphics.clcltr.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.azizgraphics.clcltr.data.database.AppDatabase
import com.azizgraphics.clcltr.data.entity.Order
import com.azizgraphics.clcltr.data.model.OrderItem
import com.azizgraphics.clcltr.data.preferences.AppPreferences
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

data class CalcItemState(
    val uid: String = UUID.randomUUID().toString(),
    val name: String = "",
    val material: String? = null,
    val widthFt: String = "",
    val widthIn: String = "",
    val heightFt: String = "",
    val heightIn: String = "",
    val quantity: Int = 1,
    val pricePerSqFt: String = ""
) {
    fun toOrderItem(): OrderItem = OrderItem(
        name = name,
        material = material,
        widthFt = widthFt.toDoubleOrNull() ?: 0.0,
        widthIn = widthIn.toDoubleOrNull() ?: 0.0,
        heightFt = heightFt.toDoubleOrNull() ?: 0.0,
        heightIn = heightIn.toDoubleOrNull() ?: 0.0,
        quantity = quantity,
        pricePerSqFt = pricePerSqFt.toDoubleOrNull() ?: 0.0
    )
}

class CalculateViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getDatabase(application)
    private val prefs = AppPreferences(application)

    private val _items = MutableStateFlow(listOf(CalcItemState()))
    val items: StateFlow<List<CalcItemState>> = _items.asStateFlow()

    private val _saveSuccess = MutableStateFlow(false)
    val saveSuccess: StateFlow<Boolean> = _saveSuccess.asStateFlow()

    val materials = db.materialDao().getAllMaterials()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val currency = prefs.currency
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "PKR")

    val defaultPrice = prefs.defaultPrice
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 50.0)

    val totalAmount: Double
        get() = _items.value.sumOf { it.toOrderItem().totalPrice }

    val totalArea: Double
        get() = _items.value.sumOf { it.toOrderItem().totalArea }

    val totalQuantity: Int
        get() = _items.value.sumOf { it.quantity }

    fun updateItem(uid: String, updater: (CalcItemState) -> CalcItemState) {
        _items.value = _items.value.map { if (it.uid == uid) updater(it) else it }
    }

    fun addItem() {
        val price = defaultPrice.value.toString()
        _items.value = _items.value + CalcItemState(pricePerSqFt = price)
    }

    fun removeItem(uid: String) {
        if (_items.value.size > 1) {
            _items.value = _items.value.filter { it.uid != uid }
        }
    }

    fun clearAll() {
        _items.value = listOf(CalcItemState(pricePerSqFt = defaultPrice.value.toString()))
    }

    fun saveOrder(name: String) {
        viewModelScope.launch {
            val orderItems = _items.value.map { it.toOrderItem() }
            val order = Order(
                name = name.ifEmpty { "Order" },
                itemsJson = Gson().toJson(orderItems),
                totalAmount = orderItems.sumOf { it.totalPrice },
                totalArea = orderItems.sumOf { it.totalArea },
                totalQuantity = orderItems.sumOf { it.quantity },
                currency = currency.value
            )
            db.orderDao().insert(order)
            _saveSuccess.value = true
        }
    }

    fun resetSaveSuccess() {
        _saveSuccess.value = false
    }
}
