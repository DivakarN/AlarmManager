package com.sysaxiom.alarmmanager.setrepeating

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface NetworkApis {

    @GET("ping")
    fun ping() : Call<ResponseBody>

}