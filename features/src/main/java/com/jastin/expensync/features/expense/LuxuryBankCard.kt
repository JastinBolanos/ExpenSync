package com.jastin.expensync.features.expense

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.abs

/**
 * An ultra-luxurious, professional bank card component (Obsidian Titanium & Brushed Gold)
 * with a subtle floating animation, metallic EMV smart chip, contactless payment icon,
 * dual-ring luxury emblem, and real-time balance metrics.
 */
@Composable
fun LuxuryBankCard(
    totalSpent: Double,
    myNetBalance: Double,
    transactionsCount: Int,
    modifier: Modifier = Modifier
) {
    // 1. Subtle, smooth floating animation (floating up and down)
    val infiniteTransition = rememberInfiniteTransition(label = "luxury_card_float_anim")

    val floatOffsetY by infiniteTransition.animateFloat(
        initialValue = -5.5f,
        targetValue = 5.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "float_offset_y"
    )

    // Subtle breathing shadow intensity synchronized with hover
    val shadowScale by infiniteTransition.animateFloat(
        initialValue = 0.94f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "shadow_scale"
    )

    // Periodic diagonal light shimmer traveling across the metallic surface
    val shimmerProgress by infiniteTransition.animateFloat(
        initialValue = -0.5f,
        targetValue = 1.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 4000, delayMillis = 800, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "metallic_shimmer"
    )

    val cardShape = RoundedCornerShape(22.dp)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
            // Ambient Luxury Drop Glow (Simulating physical floating elevation over surface)
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.92f)
                    .height(180.dp)
                    .graphicsLayer {
                        translationY = floatOffsetY + 16f
                        scaleX = shadowScale
                        scaleY = shadowScale
                        alpha = 0.45f
                    }
                    .clip(RoundedCornerShape(32.dp))
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                LogoGold.copy(alpha = 0.28f),
                                LogoCopper.copy(alpha = 0.16f),
                                Color(0xFF041411).copy(alpha = 0.40f),
                                Color.Transparent
                            )
                        )
                    )
            )

            // Main Floating Bank Card Container
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .graphicsLayer {
                        translationY = floatOffsetY
                    }
                    .clip(cardShape)
                    // Multi-layered metallic obsidian & midnight petrol gradient
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                Color(0xFF142923), // Top-left brushed metallic pine obsidian
                                Color(0xFF0C1B17), // Mid deep obsidian
                                Color(0xFF06110F), // Bottom dark shadow
                                Color(0xFF0A221C)  // Subtle bottom-right luster
                            ),
                            start = Offset(0f, 0f),
                            end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                        )
                    )
                    // Luxury Chamfered Metallic Gold & Platinum Border
                    .border(
                        width = 1.2.dp,
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color(0xFFFFE082).copy(alpha = 0.85f), // Radiant champagne gold
                                LogoGold.copy(alpha = 0.65f),
                                Color(0xFF8D733B).copy(alpha = 0.35f),
                                LogoCopper.copy(alpha = 0.55f),
                                Color(0xFFFFFFFF).copy(alpha = 0.40f),
                                LogoGold.copy(alpha = 0.75f)
                            ),
                            start = Offset(0f, 0f),
                            end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                        ),
                        shape = cardShape
                    )
                    // Diagonal metallic reflection gleam
                    .drawWithCache {
                        val w = size.width
                        val h = size.height
                        val beamWidth = w * 0.45f
                        val currentX = (w + beamWidth) * shimmerProgress - (beamWidth / 2)

                        val shimmerBrush = Brush.linearGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color(0xFFFFD54F).copy(alpha = 0.05f),
                                Color.White.copy(alpha = 0.14f),
                                Color(0xFFFFE082).copy(alpha = 0.08f),
                                Color.Transparent
                            ),
                            start = Offset(currentX, 0f),
                            end = Offset(currentX + beamWidth, h)
                        )

                        onDrawWithContent {
                            drawContent()
                            drawRect(shimmerBrush)
                        }
                    }
                    .padding(horizontal = 20.dp, vertical = 20.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Top Row: EMV Chip + Contactless Wave (Left) | Bank Branding (Right)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Left: EMV Smart Chip + Contactless Payment Wave
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            EmvGoldChip()
                            ContactlessWavesIcon(modifier = Modifier.size(20.dp))
                        }

                        // Right: Net Balance Status replacing EXPENSYNC
                        val isPositive = myNetBalance > 0.01
                        val isNegative = myNetBalance < -0.01
                        val targetStatusColor = when {
                            isPositive -> NeonEmerald
                            isNegative -> LogoCopper
                            else -> LogoGold
                        }
                        val statusColor by animateColorAsState(
                            targetValue = targetStatusColor,
                            label = "card_status_color"
                        )

                        val statusLabel = when {
                            isPositive -> "Te deben"
                            isNegative -> "Debes"
                            else -> "Equilibrado"
                        }

                        // Floating frameless balance status with elegant Serif typography
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(statusColor)
                            )

                            Text(
                                text = statusLabel,
                                fontSize = 13.sp,
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.4.sp,
                                color = statusColor
                            )

                            if (isPositive || isNegative) {
                                AnimatedAmountText(
                                    targetAmount = abs(myNetBalance),
                                    prefix = "$",
                                    fontSize = 13.5.sp,
                                    fontFamily = FontFamily.Serif,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.4.sp,
                                    color = statusColor,
                                    durationMillis = 1100
                                )
                            }
                        }
                    }

                    // Middle Section: Total Balance Display
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Text(
                            text = "TOTAL GASTADO",
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.8.sp,
                            color = Color(0xFFA5B8B4)
                        )

                        AnimatedAmountText(
                            targetAmount = totalSpent,
                            prefix = "$",
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White,
                            durationMillis = 1300
                        )
                    }

                    // Bottom Row inside Card: Masked Card Number & Dual Rings Emblem
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "••••   ••••   ••••   2026",
                            fontSize = 12.5.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Medium,
                            letterSpacing = 2.2.sp,
                            color = Color(0xFF7E9490)
                        )

                        DualInterlockingEmblem()
                    }
                }
            }
        }
    }

