package com.jastin.expensync.data.di

import com.jastin.expensync.data.repository.ExpenseRepositoryImpl
import com.jastin.expensync.domain.repository.ExpenseRepository
import com.jastin.expensync.domain.usecase.AddExpenseUseCase
import com.jastin.expensync.domain.usecase.CalculateDebtsUseCase
import com.jastin.expensync.domain.usecase.GetExpensesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideExpenseRepository(): ExpenseRepository {
        return ExpenseRepositoryImpl()
    }

    @Provides
    @Singleton
    fun provideAddExpenseUseCase(repository: ExpenseRepository): AddExpenseUseCase {
        return AddExpenseUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideCalculateDebtsUseCase(): CalculateDebtsUseCase {
        return CalculateDebtsUseCase()
    }

    @Provides
    @Singleton
    fun provideGetExpensesUseCase(repository: ExpenseRepository): GetExpensesUseCase {
        return GetExpensesUseCase(repository)
    }
}