package com.jastin.expensync.features.expense

import com.jastin.expensync.domain.model.Debt
import com.jastin.expensync.domain.model.Expense

enum class ExpenseFilter(val title: String) {
    ALL("Todos"),
    PAID_BY_ME("Pagados por mí"),
    INVOLVED("Mis compartidos")
}

data class ExpenseUiState(
    val expenses: List<Expense> = emptyList(),
    val debts: List<Debt> = emptyList(),
    val errorMessage: String? = null,
    val searchQuery: String = "",
    val selectedFilter: ExpenseFilter = ExpenseFilter.ALL,
    val selectedCategory: ExpenseCategory? = null
) {
    val filteredExpenses: List<Expense>
        get() {
            var list = expenses
            if (searchQuery.isNotBlank()) {
                val q = searchQuery.trim().lowercase()
                list = list.filter {
                    it.title.lowercase().contains(q) ||
                    it.paidByUserId.lowercase().contains(q) ||
                    it.participantsIds.any { p -> p.lowercase().contains(q) }
                }
            }
            list = when (selectedFilter) {
                ExpenseFilter.ALL -> list
                ExpenseFilter.PAID_BY_ME -> list.filter { it.paidByUserId.equals("yo", ignoreCase = true) }
                ExpenseFilter.INVOLVED -> list.filter { it.participantsIds.any { p -> p.equals("yo", ignoreCase = true) } }
            }
            if (selectedCategory != null) {
                list = list.filter { ExpenseCategory.detect(it.title) == selectedCategory }
            }
            return list
        }

    val totalGroupSpent: Double
        get() = expenses.sumOf { it.amount }

    val myNetBalance: Double
        get() {
            val owedToMe = debts.filter { it.creditorId.equals("yo", ignoreCase = true) }.sumOf { it.amount }
            val iOwe = debts.filter { it.debtorId.equals("yo", ignoreCase = true) }.sumOf { it.amount }
            return owedToMe - iOwe
        }
}
