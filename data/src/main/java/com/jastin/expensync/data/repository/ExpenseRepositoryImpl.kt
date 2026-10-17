package com.jastin.expensync.data.repository

import com.jastin.expensync.domain.model.Expense
import com.jastin.expensync.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class ExpenseRepositoryImpl : ExpenseRepository {
    private val fakeDatabase = mutableListOf<Expense>()
    private val expensesFlow = MutableStateFlow<List<Expense>>(emptyList())

    override suspend fun addExpense(expense: Expense) {
        fakeDatabase.add(expense)
        expensesFlow.value = fakeDatabase.toList()
    }

    override fun getExpenses(): Flow<List<Expense>> {
        return expensesFlow
    }

    override suspend fun getExpenseById(id: String): Expense? {
        return fakeDatabase.find { it.id == id }
    }
}