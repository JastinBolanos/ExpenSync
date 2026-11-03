package com.jastin.expensync.features.expense

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color(0xFF0F172A),
        scrimColor = Color.Black.copy(alpha = 0.6f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Registrar Nuevo Gasto",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            val textFieldColors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = NeonCyan,
                unfocusedBorderColor = Color.White.copy(alpha = 0.1f),
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary,
                cursorColor = NeonCyan,
                focusedLabelColor = NeonCyan,
                unfocusedLabelColor = TextSecondary,
                focusedContainerColor = SurfaceDark,
                unfocusedContainerColor = SurfaceDark
            )

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Concepto (ej. Cena, Uber)") },
                singleLine = true,
                colors = textFieldColors,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = amountText,
                onValueChange = { amountText = it },
                label = { Text("Monto ($)") },
                singleLine = true,
                colors = textFieldColors,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Text("¿Quién pagó?", fontSize = 14.sp, color = TextSecondary)
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                users.forEach { user ->
                    val isSelected = (paidBy == user)
                    FilterChip(
                        selected = isSelected,
                        onClick = { paidBy = user },
                        label = { Text(user) },
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = SurfaceDark,
                            labelColor = TextSecondary,
                            selectedContainerColor = NeonCyan.copy(alpha = 0.15f),
                            selectedLabelColor = NeonCyan
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = isSelected,
                            borderColor = Color.White.copy(alpha = 0.05f),
                            selectedBorderColor = NeonCyan
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )
                }
            }

            Text("¿Entre quiénes se divide?", fontSize = 14.sp, color = TextSecondary)
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                users.forEach { user ->
                    val isSelected = selectedParticipants.contains(user)
                    FilterChip(
                        selected = isSelected,
                        onClick = {
                            selectedParticipants = if (isSelected) {
                                selectedParticipants - user
                            } else {
                                selectedParticipants + user
                            }
                        },
                        label = { Text(user) },
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = SurfaceDark,
                            labelColor = TextSecondary,
                            selectedContainerColor = NeonCyan.copy(alpha = 0.15f),
                            selectedLabelColor = NeonCyan
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = isSelected,
                            borderColor = Color.White.copy(alpha = 0.05f),
                            selectedBorderColor = NeonCyan
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    val amount = amountText.toDoubleOrNull() ?: 0.0
                    onConfirm(title, amount, paidBy, selectedParticipants.toList())
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = NeonCyan,
                    contentColor = Color(0xFF050B14),
                    disabledContainerColor = SurfaceDark,
                    disabledContentColor = TextSecondary
                ),
                enabled = title.isNotBlank() && amountText.isNotBlank() && selectedParticipants.isNotEmpty()
            ) {
                Text("Guardar Gasto", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    }
}