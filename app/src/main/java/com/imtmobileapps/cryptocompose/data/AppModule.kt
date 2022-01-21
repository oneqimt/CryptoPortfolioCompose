package com.imtmobileapps.cryptocompose.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.imtmobileapps.cryptocompose.data.local.AppDatabase
import com.imtmobileapps.cryptocompose.data.local.CryptoValuesDao
import com.imtmobileapps.cryptocompose.data.local.PersonDao
import com.imtmobileapps.cryptocompose.data.local.TotalValuesDao
import com.imtmobileapps.cryptocompose.data.remote.CryptoApi
import com.imtmobileapps.cryptocompose.data.remote.RemoteDataSource
import com.imtmobileapps.cryptocompose.util.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private val gson: Gson = GsonBuilder()
        .serializeNulls()
        .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
        .setLenient()
        .create()

    @Singleton
    @Provides
     fun cryptoApi(): CryptoApi {
        return Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(getOkHttpClient().build())
            .build()
            .create(CryptoApi::class.java)
    }

    @Singleton
    @Provides
    fun getOkHttpClient(): OkHttpClient.Builder {
        val builder = OkHttpClient.Builder()
        builder.connectTimeout(15, TimeUnit.SECONDS)
        builder.writeTimeout(1, TimeUnit.SECONDS)
        builder.readTimeout(5, TimeUnit.MINUTES)
        return builder
    }

    // TODO - add RemoteDataSource to be injected the CryptoRepository

    @Singleton
    @Provides
    fun provideCryptoRepository(remoteDataSource: RemoteDataSource, cryptoValuesDao: CryptoValuesDao, personDao: PersonDao, totalValuesDao: TotalValuesDao): CryptoRepository {
        return CryptoRepositoryImpl(remoteDataSource = remoteDataSource, cryptoValuesDao = cryptoValuesDao, personDao = personDao, totalValuesDao = totalValuesDao)
    }

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context) = AppDatabase(appContext)

    @Singleton
    @Provides
    fun provideCryptoValuesDao(db: AppDatabase) = db.cryptoValueDao()

    @Singleton
    @Provides
    fun providePersonDao(db: AppDatabase) = db.personDao()

    @Singleton
    @Provides
    fun provideTotalValuesDao(db: AppDatabase) = db.totalValuesDao()



}