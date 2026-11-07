package com.jastin.expensync.domain.usecase

import com.jastin.expensync.domain.model.Expense
import com.jastin.expensync.domain.repository.ExpenseRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import java.util.Date

class AddExpenseUseCaseTest {
    private val repository: ExpenseRepository = mockk(relaxed = true)
    private lateinit var addExpenseUseCase: AddExpenseUseCase

    @Before
    fun setUp() {
        addExpenseUseCase = AddExpenseUseCase(repository)
    }

    @Test
    fun `when amount is zero or less, throws exception`() = runTest {
        val invalidExpense = Expense(
            id = "1", title = "Cena", amount = 0.0, paidByUserId = "Yo",
            participantsIds = listOf("Yo"), date = Date(), isSettled = false
        )

        try {
            addExpenseUseCase(invalidExpense)
            fail("Se esperaba que lanzara una excepción por monto cero")
        } catch (e: IllegalArgumentException) {
            assertEquals("El monto del gasto debe ser mayor a cero.", e.message)
        }

        coVerify(exactly = 0) { repository.addExpense(any()) }
    }

    @Test
    fun `when participants list is empty, throws exception`() = runTest {
        val invalidExpense = Expense(
            id = "1", title = "Cena", amount = 10.0, paidByUserId = "Yo",
            participantsIds = emptyList(), date = Date(), isSettled = false
        )

        try {
            addExpenseUseCase(invalidExpense)
            fail("Se esperaba que lanzara una excepción por lista vacía")
        } catch (e: IllegalArgumentException) {
            assertEquals("Debe haber al menos un participante en el gasto.", e.message)
        }

        coVerify(exactly = 0) { repository.addExpense(any()) }
    }

    @Test
    fun `when title is blank, throws exception`() = runTest {
        val invalidExpense = Expense(
            id = "1", title = "   ", amount = 10.0, paidByUserId = "Yo",
            participantsIds = listOf("Yo"), date = Date(), isSettled = false
        )

        try {
            addExpenseUseCase(invalidExpense)
            fail("Se esperaba que lanzara una excepción por título vacío")
        } catch (e: IllegalArgumentException) {
            assertEquals("El gasto debe tener un título.", e.message)
        }

        coVerify(exactly = 0) { repository.addExpense(any()) }
    }

    @Test
    fun `when expense is completely valid, repository is called once`() = runTest {
        val validExpense = Expense(
            id = "1", title = "Cena", amount = 100.0, paidByUserId = "Yo",
            participantsIds = listOf("Yo", "Ana"), date = Date(), isSettled = false
        )
        addExpenseUseCase(validExpense)

        coVerify(exactly = 1) { repository.addExpense(validExpense) }
    }
}