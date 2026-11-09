package com.jastin.expensync.features.expense

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jastin.expensync.domain.model.Expense
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Modern unboxed transaction row designed for the unified activity feed.
 * Eliminates isolated chunky boxes and integrates the 4 logo brand colors.
 */
@Composable
fun ExpenseRow(
    expense: Expense,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val category = remember(expense.title) { ExpenseCategory.detect(expense.title) }

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val rowScale by animateFloatAsState(
        targetValue = if (isPressed) 0.985f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "row_scale"
    )

    val chevronRotation by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMediumLow
        ),
        label = "chevron_rotation"
    )

    val rowHighlightBg by animateColorAsState(
        targetValue = if (expanded) Color(0xFF0C2420) else if (isPressed) Color.White.copy(alpha = 0.04f) else Color.Transparent,
        animationSpec = tween(durationMillis = 200),
        label = "row_highlight"
    )

    val dateFormatter = remember {
        SimpleDateFormat("d MMM, HH:mm", Locale("es", "ES"))
    }
    val formattedDate = remember(expense.date) { dateFormatter.format(expense.date) }
    val splitPerPerson = remember(expense.amount, expense.participantsIds.size) {
        if (expense.participantsIds.isNotEmpty()) {
            expense.amount / expense.participantsIds.size
        } else {
            expense.amount
        }
    }

    val rowShape = RoundedCornerShape(12.dp)
    Box(
        modifier = modifier
            .fillMaxWidth()
            .graphicsLayer {
                scaleX = rowScale
                scaleY = rowScale
            }
            .clip(rowShape)
            .background(rowHighlightBg)
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(bounded = true, color = LogoGold.copy(alpha = 0.20f))
            ) {
                expanded = !expanded
            }
            .padding(horizontal = 10.dp, vertical = 9.dp)
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessMediumLow
                )
            )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Main row: Icon + Concept/User + Amount/Split
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Category Icon + Details
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.weight(1f, fill = false)
                ) {
                    // Pulsing Glowing Circular Icon
                    GlowingCategoryIcon(category = category, size = 36.dp)

                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(
                            text = expense.title,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "Por ${expense.paidByUserId}",
                                fontSize = 12.sp,
                                color = TextSecondary
                            )
                            Text(text = "•", fontSize = 10.sp, color = LogoSlate)
                            Text(
                                text = formattedDate,
                                fontSize = 12.sp,
                                color = TextMuted
                            )
                        }
                    }
                }

                // Amount & Split & Expand Chevron
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        AnimatedAmountText(
                            targetAmount = expense.amount,
                            prefix = "- $",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = TextPrimary,
                            durationMillis = 900
                        )

                        Text(
                            text = String.format(Locale.US, "$%.2f c/u", splitPerPerson),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = LogoSlate
                        )
                    }

                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = if (expanded) "Colapsar" else "Expandir",
                        tint = if (expanded) LogoGold else LogoSlate,
                        modifier = Modifier
                            .size(18.dp)
                            .rotate(chevronRotation)
                    )
                }
            }

            // Expanded Breakdown: Animated Participant Grid & Live Living Line
            AnimatedVisibility(
                visible = expanded,
                enter = fadeIn(animationSpec = tween(180)) + expandVertically(animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)),
                exit = fadeOut(animationSpec = tween(120)) + shrinkVertically()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Living energy line inside the expanded card
                    AnimatedLivingLine(
                        colorA = LogoGold,
                        colorB = LogoCopper,
                        height = 1.dp,
                        durationMillis = 2000
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "DIVISIÓN DEL GASTO (${expense.participantsIds.size})",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp,
                            color = LogoSlate
                        )
                        Text(
                            text = String.format(Locale.US, "$%.2f cada uno", splitPerPerson),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = LogoGold
                        )
                    }

                    // Participants badges with custom identity colors
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        expense.participantsIds.forEach { participant ->
                            val isPayer = participant.equals(expense.paidByUserId, ignoreCase = true)
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(
                                        if (isPayer) LogoGold.copy(alpha = 0.16f) else Color(0xFF09201C)
                                    )
                                    .border(
                                        width = 0.8.dp,
                                        color = if (isPayer) LogoGold.copy(alpha = 0.5f) else LogoSlate.copy(alpha = 0.20f),
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    UserAvatar(name = participant, size = 18.dp)
                                    Text(
                                        text = participant,
                                        fontSize = 11.sp,
                                        fontWeight = if (isPayer) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isPayer) LogoGold else TextSecondary
                                    )
                                    if (isPayer) {
                                        Text(
                                            text = "(pagó)",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = LogoCopper
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}