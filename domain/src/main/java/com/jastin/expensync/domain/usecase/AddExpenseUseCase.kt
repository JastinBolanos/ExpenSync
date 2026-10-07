package com.jastin.expensync.domain.usecase

import com.jastin.expensync.domain.model.Expense
import com.jastin.expensync.domain.repository.ExpenseRepository

class AddExpenseUseCase(
    private val repository: ExpenseRepository
) {
    suspend operator fun invoke(expense: Expense) {
        if (expense.amount <= 0.0) {
            throw IllegalArgumentException("El monto del gasto debe ser mayor a cero.")
        }
        if (expense.participantsIds.isEmpty()) {
            throw IllegalArgumentException("Debe haber al menos un participante en el gasto.")
        }
        if (expense.title.isBlank()) {
            throw IllegalArgumentException("El gasto debe tener un título.")
        }
        repository.addExpense(expense)
    }
}