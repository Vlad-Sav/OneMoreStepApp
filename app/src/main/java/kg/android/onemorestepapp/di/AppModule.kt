package kg.android.onemorestepapp.di

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kg.android.onemorestepapp.repository.*
import kg.android.onemorestepapp.service.apiservice.AuthApi
import kg.android.onemorestepapp.service.apiservice.ProfileApi
import kg.android.onemorestepapp.service.apiservice.RewardsApi
import kg.android.onemorestepapp.service.apiservice.RouteApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    val interceptor : HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    val client : OkHttpClient = OkHttpClient.Builder().apply {
        addInterceptor(interceptor)
    }.build()

    @Provides
    @Singleton
    fun provideAuthApi(): AuthApi {
        return Retrofit.Builder()
            .baseUrl("http://192.168.1.5:81/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create()
    }

    @Provides
    @Singleton
    fun provideProfileApi(): ProfileApi {
        return Retrofit.Builder()
            .baseUrl("http://192.168.1.5:81/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create()
    }

    @Provides
    @Singleton
    fun provideRouteApi(): RouteApi {
        return Retrofit.Builder()
            .baseUrl("http://192.168.1.5:81/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create()
    }

    @Provides
    @Singleton
    fun provideRewardsApi(): RewardsApi {
        return Retrofit.Builder()
            .baseUrl("http://192.168.1.5:81/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create()
    }

    @Provides
    @Singleton
    fun provideSharedPref(app: Application): SharedPreferences {
        return app.getSharedPreferences("prefs", MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(api: AuthApi, prefs: SharedPreferences): AuthRepository {
        return AuthRepository(api, prefs)
    }

    @Provides
    @Singleton
    fun provideProfileRepository(api: ProfileApi, prefs: SharedPreferences): ProfileRepository {
        return ProfileRepository(api, prefs)
    }

    @Provides
    @Singleton
    fun provideRouteRecordRepository(api: RouteApi, prefs: SharedPreferences): RouteRecordRepository {
        return RouteRecordRepository(api, prefs)
    }

    @Provides
    @Singleton
    fun provideRoutesRepository(api: RouteApi, prefs: SharedPreferences): RoutesRepository {
        return RoutesRepository(api, prefs)
    }

    @Provides
    @Singleton
    fun provideRewardsRepository(api: RewardsApi, prefs: SharedPreferences): RewardsRepository {
        return RewardsRepository(api, prefs)
    }

    @Provides
    @Singleton
    fun provideStatisticsRepository(api: ProfileApi, prefs: SharedPreferences): StatisticsRepository {
        return StatisticsRepository(api, prefs)
    }
}