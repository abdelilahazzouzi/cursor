package com.azizgraphics.clcltr.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.azizgraphics.clcltr.data.database.AppDatabase
import com.azizgraphics.clcltr.data.entity.Customer
import com.azizgraphics.clcltr.data.entity.Material
import com.azizgraphics.clcltr.data.preferences.AppPreferences
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getDatabase(application)
    private val prefs = AppPreferences(application)

    val useFeet = prefs.useFeet
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)
    val defaultPrice = prefs.defaultPrice
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 50.0)
    val themeMode = prefs.themeMode
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)
    val currency = prefs.currency
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "PKR")

    val materials = db.materialDao().getAllMaterials()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val customers = db.customerDao().getAllCustomers()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setUseFeet(v: Boolean) = viewModelScope.launch { prefs.setUseFeet(v) }
    fun setDefaultPrice(v: Double) = viewModelScope.launch { prefs.setDefaultPrice(v) }
    fun setThemeMode(v: Int) = viewModelScope.launch { prefs.setThemeMode(v) }
    fun setCurrency(v: String) = viewModelScope.launch { prefs.setCurrency(v) }

    fun addMaterial(name: String, price: Double) = viewModelScope.launch {
        db.materialDao().insert(Material(name = name, defaultPrice = price))
    }

    fun updateMaterial(material: Material) = viewModelScope.launch {
        db.materialDao().update(material)
    }

    fun deleteMaterial(material: Material) = viewModelScope.launch {
        db.materialDao().delete(material)
    }

    fun addCustomer(name: String, phone: String) = viewModelScope.launch {
        db.customerDao().insert(Customer(name = name, phone = phone))
    }

    fun updateCustomer(customer: Customer) = viewModelScope.launch {
        db.customerDao().update(customer)
    }

    fun deleteCustomer(customer: Customer) = viewModelScope.launch {
        db.customerDao().delete(customer)
    }
}
