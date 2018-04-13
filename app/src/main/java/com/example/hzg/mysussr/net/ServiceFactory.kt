package com.example.hzg.mysussr.net

import com.example.hzg.mysussr.AppConfig
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by hzg on 2017/9/30.
 *
 */
class ServiceFactory {

    private var mGson: Gson
    private var mRetrofit: Retrofit
    private var mOkHttpClient: OkHttpClient
    private val DEFAULT_TIMEOUT: Long = 10

    init {
        mGson = GsonBuilder()
                .setDateFormat("yyyy-MM-dd hh:mm:ss")
                .create()
        mOkHttpClient = getOkHttpClient()
        mRetrofit = Retrofit.Builder()
                .baseUrl(AppConfig.BASE_URL)
                .client(mOkHttpClient)
                .addConverterFactory(GsonConverterFactory.create(mGson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
    }

    fun createNormalRetrofit(): Retrofit {
        return Retrofit.Builder()
                .baseUrl(AppConfig.BASE_URL)
                .client(getOkHttpClient())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
    }

    companion object {
        private var INSTANCE: ServiceFactory? = null
        fun getInstance(): ServiceFactory {
            if (INSTANCE == null) {
                INSTANCE = ServiceFactory()
            }
            return INSTANCE!!
        }
    }


    fun <S> createService(serviceClass: Class<S>): S {
        return mRetrofit.create(serviceClass)
    }

    fun createService(): HttpService {
        return createService(HttpService::class.java)
    }

    fun getOkHttpClient(): OkHttpClient {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        //定制OkHttp
        val httpClientBuilder = OkHttpClient.Builder()
        //设置超时时间
        httpClientBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)//错误重连
                .addInterceptor(httpLoggingInterceptor)//日记拦截器

        return httpClientBuilder.build()
    }

}