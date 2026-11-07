package com.jastin.expensync.data.repository

import com.jastin.expensync.data.local.ExpenseDao
import com.jastin.expensync.data.local.toDomain as localToDomain
import com.jastin.expensync.data.local.toEntity
import com.jastin.expensync.data.remote.ExpenseDto
import com.jastin.expensync.data.remote.toDomain as remoteToDomain
import com.jastin.expensync.data.remote.toDto
import com.jastin.expensync.domain.model.Expense
import com.jastin.expensync.domain.repository.ExpenseRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class ExpenseRepositoryImpl @Inject constructor(
    private val supabaseClient: SupabaseClient,
    private val dao: ExpenseDao
) : ExpenseRepository {

    override fun getExpenses(): Flow<List<Expense>> {
        return dao.getAllExpenses()
            .map { entities ->
                entities.map { it.localToDomain() }
            }
            .onStart {
                try {
                    val dtos = supabaseClient.postgrest["expenses"]
                        .select()
                        .decodeList<ExpenseDto>()
                    val entities = dtos.map { it.remoteToDomain().toEntity() }
                    dao.insertAll(entities)
                } catch (e: Exception) {
                }
            }
    }

    override suspend fun getExpenseById(id: String): Expense? {
        return try {
            val dto = supabaseClient.postgrest["expenses"]
                .select {
                    filter { eq("id", id) }
                }
                .decodeSingleOrNull<ExpenseDto>()

            dto?.remoteToDomain()
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun addExpense(expense: Expense) {
        dao.insertExpense(expense.toEntity())
        try {
            val dto = expense.toDto()
            supabaseClient.postgrest["expenses"].insert(dto)
        } catch (e: Exception) {
        }
    }
}