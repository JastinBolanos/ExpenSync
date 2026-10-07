package com.jastin.expensync.domain.repository

import com.jastin.expensync.domain.model.Expense
import kotlinx.coroutines.flow.Flow

interface ExpenseRepository {
    suspend fun addExpense(expense: Expense)
    fun getExpenses(): Flow<List<Expense>>
    suspend fun getExpenseById(id: String): Expense?
}