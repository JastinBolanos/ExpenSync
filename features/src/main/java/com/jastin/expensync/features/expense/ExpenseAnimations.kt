package com.jastin.expensync.features.expense

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale

/**
 * Counts up a monetary amount from 0.00 to the target amount like filling a glass.
 */
@Composable
fun AnimatedAmountText(
    targetAmount: Double,
    modifier: Modifier = Modifier,
    prefix: String = "$",
    suffix: String = "",
    fontSize: TextUnit = 16.sp,
    fontWeight: FontWeight = FontWeight.Bold,
    fontFamily: androidx.compose.ui.text.font.FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    color: Color = LogoGold,
    durationMillis: Int = 1100,
    delayMillis: Int = 0
) {
    var isTriggered by remember { mutableStateOf(false) }

    LaunchedEffect(targetAmount) {
        isTriggered = false
        kotlinx.coroutines.delay(16)
        isTriggered = true
    }

    val animatedValue by animateFloatAsState(
        targetValue = if (isTriggered) targetAmount.toFloat() else 0f,
        animationSpec = tween(
            durationMillis = durationMillis,
            delayMillis = delayMillis,
            easing = FastOutSlowInEasing
        ),
        label = "fill_amount"
    )

    Text(
        text = String.format(Locale.US, "%s%.2f%s", prefix, animatedValue, suffix),
        fontSize = fontSize,
        fontWeight = fontWeight,
        fontFamily = fontFamily,
        letterSpacing = letterSpacing,
        color = color,
        modifier = modifier
    )
}

/**
 * A living separator line where an electric beam of logo light (Gold to Copper) continuously travels across it.
 */
@Composable
fun AnimatedLivingLine(
    modifier: Modifier = Modifier.fillMaxWidth(),
    colorA: Color = LogoGold,
    colorB: Color = LogoCopper,
    height: Dp = 1.2.dp,
    durationMillis: Int = 2400
) {
    val infiniteTransition = rememberInfiniteTransition(label = "living_line_transition")
    val progress by infiniteTransition.animateFloat(
        initialValue = -0.3f,
        targetValue = 1.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = durationMillis, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "beam_progress"
    )

    Box(
        modifier = modifier
            .height(height)
            .drawWithCache {
                val width = size.width
                val beamSpan = width * 0.4f
                val currentX = width * progress

                val baseLineColor = Color.White.copy(alpha = 0.05f)
                val brush = Brush.horizontalGradient(
                    colors = listOf(
                        baseLineColor,
                        colorA.copy(alpha = 0.25f),
                        colorA.copy(alpha = 0.95f),
                        LogoWhite.copy(alpha = 0.90f),
                        colorB.copy(alpha = 0.95f),
                        colorB.copy(alpha = 0.25f),
                        baseLineColor
                    ),
                    startX = currentX - beamSpan,
                    endX = currentX + beamSpan
                )

                onDrawBehind {
                    drawLine(
                        color = baseLineColor,
                        start = Offset(0f, size.height / 2),
                        end = Offset(width, size.height / 2),
                        strokeWidth = size.height
                    )
                    drawLine(
                        brush = brush,
                        start = Offset(0f, size.height / 2),
                        end = Offset(width, size.height / 2),
                        strokeWidth = size.height
                    )
                }
            }
    )
}

/**
 * Animated glowing circular category icon that pulses and radiates light in its accent color.
 */
@Composable
fun GlowingCategoryIcon(
    category: ExpenseCategory,
    size: Dp = 38.dp,
    icon: ImageVector? = null,
    accentColor: Color? = null,
    modifier: Modifier = Modifier
) {
    val displayIcon = icon ?: category.icon
    val displayColor = accentColor ?: category.accentColor
    val infiniteTransition = rememberInfiniteTransition(label = "icon_glow")

    // Pulsing aura radius
    val auraScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.35f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "aura_scale"
    )

    // Pulsing aura brightness
    val auraAlpha by infiniteTransition.animateFloat(
        initialValue = 0.20f,
        targetValue = 0.55f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "aura_alpha"
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.size(size + 10.dp)
    ) {
        // Outer glowing halo
        Box(
            modifier = Modifier
                .size(size)
                .graphicsLayer {
                    scaleX = auraScale
                    scaleY = auraScale
                    alpha = auraAlpha
                }
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            displayColor.copy(alpha = 0.85f),
                            displayColor.copy(alpha = 0.3f),
                            Color.Transparent
                        )
                    )
                )
        )

        // Center circular badge with glowing gradient ring on deep pine
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(size)
                .clip(CircleShape)
                .background(Color(0xFF061A17))
                .border(
                    width = 1.4.dp,
                    brush = Brush.sweepGradient(
                        colors = listOf(
                            displayColor.copy(alpha = 0.4f),
                            displayColor,
                            LogoWhite.copy(alpha = 0.9f),
                            displayColor,
                            displayColor.copy(alpha = 0.4f)
                        )
                    ),
                    shape = CircleShape
                )
        ) {
            Icon(
                imageVector = displayIcon,
                contentDescription = category.label,
                tint = displayColor,
                modifier = Modifier.size(size * 0.52f)
            )
        }
    }
}

/**
 * Animated glowing circular avatar that pulses with a personalized logo aura and glowing sweep ring.
 */
@Composable
fun GlowingUserAvatar(
    name: String,
    size: Dp = 32.dp,
    modifier: Modifier = Modifier
) {
    val (colorStart, colorEnd) = getUserColorPair(name)
    val initials = name.trim().take(2).uppercase()
    val infiniteTransition = rememberInfiniteTransition(label = "avatar_glow_$name")

    // Pulsing aura scale
    val auraScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.35f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "aura_scale"
    )

    // Pulsing aura brightness
    val auraAlpha by infiniteTransition.animateFloat(
        initialValue = 0.22f,
        targetValue = 0.60f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "aura_alpha"
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.size(size + 10.dp)
    ) {
        // Outer glowing halo
        Box(
            modifier = Modifier
                .size(size)
                .graphicsLayer {
                    scaleX = auraScale
                    scaleY = auraScale
                    alpha = auraAlpha
                }
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            colorStart.copy(alpha = 0.85f),
                            colorEnd.copy(alpha = 0.35f),
                            Color.Transparent
                        )
                    )
                )
        )

        // Center circular badge with glowing sweep ring
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(size)
                .clip(CircleShape)
                .background(Color(0xFF061A17))
                .border(
                    width = 1.4.dp,
                    brush = Brush.sweepGradient(
                        colors = listOf(
                            colorStart.copy(alpha = 0.4f),
                            colorStart,
                            LogoWhite.copy(alpha = 0.9f),
                            colorEnd,
                            colorStart.copy(alpha = 0.4f)
                        )
                    ),
                    shape = CircleShape
                )
        ) {
            Text(
                text = initials,
                color = LogoWhite,
                fontSize = (size.value * 0.38f).sp,
                fontWeight = FontWeight.Black
            )
        }
    }
}
