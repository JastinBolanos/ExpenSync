package com.jastin.expensync.features.expense

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// =========================================================================
// Official Brand Palette extracted from the App Logo
// =========================================================================
val LogoGold = Color(0xFFFCB91E)        // Bright Golden Amber (Top-left logo quadrant)
val LogoCopper = Color(0xFFCD680F)      // Warm Terracotta Copper (Top-right logo quadrant)
val LogoSlate = Color(0xFF697978)       // Slate Mineral Steel Gray (Bottom-left logo quadrant)
val LogoPine = Color(0xFF061F1C)        // Deep Obsidian Pine / Petrol Teal (Bottom-right logo quadrant)
val LogoWhite = Color(0xFFFFFFFF)       // Negative-Space Dynamic Ribbon White

// =========================================================================
// Atmospheric Theme Tokens rooted in the 4 Logo Colors
// =========================================================================
val DeepBgStart = Color(0xFF051714)     // Rich Pine Night top
val DeepBgMid = Color(0xFF061F1C)       // Pure Logo Pine Teal
val DeepBgEnd = Color(0xFF020A09)       // Deep Velvet Shadow base

val CardSurface = Color(0xFF0A221E)     // Elevated Pine Slate glass
val CardSurfaceElevated = Color(0xFF0F2D27) // Lighter Pine Glass
val CardSurfaceGlass = Color(0xFF071C18).copy(alpha = 0.94f)

// Signature Accents
val NeonCyan = LogoGold                 // Main high-energy accent = LogoGold
val NeonBlue = LogoSlate                // Secondary structural balance = LogoSlate
val NeonAmber = LogoGold
val NeonRose = LogoCopper               // Warm debt/expense accent = LogoCopper
val NeonEmerald = Color(0xFF10B981)
val NeonPurple = Color(0xFFA855F7)

val BorderSubtle = Color.White.copy(alpha = 0.08f)
val BorderHighlight = LogoGold.copy(alpha = 0.35f)
val BorderCyanGlow = LogoGold.copy(alpha = 0.60f)
val BorderSlate = LogoSlate.copy(alpha = 0.28f)

val TextPrimary = Color(0xFFFFFFFF)
val TextSecondary = Color(0xFFB4C2C0)   // Tinted with Slate Mineral
val TextMuted = LogoSlate               // Exact Slate Mineral Gray

// =========================================================================
// User Avatar Helper connected to the Logo Palette
// =========================================================================
fun getUserColorPair(userName: String): Pair<Color, Color> {
    return when (userName.trim().lowercase()) {
        "yo" -> Pair(LogoGold, LogoCopper)          // Golden Amber & Terracotta Copper (Logo top curve)
        "ana" -> Pair(LogoCopper, Color(0xFFE67E22))
        "carlos" -> Pair(LogoGold, Color(0xFFFFD166))
        "luis" -> Pair(Color(0xFF2EC4B6), LogoPine)  // Emerald Teal & Pine Night
        else -> Pair(LogoSlate, Color(0xFF8FA3A1))
    }
}

@Composable
fun UserAvatar(
    name: String,
    modifier: Modifier = Modifier,
    size: Dp = 36.dp,
    showBorder: Boolean = false,
    borderColor: Color = LogoGold,
    glow: Boolean = false
) {
    val (colorStart, colorEnd) = getUserColorPair(name)
    val initials = name.trim().take(2).uppercase()
    val gradient = Brush.linearGradient(listOf(colorStart, colorEnd))

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        if (showBorder || glow) {
            val infiniteTransition = rememberInfiniteTransition(label = "avatar_glow")
            val auraAlpha by infiniteTransition.animateFloat(
                initialValue = 0.2f,
                targetValue = 0.55f,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = 1500, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "avatar_alpha"
            )
            Box(
                modifier = Modifier
                    .size(size + 6.dp)
                    .graphicsLayer { alpha = auraAlpha }
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            listOf(colorStart.copy(alpha = 0.8f), colorEnd.copy(alpha = 0.2f), Color.Transparent)
                        )
                    )
            )
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(size)
                .clip(CircleShape)
                .background(gradient)
                .then(
                    if (showBorder) {
                        Modifier.border(1.6.dp, borderColor, CircleShape)
                    } else {
                        Modifier.border(1.dp, Color.White.copy(alpha = 0.25f), CircleShape)
                    }
                )
        ) {
            Text(
                text = initials,
                color = Color(0xFF030D0B),
                fontSize = (size.value * 0.38f).sp,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}
