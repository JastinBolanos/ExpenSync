package com.jastin.expensync.data.di

import android.content.Context
import androidx.room.Room
import com.jastin.expensync.data.local.ExpenSyncDatabase
import com.jastin.expensync.data.local.ExpenseDao
import com.jastin.expensync.data.repository.ExpenseRepositoryImpl
import com.jastin.expensync.domain.repository.ExpenseRepository
import com.jastin.expensync.domain.usecase.AddExpenseUseCase
import com.jastin.expensync.domain.usecase.CalculateDebtsUseCase
import com.jastin.expensync.domain.usecase.GetExpensesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): ExpenSyncDatabase {
        return Room.databaseBuilder(
            context,
            ExpenSyncDatabase::class.java,
            "expensync_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideExpenseDao(database: ExpenSyncDatabase): ExpenseDao {
        return database.expenseDao
    }

    @Provides
    @Singleton
    fun provideExpenseRepository(
        supabaseClient: SupabaseClient,
        dao: ExpenseDao
    ): ExpenseRepository {
        return ExpenseRepositoryImpl(supabaseClient, dao)
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