package com.jastin.expensync.features.expense

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jastin.expensync.domain.model.Debt

@Composable
fun DebtSummarySection(
    debts: List<Debt>,
    myNetBalance: Double,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = debts.isNotEmpty(),
        enter = fadeIn(animationSpec = tween(350)) + expandVertically(animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)),
        exit = fadeOut(animationSpec = tween(200)) + shrinkVertically()
    ) {
        Column(
            modifier = modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Section Header
            Text(
                text = "PLAN DE LIQUIDACIÓN",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.2.sp,
                color = LogoSlate
            )

            val infiniteTransition = rememberInfiniteTransition(label = "debt_section_animations")
            val boxShape = RoundedCornerShape(12.dp)

            // Logo-Infused Pine Obsidian Container
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(boxShape)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF092520), // Top Deep Pine Slate
                                Color(0xFF061A17), // Mid Logo Pine Teal
                                Color(0xFF030E0C)  // Base Midnight Shadow
                            )
                        )
                    )
                    .border(
                        width = 1.dp,
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                LogoGold.copy(alpha = 0.35f),
                                LogoCopper.copy(alpha = 0.20f),
                                LogoSlate.copy(alpha = 0.12f)
                            )
                        ),
                        shape = boxShape
                    )
            ) {
                // Settlement Transfer Items
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                ) {
                        val arrowTranslateX by infiniteTransition.animateFloat(
                            initialValue = -3f,
                            targetValue = 3f,
                            animationSpec = infiniteRepeatable(
                                animation = tween(durationMillis = 850, easing = FastOutSlowInEasing),
                                repeatMode = RepeatMode.Reverse
                            ),
                            label = "arrow_offset"
                        )
                        val arrowTint by infiniteTransition.animateColor(
                            initialValue = LogoSlate,
                            targetValue = LogoGold,
                            animationSpec = infiniteRepeatable(
                                animation = tween(durationMillis = 850, easing = FastOutSlowInEasing),
                                repeatMode = RepeatMode.Reverse
                            ),
                            label = "arrow_tint"
                        )

                        debts.forEachIndexed { index, debt ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Debtor -> Flow Vector -> Creditor
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    GlowingUserAvatar(name = debt.debtorId, size = 28.dp)
                                    Text(
                                        text = debt.debtorId,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = TextPrimary
                                    )

                                    // Electric transfer track
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier.padding(horizontal = 3.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                            contentDescription = "Paga a",
                                            tint = arrowTint,
                                            modifier = Modifier
                                                .size(15.dp)
                                                .graphicsLayer {
                                                    translationX = arrowTranslateX
                                                }
                                        )
                                    }

                                    GlowingUserAvatar(name = debt.creditorId, size = 28.dp)
                                    Text(
                                        text = debt.creditorId,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = TextPrimary
                                    )
                                }

                                // Logo-Themed Amount Pill Badge
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color(0xFF0A221E))
                                        .border(
                                            width = 1.dp,
                                            brush = Brush.verticalGradient(
                                                listOf(
                                                    LogoGold.copy(alpha = 0.30f),
                                                    LogoCopper.copy(alpha = 0.15f)
                                                )
                                            ),
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                        .padding(horizontal = 10.dp, vertical = 5.dp)
                                ) {
                                    AnimatedAmountText(
                                        targetAmount = debt.amount,
                                        prefix = "$",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = LogoGold,
                                        delayMillis = index * 120,
                                        durationMillis = 1100
                                    )
                                }
                            }

                            // Living animated dividing line between transfers
                            if (index < debts.lastIndex) {
                                AnimatedLivingLine(
                                    colorA = LogoGold,
                                    colorB = LogoCopper,
                                    height = 1.dp,
                                    durationMillis = 2400 + index * 400
                                )
                            }
                        }
                    }
            }
        }
    }
}
