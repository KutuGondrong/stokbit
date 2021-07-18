package com.kutugondrong.cryptoonlinekg.screen

import android.app.Application
import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.kutugondrong.cryptoonlinekg.AppCryptoOnlineKG
import com.kutugondrong.cryptoonlinekg.BuildConfig
import com.kutugondrong.cryptoonlinekg.data.db.AppDataBase
import com.kutugondrong.cryptoonlinekg.preference.LocalPreferences
import com.kutugondrong.cryptoonlinekg.screen.home.HomeFragment
import com.kutugondrong.cryptoonlinekg.screen.login.LoginFragment
import com.kutugondrong.data.local.dao.CryptoDao
import com.kutugondrong.data.local.dao.RemoteKeysDao
import com.kutugondrong.data.remote.service.CryptoApi
import com.kutugondrong.data.socket.CryptoSocket
import com.kutugondrong.data.socket.FlowStreamAdapter
import com.tinder.scarlet.Scarlet
import com.tinder.scarlet.lifecycle.android.AndroidLifecycle
import com.tinder.scarlet.messageadapter.gson.GsonMessageAdapter
import com.tinder.scarlet.websocket.okhttp.newWebSocketFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Singleton


@ExperimentalCoroutinesApi
class CryptoOnlineFragmentFactoryAndroidTest @Inject constructor() : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when(className){
            LoginFragment::class.java.name -> LoginFragment()
            HomeFragment::class.java.name -> HomeFragment()
            else -> super.instantiate(classLoader, className)
        }

    }
}


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideBaseUrl() = BuildConfig.SERVER_BASE_URL

    @Singleton
    @Provides
    fun provideOkHttpClient() = if (BuildConfig.DEBUG) {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    } else OkHttpClient
        .Builder()
        .build()

    @Singleton
    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        BASE_URL: String
    ): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .build()

    @Provides
    @Singleton
    fun provideCryptoApi(retrofit: Retrofit): CryptoApi = retrofit.create(CryptoApi::class.java)

    @Provides
    fun providesApplication(@ApplicationContext context: Context): AppCryptoOnlineKG {
        return context as AppCryptoOnlineKG
    }

    @Provides
    fun providesLocalPreferences(@ApplicationContext appContext: Context): LocalPreferences {
        return LocalPreferences(appContext, BuildConfig.APPLICATION_ID)
    }

    @Provides
    @Singleton
    fun providesDB(@ApplicationContext context: Context): AppDataBase {
        return AppDataBase.invoke(context)
    }

    @Singleton
    @Provides
    fun providesKeysDao(appDataBase: AppDataBase): RemoteKeysDao = appDataBase.remoteKeysDao

    @Singleton
    @Provides
    fun providesCryptoDao(appDataBase: AppDataBase): CryptoDao = appDataBase.cryptoDao

    @Singleton
    @Provides
    fun provideScarlet(application: Application, okHttpClient: OkHttpClient): Scarlet {
        return Scarlet.Builder()
            .webSocketFactory(okHttpClient.newWebSocketFactory(BuildConfig.WEBSOCKET_BASE_URL))
            .addMessageAdapterFactory(GsonMessageAdapter.Factory())
            .addStreamAdapterFactory(FlowStreamAdapter.Factory)
            .lifecycle(AndroidLifecycle.ofApplicationForeground(application))
            .build()
    }

    @Singleton
    @Provides
    fun provideCryptoSocket(scarlet: Scarlet): CryptoSocket {
        return scarlet.create()
    }
}