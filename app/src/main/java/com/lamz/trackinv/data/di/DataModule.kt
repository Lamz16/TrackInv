package com.lamz.trackinv.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.lamz.trackinv.data.TrackRepository
import com.lamz.trackinv.data.pref.UserPreference
import com.lamz.trackinv.data.pref.dataStore
import com.lamz.trackinv.ui.screen.add.AddProductViewModel
import com.lamz.trackinv.ui.screen.home.HomeViewModel
import com.lamz.trackinv.ui.screen.inventory.InventoryViewModel
import com.lamz.trackinv.ui.screen.login.LoginViewModel
import com.lamz.trackinv.ui.screen.partner.PartnerViewModel
import com.lamz.trackinv.ui.screen.partner.TransactionViewModel
import com.lamz.trackinv.ui.screen.register.RegisterViewModel
import com.lamz.trackinv.ui.view.main.MainViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val dataModule = module {
    single { provideDataStore(androidContext()) }
    single { UserPreference(get()) }
    single { TrackRepository(get()) }
    viewModel { MainViewModel(get()) }
    viewModel { HomeViewModel(get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { RegisterViewModel(get()) }
    viewModel { PartnerViewModel(get())}
    viewModel { AddProductViewModel(get())}
    viewModel { InventoryViewModel(get())}
    viewModel { TransactionViewModel(get()) }

}

fun provideDataStore(context: Context): DataStore<Preferences> {
    return context.dataStore
}