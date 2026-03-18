package com.azizgraphics.clcltr.ui.screens.calculate

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ClearAll
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.azizgraphics.clcltr.ui.components.GlassCard
import com.azizgraphics.clcltr.ui.components.ItemCard
import com.azizgraphics.clcltr.ui.theme.AccentTeal
import com.azizgraphics.clcltr.ui.theme.ErrorRed
import com.azizgraphics.clcltr.viewmodel.CalcItemState
import com.azizgraphics.clcltr.viewmodel.CalculateViewModel
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculateScreen(
    snackbarHostState: SnackbarHostState,
    viewModel: CalculateViewModel = viewModel()
) {
    val items by viewModel.items.collectAsState()
    val materials by viewModel.materials.collectAsState()
    val currency by viewModel.currency.collectAsState()
    val saveSuccess by viewModel.saveSuccess.collectAsState()
    val scope = rememberCoroutineScope()

    var showSaveDialog by remember { mutableStateOf(false) }
    var orderName by remember { mutableStateOf("") }

    LaunchedEffect(saveSuccess) {
        if (saveSuccess) {
            snackbarHostState.showSnackbar("Order saved successfully!")
            viewModel.resetSaveSuccess()
        }
    }

    val totalAmount = items.sumOf { it.toOrderItem().totalPrice }
    val totalArea = items.sumOf { it.toOrderItem().totalArea }

    val currencyFormat = NumberFormat.getNumberInstance(Locale.US).apply {
        minimumFractionDigits = 2
        maximumFractionDigits = 2
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            GlassCard {
                Column {
                    Text(
                        text = "Total Amount",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "$currency ${currencyFormat.format(totalAmount)}",
                        style = MaterialTheme.typography.headlineLarge.copy(fontSize = 32.sp),
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row {
                        Text(
                            text = "Total Area: ${"%.2f".format(totalArea)} sq ft",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = "(${"%.2f".format(totalArea * 0.0929)} sq m)",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.5f)
                        )
                    }
                }
            }
        }

        itemsIndexed(items, key = { _, item -> item.uid }) { index, item ->
            AnimatedVisibility(
                visible = true,
                enter = slideInVertically() + fadeIn(),
                exit = fadeOut()
            ) {
                CalcItemCard(
                    index = index,
                    item = item,
                    currency = currency,
                    materialNames = materials.map { it.name },
                    onUpdate = { updater -> viewModel.updateItem(item.uid, updater) },
                    onDelete = { viewModel.removeItem(item.uid) },
                    canDelete = items.size > 1
                )
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                FilledTonalButton(
                    onClick = { viewModel.addItem() },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(Icons.Filled.Add, null, Modifier.size(18.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("Add Item")
                }
                OutlinedButton(
                    onClick = { viewModel.clearAll() },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(Icons.Filled.ClearAll, null, Modifier.size(18.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("Clear All")
                }
            }
        }

        item {
            Button(
                onClick = { showSaveDialog = true },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AccentTeal)
            ) {
                Icon(Icons.Filled.Save, null, Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text("Save Order", fontWeight = FontWeight.SemiBold)
            }
        }

        item { Spacer(modifier = Modifier.height(80.dp)) }
    }

    if (showSaveDialog) {
        AlertDialog(
            onDismissRequest = { showSaveDialog = false },
            title = { Text("Save Order", fontFamily = androidx.compose.ui.text.font.FontFamily.Serif) },
            text = {
                OutlinedTextField(
                    value = orderName,
                    onValueChange = { orderName = it },
                    label = { Text("Order Name") },
                    placeholder = { Text("e.g. Shop Banner Order") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp)
                )
            },
            confirmButton = {
                Button(onClick = {
                    viewModel.saveOrder(orderName)
                    showSaveDialog = false
                    orderName = ""
                    scope.launch { snackbarHostState.showSnackbar("Order saved!") }
                }) { Text("Save") }
            },
            dismissButton = {
                TextButton(onClick = { showSaveDialog = false }) { Text("Cancel") }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CalcItemCard(
    index: Int,
    item: CalcItemState,
    currency: String,
    materialNames: List<String>,
    onUpdate: (updater: (CalcItemState) -> CalcItemState) -> Unit,
    onDelete: () -> Unit,
    canDelete: Boolean
) {
    val orderItem = item.toOrderItem()
    var matExpanded by remember { mutableStateOf(false) }
    var qtyExpanded by remember { mutableStateOf(false) }
    var customQty by remember { mutableStateOf(false) }
    var customQtyText by remember(item.quantity) { mutableStateOf("${item.quantity}") }

    ItemCard {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Item ${index + 1}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                if (canDelete) {
                    IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Filled.Close, "Delete", tint = ErrorRed, modifier = Modifier.size(20.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            if (materialNames.isNotEmpty()) {
                ExposedDropdownMenuBox(
                    expanded = matExpanded,
                    onExpandedChange = { matExpanded = it }
                ) {
                    OutlinedTextField(
                        value = item.material ?: "",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Material") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = matExpanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors()
                    )
                    ExposedDropdownMenu(expanded = matExpanded, onDismissRequest = { matExpanded = false }) {
                        DropdownMenuItem(
                            text = { Text("None") },
                            onClick = {
                                onUpdate { it.copy(material = null) }
                                matExpanded = false
                            }
                        )
                        materialNames.forEach { name ->
                            DropdownMenuItem(
                                text = { Text(name) },
                                onClick = {
                                    onUpdate { it.copy(material = name) }
                                    matExpanded = false
                                }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
            }

            Text("Width", style = MaterialTheme.typography.labelLarge)
            Spacer(modifier = Modifier.height(4.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = item.widthFt,
                    onValueChange = { onUpdate { s -> s.copy(widthFt = it) } },
                    label = { Text("Feet") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp)
                )
                OutlinedTextField(
                    value = item.widthIn,
                    onValueChange = { onUpdate { s -> s.copy(widthIn = it) } },
                    label = { Text("Inches") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text("Height", style = MaterialTheme.typography.labelLarge)
            Spacer(modifier = Modifier.height(4.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = item.heightFt,
                    onValueChange = { onUpdate { s -> s.copy(heightFt = it) } },
                    label = { Text("Feet") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp)
                )
                OutlinedTextField(
                    value = item.heightIn,
                    onValueChange = { onUpdate { s -> s.copy(heightIn = it) } },
                    label = { Text("Inches") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (customQty) {
                    OutlinedTextField(
                        value = customQtyText,
                        onValueChange = { v ->
                            customQtyText = v
                            val parsed = v.toIntOrNull()
                            if (parsed != null && parsed > 0) {
                                onUpdate { it.copy(quantity = parsed) }
                            }
                        },
                        label = { Text("Qty") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        trailingIcon = {
                            IconButton(onClick = { customQty = false }, modifier = Modifier.size(20.dp)) {
                                Icon(Icons.Filled.Close, "Back to list", modifier = Modifier.size(16.dp))
                            }
                        }
                    )
                } else {
                    ExposedDropdownMenuBox(
                        expanded = qtyExpanded,
                        onExpandedChange = { qtyExpanded = it },
                        modifier = Modifier.weight(1f)
                    ) {
                        OutlinedTextField(
                            value = "${item.quantity}",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Qty") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = qtyExpanded) },
                            modifier = Modifier.menuAnchor().fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp)
                        )
                        ExposedDropdownMenu(expanded = qtyExpanded, onDismissRequest = { qtyExpanded = false }) {
                            (1..10).forEach { qty ->
                                DropdownMenuItem(
                                    text = { Text("$qty") },
                                    onClick = {
                                        onUpdate { it.copy(quantity = qty) }
                                        qtyExpanded = false
                                    }
                                )
                            }
                            DropdownMenuItem(
                                text = { Text("Custom...") },
                                onClick = {
                                    customQty = true
                                    customQtyText = "${item.quantity}"
                                    qtyExpanded = false
                                }
                            )
                        }
                    }
                }
                OutlinedTextField(
                    value = item.pricePerSqFt,
                    onValueChange = { onUpdate { s -> s.copy(pricePerSqFt = it) } },
                    label = { Text("$currency/sq ft") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        "Area: ${"%.2f".format(orderItem.areaSqFt)} sq ft",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    if (item.quantity > 1) {
                        Text(
                            "Total Area: ${"%.2f".format(orderItem.totalArea)} sq ft",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                Text(
                    text = "$currency ${"%.2f".format(orderItem.totalPrice)}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
