package com.azizgraphics.clcltr.ui.screens.history

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.azizgraphics.clcltr.data.entity.Order
import com.azizgraphics.clcltr.data.model.OrderItem
import com.azizgraphics.clcltr.ui.theme.BluePrimary
import com.azizgraphics.clcltr.ui.theme.ErrorRed
import com.azizgraphics.clcltr.ui.theme.TextGray
import com.azizgraphics.clcltr.util.ShareUtil
import com.azizgraphics.clcltr.viewmodel.HistoryViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HistoryScreen(viewModel: HistoryViewModel = viewModel()) {
    val orders by viewModel.orders.collectAsState()

    if (orders.isEmpty()) {
        EmptyHistoryView()
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    text = "Order History",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${orders.size} saved orders",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            items(orders, key = { it.id }) { order ->
                OrderCard(
                    order = order,
                    onDelete = { viewModel.deleteOrder(order) }
                )
            }

            item { Spacer(modifier = Modifier.height(80.dp)) }
        }
    }
}

@Composable
private fun EmptyHistoryView() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Schedule,
                    contentDescription = null,
                    modifier = Modifier.size(40.dp),
                    tint = TextGray
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "No orders yet",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Your saved orders will appear here",
                style = MaterialTheme.typography.bodyMedium,
                color = TextGray,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun OrderCard(order: Order, onDelete: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val dateStr = remember(order.date) {
        SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(Date(order.date))
    }
    val items = remember(order.itemsJson) { ShareUtil.parseItems(order.itemsJson) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = order.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(text = dateStr, style = MaterialTheme.typography.labelSmall, color = TextGray)
                }
                Icon(
                    imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                    contentDescription = "Expand",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Total", style = MaterialTheme.typography.labelSmall, color = TextGray)
                    Text(
                        "${order.currency} ${"%.2f".format(order.totalAmount)}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = BluePrimary
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Area", style = MaterialTheme.typography.labelSmall, color = TextGray)
                    Text(
                        "${"%.1f".format(order.totalArea)} sq ft",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("Qty", style = MaterialTheme.typography.labelSmall, color = TextGray)
                    Text("${order.totalQuantity}", style = MaterialTheme.typography.bodyMedium)
                }
            }

            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Column {
                    Spacer(modifier = Modifier.height(12.dp))

                    DimensionGridView(items = items)

                    Spacer(modifier = Modifier.height(12.dp))

                    items.forEachIndexed { i, item ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            )
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    item.name.ifEmpty { "Item ${i + 1}" },
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold
                                )
                                if (!item.material.isNullOrEmpty()) {
                                    Text("Material: ${item.material}", style = MaterialTheme.typography.bodyMedium, color = TextGray)
                                }
                                Text("Size: ${item.dimensionString()} • Qty: ${item.quantity}", style = MaterialTheme.typography.bodyMedium, color = TextGray)
                                Text(
                                    "${order.currency} ${"%.2f".format(item.totalPrice)}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = BluePrimary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { ShareUtil.shareAsText(context, order) }) {
                            Icon(Icons.Filled.Share, "Share Text", tint = BluePrimary, modifier = Modifier.size(20.dp))
                        }
                        IconButton(onClick = { ShareUtil.shareAsImage(context, order) }) {
                            Icon(Icons.Filled.Image, "Share Image", tint = BluePrimary, modifier = Modifier.size(20.dp))
                        }
                        IconButton(onClick = onDelete) {
                            Icon(Icons.Filled.Delete, "Delete", tint = ErrorRed, modifier = Modifier.size(20.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DimensionGridView(items: List<OrderItem>) {
    val primary = BluePrimary
    val gridColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f)

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
    ) {
        val dotSpacing = 20f
        val dash = PathEffect.dashPathEffect(floatArrayOf(4f, 8f))
        for (x in 0..(size.width / dotSpacing).toInt()) {
            for (y in 0..(size.height / dotSpacing).toInt()) {
                drawCircle(gridColor, 1.5f, Offset(x * dotSpacing, y * dotSpacing))
            }
        }

        val maxDim = items.maxOfOrNull { maxOf(it.widthTotalFt, it.heightTotalFt) } ?: 1.0
        val scale = minOf(size.width, size.height) * 0.7f / maxDim.toFloat()
        val padding = 20f
        var xOffset = padding

        items.forEachIndexed { _, item ->
            val w = (item.widthTotalFt * scale).toFloat().coerceAtLeast(30f)
            val h = (item.heightTotalFt * scale).toFloat().coerceAtLeast(20f)

            if (xOffset + w > size.width - padding) {
                xOffset = padding
            }

            val yCenter = (size.height - h) / 2

            drawRect(
                color = primary.copy(alpha = 0.15f),
                topLeft = Offset(xOffset, yCenter),
                size = Size(w, h)
            )
            drawRect(
                color = primary,
                topLeft = Offset(xOffset, yCenter),
                size = Size(w, h),
                style = Stroke(width = 2f, pathEffect = dash)
            )

            xOffset += w + 12f
        }
    }
}
