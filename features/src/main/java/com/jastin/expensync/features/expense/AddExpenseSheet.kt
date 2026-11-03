package com.jastin.expensync.features.expense

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

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
        sheetState = sheetState
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
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Concepto (ej. Cena, Uber)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = amountText,
                onValueChange = { amountText = it },
                label = { Text("Monto ($)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Text("¿Quién pagó?", style = MaterialTheme.typography.labelLarge)
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                users.forEach { user ->
                    FilterChip(
                        selected = (paidBy == user),
                        onClick = { paidBy = user },
                        label = { Text(user) }
                    )
                }
            }

            Text("¿Entre quiénes se divide?", style = MaterialTheme.typography.labelLarge)
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
                        label = { Text(user) }
                    )
                }
            }

            Button(
                onClick = {
                    val amount = amountText.toDoubleOrNull() ?: 0.0
                    onConfirm(title, amount, paidBy, selectedParticipants.toList())
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = title.isNotBlank() && amountText.isNotBlank() && selectedParticipants.isNotEmpty()
            ) {
                Text("Guardar Gasto")
            }
        }
    }
}