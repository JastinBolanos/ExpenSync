package com.jastin.expensync.features.expense

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BeachAccess
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.BreakfastDining
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.DeliveryDining
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Hotel
import androidx.compose.material.icons.filled.LocalBar
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material.icons.filled.LocalGasStation
import androidx.compose.material.icons.filled.LocalParking
import androidx.compose.material.icons.filled.LocalPharmacy
import androidx.compose.material.icons.filled.LocalTaxi
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Toll
import androidx.compose.material.icons.filled.Wifi
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
        keywords = listOf("cena", "comida", "almuerzo", "desayuno", "pizza", "burger", "tacos", "bar", "cerveza", "café", "cafe", "restaurante", "drinks", "sushi", "delivery")
    ),
    TRANSPORT(
        label = "Transporte & Viajes",
        icon = Icons.Default.DirectionsCar,
        accentColor = Color(0xFF2EC4B6),
        keywords = listOf("uber", "taxi", "gasolina", "peaje", "metro", "bus", "vuelo", "vuelos", "avion", "gas", "estacionamiento", "parking", "viaje")
    ),
    SHOPPING(
        label = "Supermercado & Compras",
        icon = Icons.Default.ShoppingCart,
        accentColor = Color(0xFF10B981),
        keywords = listOf("super", "mercado", "compras", "oxxo", "tienda", "walmart", "despensa", "market", "groceries", "ropa")
    ),
    HOUSING(
        label = "Alojamiento & Servicios",
        icon = Icons.Default.Home,
        accentColor = LogoCopper,
        keywords = listOf("hotel", "airbnb", "casa", "alquiler", "renta", "luz", "agua", "internet", "wifi", "piso")
    ),
    ENTERTAINMENT(
        label = "Ocio & Salidas",
        icon = Icons.Default.ConfirmationNumber,
        accentColor = Color(0xFFA855F7),
        keywords = listOf("cine", "fiesta", "concierto", "evento", "juego", "boliche", "party", "bolera", "teatro", "entradas")
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

data class ConceptItem(
    val name: String,
    val icon: ImageVector,
    val category: ExpenseCategory,
    val accentColor: Color = category.accentColor
)

object ExpenseConcepts {
    val ALL = listOf(
        ConceptItem("Cena", Icons.Default.Restaurant, ExpenseCategory.FOOD, LogoGold),
        ConceptItem("Supermercado", Icons.Default.ShoppingCart, ExpenseCategory.SHOPPING, Color(0xFF10B981)),
        ConceptItem("Almuerzo", Icons.Default.Restaurant, ExpenseCategory.FOOD, LogoGold),
        ConceptItem("Desayuno", Icons.Default.BreakfastDining, ExpenseCategory.FOOD, LogoGold),
        ConceptItem("Café", Icons.Default.LocalCafe, ExpenseCategory.FOOD, LogoGold),
        ConceptItem("Bar & Copas", Icons.Default.LocalBar, ExpenseCategory.FOOD, LogoCopper),
        ConceptItem("Uber / Taxi", Icons.Default.LocalTaxi, ExpenseCategory.TRANSPORT, Color(0xFF2EC4B6)),
        ConceptItem("Gasolina", Icons.Default.LocalGasStation, ExpenseCategory.TRANSPORT, Color(0xFF2EC4B6)),
        ConceptItem("Peaje", Icons.Default.Toll, ExpenseCategory.TRANSPORT, Color(0xFF2EC4B6)),
        ConceptItem("Estacionamiento", Icons.Default.LocalParking, ExpenseCategory.TRANSPORT, Color(0xFF2EC4B6)),
        ConceptItem("Vuelos", Icons.Default.Flight, ExpenseCategory.TRANSPORT, Color(0xFF38BDF8)),
        ConceptItem("Hotel & Hospedaje", Icons.Default.Hotel, ExpenseCategory.HOUSING, LogoCopper),
        ConceptItem("Transporte", Icons.Default.DirectionsBus, ExpenseCategory.TRANSPORT, Color(0xFF2EC4B6)),
        ConceptItem("Entradas & Conciertos", Icons.Default.ConfirmationNumber, ExpenseCategory.ENTERTAINMENT, Color(0xFFA855F7)),
        ConceptItem("Cine", Icons.Default.Movie, ExpenseCategory.ENTERTAINMENT, Color(0xFFA855F7)),
        ConceptItem("Farmacia & Salud", Icons.Default.LocalPharmacy, ExpenseCategory.GENERAL, Color(0xFFF43F5E)),
        ConceptItem("Compras", Icons.Default.ShoppingCart, ExpenseCategory.SHOPPING, Color(0xFF10B981)),
        ConceptItem("Ropa", Icons.Default.Checkroom, ExpenseCategory.SHOPPING, Color(0xFFEC4899)),
        ConceptItem("Servicios (Luz/Agua)", Icons.Default.Bolt, ExpenseCategory.HOUSING, LogoGold),
        ConceptItem("Internet & Teléfono", Icons.Default.Wifi, ExpenseCategory.HOUSING, Color(0xFF38BDF8)),
        ConceptItem("Fiesta", Icons.Default.Celebration, ExpenseCategory.ENTERTAINMENT, Color(0xFFA855F7)),
        ConceptItem("Regalo", Icons.Default.CardGiftcard, ExpenseCategory.GENERAL, LogoCopper),
        ConceptItem("Gimnasio & Deportes", Icons.Default.FitnessCenter, ExpenseCategory.GENERAL, Color(0xFF34D399)),
        ConceptItem("Delivery", Icons.Default.DeliveryDining, ExpenseCategory.FOOD, LogoGold),
        ConceptItem("Comida rápida", Icons.Default.Fastfood, ExpenseCategory.FOOD, LogoGold),
        ConceptItem("Piso / Alquiler", Icons.Default.Home, ExpenseCategory.HOUSING, LogoCopper),
        ConceptItem("Mantenimiento", Icons.Default.Build, ExpenseCategory.GENERAL, LogoSlate),
        ConceptItem("Mascotas", Icons.Default.Pets, ExpenseCategory.GENERAL, Color(0xFFF59E0B)),
        ConceptItem("Libros & Cursos", Icons.Default.MenuBook, ExpenseCategory.GENERAL, Color(0xFF818CF8)),
        ConceptItem("Viaje / Vacaciones", Icons.Default.BeachAccess, ExpenseCategory.TRANSPORT, Color(0xFF38BDF8))
    )

    fun getIconForTitle(title: String): ImageVector {
        val lower = title.lowercase().trim()
        val exactMatch = ALL.find { it.name.equals(title, ignoreCase = true) }
        if (exactMatch != null) return exactMatch.icon

        val partial = ALL.find { lower.contains(it.name.lowercase()) || it.name.lowercase().contains(lower) }
        if (partial != null) return partial.icon

        return when {
            lower.contains("vuelo") || lower.contains("avion") || lower.contains("aeropuerto") -> Icons.Default.Flight
            lower.contains("cafe") || lower.contains("café") || lower.contains("starbucks") -> Icons.Default.LocalCafe
            lower.contains("bar") || lower.contains("copa") || lower.contains("cerveza") -> Icons.Default.LocalBar
            lower.contains("uber") || lower.contains("taxi") || lower.contains("cabify") -> Icons.Default.LocalTaxi
            lower.contains("gasolina") || lower.contains("combustible") -> Icons.Default.LocalGasStation
            lower.contains("cine") || lower.contains("pelicula") -> Icons.Default.Movie
            lower.contains("hotel") || lower.contains("airbnb") || lower.contains("hospedaje") -> Icons.Default.Hotel
            lower.contains("super") || lower.contains("mercado") || lower.contains("compra") -> Icons.Default.ShoppingCart
            lower.contains("cena") || lower.contains("almuerzo") || lower.contains("comida") || lower.contains("restaurant") -> Icons.Default.Restaurant
            lower.contains("gym") || lower.contains("gimnasio") -> Icons.Default.FitnessCenter
            lower.contains("mascota") || lower.contains("perro") || lower.contains("gato") || lower.contains("vet") -> Icons.Default.Pets
            lower.contains("fiesta") || lower.contains("party") -> Icons.Default.Celebration
            lower.contains("regalo") -> Icons.Default.CardGiftcard
            lower.contains("ropa") || lower.contains("zapatos") -> Icons.Default.Checkroom
            lower.contains("farmacia") || lower.contains("medicina") || lower.contains("doctor") -> Icons.Default.LocalPharmacy
            else -> ExpenseCategory.detect(title).icon
        }
    }

    fun getColorForTitle(title: String): Color {
        val exactMatch = ALL.find { it.name.equals(title, ignoreCase = true) }
        if (exactMatch != null) return exactMatch.accentColor

        val lower = title.lowercase().trim()
        val partial = ALL.find { lower.contains(it.name.lowercase()) }
        if (partial != null) return partial.accentColor

        return ExpenseCategory.detect(title).accentColor
    }
}
