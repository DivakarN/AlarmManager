package com.sysaxiom.alarmmanager.setrepeating

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.sysaxiom.alarmmanager.set.NotificationHelper
import okhttp3.ResponseBody
import retrofit2.Callback
import retrofit2.Response

class SetRepeatingAlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        updateLocationCall(context)
    }

    private fun updateLocationCall(context: Context){
        try{
            val networkConnectionInterceptor =
                NetworkConnectionInterceptor(context)
            val api =
                RetrofitHandler(networkConnectionInterceptor)

            var call : retrofit2.Call<ResponseBody> = api.ping()

            call.enqueue(object : Callback<ResponseBody> {
                override fun onFailure(call: retrofit2.Call<ResponseBody>, t: Throwable) {
                    val notificationHelper = NotificationHelper(context)
                    val nb = notificationHelper.channelNotification
                    notificationHelper.manager?.notify(1, nb.build())
                    Toast.makeText(context,t.message,Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: retrofit2.Call<ResponseBody>, response: Response<ResponseBody>) {
                    val notificationHelper = NotificationHelper(context)
                    val nb = notificationHelper.channelNotification
                    notificationHelper.manager?.notify(1, nb.build())
                    Toast.makeText(context,response.toString(),Toast.LENGTH_LONG).show()
                }
            })
        } catch (e : Exception){
            println(e)
        }
    }

}