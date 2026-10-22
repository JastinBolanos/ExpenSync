package com.jastin.expensync.data.di

import com.jastin.expensync.data.repository.ExpenseRepositoryImpl
import com.jastin.expensync.domain.repository.ExpenseRepository
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
}