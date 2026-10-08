package com.jastin.expensync.domain.model

data class Debt(
    val debtorId: String,
    val creditorId: String,
    val amount: Double
)