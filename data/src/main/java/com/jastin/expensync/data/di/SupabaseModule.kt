package com.jastin.expensync.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SupabaseModule {

    @Provides
    @Singleton
    fun provideSupabaseClient(): SupabaseClient {
        return createSupabaseClient(
            supabaseUrl = "https://eeoztaodprttanxtibwt.supabase.co",
            supabaseKey = "sb_publishable_WLYB6WYiXveLvbE03dHImQ_gFaPfJAa"
        ) {
            install(Postgrest)
        }
    }
}