/**
 * Net Balance Status Capsule Pill (placed outside the bank card)
 */
@Composable
fun BalanceStatusPill(
    myNetBalance: Double,
    transactionsCount: Int,
    modifier: Modifier = Modifier
) {
    val isPositive = myNetBalance > 0.01
    val isNegative = myNetBalance < -0.01
    val targetStatusColor = when {
        isPositive -> NeonEmerald
        isNegative -> LogoCopper
        else -> LogoGold
    }
    val statusColor by animateColorAsState(
        targetValue = targetStatusColor,
        label = "card_status_color"
    )

    val statusLabel = when {
        isPositive -> "Te deben"
        isNegative -> "Debes"
        else -> "Cuentas equilibradas"
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(7.dp),
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFF051713).copy(alpha = 0.90f))
            .border(
                width = 0.8.dp,
                color = statusColor.copy(alpha = 0.45f),
                shape = RoundedCornerShape(10.dp)
            )
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        // Pulsing status dot
        Box(
            modifier = Modifier
                .size(7.dp)
                .clip(CircleShape)
                .background(statusColor)
        )

        Text(
            text = statusLabel,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = statusColor
        )

        if (isPositive || isNegative) {
            AnimatedAmountText(
                targetAmount = abs(myNetBalance),
                prefix = "$",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = statusColor,
                durationMillis = 1100
            )
        }

        Text(
            text = "•",
            fontSize = 10.sp,
            color = LogoSlate
        )

        Text(
            text = "$transactionsCount trans.",
            fontSize = 11.5.sp,
            fontWeight = FontWeight.Medium,
            color = TextSecondary
        )
    }
}

/**
 * Detailed EMV Metallic Gold Smart Chip with simulated etched circuitry
 */
@Composable
private fun EmvGoldChip(
    modifier: Modifier = Modifier
) {
    val chipShape = RoundedCornerShape(6.dp)

    Box(
        modifier = modifier
            .size(width = 36.dp, height = 27.dp)
            .clip(chipShape)
            // Brushed brass-gold base
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color(0xFFFFDE82), // Highlight
                        Color(0xFFD4AF37), // Metallic gold
                        Color(0xFF9E7E24), // Shadow
                        Color(0xFFECC763)  // Secondary luster
                    ),
                    start = Offset(0f, 0f),
                    end = Offset(36f, 27f)
                )
            )
            .border(
                width = 0.8.dp,
                color = Color(0xFF6A5416),
                shape = chipShape
            )
    ) {
        // Draw the authentic etched circuitry lines onto the chip
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height
            val lineStroke = 0.8.dp.toPx()
            val circuitColor = Color(0xFF5A430F).copy(alpha = 0.65f)

            // Center horizontal division
            drawLine(
                color = circuitColor,
                start = Offset(0f, h * 0.5f),
                end = Offset(w, h * 0.5f),
                strokeWidth = lineStroke
            )

            // Left vertical branch
            drawLine(
                color = circuitColor,
                start = Offset(w * 0.32f, 0f),
                end = Offset(w * 0.32f, h),
                strokeWidth = lineStroke
            )

            // Right vertical branch
            drawLine(
                color = circuitColor,
                start = Offset(w * 0.68f, 0f),
                end = Offset(w * 0.68f, h),
                strokeWidth = lineStroke
            )

            // Center contact cell
            drawRoundRect(
                color = circuitColor,
                topLeft = Offset(w * 0.32f, h * 0.32f),
                size = Size(w * 0.36f, h * 0.36f),
                cornerRadius = CornerRadius(2.dp.toPx(), 2.dp.toPx()),
                style = Stroke(width = lineStroke)
            )
        }
    }
}

/**
 * Contactless / NFC Payment Waves Icon rendered via custom Canvas arcs
 */
@Composable
private fun ContactlessWavesIcon(
    modifier: Modifier = Modifier,
    color: Color = LogoGold.copy(alpha = 0.85f)
) {
    Canvas(modifier = modifier) {
        val strokeWidth = 1.4.dp.toPx()
        val center = Offset(size.width * 0.2f, size.height * 0.5f)
        val radii = listOf(size.width * 0.35f, size.width * 0.58f, size.width * 0.82f)

        radii.forEach { r ->
            drawArc(
                color = color,
                startAngle = -42f,
                sweepAngle = 84f,
                useCenter = false,
                topLeft = Offset(center.x - r, center.y - r),
                size = Size(r * 2, r * 2),
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
        }
    }
}

/**
 * Dual Interlocking Frosted Metallic Circles (Luxury Credit Network Emblem)
 */
@Composable
private fun DualInterlockingEmblem(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(contentAlignment = Alignment.Center) {
            // First Circle: Radiant Gold
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            listOf(
                                LogoGold.copy(alpha = 0.85f),
                                Color(0xFFC78B0C).copy(alpha = 0.70f)
                            )
                        )
                    )
            )

            // Second Overlapping Circle: Radiant Copper / Rose Gold
            Box(
                modifier = Modifier
                    .padding(start = 14.dp)
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            listOf(
                                LogoCopper.copy(alpha = 0.85f),
                                Color(0xFFA14908).copy(alpha = 0.70f)
                            )
                        )
                    )
            )
        }
    }
}
