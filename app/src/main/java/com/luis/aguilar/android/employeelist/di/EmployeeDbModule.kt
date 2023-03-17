package com.luis.aguilar.android.employeelist.di

import android.content.Context
import com.android.code.challenge.justo.data.api.EmployeesServiceHelper
import com.android.code.challenge.justo.data.api.EmployeesServiceImpl
import com.android.code.challenge.justo.data.retrofit.EmployeesService
import com.luis.aguilar.android.employeelist.domain.usecase.GetEmployeesUseCase
import com.luis.aguilar.android.employeelist.data.api.AuthInterceptor
import com.luis.aguilar.android.employeelist.data.helpers.StringDataCommonHelper.Companion.BASE_URL
import com.luis.aguilar.android.employeelist.data.repository.EmployeesRepositoryImp
import com.luis.aguilar.android.employeelist.data.repository.RemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object EmployeeDbModule {

    @Singleton
    @Provides
    fun provideApplication(@ApplicationContext app: Context): App{
        return app as App
    }

    @Provides
    fun provideBaseUrl() = BASE_URL


    @Singleton
    @Provides
    fun provideOkHttpClient() = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor())
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, BASE_URL : String) = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    @Singleton
    @Provides
    fun provideEmployeesService(retrofit: Retrofit) = retrofit.create(EmployeesService::class.java)

    @Singleton
    @Provides
    fun provideEmployeesServiceHelper(employeeImpl: EmployeesServiceImpl): EmployeesServiceHelper = employeeImpl

    @Singleton
    @Provides
    fun provideRemoteDataSource() = RemoteDataSource()

    @Singleton
    @Provides
    fun provideRepository(
        employeesServiceHelper: EmployeesServiceHelper,
        remoteDataSource: RemoteDataSource
    ) = EmployeesRepositoryImp(employeesServiceHelper, Dispatchers.IO)

    @Singleton
    @Provides
    fun provideGetEmployeesUseCase(employeesRepositoryImp: EmployeesRepositoryImp) = GetEmployeesUseCase(employeesRepositoryImp)


}