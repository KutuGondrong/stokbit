package com.kutugondrong.cryptoonlinekg.di

import android.app.Application
import android.content.Context
import com.kutugondrong.cryptoonlinekg.AppCryptoOnlineKG
import com.kutugondrong.cryptoonlinekg.BuildConfig
import com.kutugondrong.cryptoonlinekg.data.interceptor.ApiKeyInterceptor
import com.kutugondrong.cryptoonlinekg.data.local.dao.CryptoDao
import com.kutugondrong.cryptoonlinekg.data.local.dao.RemoteKeysDao
import com.kutugondrong.cryptoonlinekg.data.local.db.AppDataBase
import com.kutugondrong.cryptoonlinekg.data.remote.service.CryptoApi
import com.kutugondrong.cryptoonlinekg.data.socket.CryptoSocket
import com.kutugondrong.cryptoonlinekg.data.socket.FlowStreamAdapter
import com.kutugondrong.cryptoonlinekg.preference.LocalPreferences
import com.tinder.scarlet.Scarlet
import com.tinder.scarlet.lifecycle.android.AndroidLifecycle
import com.tinder.scarlet.messageadapter.gson.GsonMessageAdapter
import com.tinder.scarlet.websocket.okhttp.newWebSocketFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun providesApplication(@ApplicationContext context: Context): AppCryptoOnlineKG {
        return context as AppCryptoOnlineKG
    }

    @Provides
    fun providesLocalPreferences(@ApplicationContext appContext: Context): LocalPreferences {
        return LocalPreferences(appContext, BuildConfig.APPLICATION_ID)
    }

    @Provides
    fun provideHttpClient(): OkHttpClient {
        val logger = HttpLoggingInterceptor()
            .setLevel(
                if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
                else HttpLoggingInterceptor.Level.NONE
            )
        val apiKeyInterceptor = ApiKeyInterceptor(BuildConfig.API_KEY)

        return OkHttpClient.Builder()
            .addInterceptor(logger)
            .addInterceptor(apiKeyInterceptor)
            .build()
    }


    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BuildConfig.SERVER_BASE_URL)
            .client(okHttpClient)
            .build()


    @Provides
    @Singleton
    fun provideCryptoApi(retrofit: Retrofit): CryptoApi = retrofit.create(CryptoApi::class.java)

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
    fun provideScarlet( application: Application, okHttpClient: OkHttpClient): Scarlet {
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