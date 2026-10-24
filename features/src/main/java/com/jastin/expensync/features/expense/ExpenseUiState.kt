package com.jastin.expensync.features.expense

import com.jastin.expensync.domain.model.Debt
import com.jastin.expensync.domain.model.Expense

data class ExpenseUiState(
    val expenses: List<Expense> = emptyList(),
    val debts: List<Debt> = emptyList(),
    val errorMessage: String? = null
)
