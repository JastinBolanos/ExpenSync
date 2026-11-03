package com.jastin.expensync.features.expense

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jastin.expensync.domain.model.Debt
import com.jastin.expensync.domain.model.Expense

val DeepBackground = Color(0xFF050B14)
val SurfaceDark = Color(0xFF111827).copy(alpha = 0.7f)
val NeonCyan = Color(0xFF00F2FE)
val NeonBlue = Color(0xFF4FACFE)
val TextPrimary = Color(0xFFFFFFFF)
val TextSecondary = Color(0xFF9CA3AF)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: ExpenseViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showBottomSheet by remember { mutableStateOf(false) }
    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(Color(0xFF0F172A), DeepBackground, DeepBackground)
    )

    Scaffold(
        containerColor = Color.Transparent,
        floatingActionButton = {
            PremiumFloatingActionButton(onClick = { showBottomSheet = true })
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundBrush)
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp)
            ) {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "ExpenSync",
                    color = TextPrimary,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "Gestión de gastos inteligente",
                    color = TextSecondary,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                AnimatedVisibility(
                    visible = uiState.debts.isNotEmpty(),
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    DebtSummaryCard(
                        debts = uiState.debts,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }

                Text(
                    text = "TRANSACCIONES",
                    color = TextSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp,
                    modifier = Modifier.padding(bottom = 16.dp, start = 4.dp, top = 8.dp)
                )

                Box(modifier = Modifier.fillMaxSize()) {
                    this@Column.AnimatedVisibility(
                        visible = uiState.expenses.isEmpty(),
                        enter = fadeIn(),
                        exit = fadeOut(),
                        modifier = Modifier.align(Alignment.Center)
                    ) {
                        Text(
                            text = "No hay gastos aún. ¡Añade el primero!",
                            style = MaterialTheme.typography.bodyLarge,
                            color = TextSecondary
                        )
                    }

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = 100.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(
                            items = uiState.expenses,
                            key = { expense -> expense.id }
                        ) { expense ->
                            PremiumExpenseCard(expense = expense)
                        }
                    }
                }
            }
        }

        if (showBottomSheet) {
            AddExpenseSheet(
                onDismiss = { showBottomSheet = false },
                onConfirm = { title, amount, paidBy, participants ->
                    viewModel.addExpense(
                        title = title,
                        amount = amount,
                        paidByUserId = paidBy,
                        participantsIds = participants
                    )
                    showBottomSheet = false
                }
            )
        }
    }
}

@Composable
private fun DebtSummaryCard(
    debts: List<Debt>,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceDark),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f)),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Plan de Liquidación",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextSecondary
            )

            debts.forEach { debt ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = debt.debtorId,
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary,
                            fontSize = 16.sp
                        )
                        Icon(
                            imageVector = Icons.Default.SwapHoriz,
                            contentDescription = "debe a",
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                                .size(18.dp),
                            tint = NeonCyan
                        )
                        Text(
                            text = debt.creditorId,
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary,
                            fontSize = 16.sp
                        )
                    }
                    Text(
                        text = String.format(java.util.Locale.US, "$%.2f", debt.amount),
                        fontWeight = FontWeight.Bold,
                        color = NeonCyan,
                        fontSize = 18.sp
                    )
                }
            }
        }
    }
}

@Composable
fun PremiumExpenseCard(expense: Expense) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = SurfaceDark),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = expense.title,
                    color = TextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Pagado por ${expense.paidByUserId}",
                    color = TextSecondary,
                    fontSize = 13.sp
                )
            }
            Text(
                text = "- $${expense.amount}",
                color = TextPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun PremiumFloatingActionButton(onClick: () -> Unit) {
    val borderGradient = Brush.sweepGradient(
        colors = listOf(NeonCyan, NeonBlue, NeonCyan)
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(64.dp)
            .clip(CircleShape)
            .background(Color(0xFF0B1120))
            .border(width = 2.dp, brush = borderGradient, shape = CircleShape)
            .padding(8.dp)
    ) {
        IconButton(onClick = onClick, modifier = Modifier.fillMaxSize()) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Nuevo Gasto",
                tint = NeonCyan,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}