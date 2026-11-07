package com.jastin.expensync.domain.usecase

import com.jastin.expensync.domain.model.Expense
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.Date

class CalculateDebtsUseCaseTest {
    private lateinit var calculateDebtsUseCase: CalculateDebtsUseCase

    @Before
    fun setUp() {
        calculateDebtsUseCase = CalculateDebtsUseCase()
    }

    @Test
    fun `when list is empty, returns empty debts`() {
        val result = calculateDebtsUseCase(emptyList())
        assertEquals(0, result.size)
    }

    @Test
    fun `when two users share an expense, calculates exact split`() {
        val expense = Expense(
            id = "1",
            title = "Cena",
            amount = 100.0,
            paidByUserId = "Yo",
            participantsIds = listOf("Yo", "Ana"),
            date = Date(),
            isSettled = false
        )

        val debts = calculateDebtsUseCase(listOf(expense))

        assertEquals(1, debts.size)
        assertEquals("Ana", debts[0].debtorId)
        assertEquals("Yo", debts[0].creditorId)
        assertEquals(50.0, debts[0].amount, 0.0)
    }

    @Test
    fun `when expenses are settled, they are ignored`() {
        val expense = Expense(
            id = "1",
            title = "Cena",
            amount = 100.0,
            paidByUserId = "Yo",
            participantsIds = listOf("Yo", "Ana"),
            date = Date(),
            isSettled = true
        )

        val debts = calculateDebtsUseCase(listOf(expense))

        assertEquals(0, debts.size)
    }

    @Test
    fun `when multiple complex expenses occur, balances are consolidated correctly`() {
        val expense1 = Expense(
            id = "1",
            title = "Taxi",
            amount = 60.0,
            paidByUserId = "Ana",
            participantsIds = listOf("Ana", "Carlos", "Yo"),
            date = Date(),
            isSettled = false
        )

        val expense2 = Expense(
            id = "2",
            title = "Cine",
            amount = 30.0,
            paidByUserId = "Carlos",
            participantsIds = listOf("Carlos", "Yo"),
            date = Date(),
            isSettled = false
        )

        val debts = calculateDebtsUseCase(listOf(expense1, expense2))

        assertEquals(2, debts.size)

        val debtFromMe = debts.find { it.debtorId == "Yo" }
        assertEquals("Ana", debtFromMe?.creditorId)
        assertEquals(35.0, debtFromMe?.amount ?: 0.0, 0.0)

        val debtFromCarlos = debts.find { it.debtorId == "Carlos" }
        assertEquals("Ana", debtFromCarlos?.creditorId)
        assertEquals(5.0, debtFromCarlos?.amount ?: 0.0, 0.0)
    }
}