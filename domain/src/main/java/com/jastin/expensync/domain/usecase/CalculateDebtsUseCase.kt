package com.jastin.expensync.domain.usecase

import com.jastin.expensync.domain.model.Debt
import com.jastin.expensync.domain.model.Expense
import kotlin.math.min
import kotlin.math.round

class CalculateDebtsUseCase {

    operator fun invoke(expenses: List<Expense>): List<Debt> {
        val balances = mutableMapOf<String, Double>()

        for (expense in expenses) {
            if (expense.isSettled) continue

            val splitAmount = expense.amount / expense.participantsIds.size

            balances[expense.paidByUserId] = (balances[expense.paidByUserId] ?: 0.0) + expense.amount

            for (participantId in expense.participantsIds) {
                balances[participantId] = (balances[participantId] ?: 0.0) - splitAmount
            }
        }

        val debtors = balances.filterValues { it < -0.01 }.mapValues { -it.value }.toMutableMap()
        val creditors = balances.filterValues { it > 0.01 }.toMutableMap()
        val finalDebts = mutableListOf<Debt>()

        for ((debtor, debtorAmount) in debtors) {

            var remainingDebt = debtorAmount
            val creditorIterator = creditors.iterator()

            while (creditorIterator.hasNext() && remainingDebt > 0.01) {

                val (creditor, creditorAmount) = creditorIterator.next()
                val settledAmount = min(remainingDebt, creditorAmount)
                val roundedAmount = round(settledAmount * 100) / 100.0

                finalDebts.add(Debt(debtorId = debtor, creditorId = creditor, amount = roundedAmount))
                remainingDebt -= settledAmount
                creditors[creditor] = creditorAmount - settledAmount

                if (creditors[creditor]!! < 0.01) {
                    creditorIterator.remove()
                }
            }
        }

        return finalDebts
    }
}