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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
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
    val quickConcepts = remember { ExpenseConcepts.ALL }
    var title by remember { mutableStateOf(quickConcepts.first().name) }
    var amountText by remember { mutableStateOf("") }
    val amountFocusRequester = remember { FocusRequester() }
    val users = remember { listOf("Yo", "Ana", "Carlos", "Luis") }
    var paidBy by remember { mutableStateOf(users.first()) }
    var selectedParticipants by remember { mutableStateOf(users.toSet()) }

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

            // Centered Large Amount Input with precise Living Underline
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "MONTO DEL GASTO",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.4.sp,
                    color = LogoSlate
                )

                // Perfectly Centered Amount Entry
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            amountFocusRequester.requestFocus()
                        },
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "$",
                        fontSize = 38.sp,
                        fontWeight = FontWeight.Black,
                        color = if (amountText.isNotEmpty()) LogoGold else LogoSlate.copy(alpha = 0.40f)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    BasicTextField(
                        value = amountText,
                        onValueChange = { input ->
                            if (input.isEmpty() || input.matches(Regex("""^\d*(\.\d{0,2})?$"""))) {
                                amountText = input
                            }
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        cursorBrush = SolidColor(LogoGold),
                        textStyle = TextStyle(
                            fontSize = 42.sp,
                            fontWeight = FontWeight.Black,
                            color = TextPrimary,
                            textAlign = TextAlign.Start
                        ),
                        modifier = Modifier
                            .focusRequester(amountFocusRequester)
                            .width(IntrinsicSize.Min)
                            .defaultMinSize(minWidth = if (amountText.isEmpty()) 96.dp else 24.dp),
                        decorationBox = { innerTextField ->
                            Box(contentAlignment = Alignment.CenterStart) {
                                if (amountText.isEmpty()) {
                                    Text(
                                        text = "0.00",
                                        fontSize = 42.sp,
                                        fontWeight = FontWeight.Black,
                                        color = LogoSlate.copy(alpha = 0.35f)
                                    )
                                }
                                innerTextField()
                            }
                        }
                    )
                }

                // Living animated light beam placed directly below the centered amount
                AnimatedLivingLine(
                    colorA = LogoGold,
                    colorB = LogoCopper,
                    height = 1.4.dp,
                    durationMillis = 2400,
                    modifier = Modifier
                        .width(180.dp)
                        .padding(top = 4.dp)
                )
            }

            // Concept Field + Bandeja Lateral de Conceptos
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "¿En qué consistió?",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextSecondary
                )


                // Bandeja lateral horizontal sin cajas ni emojis, con logo de cada tipo
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(horizontal = 4.dp, vertical = 6.dp)
                ) {
                    items(quickConcepts) { conceptItem ->
                        val isSelected = title == conceptItem.name
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = ripple(bounded = false, radius = 28.dp, color = LogoGold)
                                ) {
                                    title = conceptItem.name
                                }
                                .padding(vertical = 4.dp, horizontal = 2.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = conceptItem.icon,
                                    contentDescription = null,
                                    tint = if (isSelected) LogoGold else LogoSlate,
                                    modifier = Modifier.size(15.dp)
                                )
                                Text(
                                    text = conceptItem.name,
                                    fontSize = 13.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) LogoGold else LogoSlate,
                                    letterSpacing = 0.2.sp
                                )
                            }
                            if (isSelected) {
                                Box(
                                    modifier = Modifier
                                        .padding(top = 4.dp)
                                        .width(24.dp)
                                        .height(2.dp)
                                        .clip(CircleShape)
                                        .background(LogoGold)
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
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    users.forEach { user ->
                        val isSelected = paidBy == user
                        val textColor by animateColorAsState(
                            targetValue = if (isSelected) LogoGold else LogoSlate,
                            label = "payer_text"
                        )
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .weight(1f)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = ripple(bounded = false, radius = 28.dp, color = LogoGold)
                                ) {
                                    paidBy = user
                                }
                                .padding(vertical = 8.dp)
                        ) {
                            Text(
                                text = user,
                                fontSize = 14.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = textColor,
                                maxLines = 1
                            )
                            if (isSelected) {
                                Box(
                                    modifier = Modifier
                                        .padding(top = 4.dp)
                                        .width(20.dp)
                                        .height(2.dp)
                                        .clip(CircleShape)
                                        .background(LogoGold)
                                )
                            } else {
                                Spacer(modifier = Modifier.height(6.dp))
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
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    users.forEach { user ->
                        val isSelected = selectedParticipants.contains(user)
                        val textColor by animateColorAsState(
                            targetValue = if (isSelected) LogoCopper else LogoSlate.copy(alpha = 0.45f),
                            label = "part_text"
                        )
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .weight(1f)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = ripple(bounded = false, radius = 28.dp, color = LogoCopper)
                                ) {
                                    selectedParticipants = if (isSelected) {
                                        if (selectedParticipants.size > 1) selectedParticipants - user else selectedParticipants
                                    } else {
                                        selectedParticipants + user
                                    }
                                }
                                .padding(vertical = 8.dp)
                        ) {
                            Text(
                                text = user,
                                fontSize = 14.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = textColor,
                                maxLines = 1
                            )
                            if (isSelected) {
                                Box(
                                    modifier = Modifier
                                        .padding(top = 4.dp)
                                        .width(20.dp)
                                        .height(2.dp)
                                        .clip(CircleShape)
                                        .background(LogoCopper)
                                )
                            } else {
                                Spacer(modifier = Modifier.height(6.dp))
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