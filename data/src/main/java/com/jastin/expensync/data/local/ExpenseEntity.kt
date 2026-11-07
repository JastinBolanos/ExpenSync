package com.jastin.expensync.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jastin.expensync.domain.model.Expense
import java.util.Date

@Entity(tableName = "expenses_table")
data class ExpenseEntity(
    @PrimaryKey
    val id: String,

    val title: String,

    val amount: Double,

    @ColumnInfo(name = "paid_by_user_id")
    val paidByUserId: String,

    @ColumnInfo(name = "participants_ids")
    val participantsIds: String,

    val date: Long,

    @ColumnInfo(name = "is_settled")
    val isSettled: Boolean
)

fun ExpenseEntity.toDomain(): Expense {
    return Expense(
        id = id,
        title = title,
        amount = amount,
        paidByUserId = paidByUserId,
        participantsIds = if (participantsIds.isBlank()) emptyList() else participantsIds.split(","),
        date = Date(date),
        isSettled = isSettled
    )
}

fun Expense.toEntity(): ExpenseEntity {
    return ExpenseEntity(
        id = id,
        title = title,
        amount = amount,
        paidByUserId = paidByUserId,
        participantsIds = participantsIds.joinToString(","),
        date = date.time,
        isSettled = isSettled
    )
}