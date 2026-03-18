package com.azizgraphics.clcltr.ui.screens.settings

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Brightness4
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.azizgraphics.clcltr.data.entity.Customer
import com.azizgraphics.clcltr.data.entity.Material
import com.azizgraphics.clcltr.data.model.Currency
import com.azizgraphics.clcltr.ui.theme.BlueDark
import com.azizgraphics.clcltr.ui.theme.BluePrimary
import com.azizgraphics.clcltr.ui.theme.ErrorRed
import com.azizgraphics.clcltr.ui.theme.GlassBorder
import com.azizgraphics.clcltr.viewmodel.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = viewModel(),
    onThemeChanged: (Int) -> Unit
) {
    val useFeet by viewModel.useFeet.collectAsState()
    val defaultPrice by viewModel.defaultPrice.collectAsState()
    val themeMode by viewModel.themeMode.collectAsState()
    val currency by viewModel.currency.collectAsState()
    val materials by viewModel.materials.collectAsState()
    val customers by viewModel.customers.collectAsState()

    var priceText by remember(defaultPrice) { mutableStateOf(defaultPrice.toString()) }
    var showAddMaterial by remember { mutableStateOf(false) }
    var showAddCustomer by remember { mutableStateOf(false) }
    var currExpanded by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Settings",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        item {
            SectionHeader(icon = Icons.Filled.Settings, title = "Defaults")
        }

        item {
            SettingsCard {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Straighten, null, Modifier.size(20.dp), MaterialTheme.colorScheme.primary)
                        Spacer(Modifier.width(12.dp))
                        Text("Default Unit: Feet", style = MaterialTheme.typography.bodyLarge)
                    }
                    Switch(
                        checked = useFeet,
                        onCheckedChange = { viewModel.setUseFeet(it) }
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = priceText,
                    onValueChange = {
                        priceText = it
                        it.toDoubleOrNull()?.let { p -> viewModel.setDefaultPrice(p) }
                    },
                    label = { Text("Default Price per sq ft") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true
                )
            }
        }

        item {
            SectionHeader(icon = Icons.Filled.Palette, title = "Appearance")
        }

        item {
            SettingsCard {
                Text("Theme", style = MaterialTheme.typography.labelLarge)
                Spacer(modifier = Modifier.height(8.dp))
                SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                    listOf("System", "Light", "Dark").forEachIndexed { i, label ->
                        SegmentedButton(
                            selected = themeMode == i,
                            onClick = {
                                viewModel.setThemeMode(i)
                                onThemeChanged(i)
                            },
                            shape = SegmentedButtonDefaults.itemShape(i, 3)
                        ) {
                            Text(label)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                ExposedDropdownMenuBox(expanded = currExpanded, onExpandedChange = { currExpanded = it }) {
                    OutlinedTextField(
                        value = currency,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Currency") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = currExpanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp)
                    )
                    ExposedDropdownMenu(expanded = currExpanded, onDismissRequest = { currExpanded = false }) {
                        Currency.entries.forEach { c ->
                            DropdownMenuItem(
                                text = { Text("${c.symbol} - ${c.displayName}") },
                                onClick = {
                                    viewModel.setCurrency(c.name)
                                    currExpanded = false
                                }
                            )
                        }
                    }
                }
            }
        }

        item {
            SectionHeader(icon = Icons.Filled.Brightness4, title = "Materials")
        }

        item {
            SettingsCard {
                if (materials.isEmpty()) {
                    Text("No materials added yet", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                materials.forEach { mat ->
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(mat.name, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
                            Text("$currency ${mat.defaultPrice}/sq ft", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        IconButton(onClick = { viewModel.deleteMaterial(mat) }, modifier = Modifier.size(32.dp)) {
                            Icon(Icons.Filled.Delete, "Delete", tint = ErrorRed, modifier = Modifier.size(18.dp))
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                FilledTonalButton(
                    onClick = { showAddMaterial = true },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(Icons.Filled.Add, null, Modifier.size(18.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("Add Material")
                }
            }
        }

        item {
            SectionHeader(icon = Icons.Filled.Person, title = "Customers")
        }

        item {
            SettingsCard {
                if (customers.isEmpty()) {
                    Text("No customers added yet", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                customers.forEach { cust ->
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(cust.name, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
                            if (cust.phone.isNotEmpty()) {
                                Text(cust.phone, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                        IconButton(onClick = { viewModel.deleteCustomer(cust) }, modifier = Modifier.size(32.dp)) {
                            Icon(Icons.Filled.Delete, "Delete", tint = ErrorRed, modifier = Modifier.size(18.dp))
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                FilledTonalButton(
                    onClick = { showAddCustomer = true },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(Icons.Filled.Add, null, Modifier.size(18.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("Add Customer")
                }
            }
        }

        item {
            AboutSection()
        }

        item { Spacer(modifier = Modifier.height(80.dp)) }
    }

    if (showAddMaterial) {
        AddMaterialDialog(
            onDismiss = { showAddMaterial = false },
            onAdd = { name, price ->
                viewModel.addMaterial(name, price)
                showAddMaterial = false
            }
        )
    }

    if (showAddCustomer) {
        AddCustomerDialog(
            onDismiss = { showAddCustomer = false },
            onAdd = { name, phone ->
                viewModel.addCustomer(name, phone)
                showAddCustomer = false
            }
        )
    }
}

@Composable
private fun SectionHeader(icon: ImageVector, title: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, Modifier.size(20.dp), MaterialTheme.colorScheme.primary)
        Spacer(Modifier.width(8.dp))
        Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun SettingsCard(content: @Composable () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            content()
        }
    }
}

@Composable
private fun AboutSection() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        border = BorderStroke(1.dp, GlassBorder)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(listOf(BluePrimary.copy(alpha = 0.7f), BlueDark.copy(alpha = 0.85f)))
                )
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            Brush.linearGradient(listOf(Color(0xFF1A73E8), Color(0xFF00BCD4)))
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "AG",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Serif,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "\uD835\uDDB0\uD835\uDDBB\uD835\uDDB8\uD835\uDDBB-\uD835\uDDB6\uD835\uDDC8\uD835\uDDB0\uD835\uDDC5\uD835\uDDB7\uD835\uDDB8\uD835\uDDB2\uD835\uDDCA",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text("0305-3345518", color = Color.White.copy(alpha = 0.9f), style = MaterialTheme.typography.bodyLarge)
                Text("0305-3345518", color = Color.White.copy(alpha = 0.9f), style = MaterialTheme.typography.bodyLarge)

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    "Banner Calculator v1.0",
                    color = Color.White.copy(alpha = 0.6f),
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}

@Composable
private fun AddMaterialDialog(onDismiss: () -> Unit, onAdd: (String, Double) -> Unit) {
    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Material", fontFamily = FontFamily.Serif) },
        text = {
            Column {
                OutlinedTextField(
                    value = name, onValueChange = { name = it },
                    label = { Text("Material Name") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    singleLine = true
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = price, onValueChange = { price = it },
                    label = { Text("Default Price/sq ft") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { if (name.isNotBlank()) onAdd(name, price.toDoubleOrNull() ?: 0.0) },
                enabled = name.isNotBlank()
            ) { Text("Add") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}

@Composable
private fun AddCustomerDialog(onDismiss: () -> Unit, onAdd: (String, String) -> Unit) {
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Customer", fontFamily = FontFamily.Serif) },
        text = {
            Column {
                OutlinedTextField(
                    value = name, onValueChange = { name = it },
                    label = { Text("Customer Name") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    singleLine = true
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = phone, onValueChange = { phone = it },
                    label = { Text("Phone Number") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { if (name.isNotBlank()) onAdd(name, phone) },
                enabled = name.isNotBlank()
            ) { Text("Add") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}
