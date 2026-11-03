package com.jastin.expensync.data.repository

import com.jastin.expensync.data.remote.ExpenseDto
import com.jastin.expensync.data.remote.toDomain
import com.jastin.expensync.data.remote.toDto
import com.jastin.expensync.domain.model.Expense
import com.jastin.expensync.domain.repository.ExpenseRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ExpenseRepositoryImpl @Inject constructor(
    private val supabaseClient: SupabaseClient
) : ExpenseRepository {

    override fun getExpenses(): Flow<List<Expense>> = flow {
        val expensesDto = supabaseClient.postgrest["expenses"]
            .select()
            .decodeList<ExpenseDto>()

        emit(expensesDto.map { it.toDomain() })
    }

    override suspend fun getExpenseById(id: String): Expense? {
        val dto = supabaseClient.postgrest["expenses"]
            .select {
                filter { eq("id", id) }
            }
            .decodeSingleOrNull<ExpenseDto>()

        return dto?.toDomain()
    }

    override suspend fun addExpense(expense: Expense) {
        val dto = expense.toDto()
        supabaseClient.postgrest["expenses"].insert(dto)
    }
}