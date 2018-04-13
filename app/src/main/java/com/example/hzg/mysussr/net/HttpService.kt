package com.example.hzg.mysussr.net

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Url

/**
 * Created by hzg on 2017/9/30.
 *
 */
interface HttpService {
    @GET("http://pv.sohu.com/cityjson?ie=utf-8")
    fun checkIp(): Observable<ResponseBody>

}