package com.jastin.expensync.features.expense

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AddExpenseSheet(
    onDismiss: () -> Unit,
    onConfirm: (title: String, amount: Double, paidBy: String, participants: List<String>) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var title by remember { mutableStateOf("") }
    var amountText by remember { mutableStateOf("") }
    val users = remember { listOf("Yo", "Ana", "Carlos", "Luis") }
    var paidBy by remember { mutableStateOf(users.first()) }
    var selectedParticipants by remember { mutableStateOf(users.toSet()) }

    val quickConcepts = remember {
        listOf(
            Triple("Cena", LogoGold, "🍽️"),
            Triple("Supermercado", Color(0xFF10B981), "🛒"),
            Triple("Uber", LogoSlate, "🚗"),
            Triple("Café", LogoGold, "☕"),
            Triple("Bar", LogoCopper, "🍸"),
            Triple("Gasolina", LogoCopper, "⛽")
        )
    }

    val currentAmount = amountText.toDoubleOrNull() ?: 0.0
    val splitCount = selectedParticipants.size
    val splitAmount = if (splitCount > 0) currentAmount / splitCount else 0.0

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color(0xFF041411), // Deep Logo Pine Night
        scrimColor = Color.Black.copy(alpha = 0.70f),
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .width(36.dp)
                    .height(4.dp)
                    .clip(CircleShape)
                    .background(LogoSlate.copy(alpha = 0.35f))
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Nuevo Gasto",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "Registra y distribuye equitativamente",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF09221E))
                        .border(1.dp, LogoSlate.copy(alpha = 0.25f), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Cerrar",
                        tint = LogoSlate,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            // Minimal Large Amount Input with Logo Living Underline
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = "MONTO DEL GASTO",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.3.sp,
                    color = LogoSlate
                )

                OutlinedTextField(
                    value = amountText,
                    onValueChange = { input ->
                        if (input.isEmpty() || input.matches(Regex("""^\d*(\.\d{0,2})?$"""))) {
                            amountText = input
                        }
                    },
                    placeholder = {
                        Text(
                            text = "$0.00",
                            fontSize = 42.sp,
                            fontWeight = FontWeight.Black,
                            color = LogoSlate.copy(alpha = 0.35f),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    },
                    leadingIcon = if (amountText.isNotEmpty()) {
                        {
                            Text(
                                text = "$",
                                fontSize = 34.sp,
                                fontWeight = FontWeight.Black,
                                color = LogoGold
                            )
                        }
                    } else null,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        cursorColor = LogoGold,
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent
                    ),
                    textStyle = LocalTextStyle.current.copy(
                        fontSize = 42.sp,
                        fontWeight = FontWeight.Black,
                        color = TextPrimary,
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                // Living animated light beam under amount
                AnimatedLivingLine(
                    colorA = LogoGold,
                    colorB = LogoCopper,
                    height = 1.2.dp,
                    durationMillis = 2600,
                    modifier = Modifier
                        .fillMaxWidth(0.55f)
                        .padding(top = 2.dp)
                )
            }

            // Concept Field + Animated Quick Concept Chips
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "¿En qué consistió?",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextSecondary
                )

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    placeholder = { Text("Ej. Cena, Supermercado...", color = LogoSlate) },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        cursorColor = LogoGold,
                        focusedBorderColor = LogoGold.copy(alpha = 0.6f),
                        unfocusedBorderColor = LogoSlate.copy(alpha = 0.25f),
                        focusedContainerColor = Color(0xFF09221E),
                        unfocusedContainerColor = Color(0xFF09221E)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                // Quick suggestions with category accents
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    quickConcepts.forEach { (concept, accentColor, emoji) ->
                        val isSelected = title == concept
                        val chipBg by animateColorAsState(
                            targetValue = if (isSelected) accentColor.copy(alpha = 0.18f) else Color(0xFF09221E),
                            label = "concept_bg"
                        )
                        val chipBorderColor by animateColorAsState(
                            targetValue = if (isSelected) accentColor.copy(alpha = 0.60f) else LogoSlate.copy(alpha = 0.20f),
                            label = "concept_border"
                        )
                        val chipTextColor by animateColorAsState(
                            targetValue = if (isSelected) accentColor else TextSecondary,
                            label = "concept_text"
                        )
                        val chipScale by animateFloatAsState(
                            targetValue = if (isSelected) 1.04f else 1f,
                            animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                            label = "concept_scale"
                        )

                        val chipShape = RoundedCornerShape(10.dp)
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .graphicsLayer {
                                    scaleX = chipScale
                                    scaleY = chipScale
                                }
                                .clip(chipShape)
                                .background(chipBg)
                                .border(width = 0.8.dp, color = chipBorderColor, shape = chipShape)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = ripple(bounded = true, color = accentColor.copy(alpha = 0.25f))
                                ) {
                                    title = concept
                                }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(text = emoji, fontSize = 12.sp)
                                Text(
                                    text = concept,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                    color = chipTextColor
                                )
                            }
                        }
                    }
                }
            }

            // Payer Selection with Glowing Avatars
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "¿Quién pagó?",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextSecondary
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    users.forEach { user ->
                        val isSelected = paidBy == user
                        val pillBg by animateColorAsState(
                            targetValue = if (isSelected) LogoGold.copy(alpha = 0.16f) else Color(0xFF09221E),
                            label = "payer_bg"
                        )
                        val pillBorderColor by animateColorAsState(
                            targetValue = if (isSelected) LogoGold.copy(alpha = 0.60f) else LogoSlate.copy(alpha = 0.20f),
                            label = "payer_border"
                        )
                        val pillTextColor by animateColorAsState(
                            targetValue = if (isSelected) LogoGold else TextPrimary,
                            label = "payer_text"
                        )
                        val pillScale by animateFloatAsState(
                            targetValue = if (isSelected) 1.03f else 1f,
                            animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                            label = "payer_scale"
                        )

                        val pillShape = RoundedCornerShape(12.dp)
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .graphicsLayer {
                                    scaleX = pillScale
                                    scaleY = pillScale
                                }
                                .clip(pillShape)
                                .background(pillBg)
                                .border(width = 0.8.dp, color = pillBorderColor, shape = pillShape)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = ripple(bounded = true, color = LogoGold.copy(alpha = 0.25f))
                                ) {
                                    paidBy = user
                                }
                                .padding(vertical = 8.dp, horizontal = 4.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                GlowingUserAvatar(name = user, size = 20.dp)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = user,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = pillTextColor,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }

            // Participants Division Selection
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "¿Entre quiénes se divide?",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextSecondary
                    )

                    val toggleShape = RoundedCornerShape(8.dp)
                    Box(
                        modifier = Modifier
                            .clip(toggleShape)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = ripple(bounded = true, color = LogoGold.copy(alpha = 0.25f))
                            ) {
                                selectedParticipants = if (selectedParticipants.size == users.size) {
                                    setOf(paidBy)
                                } else {
                                    users.toSet()
                                }
                            }
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = if (selectedParticipants.size == users.size) "Deseleccionar" else "Todos",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = LogoGold
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    users.forEach { user ->
                        val isSelected = selectedParticipants.contains(user)
                        val partBg by animateColorAsState(
                            targetValue = if (isSelected) LogoCopper.copy(alpha = 0.16f) else Color(0xFF09221E),
                            label = "part_bg"
                        )
                        val partBorderColor by animateColorAsState(
                            targetValue = if (isSelected) LogoCopper.copy(alpha = 0.60f) else LogoSlate.copy(alpha = 0.20f),
                            label = "part_border"
                        )
                        val partColor by animateColorAsState(
                            targetValue = if (isSelected) LogoCopper else TextMuted,
                            label = "part_text"
                        )
                        val partScale by animateFloatAsState(
                            targetValue = if (isSelected) 1.03f else 1f,
                            animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                            label = "part_scale"
                        )

                        val partShape = RoundedCornerShape(12.dp)
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .graphicsLayer {
                                    scaleX = partScale
                                    scaleY = partScale
                                }
                                .clip(partShape)
                                .background(partBg)
                                .border(width = 0.8.dp, color = partBorderColor, shape = partShape)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = ripple(bounded = true, color = LogoCopper.copy(alpha = 0.25f))
                                ) {
                                    selectedParticipants = if (isSelected) {
                                        if (selectedParticipants.size > 1) selectedParticipants - user else selectedParticipants
                                    } else {
                                        selectedParticipants + user
                                    }
                                }
                                .padding(vertical = 8.dp, horizontal = 4.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                GlowingUserAvatar(name = user, size = 20.dp)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = user,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = partColor,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }

                // Live split preview animated in Logo-Themed card
                AnimatedVisibility(
                    visible = currentAmount > 0 && splitCount > 0,
                    enter = fadeIn(animationSpec = tween(200)) + expandVertically(),
                    exit = fadeOut(animationSpec = tween(150)) + shrinkVertically()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFF09221E))
                            .border(0.8.dp, LogoGold.copy(alpha = 0.35f), RoundedCornerShape(10.dp))
                            .padding(horizontal = 14.dp, vertical = 9.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Calculate,
                                    contentDescription = null,
                                    tint = LogoGold,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "División entre $splitCount personas",
                                    fontSize = 12.sp,
                                    color = TextSecondary
                                )
                            }

                            AnimatedAmountText(
                                targetAmount = splitAmount,
                                prefix = "$",
                                suffix = " c/u",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = LogoGold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(2.dp))

            // Save Button in vibrant Logo Gold-to-Copper gradient
            val isEnabled = title.isNotBlank() && currentAmount > 0.0 && selectedParticipants.isNotEmpty()
            val buttonInteractionSource = remember { MutableInteractionSource() }
            val isButtonPressed by buttonInteractionSource.collectIsPressedAsState()
            val buttonScale by animateFloatAsState(
                targetValue = if (isButtonPressed) 0.96f else 1f,
                animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                label = "button_scale"
            )

            Button(
                onClick = {
                    if (isEnabled) {
                        onConfirm(title.trim(), currentAmount, paidBy, selectedParticipants.toList())
                    }
                },
                enabled = isEnabled,
                interactionSource = buttonInteractionSource,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color(0xFF030E0C),
                    disabledContainerColor = Color(0xFF09221E),
                    disabledContentColor = LogoSlate
                ),
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .graphicsLayer {
                        scaleX = buttonScale
                        scaleY = buttonScale
                    }
                    .clip(RoundedCornerShape(12.dp))
                    .then(
                        if (isEnabled) {
                            Modifier.background(
                                Brush.horizontalGradient(listOf(LogoGold, LogoCopper))
                            )
                        } else {
                            Modifier
                                .background(Color(0xFF09221E))
                                .border(1.dp, LogoSlate.copy(alpha = 0.20f), RoundedCornerShape(12.dp))
                        }
                    )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (isEnabled) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = Color(0xFF030E0C),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Text(
                        text = if (currentAmount > 0 && title.isNotBlank()) {
                            String.format(Locale.US, "Guardar — $%.2f", currentAmount)
                        } else if (currentAmount > 0) {
                            "Escribe o elige un concepto"
                        } else {
                            "Ingresa el monto del gasto"
                        },
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isEnabled) Color(0xFF030E0C) else LogoSlate
                    )
                }
            }
        }
    }
}