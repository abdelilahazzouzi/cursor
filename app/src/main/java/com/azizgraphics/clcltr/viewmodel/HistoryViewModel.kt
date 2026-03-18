package com.azizgraphics.clcltr.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.azizgraphics.clcltr.data.database.AppDatabase
import com.azizgraphics.clcltr.data.entity.Order
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HistoryViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getDatabase(application)

    val orders = db.orderDao().getAllOrders()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun deleteOrder(order: Order) {
        viewModelScope.launch {
            db.orderDao().delete(order)
        }
    }

    fun deleteAllOrders() {
        viewModelScope.launch {
            db.orderDao().deleteAll()
        }
    }
}
