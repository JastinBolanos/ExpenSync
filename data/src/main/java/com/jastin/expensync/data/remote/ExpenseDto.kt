package com.jastin.expensync.data.remote

import com.jastin.expensync.domain.model.Expense
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.InternalSerializationApi

@OptIn(InternalSerializationApi::class)
@Serializable
data class ExpenseDto(
    val id: String,
    val title: String,
    val amount: Double,
    @SerialName("paid_by_user_id") val paidByUserId: String,
    @SerialName("participants_ids") val participantsIds: List<String>,
    val date: String? = null,
    @SerialName("is_settled") val isSettled: Boolean
)

fun ExpenseDto.toDomain(): Expense {
    return Expense(
        id = id,
        title = title,
        amount = amount,
        paidByUserId = paidByUserId,
        participantsIds = participantsIds,
        date = java.util.Date(),
        isSettled = isSettled
    )
}

fun Expense.toDto(): ExpenseDto {
    return ExpenseDto(
        id = id,
        title = title,
        amount = amount,
        paidByUserId = paidByUserId,
        participantsIds = participantsIds,
        isSettled = isSettled
    )
}