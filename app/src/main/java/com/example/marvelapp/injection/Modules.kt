package com.example.marvelapp.injection

import android.app.Application
import com.example.marvelapp.datasource.IMarvelRepo
import com.example.marvelapp.datasource.MarvelRepo
import com.example.marvelapp.datasource.local.IMarvelCache
import com.example.marvelapp.datasource.local.MarvelCache
import com.example.marvelapp.datasource.remote.MarvelApi
import com.example.marvelapp.db.CharacterDao
import com.example.marvelapp.db.CharacterListViewDao
import com.example.marvelapp.db.MarvelDatabase
import com.example.marvelapp.model.character.CharacterListMapper
import com.example.marvelapp.network.retrofit.RetrofitFactory
import com.example.marvelapp.ui.character.details.CharacterDetailsViewModel
import com.example.marvelapp.ui.character.list.CharacterListViewModel
import com.example.marvelapp.usecase.GetCharacterDetailsUseCase
import com.example.marvelapp.usecase.GetCharacterListUseCase
import com.example.marvelapp.usecase.IGetCharacterDetailsUseCase
import com.example.marvelapp.usecase.IGetCharacterListUseCase
import com.example.marvelapp.utils.date.DateUtils
import com.example.marvelapp.utils.date.IDateUtils
import com.example.marvelapp.utils.constants.NetworkUrl
import com.example.marvelapp.utils.encrypt.EncryptUtils
import com.example.marvelapp.utils.encrypt.IEncryptUtils
import com.example.marvelapp.utils.network.INetworkUtils
import com.example.marvelapp.utils.network.NetworkUtils
import com.example.marvelapp.utils.preferences.IPreferenceUtils
import com.example.marvelapp.utils.preferences.PreferenceUtils
import com.example.marvelapp.utils.strings.IStringUtils
import com.example.marvelapp.utils.strings.StringUtils
import com.example.marvelapp.utils.viewpager.IViewPagerUtils
import com.example.marvelapp.utils.viewpager.ViewPagerUtils
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val utilsModule = module {
    single<INetworkUtils> { NetworkUtils() }
    single<IEncryptUtils> { EncryptUtils() }
    single<IDateUtils> { DateUtils() }
    single<IStringUtils> { StringUtils() }
    single<IViewPagerUtils> { ViewPagerUtils() }
    single<IPreferenceUtils> { PreferenceUtils(get()) }
}

val databaseModule = module {
    fun provideDatabase(application: Application): MarvelDatabase {
        return MarvelDatabase.buildDatabase(application)
    }

    fun provideCharacterDao(database: MarvelDatabase): CharacterDao {
        return database.characterDao()
    }

    fun provideCharacterListViewDao(database: MarvelDatabase): CharacterListViewDao {
        return database.characterListViewDao()
    }

    single { provideDatabase(androidApplication()) }
    single { provideCharacterDao(get()) }
    single { provideCharacterListViewDao(get()) }
}

val apiModule = module {
    factory<MarvelApi> {
        RetrofitFactory().makeRetrofit(NetworkUrl.BASE_URL).create(MarvelApi::class.java)
    }
}

val cacheModule = module {
    factory<IMarvelCache> {
        MarvelCache(get(), get())
    }
}

val repoModule = module {
    single { CharacterListMapper() }
    factory<IMarvelRepo> {
        MarvelRepo(get(), get())
    }
}

val useCaseModule = module {
    factory<IGetCharacterListUseCase> { GetCharacterListUseCase(get()) }
    factory<IGetCharacterDetailsUseCase> { GetCharacterDetailsUseCase(get()) }
}

val viewModelsModule = module {
    viewModel {
        CharacterListViewModel(get())
    }

    viewModel {
        CharacterDetailsViewModel(get())
    }
}
