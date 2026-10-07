package com.jastin.expensync.domain.model

import java.util.Date

data class Expense(
    val id: String,
    val title: String,
    val amount: Double,
    val paidByUserId: String,
    val participantsIds: List<String>,
    val date: Date,
    val isSettled: Boolean = false
)