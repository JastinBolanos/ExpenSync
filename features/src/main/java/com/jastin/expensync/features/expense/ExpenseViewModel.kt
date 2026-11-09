package com.jastin.expensync.features.expense

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jastin.expensync.domain.model.Expense
import com.jastin.expensync.domain.usecase.AddExpenseUseCase
import com.jastin.expensync.domain.usecase.CalculateDebtsUseCase
import com.jastin.expensync.domain.usecase.GetExpensesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ExpenseViewModel @Inject constructor(
    private val getExpensesUseCase: GetExpensesUseCase,
    private val addExpenseUseCase: AddExpenseUseCase,
    private val calculateDebtsUseCase: CalculateDebtsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExpenseUiState())
    val uiState: StateFlow<ExpenseUiState> = _uiState.asStateFlow()

    init {
        observeExpenses()
    }

    private fun observeExpenses() {
        viewModelScope.launch {
            getExpensesUseCase().collect { expensesList ->
                val calculatedDebts = calculateDebtsUseCase(expensesList)

                _uiState.update { currentState ->
                    currentState.copy(
                        expenses = expensesList,
                        debts = calculatedDebts
                    )
                }
            }
        }
    }

    fun addExpense(
        title: String,
        amount: Double,
        paidByUserId: String,
        participantsIds: List<String>
    ) {
        viewModelScope.launch {
            try {
                val newExpense = Expense(
                    id = UUID.randomUUID().toString(),
                    title = title,
                    amount = amount,
                    paidByUserId = paidByUserId,
                    participantsIds = participantsIds,
                    date = java.util.Date(System.currentTimeMillis())
                )

                addExpenseUseCase(newExpense)
                _uiState.update { it.copy(errorMessage = null) }

            } catch (e: IllegalArgumentException) {
                _uiState.update { currentState ->
                    currentState.copy(errorMessage = e.message)
                }
            }
        }
    }

    fun setSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun setFilter(filter: ExpenseFilter) {
        _uiState.update { it.copy(selectedFilter = filter) }
    }

    fun setCategoryFilter(category: ExpenseCategory?) {
        _uiState.update {
            val next = if (it.selectedCategory == category) null else category
            it.copy(selectedCategory = next)
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}