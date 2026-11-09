package com.jastin.expensync.features.expense

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

enum class ExpenseCategory(
    val label: String,
    val icon: ImageVector,
    val accentColor: Color,
    val keywords: List<String>
) {
    FOOD(
        label = "Comida & Restaurante",
        icon = Icons.Default.Restaurant,
        accentColor = LogoGold,
        keywords = listOf("cena", "comida", "almuerzo", "desayuno", "pizza", "burger", "tacos", "bar", "cerveza", "café", "cafe", "restaurante", "drinks", "sushi")
    ),
    TRANSPORT(
        label = "Transporte & Viajes",
        icon = Icons.Default.DirectionsCar,
        accentColor = Color(0xFF2EC4B6),
        keywords = listOf("uber", "taxi", "gasolina", "peaje", "metro", "bus", "vuelo", "avion", "gas", "estacionamiento", "parking")
    ),
    SHOPPING(
        label = "Supermercado & Compras",
        icon = Icons.Default.ShoppingCart,
        accentColor = Color(0xFF10B981),
        keywords = listOf("super", "mercado", "compras", "oxxo", "tienda", "walmart", "despensa", "market", "groceries")
    ),
    HOUSING(
        label = "Alojamiento & Servicios",
        icon = Icons.Default.Home,
        accentColor = LogoCopper,
        keywords = listOf("hotel", "airbnb", "casa", "alquiler", "renta", "luz", "agua", "internet", "wifi")
    ),
    ENTERTAINMENT(
        label = "Ocio & Salidas",
        icon = Icons.Default.ConfirmationNumber,
        accentColor = Color(0xFFA855F7),
        keywords = listOf("cine", "fiesta", "concierto", "evento", "juego", "boliche", "party", "bolera", "teatro")
    ),
    GENERAL(
        label = "Gastos Generales",
        icon = Icons.Default.ReceiptLong,
        accentColor = LogoSlate,
        keywords = emptyList()
    );

    companion object {
        fun detect(title: String): ExpenseCategory {
            val lower = title.lowercase().trim()
            for (category in entries) {
                if (category == GENERAL) continue
                if (category.keywords.any { lower.contains(it) }) {
                    return category
                }
            }
            return GENERAL
        }
    }
}
