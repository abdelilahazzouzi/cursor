package com.azizgraphics.clcltr.util

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import androidx.core.content.FileProvider
import com.azizgraphics.clcltr.data.entity.Order
import com.azizgraphics.clcltr.data.model.OrderItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object ShareUtil {

    fun parseItems(json: String): List<OrderItem> {
        val type = object : TypeToken<List<OrderItem>>() {}.type
        return Gson().fromJson(json, type)
    }

    fun formatOrderText(order: Order): String {
        val items = parseItems(order.itemsJson)
        val dateStr = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
            .format(Date(order.date))

        val sb = StringBuilder()
        sb.appendLine("═══════════════════════════════")
        sb.appendLine("  AZIZ-GRAPHICS")
        sb.appendLine("  Banner Calculator")
        sb.appendLine("═══════════════════════════════")
        sb.appendLine("Order: ${order.name}")
        sb.appendLine("Date: $dateStr")
        sb.appendLine("───────────────────────────────")

        items.forEachIndexed { i, item ->
            sb.appendLine("Item ${i + 1}: ${item.name.ifEmpty { "Banner ${i + 1}" }}")
            if (!item.material.isNullOrEmpty()) sb.appendLine("  Material: ${item.material}")
            sb.appendLine("  Size: ${item.dimensionString()}")
            sb.appendLine("  Area: ${"%.2f".format(item.areaSqFt)} sq ft")
            sb.appendLine("  Qty: ${item.quantity}")
            sb.appendLine("  Rate: ${order.currency} ${"%.2f".format(item.pricePerSqFt)}/sq ft")
            sb.appendLine("  Total: ${order.currency} ${"%.2f".format(item.totalPrice)}")
            sb.appendLine("───────────────────────────────")
        }

        sb.appendLine("Total Area: ${"%.2f".format(order.totalArea)} sq ft")
        sb.appendLine("Total Qty: ${order.totalQuantity}")
        sb.appendLine("TOTAL: ${order.currency} ${"%.2f".format(order.totalAmount)}")
        sb.appendLine("═══════════════════════════════")
        return sb.toString()
    }

    fun shareAsText(context: Context, order: Order) {
        val text = formatOrderText(order)
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, text)
            putExtra(Intent.EXTRA_SUBJECT, "Order: ${order.name}")
        }
        context.startActivity(Intent.createChooser(intent, "Share Order"))
    }

    fun shareAsImage(context: Context, order: Order) {
        val text = formatOrderText(order)
        val lines = text.lines()

        val paint = Paint().apply {
            color = android.graphics.Color.BLACK
            textSize = 32f
            isAntiAlias = true
            typeface = Typeface.MONOSPACE
        }

        val lineHeight = 42f
        val padding = 40f
        val maxWidth = lines.maxOf { paint.measureText(it) } + padding * 2
        val height = (lines.size * lineHeight + padding * 2).toInt()

        val bitmap = Bitmap.createBitmap(maxWidth.toInt(), height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(android.graphics.Color.WHITE)

        lines.forEachIndexed { i, line ->
            canvas.drawText(line, padding, padding + (i + 1) * lineHeight, paint)
        }

        val file = File(context.cacheDir, "order_${order.id}.png")
        FileOutputStream(file).use { bitmap.compress(Bitmap.CompressFormat.PNG, 100, it) }

        val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "image/png"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(intent, "Share Order Image"))
    }
}
